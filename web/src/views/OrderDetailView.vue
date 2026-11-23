<template>
  <section class="order-detail-page" v-loading="loading">
    <div class="page-heading">
      <div>
        <p class="order-breadcrumb">订单管理 / 订单详情</p>
        <h1 class="page-title">{{ order.orderNo || '订单详情' }}</h1>
        <p class="page-desc">维护订单信息、订单明细、收款计划和关联发票。</p>
      </div>
      <div class="page-actions">
        <el-button @click="router.push('/orders')">返回订单列表</el-button>
        <el-button :loading="loading" @click="loadAll">刷新</el-button>
        <el-button type="primary" :loading="saving" @click="saveOrder">保存订单</el-button>
      </div>
    </div>

    <el-form ref="orderFormRef" :model="order" :rules="orderRules" label-position="left" label-width="106px">
      <section class="content-panel detail-section">
        <div class="detail-section-title"><span>01</span><div><strong>订单基础信息</strong><small>每行显示三个字段，标签位于输入框左侧</small></div></div>
        <div class="detail-grid-3">
          <el-form-item label="订单号" prop="orderNo"><el-input v-model="order.orderNo" readonly class="auto-code-input" /></el-form-item>
          <el-form-item label="客户单位" prop="unitId">
            <el-select v-model="order.unitId" filterable style="width:100%"><el-option v-for="item in unitOptions" :key="item.id" :label="optionLabel('units',item)" :value="item.id" /></el-select>
          </el-form-item>
          <el-form-item label="订单类型"><el-select v-model="order.orderType" style="width:100%"><el-option v-for="item in orderTypeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
          <el-form-item label="合同号"><el-input v-model="order.contractNo" /></el-form-item>
          <el-form-item label="归属部门"><el-select v-model="order.deptId" filterable style="width:100%"><el-option v-for="item in deptOptions" :key="item.id" :label="optionLabel('depts',item)" :value="item.id" /></el-select></el-form-item>
          <el-form-item label="业务员"><el-select v-model="order.salesmanId" filterable clearable style="width:100%"><el-option v-for="item in staffOptions" :key="item.id" :label="optionLabel('staffs',item)" :value="item.id" /></el-select></el-form-item>
          <el-form-item label="下单日期" prop="orderDate"><el-date-picker v-model="order.orderDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="开始日期"><el-date-picker v-model="order.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="预计交期" prop="deliveryDatePlan"><el-date-picker v-model="order.deliveryDatePlan" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="实际交期"><el-date-picker v-model="order.deliveryDateAct" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="预计首款"><el-date-picker v-model="order.payDatePlan" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="币种"><el-input v-model="order.currency" /></el-form-item>
        </div>
      </section>

      <section class="content-panel detail-section">
        <div class="detail-section-title"><span>02</span><div><strong>金额与生产控制</strong><small>订单金额、成本和状态</small></div></div>
        <div class="detail-grid-3">
          <el-form-item label="订单数量"><el-input-number v-model="order.qtyTotal" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="订单金额"><el-input-number v-model="order.amountTotal" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="税率"><el-input-number v-model="order.taxRate" :min="0" :max="1" :precision="4" style="width:100%" /></el-form-item>
          <el-form-item label="价税合计"><el-input-number v-model="order.amountTax" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="预计成本"><el-input-number v-model="order.costPlan" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="实际成本"><el-input-number v-model="order.costAct" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="已收定金"><el-input-number v-model="order.depositAmount" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="允许生产"><el-select v-model="order.isProducible" style="width:100%"><el-option label="否" :value="0" /><el-option label="是" :value="1" /></el-select></el-form-item>
          <el-form-item label="订单状态"><el-select v-model="order.status" style="width:100%"><el-option v-for="item in orderStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
          <el-form-item label="备注" class="detail-wide"><el-input v-model="order.remark" type="textarea" :rows="3" /></el-form-item>
        </div>
      </section>
    </el-form>

    <section class="content-panel detail-section">
      <div class="detail-section-heading">
        <div class="detail-section-title compact"><span>03</span><div><strong>订单明细</strong><small>手动维护款式、颜色、尺码、数量和价格</small></div></div>
        <el-button type="primary" plain @click="openItemCreate">新增明细</el-button>
      </div>
      <el-table :data="items" border empty-text="暂无订单明细">
        <el-table-column prop="itemNo" label="行号" width="70" />
        <el-table-column label="款式" min-width="160"><template #default="{row}">{{ styleLabel(row.styleId) }}</template></el-table-column>
        <el-table-column prop="styleCode" label="款号" width="110" />
        <el-table-column prop="color" label="颜色" width="90" /><el-table-column prop="size" label="尺码" width="80" />
        <el-table-column prop="qty" label="数量" width="90" /><el-table-column label="单价" width="100"><template #default="{row}">{{ money(row.unitPrice) }}</template></el-table-column>
        <el-table-column label="金额" width="110"><template #default="{row}">{{ money(row.amount) }}</template></el-table-column>
        <el-table-column prop="deliveryDatePlan" label="行交期" width="110" />
        <el-table-column label="操作" width="130" fixed="right"><template #default="{row}"><el-button link type="primary" @click="openItemEdit(row)">编辑</el-button><el-button link type="danger" @click="removeItem(row)">删除</el-button></template></el-table-column>
      </el-table>
    </section>

    <section class="content-panel detail-section">
      <div class="detail-section-heading">
        <div class="detail-section-title compact"><span>04</span><div><strong>收款计划</strong><small>按订单节点维护定金、进度款和尾款</small></div></div>
        <el-button type="primary" plain @click="openPlanCreate">新增收款计划</el-button>
      </div>
      <el-table :data="plans" border empty-text="暂无收款计划">
        <el-table-column prop="planNo" label="计划单号" width="150" />
        <el-table-column prop="seqNo" label="节点" width="70" />
        <el-table-column label="节点类型" width="120"><template #default="{row}">{{ optionText(planNodeOptions,row.nodeType) }}</template></el-table-column>
        <el-table-column prop="ratio" label="占比(%)" width="90" />
        <el-table-column prop="planDate" label="计划日期" width="110" />
        <el-table-column label="计划金额" width="115"><template #default="{row}">{{ money(row.planAmount) }}</template></el-table-column>
        <el-table-column label="已收金额" width="115"><template #default="{row}">{{ money(row.paidAmount) }}</template></el-table-column>
        <el-table-column label="状态" width="100"><template #default="{row}"><el-tag :type="planStatusType(row.status)">{{ optionText(planStatusOptions,row.status) }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="130" fixed="right"><template #default="{row}"><el-button link type="primary" @click="openPlanEdit(row)">编辑</el-button><el-button link type="danger" @click="removePlan(row)">删除</el-button></template></el-table-column>
      </el-table>
    </section>

    <section class="content-panel detail-section">
      <div class="detail-section-title"><span>05</span><div><strong>关联发票</strong><small>展示财务开票后关联到本订单的发票信息</small></div></div>
      <el-table :data="invoices" border empty-text="暂无关联发票">
        <el-table-column prop="invoiceNo" label="发票号码" width="150" />
        <el-table-column label="类型" width="100"><template #default="{row}">{{ optionText(invoiceTypeOptions,row.invoiceType) }}</template></el-table-column>
        <el-table-column prop="invoiceTitle" label="发票抬头" min-width="180" />
        <el-table-column label="不含税金额" width="115"><template #default="{row}">{{ money(row.amount) }}</template></el-table-column>
        <el-table-column label="税额" width="100"><template #default="{row}">{{ money(row.taxAmount) }}</template></el-table-column>
        <el-table-column label="价税合计" width="115"><template #default="{row}">{{ money(row.totalAmount) }}</template></el-table-column>
        <el-table-column prop="invoiceDate" label="开票日期" width="110" />
        <el-table-column label="状态" width="100"><template #default="{row}"><el-tag>{{ optionText(invoiceStatusOptions,row.status) }}</el-tag></template></el-table-column>
      </el-table>
    </section>

    <el-dialog v-model="itemDialog" :title="itemEditingId ? '编辑订单明细' : '新增订单明细'" width="780px" destroy-on-close>
      <el-form :model="itemForm" label-position="left" label-width="100px">
        <div class="detail-grid-2">
          <el-form-item label="行号"><el-input-number v-model="itemForm.itemNo" :min="1" style="width:100%" /></el-form-item>
          <el-form-item label="款式"><el-select v-model="itemForm.styleId" filterable style="width:100%" @change="syncStyleCode"><el-option v-for="item in styleOptions" :key="item.id" :label="optionLabel('styles',item)" :value="item.id" /></el-select></el-form-item>
          <el-form-item label="款号"><el-input v-model="itemForm.styleCode" /></el-form-item>
          <el-form-item label="颜色"><el-input v-model="itemForm.color" /></el-form-item>
          <el-form-item label="尺码"><el-input v-model="itemForm.size" /></el-form-item>
          <el-form-item label="数量"><el-input-number v-model="itemForm.qty" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="单价"><el-input-number v-model="itemForm.unitPrice" :min="0" :precision="4" style="width:100%" /></el-form-item>
          <el-form-item label="金额"><el-input-number v-model="itemForm.amount" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="税率"><el-input-number v-model="itemForm.taxRate" :min="0" :max="1" :precision="4" style="width:100%" /></el-form-item>
          <el-form-item label="行交期"><el-date-picker v-model="itemForm.deliveryDatePlan" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="备注" class="detail-wide"><el-input v-model="itemForm.remark" type="textarea" :rows="2" /></el-form-item>
        </div>
      </el-form>
      <template #footer><el-button @click="itemDialog=false">取消</el-button><el-button type="primary" :loading="saving" @click="saveItem">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="planDialog" :title="planEditingId ? '编辑收款计划' : '新增收款计划'" width="720px" destroy-on-close>
      <el-form :model="planForm" label-position="left" label-width="110px">
        <div class="detail-grid-2">
          <el-form-item label="计划单号"><el-input v-model="planForm.planNo" /></el-form-item>
          <el-form-item label="节点序号"><el-input-number v-model="planForm.seqNo" :min="1" style="width:100%" /></el-form-item>
          <el-form-item label="节点类型"><el-select v-model="planForm.nodeType" style="width:100%"><el-option v-for="item in planNodeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
          <el-form-item label="占比(%)"><el-input-number v-model="planForm.ratio" :min="0" :max="100" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="计划日期"><el-date-picker v-model="planForm.planDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="计划金额"><el-input-number v-model="planForm.planAmount" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="状态"><el-select v-model="planForm.status" style="width:100%"><el-option v-for="item in planStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
          <el-form-item label="备注" class="detail-wide"><el-input v-model="planForm.remark" type="textarea" :rows="2" /></el-form-item>
        </div>
      </el-form>
      <template #footer><el-button @click="planDialog=false">取消</el-button><el-button type="primary" :loading="saving" @click="savePlan">保存</el-button></template>
    </el-dialog>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { loadLookupData, lookupDefinitions } from '../api/lookups'
