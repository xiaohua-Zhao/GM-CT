package com.chengtai.fashionmgmt.module.dv.controller;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.module.dv.entity.DvDeviceLog;
import com.chengtai.fashionmgmt.module.dv.service.DvDeviceLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 设备保养维修接口。 */
@Tag(name = "设备保养维修")
@RestController
@RequestMapping("/api/dv/device-logs")
@RequiredArgsConstructor
public class DvDeviceLogController extends CodeAwareCrudController<DvDeviceLog> {
    private final DvDeviceLogService service;

    /**
     * 返回设备保养维修业务服务。
     */
    @Override
    protected BaseService<DvDeviceLog> service() { return service; }

    /**
     * 返回设备保养维修列表模糊查询字段。
     */
    @Override
    protected String keywordField() { return "log_no"; }

    /**
     * 返回设备保养维修模块名称。
     */
    @Override
    protected String moduleName() { return "设备保养维修"; }

    /**
     * 返回设备保养维修自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "DEVICE_LOG"; }

    /**
     * 读取设备保养维修业务编码。
     */
    @Override
    protected String getBusinessCode(DvDeviceLog entity) { return entity.getLogNo(); }

    /**
     * 写回设备保养维修业务编码。
     */
    @Override
    protected void setBusinessCode(DvDeviceLog entity, String code) { entity.setLogNo(code); }
}
