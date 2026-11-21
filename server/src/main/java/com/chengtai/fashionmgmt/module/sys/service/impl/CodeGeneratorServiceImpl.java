package com.chengtai.fashionmgmt.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chengtai.fashionmgmt.module.sys.code.CodeGenerationException;
import com.chengtai.fashionmgmt.module.sys.code.CodeTargetRegistry;
import com.chengtai.fashionmgmt.module.sys.dto.CodeGenerateResponse;
import com.chengtai.fashionmgmt.module.sys.dto.CodeResetRequest;
import com.chengtai.fashionmgmt.module.sys.entity.SysCodeRule;
import com.chengtai.fashionmgmt.module.sys.entity.SysCodeSequence;
import com.chengtai.fashionmgmt.module.sys.mapper.SysCodeRuleMapper;
import com.chengtai.fashionmgmt.module.sys.mapper.SysCodeSequenceMapper;
import com.chengtai.fashionmgmt.module.sys.service.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** 通用编码生成实现。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CodeGeneratorServiceImpl implements CodeGeneratorService {
    private final SysCodeRuleMapper ruleMapper;
    private final SysCodeSequenceMapper sequenceMapper;
    private final CodeTargetRegistry targetRegistry;

    @Override
    public CodeGenerateResponse preview(String bizType) {
        SysCodeRule rule = requireRule(bizType);
        String periodKey = periodKey(rule);
        SysCodeSequence sequence = sequenceMapper.selectOne(new LambdaQueryWrapper<SysCodeSequence>()
                .eq(SysCodeSequence::getRuleId, rule.getId())
                .eq(SysCodeSequence::getPeriodKey, periodKey)
                .eq(SysCodeSequence::getIsDel, 0));
        long current = sequence == null ? initialCurrent(rule) : sequence.getCurrentValue();
        long next = nextAvailableValue(rule, current);
        return response(rule, periodKey, current, next, composeCode(rule, next));
    }

    @Override
    @Transactional
    public CodeGenerateResponse generate(String bizType) {
        SysCodeRule rule = requireRule(bizType);
        String periodKey = periodKey(rule);
        sequenceMapper.ensureSequence(rule.getId(), periodKey, initialCurrent(rule));
        SysCodeSequence sequence = sequenceMapper.selectForUpdate(rule.getId(), periodKey);
        if (sequence == null) {
            throw new CodeGenerationException("编码序列初始化失败");
        }
        long current = sequence.getCurrentValue();
        long next = nextAvailableValue(rule, current);
        sequenceMapper.updateCurrentValue(sequence.getId(), next);
        String code = composeCode(rule, next);
        log.info("[CODE][GENERATED] bizType={} ruleCode={} periodKey={} current={} next={} code={}",
                rule.getBizType(), rule.getRuleCode(), periodKey, current, next, code);
        return response(rule, periodKey, current, next, code);
    }

    @Override
    @Transactional
    public CodeGenerateResponse reset(Long ruleId, CodeResetRequest request) {
        SysCodeRule rule = ruleMapper.selectById(ruleId);
        if (rule == null) {
            throw new CodeGenerationException("编码规则不存在");
        }
        String periodKey = periodKey(rule);
        long current = request.getNextValue() - safeStep(rule);
        sequenceMapper.ensureSequence(rule.getId(), periodKey, current);
        SysCodeSequence sequence = sequenceMapper.selectForUpdate(rule.getId(), periodKey);
        if (sequence == null) {
            throw new CodeGenerationException("编码序列初始化失败");
        }
        sequenceMapper.updateCurrentValue(sequence.getId(), current);
        log.warn("[CODE][RESET] ruleId={} bizType={} periodKey={} nextValue={} reason={}",
                ruleId, rule.getBizType(), periodKey, request.getNextValue(), request.getReason());
        return response(rule, periodKey, current, request.getNextValue(), composeCode(rule, request.getNextValue()));
    }

    private SysCodeRule requireRule(String bizType) {
        SysCodeRule rule = ruleMapper.selectOne(new LambdaQueryWrapper<SysCodeRule>()
                .eq(SysCodeRule::getBizType, bizType)
                .eq(SysCodeRule::getIsDel, 0)
                .last("LIMIT 1"));
        if (rule == null) {
            throw new CodeGenerationException("未配置业务类型 " + bizType + " 的编码规则");
        }
        if (!Objects.equals(rule.getEnabled(), 1)) {
            throw new CodeGenerationException("编码规则已停用");
        }
        return rule;
    }

    private long initialCurrent(SysCodeRule rule) {
        return Math.max(0L, safeStart(rule) - safeStep(rule));
    }

    private long nextAvailableValue(SysCodeRule rule, long current) {
        long candidate = current + safeStep(rule);
        for (int i = 0; i < 1000; i++) {
            if (!targetRegistry.exists(rule.getBizType(), composeCode(rule, candidate))) {
                return candidate;
            }
            candidate += safeStep(rule);
        }
        throw new CodeGenerationException("连续检测1000个编码均已存在，请检查编码规则");
    }

    private String composeCode(SysCodeRule rule, long value) {
        List<String> parts = new ArrayList<>();
        if (StringUtils.hasText(rule.getPrefix())) {
            parts.add(rule.getPrefix());
        }
        if (StringUtils.hasText(rule.getDateFormat())) {
            try {
                parts.add(LocalDate.now().format(DateTimeFormatter.ofPattern(rule.getDateFormat())));
            } catch (IllegalArgumentException ex) {
                throw new CodeGenerationException("编码规则日期格式不合法：" + rule.getDateFormat());
            }
        }
        String number = String.valueOf(value);
        int length = rule.getSequenceLength() == null ? 0 : rule.getSequenceLength();
        if (length > 0) {
            number = String.format("%0" + length + "d", value);
        }
        parts.add(number);
        String separator = rule.getSeparator() == null ? "" : rule.getSeparator();
        return String.join(separator, parts);
    }

    private String periodKey(SysCodeRule rule) {
        String cycle = rule.getResetCycle() == null ? "NONE" : rule.getResetCycle().toUpperCase();
        LocalDate now = LocalDate.now();
        return switch (cycle) {
            case "YEAR" -> now.format(DateTimeFormatter.ofPattern("yyyy"));
            case "MONTH" -> now.format(DateTimeFormatter.ofPattern("yyyyMM"));
            case "DAY" -> now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            default -> "NONE";
        };
    }

    private CodeGenerateResponse response(SysCodeRule rule, String periodKey, long current, long next, String code) {
        return CodeGenerateResponse.builder()
                .ruleId(rule.getId())
                .bizType(rule.getBizType())
                .ruleName(rule.getRuleName())
                .periodKey(periodKey)
                .currentValue(current)
                .nextValue(next)
                .previewCode(code)
                .build();
    }

    private int safeStep(SysCodeRule rule) {
        return rule.getSequenceStep() == null || rule.getSequenceStep() <= 0 ? 1 : rule.getSequenceStep();
    }

    private long safeStart(SysCodeRule rule) {
        return rule.getSequenceStart() == null || rule.getSequenceStart() < 1 ? 1L : rule.getSequenceStart();
    }
}
