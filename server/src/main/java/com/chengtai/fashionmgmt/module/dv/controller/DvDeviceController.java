package com.chengtai.fashionmgmt.module.dv.controller;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.module.dv.entity.DvDevice;
import com.chengtai.fashionmgmt.module.dv.service.DvDeviceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 设备台账接口。 */
@Tag(name = "设备台账")
@RestController
@RequestMapping("/api/dv/devices")
@RequiredArgsConstructor
public class DvDeviceController extends CodeAwareCrudController<DvDevice> {
    private final DvDeviceService service;

    /**
     * 返回设备台账业务服务。
     */
    @Override
    protected BaseService<DvDevice> service() { return service; }

    /**
     * 返回设备台账列表模糊查询字段。
     */
    @Override
    protected String keywordField() { return "device_no"; }

    /**
     * 返回设备台账模块名称。
     */
    @Override
    protected String moduleName() { return "设备台账"; }

    /**
     * 返回设备台账自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "DEVICE"; }

    /**
     * 读取设备台账业务编码。
     */
    @Override
    protected String getBusinessCode(DvDevice entity) { return entity.getDeviceNo(); }

    /**
     * 写回设备台账业务编码。
     */
    @Override
    protected void setBusinessCode(DvDevice entity, String code) { entity.setDeviceNo(code); }
}
