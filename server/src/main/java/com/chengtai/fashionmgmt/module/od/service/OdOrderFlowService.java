package com.chengtai.fashionmgmt.module.od.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderCloseRequest;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderCostPreview;
import com.chengtai.fashionmgmt.module.od.entity.OdOrderItem;

import java.util.List;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderDetailResponse;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderReviewRequest;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderSaveRequest;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;

/** 货物订单流程服务：聚合保存、评审、暂停、恢复和关闭。 */
public interface OdOrderFlowService {
    /** 分页查询订单列表。 */
    IPage<OdOrder> listOrders(long current, long size, String keyword, Long unitId, Long salesmanId,
                              Integer status, String deliveryStart, String deliveryEnd, Long deptId);

    /** 查询订单详情、明细、收款节点、指标和进度。 */
    OdOrderDetailResponse detail(Long orderId);

    /** 按当前明细的款式和数量预览成本。 */
    OdOrderCostPreview previewCost(List<OdOrderItem> items);

    /** 新建或编辑订单头及全部明细。 */
    OdOrder saveOrder(OdOrderSaveRequest request, Long orderId);

    /** 提交可做性评审。 */
    OdOrder review(Long orderId, OdOrderReviewRequest request);

    /** 暂停订单。 */
    OdOrder pause(Long orderId);

    /** 恢复订单到暂停前状态。 */
    OdOrder resume(Long orderId);

    /** 按客户原因关闭订单。 */
    OdOrder close(Long orderId, OdOrderCloseRequest request);

    /** 校验订单已评审且样衣放行，允许创建生产计划。 */
    void assertCanCreatePlan(Long orderId);
}
