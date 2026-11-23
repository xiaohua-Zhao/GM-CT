package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysBank;
import com.chengtai.fashionmgmt.module.sys.service.SysBankService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 银行账户接口，调用链：Controller -> Service -> Mapper -> t_sys_bank。 */
@Tag(name = "银行账户")
@RestController
@RequestMapping("/api/sys/banks")
@RequiredArgsConstructor
public class SysBankController extends BaseCrudController<SysBank> {
    private final SysBankService service;

    /**
     * 返回银行账户模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<SysBank> service() { return service; }

    /**
     * 返回银行账户列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "account_name"; }

    /**
     * 返回“银行账户”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "银行账户"; }
}
