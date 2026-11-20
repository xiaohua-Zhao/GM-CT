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

    @Override
    protected BaseService<SysBank> service() { return service; }

    @Override
    protected String keywordField() { return "account_name"; }

    @Override
    protected String moduleName() { return "银行账户"; }
}
