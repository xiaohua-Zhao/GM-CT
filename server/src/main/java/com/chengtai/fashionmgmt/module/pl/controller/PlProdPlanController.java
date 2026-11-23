package com.chengtai.fashionmgmt.module.pl.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.pl.entity.PlProdPlan;
import com.chengtai.fashionmgmt.module.pl.service.PlProdPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 生产计划接口，调用链：Controller -> Service -> Mapper -> ct_gm.t_pl_prod_plan。 */
@Tag(name = "生产计划")
@RestController
@RequestMapping("/api/pl/plans")
@RequiredArgsConstructor
public class PlProdPlanController extends BaseCrudController<PlProdPlan> {
    private final PlProdPlanService service;

    /**
     * 返回生产计划模块的业务 Service。
     */
    @Override
    protected BaseService<PlProdPlan> service() {
        return service;
    }

    /**
     * 返回生产计划列表模糊查询使用的字段。
     */
    @Override
    protected String keywordField() {
        return "prod_plan_no";
    }

    /**
     * 返回生产计划模块名称，用于接口日志和 Swagger。
     */
    @Override
    protected String moduleName() {
        return "生产计划";
    }
}