import { getOrder,updateOrder,getOrderItems,createOrderItem,updateOrderItem,deleteOrderItem,getPlanList,createPlan,updatePlan,deletePlan,getInvoices } from '../api/orderDetail'

const route=useRoute(), router=useRouter()
const loading=ref(false), saving=ref(false)
const orderFormRef=ref(null)
const order=reactive({})
const items=ref([]), plans=ref([]), invoices=ref([])
const unitOptions=ref([]), staffOptions=ref([]), deptOptions=ref([]), styleOptions=ref([])
const itemDialog=ref(false), planDialog=ref(false), itemEditingId=ref(null), planEditingId=ref(null)
const itemForm=reactive({}), planForm=reactive({})
const orderTypeOptions=[{label:'内销订单',value:1},{label:'加工单',value:2},{label:'返单',value:3}]
const orderStatusOptions=[{label:'初始',value:1},{label:'待生产',value:2},{label:'生产中',value:3},{label:'部分交付',value:4},{label:'已完成',value:5},{label:'延期',value:6},{label:'暂停',value:7},{label:'取消',value:8}]
const planNodeOptions=[{label:'定金/预付',value:1},{label:'进度款/到货款',value:2},{label:'尾款',value:3},{label:'补款',value:4},{label:'质保金',value:5},{label:'检查/服务费',value:6}]
const planStatusOptions=[{label:'未收付',value:0},{label:'部分',value:1},{label:'已结清',value:2},{label:'逾期',value:3}]
const invoiceTypeOptions=[{label:'销项发票',value:1},{label:'进项发票',value:2}]
const invoiceStatusOptions=[{label:'待开票',value:1},{label:'已开票',value:2},{label:'已红冲',value:3},{label:'作废',value:4}]
const orderRules={orderNo:[{required:true,message:'请输入订单号',trigger:'blur'}],unitId:[{required:true,message:'请选择客户单位',trigger:'change'}],orderDate:[{required:true,message:'请选择下单日期',trigger:'change'}],deliveryDatePlan:[{required:true,message:'请选择预计交期',trigger:'change'}]}
// 把关联记录转换为下拉框显示文本。
const optionLabel=(key,item)=>lookupDefinitions[key].label(item)
// 页面显示辅助函数：统一处理关联选项和枚举文本。
const optionText=(options,value)=>options.find(x=>x.value===value)?.label||value
// 按人民币两位小数格式显示金额。
const money=value=>value==null?'—':'¥'+Number(value).toFixed(2)
// 把收付款计划状态映射为 Element Plus 标签颜色。
const planStatusType=value=>({0:'info',1:'warning',2:'success',3:'danger'}[value]||'info')
// 格式化客户单位下拉选项显示文本。
const unitLabel=id=>{const item=unitOptions.value.find(x=>Number(x.id)===Number(id));return item?optionLabel('units',item):order.unitName||''}
// 格式化款式下拉选项显示文本。
const styleLabel=id=>{const item=styleOptions.value.find(x=>Number(x.id)===Number(id));return item?optionLabel('styles',item):'—'}

