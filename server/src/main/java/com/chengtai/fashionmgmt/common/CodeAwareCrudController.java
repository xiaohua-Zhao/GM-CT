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

    protected abstract String codeBizType();

    protected abstract String getBusinessCode(T entity);

    protected abstract void setBusinessCode(T entity, String code);

    @Override
    @PostMapping
    public Result<T> create(@RequestBody T entity) {
        if (!StringUtils.hasText(getBusinessCode(entity))) {
            setBusinessCode(entity, generateCode());
        }
        return super.create(entity);
    }

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

    private String generateCode() {
        String code = codeGeneratorService.generate(codeBizType()).getPreviewCode();
        log.info("[AUTO_CODE] bizType={} generatedCode={}", codeBizType(), code);
        return code;
    }
}
