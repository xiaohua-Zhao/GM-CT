package com.chengtai.fashionmgmt.common;

import com.chengtai.fashionmgmt.module.sys.service.CodeGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 带业务编码自动生成能力的通用 CRUD Controller。
 * 前端未点击“自动生成”时，新增操作会在这里补齐编码并随实体保存。
 */
@Slf4j
public abstract class CodeAwareCrudController<T extends BaseEntity> extends BaseCrudController<T> {
    @Autowired
    private CodeGeneratorService codeGeneratorService;

    /**
     * 返回CodeAwareCrud模块使用的自动编码业务类型。
     */
    protected abstract String codeBizType();

    /**
     * 读取CodeAwareCrud实体的业务编码，用于新增时判断是否需要自动生成。
     */
    protected abstract String getBusinessCode(T entity);

    /**
     * 将后台生成的业务编码写回CodeAwareCrud实体。
     */
    protected abstract void setBusinessCode(T entity, String code);

    /**
     * 新增前自动补充业务编码，再执行通用新增保存流程。
     */
    @Override
    @PostMapping
    public Result<T> create(@RequestBody T entity) {
        if (!StringUtils.hasText(getBusinessCode(entity))) {
            setBusinessCode(entity, generateCode());
        }
        return super.create(entity);
    }

    /**
     * 修改前保留原业务编码；原记录无编码时自动生成，再执行通用修改流程。
     */
    @Override
    @PutMapping("/{id}")
    public Result<T> update(@PathVariable Long id, @RequestBody T entity) {
        T current = service().findById(id);
        String currentCode = current == null ? null : getBusinessCode(current);
        if (!StringUtils.hasText(currentCode)) {
            currentCode = generateCode();
        }
        setBusinessCode(entity, currentCode);
        return super.update(id, entity);
    }

    /**
     * 调用通用编码服务生成当前模块业务编码，并记录生成日志。
     */
    private String generateCode() {
        String code = codeGeneratorService.generate(codeBizType()).getPreviewCode();
        log.info("[AUTO_CODE] bizType={} generatedCode={}", codeBizType(), code);
        return code;
    }
}