// 并行加载订单、明细、收款计划、发票和关联基础数据。
async function loadAll(){
  loading.value=true
  try{
    const id=route.params.id
    const [orderRes,itemRes,planRes,invoiceRes,units,staffs,depts,styles]=await Promise.all([
      getOrder(id),getOrderItems(id),getPlanList(id),getInvoices(id),
      loadLookupData('units'),loadLookupData('staffs'),loadLookupData('depts'),loadLookupData('styles')
    ])
    Object.keys(order).forEach(k=>delete order[k]); Object.assign(order,orderRes.data.data||{})
    items.value=itemRes.data.data||[]; plans.value=planRes.data.data||[]; invoices.value=invoiceRes.data.data||[]
    unitOptions.value=units; staffOptions.value=staffs; deptOptions.value=depts; styleOptions.value=styles
  }catch(error){ElMessage.error(error?.response?.data?.message||error?.message||'加载订单详情失败')}
  finally{loading.value=false}
}
// 校验并保存当前订单基础信息。
async function saveOrder(){
  const valid=await orderFormRef.value?.validate().catch(()=>false); if(!valid)return
  saving.value=true
  try{await updateOrder(order.id,order);ElMessage.success('订单已保存');await loadAll()}
  catch(error){ElMessage.error(error?.response?.data?.message||'保存订单失败')}
  finally{saving.value=false}
}
// 打开新增订单明细对话框并初始化空白表单。
function openItemCreate(){
  itemEditingId.value=null;Object.keys(itemForm).forEach(k=>delete itemForm[k])
  Object.assign(itemForm,{orderId:order.id,deptId:order.deptId,itemNo:items.value.length+1,styleId:null,styleCode:'',color:'',size:'',qty:0,unitPrice:0,amount:0,taxRate:0.13,deliveryDatePlan:order.deliveryDatePlan,qtyShipped:0,techDesc:'',remark:''});itemDialog.value=true
}
// 打开明细编辑对话框并回填当前行数据。
function openItemEdit(row){itemEditingId.value=row.id;Object.keys(itemForm).forEach(k=>delete itemForm[k]);Object.assign(itemForm,row);itemDialog.value=true}
// 选择款式后自动回填款式编码。
function syncStyleCode(id){const item=styleOptions.value.find(x=>Number(x.id)===Number(id));if(item)itemForm.styleCode=item.styleCode||''}
// 新增或修改订单明细并刷新明细列表。
async function saveItem(){
  saving.value=true
  try{if(itemEditingId.value)await updateOrderItem(itemEditingId.value,itemForm);else await createOrderItem(itemForm);ElMessage.success('订单明细已保存');itemDialog.value=false;const r=await getOrderItems(order.id);items.value=r.data.data||[]}
  catch(error){ElMessage.error(error?.response?.data?.message||'保存订单明细失败')}finally{saving.value=false}
}
// 确认后删除已保存明细并重新加载列表。
async function removeItem(row){try{await ElMessageBox.confirm('确认删除该订单明细吗？','删除确认',{type:'warning'});await deleteOrderItem(row.id);ElMessage.success('已删除');const r=await getOrderItems(order.id);items.value=r.data.data||[]}catch(error){if(error!=='cancel'&&error!=='close')ElMessage.error(error?.response?.data?.message||'删除失败')}}
// 打开新增收款计划对话框并初始化空白表单。
function openPlanCreate(){
  planEditingId.value=null;Object.keys(planForm).forEach(k=>delete planForm[k])
  Object.assign(planForm,{planNo:'',deptId:order.deptId,direction:1,sourceType:1,sourceId:order.id,sourceNo:order.orderNo,partnerType:1,partnerId:order.unitId,partnerName:unitLabel(order.unitId),seqNo:plans.value.length+1,nodeType:1,ratio:null,planDate:'',planAmount:0,paidAmount:0,status:0,remark:''});planDialog.value=true
}
// 打开编辑收付款计划对话框并回填当前行。
function openPlanEdit(row){planEditingId.value=row.id;Object.keys(planForm).forEach(k=>delete planForm[k]);Object.assign(planForm,row);planDialog.value=true}
// 新增或修改收款计划并刷新计划列表。
async function savePlan(){
  saving.value=true
  try{if(planEditingId.value)await updatePlan(planEditingId.value,planForm);else await createPlan(planForm);ElMessage.success('收款计划已保存');planDialog.value=false;const r=await getPlanList(order.id);plans.value=r.data.data||[]}
  catch(error){ElMessage.error(error?.response?.data?.message||'保存收款计划失败')}finally{saving.value=false}
}
// 确认后删除已保存收付款计划并重新加载列表。
async function removePlan(row){try{await ElMessageBox.confirm('确认删除该收款计划吗？','删除确认',{type:'warning'});await deletePlan(row.id);ElMessage.success('已删除');const r=await getPlanList(order.id);plans.value=r.data.data||[]}catch(error){if(error!=='cancel'&&error!=='close')ElMessage.error(error?.response?.data?.message||'删除失败')}}
onMounted(loadAll)
</script>

