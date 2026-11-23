// 财务、计件工资、仓库和设备模块的 CRUD 页面配置。
export const operationsConfigs = [
  {
    "key": "finance-settles",
    "path": "/finance-settles",
    "title": "结算管理",
    "api": "/fin/settles",
    "keywordField": "settle_no",
    "autoCodeField": "settleNo",
    "codeBizType": "FIN_SETTLE",
    "description": "维护客户、供应商、委外和工资结算单",
    "columns": [
      {
        "prop": "settleNo",
        "label": "结算单号",
        "width": 130
      },
      {
        "prop": "settleType",
        "label": "结算类型",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "客户结算",
            "value": 1
          },
          {
            "label": "供应商结算",
            "value": 2
          },
          {
            "label": "委外结算",
            "value": 3
          },
          {
            "label": "员工工资结算",
            "value": 4
          }
        ]
      },
      {
        "prop": "partnerName",
        "label": "往来对象名称",
        "width": 130
      },
      {
        "prop": "periodStart",
        "label": "结算起日",
        "width": 130
      },
      {
        "prop": "periodEnd",
        "label": "结算止日",
        "width": 130
      },
      {
        "prop": "settleAmount",
        "label": "结算金额",
        "width": 110,
        "type": "money"
      },
      {
        "prop": "paidAmount",
        "label": "已收付金额",
        "width": 110,
        "type": "money"
      },
      {
        "prop": "settleDate",
        "label": "结算日期",
        "width": 130
      },
      {
        "prop": "status",
        "label": "状态",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "待确认",
            "value": 1
          },
          {
            "label": "已确认",
            "value": 2
          },
          {
            "label": "部分收付",
            "value": 3
          },
          {
            "label": "已结清",
            "value": 4
          },
          {
            "label": "作废",
            "value": 5
          }
        ]
      }
    ],
    "fields": [
      {
        "prop": "deptId",
        "label": "归属部门",
        "type": "number",
        "lookup": "depts"
      },
      {
        "prop": "settleNo",
        "label": "结算单号",
        "type": "input"
      },
      {
        "prop": "settleType",
        "label": "结算类型",
        "type": "select",
        "options": [
          {
            "label": "客户结算",
            "value": 1
          },
          {
            "label": "供应商结算",
            "value": 2
          },
          {
            "label": "委外结算",
            "value": 3
          },
          {
            "label": "员工工资结算",
            "value": 4
          }
        ]
      },
      {
        "prop": "partnerType",
        "label": "往来类型",
        "type": "select",
        "options": [
          {
            "label": "客户单位",
            "value": 1
          },
          {
            "label": "供应商",
            "value": 2
          },
          {
            "label": "员工",
            "value": 3
          },
          {
            "label": "其他",
            "value": 4
          }
        ]
      },
      {
        "prop": "partnerId",
        "label": "往来对象ID",
        "type": "number"
      },
      {
        "prop": "partnerName",
        "label": "往来对象名称",
        "type": "input"
      },
      {
        "prop": "periodStart",
        "label": "结算起日",
        "type": "date"
      },
      {
        "prop": "periodEnd",
        "label": "结算止日",
        "type": "date"
      },
      {
        "prop": "flowCnt",
        "label": "汇总笔数",
        "type": "number"
      },
      {
        "prop": "amountTotal",
        "label": "结算合计",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "discountAmount",
        "label": "折让金额",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "settleAmount",
        "label": "结算金额",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "paidAmount",
        "label": "已收付金额",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "settleDate",
        "label": "结算日期",
        "type": "date",
        "required": true
      },
      {
        "prop": "status",
        "label": "状态",
        "type": "select",
        "options": [
          {
            "label": "待确认",
            "value": 1
          },
          {
            "label": "已确认",
            "value": 2
          },
          {
            "label": "部分收付",
            "value": 3
          },
          {
            "label": "已结清",
            "value": 4
          },
          {
            "label": "作废",
            "value": 5
          }
        ]
      },
      {
        "prop": "confirmBy",
        "label": "确认人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "confirmTime",
        "label": "确认时间",
        "type": "datetime"
      },
      {
        "prop": "remark",
        "label": "备注",
        "type": "textarea"
      }
    ]
  },
  {
    "key": "finance-flows",
    "path": "/finance-flows",
    "title": "资金流水",
    "api": "/fin/flows",
    "keywordField": "flow_no",
    "autoCodeField": "flowNo",
    "codeBizType": "FIN_FLOW",
    "description": "维护应收、应付、收款、付款和费用流水",
    "columns": [
      {
        "prop": "flowNo",
        "label": "流水单号",
        "width": 130
      },
      {
        "prop": "flowType",
        "label": "流水类型",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "应收",
            "value": 1
          },
          {
            "label": "应付",
            "value": 2
          },
          {
            "label": "收款",
            "value": 3
          },
          {
            "label": "付款",
            "value": 4
          },
          {
            "label": "费用支出",
            "value": 5
          },
          {
            "label": "其他收入",
            "value": 6
          }
        ]
      },
      {
        "prop": "partnerName",
        "label": "往来对象名称",
        "width": 130
      },
      {
        "prop": "sourceNo",
        "label": "来源单号",
        "width": 130
      },
      {
        "prop": "bizDate",
        "label": "业务日期",
        "width": 130
      },
      {
        "prop": "amount",
        "label": "发生金额",
        "width": 110,
        "type": "money"
      },
      {
        "prop": "settledAmount",
        "label": "已核销金额",
        "width": 110,
        "type": "money"
      },
      {
        "prop": "settleStatus",
        "label": "结清状态",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "未结",
            "value": 0
          },
          {
            "label": "部分结清",
            "value": 1
          },
          {
            "label": "已结清",
            "value": 2
          },
          {
            "label": "逾期",
            "value": 3
          }
        ]
      },
      {
        "prop": "status",
        "label": "审核状态",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "待审",
            "value": 1
          },
          {
            "label": "已审",
            "value": 2
          },
          {
            "label": "作废",
            "value": 3
          }
        ]
      }
    ],
    "fields": [
      {
        "prop": "deptId",
        "label": "归属部门",
        "type": "number",
        "lookup": "depts"
      },
      {
        "prop": "flowNo",
        "label": "流水单号",
        "type": "input"
      },
      {
        "prop": "businessKey",
        "label": "幂等业务键",
        "type": "input"
      },
      {
        "prop": "flowType",
        "label": "流水类型",
        "type": "select",
        "options": [
          {
            "label": "应收",
            "value": 1
          },
          {
            "label": "应付",
            "value": 2
          },
          {
            "label": "收款",
            "value": 3
          },
          {
            "label": "付款",
            "value": 4
          },
          {
            "label": "费用支出",
            "value": 5
          },
          {
            "label": "其他收入",
            "value": 6
          }
        ]
      },
      {
        "prop": "partnerType",
        "label": "往来类型",
        "type": "select",
        "options": [
          {
            "label": "客户单位",
            "value": 1
          },
          {
            "label": "供应商",
            "value": 2
          },
          {
            "label": "员工",
            "value": 3
          },
          {
            "label": "其他",
            "value": 4
          }
        ]
      },
      {
        "prop": "partnerId",
        "label": "往来对象ID",
        "type": "number"
      },
      {
        "prop": "partnerName",
        "label": "往来对象名称",
        "type": "input"
      },
      {
        "prop": "sourceType",
        "label": "来源类型",
        "type": "select",
        "options": [
          {
            "label": "订单",
            "value": 1
          },
          {
            "label": "采购单",
            "value": 2
          },
          {
            "label": "其他订单",
            "value": 3
          },
          {
            "label": "委外单",
            "value": 4
          },
          {
            "label": "工资单",
            "value": 5
          },
          {
            "label": "手工新增",
            "value": 6
          },
          {
            "label": "受托加工单",
            "value": 7
          }
        ]
      },
      {
        "prop": "sourceId",
        "label": "来源单据ID",
        "type": "number"
      },
      {
        "prop": "sourceNo",
        "label": "来源单号",
        "type": "input"
      },
      {
        "prop": "bizDate",
        "label": "业务日期",
        "type": "date",
        "required": true
      },
      {
        "prop": "dueDate",
        "label": "到期日期",
        "type": "date"
      },
      {
        "prop": "amount",
        "label": "发生金额",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "settledAmount",
        "label": "已核销金额",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "settleStatus",
        "label": "结清状态",
        "type": "select",
        "options": [
          {
            "label": "未结",
            "value": 0
          },
          {
            "label": "部分结清",
            "value": 1
          },
          {
            "label": "已结清",
            "value": 2
          },
          {
            "label": "逾期",
            "value": 3
          }
        ]
      },
      {
        "prop": "sourceFlowId",
        "label": "核销对象流水",
        "type": "number",
        "lookup": "financeFlows"
      },
      {
        "prop": "finSettleId",
        "label": "结算单",
        "type": "number",
        "lookup": "settlements"
      },
      {
        "prop": "finPlanId",
        "label": "收付款计划",
        "type": "number",
        "lookup": "financePlans"
      },
      {
        "prop": "accountId",
        "label": "收付账户",
        "type": "number",
        "lookup": "banks"
      },
      {
        "prop": "payMethod",
        "label": "收付方式",
        "type": "select",
        "options": [
          {
            "label": "银行转账",
            "value": 1
          },
          {
            "label": "现金",
            "value": 2
          },
          {
            "label": "微信/支付宝",
            "value": 3
          },
          {
            "label": "承兑",
            "value": 4
          },
          {
            "label": "其他",
            "value": 5
          }
        ]
      },
      {
        "prop": "invoiceNo",
        "label": "发票号",
        "type": "input"
      },
      {
        "prop": "operatorId",
        "label": "经办人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "status",
        "label": "审核状态",
        "type": "select",
        "options": [
          {
            "label": "待审",
            "value": 1
          },
          {
            "label": "已审",
            "value": 2
          },
          {
            "label": "作废",
            "value": 3
          }
        ]
      },
      {
        "prop": "remark",
        "label": "备注",
        "type": "textarea"
      }
    ]
  },
  {
    "key": "wage-reports",
    "path": "/wage-reports",
    "title": "报工流水",
    "api": "/wg/reports",
    "keywordField": "report_no",
    "autoCodeField": "reportNo",
    "codeBizType": "PIECE_REPORT",
    "description": "维护员工工序报工、计件单价和计件工资",
    "columns": [
      {
        "prop": "reportNo",
        "label": "报工批次号",
        "width": 130
      },
      {
        "prop": "staffId",
        "label": "工人",
        "width": 130,
        "type": "lookup",
        "lookup": "staffs"
      },
      {
        "prop": "processStage",
        "label": "工序",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "裁剪",
            "value": 1
          },
          {
            "label": "缝制",
            "value": 2
          },
          {
            "label": "剪线",
            "value": 3
          },
          {
            "label": "整烫",
            "value": 4
          },
          {
            "label": "包装",
            "value": 5
          }
        ]
      },
      {
        "prop": "taskId",
        "label": "工序任务",
        "width": 130,
        "type": "lookup",
        "lookup": "prodTasks"
      },
      {
        "prop": "workDate",
        "label": "报工日期",
        "width": 130
      },
      {
        "prop": "qty",
        "label": "完成件数",
        "width": 130
      },
      {
        "prop": "qtyReject",
        "label": "不合格件数",
        "width": 130
      },
      {
        "prop": "price",
        "label": "单件工价",
        "width": 130
      },
      {
        "prop": "amount",
        "label": "计件金额",
        "width": 110,
        "type": "money"
      },
      {
        "prop": "status",
        "label": "审批状态",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "待审批",
            "value": 1
          },
          {
            "label": "已审批",
            "value": 2
          },
          {
            "label": "已结算",
            "value": 3
          },
          {
            "label": "驳回",
            "value": 4
          }
        ]
      }
    ],
    "fields": [
      {
        "prop": "deptId",
        "label": "归属部门",
        "type": "number",
        "lookup": "depts"
      },
      {
        "prop": "reportNo",
        "label": "报工批次号",
        "type": "input"
      },
      {
        "prop": "seqNo",
        "label": "行号",
        "type": "number"
      },
      {
        "prop": "prodPlanId",
        "label": "生产计划",
        "type": "number",
        "lookup": "prodPlans",
        "required": true
      },
      {
        "prop": "taskId",
        "label": "工序任务",
        "type": "number",
        "lookup": "prodTasks",
        "required": true
      },
      {
        "prop": "orderId",
        "label": "订单",
        "type": "number",
        "lookup": "orders",
        "required": true
      },
      {
        "prop": "processStage",
        "label": "工序",
        "type": "select",
        "options": [
          {
            "label": "裁剪",
            "value": 1
          },
          {
            "label": "缝制",
            "value": 2
          },
          {
            "label": "剪线",
            "value": 3
          },
          {
            "label": "整烫",
            "value": 4
          },
          {
            "label": "包装",
            "value": 5
          }
        ]
      },
      {
        "prop": "leaderId",
        "label": "提交组长",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "staffId",
        "label": "工人",
        "type": "number",
        "lookup": "staffs",
        "required": true
      },
      {
        "prop": "workDate",
        "label": "报工日期",
        "type": "date",
        "required": true
      },
      {
        "prop": "qty",
        "label": "完成件数",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "qtyReject",
        "label": "不合格件数",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "qcRecordId",
        "label": "质检记录ID",
        "type": "number"
      },
      {
        "prop": "price",
        "label": "单件工价",
        "type": "number",
        "precision": 4
      },
      {
        "prop": "amount",
        "label": "计件金额",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "status",
        "label": "审批状态",
        "type": "select",
        "options": [
          {
            "label": "待审批",
            "value": 1
          },
          {
            "label": "已审批",
            "value": 2
          },
          {
            "label": "已结算",
            "value": 3
          },
          {
            "label": "驳回",
            "value": 4
          }
        ]
      },
      {
        "prop": "auditorId",
        "label": "财务审批人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "auditTime",
        "label": "审批时间",
        "type": "datetime"
      },
      {
        "prop": "remark",
        "label": "备注",
        "type": "textarea"
      }
    ]
  },
  {
    "key": "wage-bills",
    "path": "/wage-bills",
    "title": "工资单",
    "api": "/wg/bills",
    "keywordField": "bill_no",
    "autoCodeField": "billNo",
    "codeBizType": "WAGE_BILL",
    "description": "维护月度工资单、计件额和审批发放状态",
    "columns": [
      {
        "prop": "billNo",
        "label": "工资单号",
        "width": 130
      },
      {
        "prop": "period",
        "label": "工资月份",
        "width": 130
      },
      {
        "prop": "staffId",
        "label": "员工",
        "width": 130,
        "type": "lookup",
        "lookup": "staffs"
      },
      {
        "prop": "staffCategory",
        "label": "人员类别",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "计件工人",
            "value": 1
          },
          {
            "label": "管理人员",
            "value": 2
          },
          {
            "label": "后勤/其他",
            "value": 3
          }
        ]
      },
      {
        "prop": "wageType",
        "label": "计薪方式",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "计件",
            "value": 1
          },
          {
            "label": "月薪",
            "value": 2
          },
          {
            "label": "计件+底薪",
            "value": 3
          }
        ]
      },
      {
        "prop": "amountPiece",
        "label": "计件工资",
        "width": 110,
        "type": "money"
      },
      {
        "prop": "amountPayable",
        "label": "应发金额",
        "width": 110,
        "type": "money"
      },
      {
        "prop": "paidAmount",
        "label": "实发金额",
        "width": 110,
        "type": "money"
      },
      {
        "prop": "status",
        "label": "状态",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "草稿",
            "value": 1
          },
          {
            "label": "待组长确认",
            "value": 2
          },
          {
            "label": "待劳资审核",
            "value": 3
          },
          {
            "label": "待财务核对",
            "value": 4
          },
          {
            "label": "待老板批准",
            "value": 5
          },
          {
            "label": "待发放",
            "value": 6
          },
          {
            "label": "已发放",
            "value": 7
          },
          {
            "label": "驳回",
            "value": 8
          },
          {
            "label": "作废",
            "value": 9
          }
        ]
      }
    ],
    "fields": [
      {
        "prop": "deptId",
        "label": "归属部门",
        "type": "number",
        "lookup": "depts"
      },
      {
        "prop": "billNo",
        "label": "工资单号",
        "type": "input"
      },
      {
        "prop": "period",
        "label": "工资月份",
        "type": "input",
        "required": true
      },
      {
        "prop": "staffId",
        "label": "员工",
        "type": "number",
        "lookup": "staffs",
        "required": true
      },
      {
        "prop": "staffCategory",
        "label": "人员类别",
        "type": "select",
        "options": [
          {
            "label": "计件工人",
            "value": 1
          },
          {
            "label": "管理人员",
            "value": 2
          },
          {
            "label": "后勤/其他",
            "value": 3
          }
        ]
      },
      {
        "prop": "wageType",
        "label": "计薪方式",
        "type": "select",
        "options": [
          {
            "label": "计件",
            "value": 1
          },
          {
            "label": "月薪",
            "value": 2
          },
          {
            "label": "计件+底薪",
            "value": 3
          }
        ]
      },
      {
        "prop": "qtyTotal",
        "label": "计件总数量",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "amountPiece",
        "label": "计件工资",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "amountTime",
        "label": "底薪/月薪",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "attendDays",
        "label": "出勤天数",
        "type": "number",
        "precision": 1
      },
      {
        "prop": "bonus",
        "label": "奖励",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "deduct",
        "label": "扣款",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "amountPayable",
        "label": "应发金额",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "paidAmount",
        "label": "实发金额",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "payDate",
        "label": "发放日期",
        "type": "date"
      },
      {
        "prop": "leaderConfirmedBy",
        "label": "组长确认人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "leaderConfirmedAt",
        "label": "组长确认时间",
        "type": "datetime"
      },
      {
        "prop": "laborReviewedBy",
        "label": "劳资审核人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "laborReviewedAt",
        "label": "劳资审核时间",
        "type": "datetime"
      },
      {
        "prop": "submittedToFinanceBy",
        "label": "提交财务人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "submittedToFinanceAt",
        "label": "提交财务时间",
        "type": "datetime"
      },
      {
        "prop": "financeReviewedBy",
        "label": "财务核对人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "financeReviewedAt",
        "label": "财务核对时间",
        "type": "datetime"
      },
      {
        "prop": "approvedBy",
        "label": "老板批准人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "approvedAt",
        "label": "批准时间",
        "type": "datetime"
      },
      {
        "prop": "payMethod",
        "label": "发放方式",
        "type": "select",
        "options": [
          {
            "label": "银行代发",
            "value": 1
          },
          {
            "label": "现金发放",
            "value": 2
          }
        ]
      },
      {
        "prop": "paymentBatchId",
        "label": "发放批次",
        "type": "number",
        "lookup": "wageBatches"
      },
      {
        "prop": "paidBy",
        "label": "出纳",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "paidAt",
        "label": "实际发放时间",
        "type": "datetime"
      },
      {
        "prop": "status",
        "label": "状态",
        "type": "select",
        "options": [
          {
            "label": "草稿",
            "value": 1
          },
          {
            "label": "待组长确认",
            "value": 2
          },
          {
            "label": "待劳资审核",
            "value": 3
          },
          {
            "label": "待财务核对",
            "value": 4
          },
          {
            "label": "待老板批准",
            "value": 5
          },
          {
            "label": "待发放",
            "value": 6
          },
          {
            "label": "已发放",
            "value": 7
          },
          {
            "label": "驳回",
            "value": 8
          },
          {
            "label": "作废",
            "value": 9
          }
        ]
      },
      {
        "prop": "remark",
        "label": "备注",
        "type": "textarea"
      }
    ]
  },
  {
    "key": "wage-batches",
    "path": "/wage-batches",
    "title": "工资发放批次",
    "api": "/wg/payment-batches",
    "keywordField": "batch_no",
    "autoCodeField": "batchNo",
    "codeBizType": "WAGE_BATCH",
    "description": "维护工资发放申请、核对、批准和实发批次",
    "columns": [
      {
        "prop": "batchNo",
        "label": "发放批次号",
        "width": 130
      },
      {
        "prop": "period",
        "label": "工资月份",
        "width": 130
      },
      {
        "prop": "totalAmount",
        "label": "应发合计",
        "width": 110,
        "type": "money"
      },
      {
        "prop": "staffCount",
        "label": "人数",
        "width": 130
      },
      {
        "prop": "status",
        "label": "状态",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "待财务",
            "value": 1
          },
          {
            "label": "已核对",
            "value": 2
          },
          {
            "label": "已批准",
            "value": 3
          },
          {
            "label": "发放中",
            "value": 4
          },
          {
            "label": "已发放",
            "value": 5
          },
          {
            "label": "作废",
            "value": 6
          }
        ]
      },
      {
        "prop": "paymentMethod",
        "label": "发放方式",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "银行代发",
            "value": 1
          },
          {
            "label": "现金发放",
            "value": 2
          }
        ]
      },
      {
        "prop": "paidAt",
        "label": "发放时间",
        "width": 130,
        "type": "datetime"
      }
    ],
    "fields": [
      {
        "prop": "deptId",
        "label": "归属部门",
        "type": "number",
        "lookup": "depts"
      },
      {
        "prop": "batchNo",
        "label": "发放批次号",
        "type": "input"
      },
      {
        "prop": "period",
        "label": "工资月份",
        "type": "input",
        "required": true
      },
      {
        "prop": "totalAmount",
        "label": "应发合计",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "staffCount",
        "label": "人数",
        "type": "number"
      },
      {
        "prop": "status",
        "label": "状态",
        "type": "select",
        "options": [
          {
            "label": "待财务",
            "value": 1
          },
          {
            "label": "已核对",
            "value": 2
          },
          {
            "label": "已批准",
            "value": 3
          },
          {
            "label": "发放中",
            "value": 4
          },
          {
            "label": "已发放",
            "value": 5
          },
          {
            "label": "作废",
            "value": 6
          }
        ]
      },
      {
        "prop": "paymentMethod",
        "label": "发放方式",
        "type": "select",
        "options": [
          {
            "label": "银行代发",
            "value": 1
          },
          {
            "label": "现金发放",
            "value": 2
          }
        ]
      },
      {
        "prop": "laborSubmittedBy",
        "label": "劳资提交人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "laborSubmittedAt",
        "label": "劳资提交时间",
        "type": "datetime"
      },
      {
        "prop": "financeConfirmedBy",
        "label": "财务核对人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "financeConfirmedAt",
        "label": "财务核对时间",
        "type": "datetime"
      },
      {
        "prop": "approvedBy",
        "label": "批准人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "approvedAt",
        "label": "批准时间",
        "type": "datetime"
      },
      {
        "prop": "paidBy",
        "label": "发放人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "paidAt",
        "label": "发放时间",
        "type": "datetime"
      },
      {
        "prop": "remark",
        "label": "备注",
        "type": "textarea"
      }
    ]
  },
  {
    "key": "wage-payment-items",
    "path": "/wage-payment-items",
    "title": "工资发放明细",
    "api": "/wg/payment-items",
    "keywordField": "remark",
    "description": "维护每张工资单在发放批次中的实发明细",
    "columns": [
      {
        "prop": "paymentBatchId",
        "label": "工资发放批次",
        "width": 130,
        "type": "lookup",
        "lookup": "wageBatches"
      },
      {
        "prop": "billId",
        "label": "工资单",
        "width": 130,
        "type": "lookup",
        "lookup": "wageBills"
      },
      {
        "prop": "staffId",
        "label": "员工",
        "width": 130,
        "type": "lookup",
        "lookup": "staffs"
      },
      {
        "prop": "amount",
        "label": "实发金额",
        "width": 110,
        "type": "money"
      },
      {
        "prop": "paymentMethod",
        "label": "发放方式",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "银行代发",
            "value": 1
          },
          {
            "label": "现金",
            "value": 2
          }
        ]
      },
      {
        "prop": "status",
        "label": "状态",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "待发放",
            "value": 1
          },
          {
            "label": "已发放",
            "value": 2
          },
          {
            "label": "失败",
            "value": 3
          }
        ]
      },
      {
        "prop": "paidAt",
        "label": "发放时间",
        "width": 130,
        "type": "datetime"
      }
    ],
    "fields": [
      {
        "prop": "deptId",
        "label": "归属部门",
        "type": "number",
        "lookup": "depts"
      },
      {
        "prop": "paymentBatchId",
        "label": "工资发放批次",
        "type": "number",
        "lookup": "wageBatches",
        "required": true
      },
      {
        "prop": "billId",
        "label": "工资单",
        "type": "number",
        "lookup": "wageBills",
        "required": true
      },
      {
        "prop": "staffId",
        "label": "员工",
        "type": "number",
        "lookup": "staffs",
        "required": true
      },
      {
        "prop": "amount",
        "label": "实发金额",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "paymentMethod",
        "label": "发放方式",
        "type": "select",
        "options": [
          {
            "label": "银行代发",
            "value": 1
          },
          {
            "label": "现金",
            "value": 2
          }
        ]
      },
      {
        "prop": "finFlowId",
        "label": "资金流水",
        "type": "number",
        "lookup": "financeFlows"
      },
      {
        "prop": "status",
        "label": "状态",
        "type": "select",
        "options": [
          {
            "label": "待发放",
            "value": 1
          },
          {
            "label": "已发放",
            "value": 2
          },
          {
            "label": "失败",
            "value": 3
          }
        ]
      },
      {
        "prop": "paidAt",
        "label": "发放时间",
        "type": "datetime"
      },
      {
        "prop": "remark",
        "label": "备注",
        "type": "textarea"
      }
    ]
  },
  {
    "key": "warehouses",
    "path": "/warehouses",
    "title": "仓库管理",
    "api": "/st/warehouses",
    "keywordField": "wh_name",
    "autoCodeField": "whCode",
    "codeBizType": "WAREHOUSE",
    "description": "维护原料、辅料、成品、半成品和不良品仓库",
    "columns": [
      {
        "prop": "whCode",
        "label": "仓库编码",
        "width": 130
      },
      {
        "prop": "whName",
        "label": "仓库名称",
        "width": 130
      },
      {
        "prop": "whType",
        "label": "仓库类型",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "原料",
            "value": 1
          },
          {
            "label": "辅料",
            "value": 2
          },
          {
            "label": "成品",
            "value": 3
          },
          {
            "label": "半成品",
            "value": 4
          },
          {
            "label": "不良品",
            "value": 5
          }
        ]
      },
      {
        "prop": "location",
        "label": "库位地址",
        "width": 130
      },
      {
        "prop": "managerId",
        "label": "仓管员",
        "width": 130,
        "type": "lookup",
        "lookup": "staffs"
      },
      {
        "prop": "status",
        "label": "状态",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "启用",
            "value": 1
          },
          {
            "label": "停用",
            "value": 0
          }
        ]
      }
    ],
    "fields": [
      {
        "prop": "deptId",
        "label": "归属部门",
        "type": "number",
        "lookup": "depts"
      },
      {
        "prop": "whCode",
        "label": "仓库编码",
        "type": "input"
      },
      {
        "prop": "whName",
        "label": "仓库名称",
        "type": "input",
        "required": true
      },
      {
        "prop": "whType",
        "label": "仓库类型",
        "type": "select",
        "options": [
          {
            "label": "原料",
            "value": 1
          },
          {
            "label": "辅料",
            "value": 2
          },
          {
            "label": "成品",
            "value": 3
          },
          {
            "label": "半成品",
            "value": 4
          },
          {
            "label": "不良品",
            "value": 5
          }
        ]
      },
      {
        "prop": "location",
        "label": "库位地址",
        "type": "input"
      },
      {
        "prop": "managerId",
        "label": "仓管员",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "status",
        "label": "状态",
        "type": "select",
        "options": [
          {
            "label": "启用",
            "value": 1
          },
          {
            "label": "停用",
            "value": 0
          }
        ]
      },
      {
        "prop": "remark",
        "label": "备注",
        "type": "textarea"
      }
    ]
  },
  {
    "key": "stocks",
    "path": "/stocks",
    "title": "库存管理",
    "api": "/st/stocks",
    "keywordField": "item_name",
    "description": "维护材料与成品现存、占用、均价和安全库存",
    "columns": [
      {
        "prop": "warehouseId",
        "label": "仓库",
        "width": 130,
        "type": "lookup",
        "lookup": "warehouses"
      },
      {
        "prop": "itemCode",
        "label": "物品编码",
        "width": 130
      },
      {
        "prop": "itemName",
        "label": "物品名称",
        "width": 130
      },
      {
        "prop": "spec",
        "label": "规格",
        "width": 130
      },
      {
        "prop": "color",
        "label": "颜色",
        "width": 130
      },
      {
        "prop": "size",
        "label": "尺码",
        "width": 130
      },
      {
        "prop": "qtyOnHand",
        "label": "现存数量",
        "width": 130
      },
      {
        "prop": "lockedQty",
        "label": "占用数量",
        "width": 130
      },
      {
        "prop": "avgPrice",
        "label": "结存均价",
        "width": 130
      },
      {
        "prop": "safetyQty",
        "label": "安全库存",
        "width": 130
      },
      {
        "prop": "status",
        "label": "状态",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "正常",
            "value": 1
          },
          {
            "label": "冻结",
            "value": 0
          }
        ]
      }
    ],
    "fields": [
      {
        "prop": "deptId",
        "label": "归属部门",
        "type": "number",
        "lookup": "depts"
      },
      {
        "prop": "warehouseId",
        "label": "仓库",
        "type": "number",
        "lookup": "warehouses",
        "required": true
      },
      {
        "prop": "itemType",
        "label": "物品类型",
        "type": "select",
        "options": [
          {
            "label": "材料",
            "value": 1
          },
          {
            "label": "成品",
            "value": 2
          }
        ]
      },
      {
        "prop": "itemId",
        "label": "物品ID",
        "type": "number",
        "required": true
      },
      {
        "prop": "itemCode",
        "label": "物品编码",
        "type": "input",
        "required": true
      },
      {
        "prop": "itemName",
        "label": "物品名称",
        "type": "input",
        "required": true
      },
      {
        "prop": "spec",
        "label": "规格",
        "type": "input"
      },
      {
        "prop": "color",
        "label": "颜色",
        "type": "input"
      },
      {
        "prop": "size",
        "label": "尺码",
        "type": "input"
      },
      {
        "prop": "unit",
        "label": "单位",
        "type": "input"
      },
      {
        "prop": "qtyOnHand",
        "label": "现存数量",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "lockedQty",
        "label": "占用数量",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "avgPrice",
        "label": "结存均价",
        "type": "number",
        "precision": 4
      },
      {
        "prop": "safetyQty",
        "label": "安全库存",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "lastInDate",
        "label": "最后入库日",
        "type": "date"
      },
      {
        "prop": "lastOutDate",
        "label": "最后出库日",
        "type": "date"
      },
      {
        "prop": "status",
        "label": "状态",
        "type": "select",
        "options": [
          {
            "label": "正常",
            "value": 1
          },
          {
            "label": "冻结",
            "value": 0
          }
        ]
      },
      {
        "prop": "remark",
        "label": "备注",
        "type": "textarea"
      }
    ]
  },
  {
    "key": "stock-flows",
    "path": "/stock-flows",
    "title": "出入库流水",
    "api": "/st/flows",
    "keywordField": "item_name",
    "autoCodeField": "flowNo",
    "codeBizType": "STOCK_FLOW",
    "description": "维护材料与成品出入库、批次和来源信息",
    "columns": [
      {
        "prop": "flowNo",
        "label": "出入库单号",
        "width": 130
      },
      {
        "prop": "direction",
        "label": "方向",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "入库",
            "value": 1
          },
          {
            "label": "出库",
            "value": 2
          }
        ]
      },
      {
        "prop": "bizType",
        "label": "业务类型",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "采购/领料",
            "value": 1
          },
          {
            "label": "成品/销售",
            "value": 2
          },
          {
            "label": "退料/委外发料",
            "value": 3
          },
          {
            "label": "其他",
            "value": 4
          },
          {
            "label": "盘盈/盘亏",
            "value": 5
          }
        ]
      },
      {
        "prop": "warehouseId",
        "label": "仓库",
        "width": 130,
        "type": "lookup",
        "lookup": "warehouses"
      },
      {
        "prop": "itemName",
        "label": "物品名称",
        "width": 130
      },
      {
        "prop": "qty",
        "label": "数量",
        "width": 130
      },
      {
        "prop": "price",
        "label": "单价",
        "width": 130
      },
      {
        "prop": "amount",
        "label": "金额",
        "width": 110,
        "type": "money"
      },
      {
        "prop": "batchNo",
        "label": "批次号",
        "width": 130
      },
      {
        "prop": "flowDate",
        "label": "业务日期",
        "width": 130
      },
      {
        "prop": "status",
        "label": "记账状态",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "已记账",
            "value": 1
          },
          {
            "label": "作废",
            "value": 2
          }
        ]
      }
    ],
    "fields": [
      {
        "prop": "deptId",
        "label": "归属部门",
        "type": "number",
        "lookup": "depts"
      },
      {
        "prop": "flowNo",
        "label": "出入库单号",
        "type": "input"
      },
      {
        "prop": "seqNo",
        "label": "行号",
        "type": "number"
      },
      {
        "prop": "direction",
        "label": "方向",
        "type": "select",
        "options": [
          {
            "label": "入库",
            "value": 1
          },
          {
            "label": "出库",
            "value": 2
          }
        ]
      },
      {
        "prop": "bizType",
        "label": "业务类型",
        "type": "select",
        "options": [
          {
            "label": "采购/领料",
            "value": 1
          },
          {
            "label": "成品/销售",
            "value": 2
          },
          {
            "label": "退料/委外发料",
            "value": 3
          },
          {
            "label": "其他",
            "value": 4
          },
          {
            "label": "盘盈/盘亏",
            "value": 5
          }
        ]
      },
      {
        "prop": "warehouseId",
        "label": "仓库",
        "type": "number",
        "lookup": "warehouses",
        "required": true
      },
      {
        "prop": "itemType",
        "label": "物品类型",
        "type": "select",
        "options": [
          {
            "label": "材料",
            "value": 1
          },
          {
            "label": "成品",
            "value": 2
          }
        ]
      },
      {
        "prop": "itemId",
        "label": "物品ID",
        "type": "number",
        "required": true
      },
      {
        "prop": "itemCode",
        "label": "物品编码",
        "type": "input",
        "required": true
      },
      {
        "prop": "itemName",
        "label": "物品名称",
        "type": "input",
        "required": true
      },
      {
        "prop": "color",
        "label": "颜色",
        "type": "input"
      },
      {
        "prop": "size",
        "label": "尺码",
        "type": "input"
      },
      {
        "prop": "unit",
        "label": "单位",
        "type": "input"
      },
      {
        "prop": "qty",
        "label": "数量",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "price",
        "label": "单价",
        "type": "number",
        "precision": 4
      },
      {
        "prop": "amount",
        "label": "金额",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "batchNo",
        "label": "批次号",
        "type": "input"
      },
      {
        "prop": "sourceType",
        "label": "来源类型",
        "type": "select",
        "options": [
          {
            "label": "采购单",
            "value": 1
          },
          {
            "label": "生产计划",
            "value": 2
          },
          {
            "label": "委外单",
            "value": 3
          },
          {
            "label": "领料单",
            "value": 4
          },
          {
            "label": "发货单",
            "value": 5
          },
          {
            "label": "无来源",
            "value": 6
          }
        ]
      },
      {
        "prop": "sourceNo",
        "label": "来源单号",
        "type": "input"
      },
      {
        "prop": "taskId",
        "label": "关联工序任务",
        "type": "number",
        "lookup": "prodTasks"
      },
      {
        "prop": "supplierId",
        "label": "供应商",
        "type": "number",
        "lookup": "suppliers"
      },
      {
        "prop": "flowDate",
        "label": "业务日期",
        "type": "date",
        "required": true
      },
      {
        "prop": "operatorId",
        "label": "经办人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "status",
        "label": "记账状态",
        "type": "select",
        "options": [
          {
            "label": "已记账",
            "value": 1
          },
          {
            "label": "作废",
            "value": 2
          }
        ]
      },
      {
        "prop": "remark",
        "label": "备注",
        "type": "textarea"
      }
    ]
  },
  {
    "key": "devices",
    "path": "/devices",
    "title": "设备台账",
    "api": "/dv/devices",
    "keywordField": "device_no",
    "autoCodeField": "deviceNo",
    "codeBizType": "DEVICE",
    "description": "维护设备档案、归属、保管人、借调和状态",
    "columns": [
      {
        "prop": "deviceNo",
        "label": "设备编号",
        "width": 130
      },
      {
        "prop": "deviceName",
        "label": "设备名称",
        "width": 130
      },
      {
        "prop": "deviceType",
        "label": "设备类型",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "缝纫机",
            "value": 1
          },
          {
            "label": "裁剪设备",
            "value": 2
          },
          {
            "label": "整烫设备",
            "value": 3
          },
          {
            "label": "锁钉/包缝",
            "value": 4
          },
          {
            "label": "特种机",
            "value": 5
          },
          {
            "label": "检测设备",
            "value": 6
          },
          {
            "label": "其他",
            "value": 7
          },
          {
            "label": "消防设备",
            "value": 8
          }
        ]
      },
      {
        "prop": "deptId",
        "label": "归属部门",
        "width": 130,
        "type": "lookup",
        "lookup": "depts"
      },
      {
        "prop": "keeperId",
        "label": "保管人",
        "width": 130,
        "type": "lookup",
        "lookup": "staffs"
      },
      {
        "prop": "location",
        "label": "存放位置",
        "width": 130
      },
      {
        "prop": "status",
        "label": "设备状态",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "在用",
            "value": 1
          },
          {
            "label": "闲置",
            "value": 2
          },
          {
            "label": "维修",
            "value": 3
          },
          {
            "label": "借出",
            "value": 4
          },
          {
            "label": "报废",
            "value": 5
          }
        ]
      },
      {
        "prop": "borrowDate",
        "label": "借调日期",
        "width": 130
      },
      {
        "prop": "expectReturnDate",
        "label": "预计归还",
        "width": 130
      }
    ],
    "fields": [
      {
        "prop": "deptId",
        "label": "归属部门",
        "type": "number",
        "lookup": "depts"
      },
      {
        "prop": "deviceNo",
        "label": "设备编号",
        "type": "input"
      },
      {
        "prop": "deviceName",
        "label": "设备名称",
        "type": "input",
        "required": true
      },
      {
        "prop": "deviceType",
        "label": "设备类型",
        "type": "select",
        "options": [
          {
            "label": "缝纫机",
            "value": 1
          },
          {
            "label": "裁剪设备",
            "value": 2
          },
          {
            "label": "整烫设备",
            "value": 3
          },
          {
            "label": "锁钉/包缝",
            "value": 4
          },
          {
            "label": "特种机",
            "value": 5
          },
          {
            "label": "检测设备",
            "value": 6
          },
          {
            "label": "其他",
            "value": 7
          },
          {
            "label": "消防设备",
            "value": 8
          }
        ]
      },
      {
        "prop": "brand",
        "label": "品牌",
        "type": "input"
      },
      {
        "prop": "model",
        "label": "型号",
        "type": "input"
      },
      {
        "prop": "serialNo",
        "label": "出厂编号",
        "type": "input"
      },
      {
        "prop": "factoryId",
        "label": "归属分厂",
        "type": "number",
        "lookup": "depts"
      },
      {
        "prop": "location",
        "label": "存放位置",
        "type": "input"
      },
      {
        "prop": "keeperId",
        "label": "保管人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "purchaseDate",
        "label": "购入日期",
        "type": "date"
      },
      {
        "prop": "price",
        "label": "设备原值",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "status",
        "label": "设备状态",
        "type": "select",
        "options": [
          {
            "label": "在用",
            "value": 1
          },
          {
            "label": "闲置",
            "value": 2
          },
          {
            "label": "维修",
            "value": 3
          },
          {
            "label": "借出",
            "value": 4
          },
          {
            "label": "报废",
            "value": 5
          }
        ]
      },
      {
        "prop": "borrowFactoryId",
        "label": "借入分厂",
        "type": "number",
        "lookup": "depts"
      },
      {
        "prop": "borrowDeptId",
        "label": "借入组",
        "type": "number",
        "lookup": "depts"
      },
      {
        "prop": "borrowDate",
        "label": "借调日期",
        "type": "date"
      },
      {
        "prop": "transportBy",
        "label": "运输人",
        "type": "input"
      },
      {
        "prop": "expectReturnDate",
        "label": "预计归还",
        "type": "date"
      },
      {
        "prop": "returnDate",
        "label": "实际归还",
        "type": "date"
      },
      {
        "prop": "remark",
        "label": "备注",
        "type": "textarea"
      }
    ]
  },
  {
    "key": "device-logs",
    "path": "/device-logs",
    "title": "设备保养维修",
    "api": "/dv/device-logs",
    "keywordField": "log_no",
    "autoCodeField": "logNo",
    "codeBizType": "DEVICE_LOG",
    "description": "维护点检、保养、维修、借调和报废记录",
    "columns": [
      {
        "prop": "logNo",
        "label": "记录单号",
        "width": 130
      },
      {
        "prop": "deviceId",
        "label": "设备",
        "width": 130,
        "type": "lookup",
        "lookup": "devices"
      },
      {
        "prop": "logType",
        "label": "记录类型",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "点检",
            "value": 1
          },
          {
            "label": "保养",
            "value": 2
          },
          {
            "label": "维修",
            "value": 3
          },
          {
            "label": "借调",
            "value": 4
          },
          {
            "label": "报废",
            "value": 5
          }
        ]
      },
      {
        "prop": "logDate",
        "label": "记录日期",
        "width": 130
      },
      {
        "prop": "handlerId",
        "label": "处理人",
        "width": 130,
        "type": "lookup",
        "lookup": "staffs"
      },
      {
        "prop": "costAmount",
        "label": "维修/保养费用",
        "width": 110,
        "type": "money"
      },
      {
        "prop": "nextDate",
        "label": "下次保养日期",
        "width": 130
      },
      {
        "prop": "status",
        "label": "状态",
        "width": 130,
        "type": "option",
        "options": [
          {
            "label": "完成",
            "value": 1
          },
          {
            "label": "处理中",
            "value": 2
          },
          {
            "label": "作废",
            "value": 3
          }
        ]
      }
    ],
    "fields": [
      {
        "prop": "deptId",
        "label": "归属部门",
        "type": "number",
        "lookup": "depts"
      },
      {
        "prop": "deviceId",
        "label": "设备",
        "type": "number",
        "lookup": "devices",
        "required": true
      },
      {
        "prop": "logNo",
        "label": "记录单号",
        "type": "input"
      },
      {
        "prop": "logType",
        "label": "记录类型",
        "type": "select",
        "options": [
          {
            "label": "点检",
            "value": 1
          },
          {
            "label": "保养",
            "value": 2
          },
          {
            "label": "维修",
            "value": 3
          },
          {
            "label": "借调",
            "value": 4
          },
          {
            "label": "报废",
            "value": 5
          }
        ]
      },
      {
        "prop": "logDate",
        "label": "记录日期",
        "type": "date",
        "required": true
      },
      {
        "prop": "handlerId",
        "label": "处理人",
        "type": "number",
        "lookup": "staffs"
      },
      {
        "prop": "faultDesc",
        "label": "故障描述",
        "type": "textarea"
      },
      {
        "prop": "logContent",
        "label": "处理内容",
        "type": "textarea"
      },
      {
        "prop": "costAmount",
        "label": "维修/保养费用",
        "type": "number",
        "precision": 2
      },
      {
        "prop": "nextDate",
        "label": "下次保养日期",
        "type": "date"
      },
      {
        "prop": "status",
        "label": "状态",
        "type": "select",
        "options": [
          {
            "label": "完成",
            "value": 1
          },
          {
            "label": "处理中",
            "value": 2
          },
          {
            "label": "作废",
            "value": 3
          }
        ]
      },
      {
        "prop": "attachCount",
        "label": "附件数量",
        "type": "number"
      },
      {
        "prop": "remark",
        "label": "备注",
        "type": "textarea"
      }
    ]
  },
  {
    "key": "finance-plans",
    "path": "/finance-plans",
    "title": "收付款计划",
    "api": "/fin/plans",
    "keywordField": "plan_no",
    "description": "维护订单和采购单对应的收付款计划",
    "columns": [
      { "prop": "planNo", "label": "计划单号", "width": 145 },
      { "prop": "direction", "label": "方向", "width": 90, "type": "option", "options": [{ "label": "收款", "value": 1 }, { "label": "付款", "value": 2 }] },
      { "prop": "partnerName", "label": "往来对象", "width": 150 },
      { "prop": "sourceNo", "label": "来源单号", "width": 145 },
      { "prop": "seqNo", "label": "序号", "width": 75 },
      { "prop": "planDate", "label": "计划日期", "width": 110 },
      { "prop": "planAmount", "label": "计划金额", "width": 110, "type": "money" },
      { "prop": "paidAmount", "label": "已收付", "width": 110, "type": "money" },
      { "prop": "status", "label": "状态", "width": 100, "type": "option", "options": [{ "label": "待执行", "value": 0 }, { "label": "部分完成", "value": 1 }, { "label": "已完成", "value": 2 }, { "label": "逾期", "value": 3 }] }
    ],
    "fields": [
      { "prop": "planNo", "label": "计划单号", "type": "input", "required": true },
      { "prop": "deptId", "label": "归属部门", "type": "number", "lookup": "depts" },
      { "prop": "direction", "label": "收付方向", "type": "select", "options": [{ "label": "收款", "value": 1 }, { "label": "付款", "value": 2 }], "default": 1 },
      { "prop": "sourceType", "label": "来源类型", "type": "select", "options": [{ "label": "订单", "value": 1 }, { "label": "采购单", "value": 2 }, { "label": "其他订单", "value": 3 }, { "label": "委外单", "value": 4 }, { "label": "工资单", "value": 5 }], "default": 1 },
      { "prop": "sourceId", "label": "来源单据ID", "type": "number" },
      { "prop": "sourceNo", "label": "来源单号", "type": "input" },
      { "prop": "partnerType", "label": "往来类型", "type": "select", "options": [{ "label": "客户单位", "value": 1 }, { "label": "供应商", "value": 2 }, { "label": "员工", "value": 3 }, { "label": "其他", "value": 4 }], "default": 1 },
      { "prop": "partnerId", "label": "往来对象ID", "type": "number" },
      { "prop": "partnerName", "label": "往来对象名称", "type": "input" },
      { "prop": "seqNo", "label": "节点序号", "type": "number", "default": 1 },
      { "prop": "nodeType", "label": "节点类型", "type": "select", "options": [{ "label": "首款", "value": 1 }, { "label": "进度款", "value": 2 }, { "label": "尾款", "value": 3 }, { "label": "全额", "value": 4 }], "default": 1 },
      { "prop": "ratio", "label": "比例", "type": "number", "precision": 4 },
      { "prop": "planDate", "label": "计划日期", "type": "date", "required": true },
      { "prop": "planAmount", "label": "计划金额", "type": "number", "precision": 2 },
      { "prop": "paidAmount", "label": "已收付金额", "type": "number", "precision": 2 },
      { "prop": "paidDate", "label": "实际收付日期", "type": "date" },
      { "prop": "status", "label": "状态", "type": "select", "options": [{ "label": "待执行", "value": 0 }, { "label": "部分完成", "value": 1 }, { "label": "已完成", "value": 2 }, { "label": "逾期", "value": 3 }], "default": 0 },
      { "prop": "remark", "label": "备注", "type": "textarea" }
    ]
  },
  {
    "key": "finance-invoices",
    "path": "/finance-invoices",
    "title": "发票管理",
    "api": "/fin/invoices",
    "keywordField": "invoice_no",
    "description": "维护销项、进项发票及订单、采购单、结算和流水关联",
    "columns": [
      { "prop": "invoiceNo", "label": "发票号", "width": 150 },
      { "prop": "invoiceType", "label": "发票类型", "width": 100, "type": "option", "options": [{ "label": "销项", "value": 1 }, { "label": "进项", "value": 2 }] },
      { "prop": "partnerName", "label": "往来对象", "width": 150 },
      { "prop": "orderNo", "label": "订单号", "width": 145 },
      { "prop": "poNo", "label": "采购单号", "width": 145 },
      { "prop": "totalAmount", "label": "价税合计", "width": 110, "type": "money" },
      { "prop": "invoiceDate", "label": "开票日期", "width": 110 },
      { "prop": "status", "label": "状态", "width": 100, "type": "option", "options": [{ "label": "待处理", "value": 1 }, { "label": "已开票", "value": 2 }, { "label": "已认证", "value": 3 }, { "label": "作废", "value": 4 }] }
    ],
    "fields": [
      { "prop": "invoiceNo", "label": "发票号", "type": "input", "required": true },
      { "prop": "deptId", "label": "归属部门", "type": "number", "lookup": "depts" },
      { "prop": "invoiceType", "label": "发票类型", "type": "select", "options": [{ "label": "销项", "value": 1 }, { "label": "进项", "value": 2 }], "default": 1 },
      { "prop": "invoiceKind", "label": "票种", "type": "select", "options": [{ "label": "专票", "value": 1 }, { "label": "普票", "value": 2 }], "default": 1 },
      { "prop": "invoiceTitle", "label": "发票抬头", "type": "input" },
      { "prop": "taxNo", "label": "税号", "type": "input" },
      { "prop": "partnerType", "label": "往来类型", "type": "select", "options": [{ "label": "客户单位", "value": 1 }, { "label": "供应商", "value": 2 }, { "label": "员工", "value": 3 }, { "label": "其他", "value": 4 }], "default": 1 },
      { "prop": "partnerId", "label": "往来对象ID", "type": "number" },
      { "prop": "partnerName", "label": "往来对象名称", "type": "input" },
      { "prop": "orderId", "label": "订单", "type": "number", "lookup": "orders" },
      { "prop": "orderNo", "label": "订单号快照", "type": "input" },
      { "prop": "poId", "label": "采购单", "type": "number", "lookup": "purchaseOrders" },
      { "prop": "poNo", "label": "采购单号快照", "type": "input" },
      { "prop": "finSettleId", "label": "结算单", "type": "number", "lookup": "settlements" },
      { "prop": "finFlowId", "label": "资金流水", "type": "number", "lookup": "financeFlows" },
      { "prop": "amount", "label": "不含税金额", "type": "number", "precision": 2 },
      { "prop": "taxRate", "label": "税率", "type": "number", "precision": 4 },
      { "prop": "taxAmount", "label": "税额", "type": "number", "precision": 2 },
      { "prop": "totalAmount", "label": "价税合计", "type": "number", "precision": 2 },
      { "prop": "invoiceDate", "label": "开票日期", "type": "date" },
      { "prop": "status", "label": "状态", "type": "select", "options": [{ "label": "待处理", "value": 1 }, { "label": "已开票", "value": 2 }, { "label": "已认证", "value": 3 }, { "label": "作废", "value": 4 }], "default": 1 },
      { "prop": "remark", "label": "备注", "type": "textarea" }
    ]
  }
]
