package com.chengtai.fashionmgmt.module.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.Result;
import com.chengtai.fashionmgmt.module.sys.dto.CodeGenerateRequest;
import com.chengtai.fashionmgmt.module.sys.dto.CodeGenerateResponse;
import com.chengtai.fashionmgmt.module.sys.dto.CodeResetRequest;
import com.chengtai.fashionmgmt.module.sys.entity.SysCodeRule;
import com.chengtai.fashionmgmt.module.sys.mapper.SysCodeRuleMapper;
import com.chengtai.fashionmgmt.module.sys.service.CodeGeneratorService;
import com.chengtai.fashionmgmt.module.sys.service.SysCodeRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 编码规则管理和编码生成接口。 */
@Tag(name = "编码规则")
@RestController
@RequestMapping("/api/sys/code-rules")
@RequiredArgsConstructor
public class SysCodeRuleController extends BaseCrudController<SysCodeRule> {
    private final SysCodeRuleService service;
    private final SysCodeRuleMapper ruleMapper;
    private final CodeGeneratorService generatorService;

    /**
     * 返回编码规则模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<SysCodeRule> service() { return service; }

    /**
     * 返回编码规则列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "rule_name"; }

    /**
     * 返回“编码规则”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "编码规则"; }

    /**
     * HTTP 接口：按业务类型查询启用的编码规则。
     */
    @Operation(summary = "按业务类型查询规则")
    @GetMapping("/by-biz/{bizType}")
    public Result<SysCodeRule> byBizType(@PathVariable String bizType) {
        return Result.success(ruleMapper.selectOne(new LambdaQueryWrapper<SysCodeRule>()
                .eq(SysCodeRule::getBizType, bizType)
                .eq(SysCodeRule::getIsDel, 0)
                .last("LIMIT 1")));
    }

    /**
     * HTTP 接口：预览指定业务类型的下一个编码，不占用序列。
     */
    @Operation(summary = "预览编码", description = "预览下一个编码，不消耗序列")
    @PostMapping("/preview")
    public Result<CodeGenerateResponse> preview(@Valid @RequestBody CodeGenerateRequest request) {
        return Result.success(generatorService.preview(request.getBizType()));
    }

    /**
     * HTTP 接口：正式生成编码并占用序列号。
     */
    @Operation(summary = "生成编码", description = "生成并消耗一个编码序列")
    @PostMapping("/generate")
    public Result<CodeGenerateResponse> generate(@Valid @RequestBody CodeGenerateRequest request) {
        return Result.success("生成成功", generatorService.generate(request.getBizType()));
    }

    /**
     * HTTP 接口：按规则重置编码序列。
     */
    @Operation(summary = "重置编码序列")
    @PostMapping("/{id}/reset")
    public Result<CodeGenerateResponse> reset(@PathVariable Long id, @Valid @RequestBody CodeResetRequest request) {
        return Result.success("重置成功", generatorService.reset(id, request));
    }
}