<style scoped>
.order-breadcrumb{margin:0 0 7px;color:#2b6cb0;font-size:12px}.detail-section{margin-bottom:14px}.detail-section-title{display:flex;align-items:center;gap:12px;margin-bottom:16px;padding-bottom:12px;border-bottom:1px solid #edf0f4}.detail-section-title>span{width:32px;height:32px;display:grid;place-items:center;border-radius:50%;background:#eaf2fb;color:#2b6cb0;font-size:11px;font-weight:700}.detail-section-title strong{display:block;color:#1f3a5f;font-size:15px}.detail-section-title small{display:block;margin-top:3px;color:#98a2b3;font-size:11px}.detail-section-heading{display:flex;justify-content:space-between;align-items:center;margin-bottom:14px}.detail-section-title.compact{margin:0;padding:0;border:0}.detail-grid-3{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:0 18px}.detail-grid-2{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:0 18px;max-height:60vh;overflow-y:auto}.detail-wide{grid-column:1/-1}.detail-grid-3 :deep(.el-form-item__label),.detail-grid-2 :deep(.el-form-item__label){justify-content:flex-start}.detail-grid-3 :deep(.el-form-item),.detail-grid-2 :deep(.el-form-item){margin-bottom:16px}@media(max-width:1050px){.detail-grid-3{grid-template-columns:repeat(2,minmax(0,1fr))}}@media(max-width:680px){.detail-grid-3,.detail-grid-2{grid-template-columns:1fr}}
</style>
