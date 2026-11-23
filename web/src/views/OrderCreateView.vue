<template>
  <section class="order-create-page" v-loading="loading">
    <div class="page-heading">
      <div>
        <p class="order-breadcrumb">订单管理 / 新增订单</p>
        <h1 class="page-title">新增订单</h1>
        <p class="page-desc">录入订单主单，并可在保存前维护订单明细和收款计划；保存成功后进入订单详情。</p>
      </div>
      <div class="page-actions"><el-button @click="router.push('/orders')">返回订单列表</el-button></div>
    </div>

    <el-form ref="formRef" :model="form" :rules="rules" label-position="left" label-width="106px">
      <section class="content-panel detail-section">
        <div class="detail-section-title"><span>01</span><div><strong>订单基础信息</strong><small>每行三个字段，标签位于输入框左侧</small></div></div>
        <div class="detail-grid-3">
          <el-form-item label="订单号" prop="orderNo"><el-input v-model="form.orderNo" readonly class="auto-code-input" placeholder="系统自动生成订单号"><template #append><el-button :loading="codeGenerating" @click="fillOrderNo">自动生成</el-button></template></el-input></el-form-item>
          <el-form-item label="客户单位" prop="unitId"><el-select v-model="form.unitId" filterable clearable style="width:100%"><el-option v-for="item in unitOptions" :key="item.id" :label="optionLabel('units',item)" :value="item.id" /></el-select></el-form-item>
          <el-form-item label="订单类型"><el-select v-model="form.orderType" style="width:100%"><el-option v-for="item in orderTypeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
          <el-form-item label="合同号"><el-input v-model="form.contractNo" /></el-form-item>
          <el-form-item label="归属部门"><el-select v-model="form.deptId" filterable clearable style="width:100%"><el-option v-for="item in deptOptions" :key="item.id" :label="optionLabel('depts',item)" :value="item.id" /></el-select></el-form-item>
          <el-form-item label="业务员"><el-select v-model="form.salesmanId" filterable clearable style="width:100%"><el-option v-for="item in staffOptions" :key="item.id" :label="optionLabel('staffs',item)" :value="item.id" /></el-select></el-form-item>
          <el-form-item label="下单日期" prop="orderDate"><el-date-picker v-model="form.orderDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="开始日期"><el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="预计交期" prop="deliveryDatePlan"><el-date-picker v-model="form.deliveryDatePlan" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="实际交期"><el-date-picker v-model="form.deliveryDateAct" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="预计首款"><el-date-picker v-model="form.payDatePlan" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="币种"><el-input v-model="form.currency" /></el-form-item>
        </div>
      </section>

      <section class="content-panel detail-section">
        <div class="detail-section-title"><span>02</span><div><strong>金额与生产控制</strong><small>订单数量、金额、成本和状态</small></div></div>
        <div class="detail-grid-3">
          <el-form-item label="订单数量"><el-input-number v-model="form.qtyTotal" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="订单金额"><el-input-number v-model="form.amountTotal" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="税率"><el-input-number v-model="form.taxRate" :min="0" :max="1" :precision="4" :step="0.01" style="width:100%" /></el-form-item>
          <el-form-item label="价税合计"><el-input-number v-model="form.amountTax" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="预计成本"><el-input-number v-model="form.costPlan" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="实际成本"><el-input-number v-model="form.costAct" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="已收定金"><el-input-number v-model="form.depositAmount" :min="0" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="允许生产"><el-select v-model="form.isProducible" style="width:100%"><el-option label="否" :value="0" /><el-option label="是" :value="1" /></el-select></el-form-item>
          <el-form-item label="订单状态"><el-select v-model="form.status" style="width:100%"><el-option v-for="item in orderStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
          <el-form-item label="备注" class="detail-wide"><el-input v-model="form.remark" type="textarea" :rows="3" /></el-form-item>
        </div>
      </section>
    </el-form>

    <section class="content-panel detail-section">
      <div class="detail-section-heading">
        <div class="detail-section-title compact"><span>03</span><div><strong>订单明细</strong><small>保存订单时一起提交，可在保存前新增多条明细</small></div></div>
        <el-button type="primary" plain @click="openItemCreate">新增明细</el-button>
      </div>
      <el-table :data="draftItems" border empty-text="尚未新增订单明细">
        <el-table-column prop="itemNo" label="行号" width="70" />
        <el-table-column label="款式" min-width="160"><template #default="{row}">{{ styleLabel(row.styleId) }}</template></el-table-column>
        <el-table-column prop="styleCode" label="款号" width="110" /><el-table-column prop="color" label="颜色" width="90" /><el-table-column prop="size" label="尺码" width="80" />
        <el-table-column prop="qty" label="数量" width="90" /><el-table-column label="单价" width="100"><template #default="{row}">{{ money(row.unitPrice) }}</template></el-table-column>
        <el-table-column label="金额" width="110"><template #default="{row}">{{ money(row.amount) }}</template></el-table-column>
        <el-table-column prop="deliveryDatePlan" label="行交期" width="110" />
        <el-table-column label="操作" width="130" fixed="right"><template #default="{row}"><el-button link type="primary" @click="openItemEdit(row)">编辑</el-button><el-button link type="danger" @click="removeDraftItem(row)">删除</el-button></template></el-table-column>
      </el-table>
    </section>

    <section class="content-panel detail-section">
      <div class="detail-section-heading">
        <div class="detail-section-title compact"><span>04</span><div><strong>收款计划</strong><small>保存订单时一起提交，自动关联当前订单和客户</small></div></div>
        <el-button type="primary" plain @click="openPlanCreate">新增收款计划</el-button>
      </div>
      <el-table :data="draftPlans" border empty-text="尚未新增收款计划">
        <el-table-column prop="planNo" label="计划单号" width="150" /><el-table-column prop="seqNo" label="节点" width="70" />
        <el-table-column label="节点类型" width="120"><template #default="{row}">{{ optionText(planNodeOptions,row.nodeType) }}</template></el-table-column>
        <el-table-column prop="ratio" label="占比(%)" width="90" /><el-table-column prop="planDate" label="计划日期" width="110" />
        <el-table-column label="计划金额" width="115"><template #default="{row}">{{ money(row.planAmount) }}</template></el-table-column>
        <el-table-column label="状态" width="100"><template #default="{row}"><el-tag>{{ optionText(planStatusOptions,row.status) }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="130" fixed="right"><template #default="{row}"><el-button link type="primary" @click="openPlanEdit(row)">编辑</el-button><el-button link type="danger" @click="removeDraftPlan(row)">删除</el-button></template></el-table-column>
      </el-table>
    </section>

    <section class="content-panel detail-section">
      <div class="detail-section-title"><span>05</span><div><strong>关联发票</strong><small>发票需要正式订单号才能关联</small></div></div>
      <el-empty description="订单保存后，可在订单详情页查看关联发票" :image-size="72" />
    </section>

    <div class="order-submit-bar"><el-button @click="router.push('/orders')">取消</el-button><el-button type="primary" :loading="saving" @click="save">保存订单及明细</el-button></div>

    <el-dialog v-model="itemDialog" :title="itemEditingKey ? '编辑订单明细' : '新增订单明细'" width="780px" destroy-on-close>
      <el-form :model="itemForm" label-position="left" label-width="100px"><div class="detail-grid-2">
        <el-form-item label="行号"><el-input-number v-model="itemForm.itemNo" :min="1" style="width:100%" /></el-form-item>
        <el-form-item label="款式"><el-select v-model="itemForm.styleId" filterable style="width:100%" @change="syncStyleCode"><el-option v-for="item in styleOptions" :key="item.id" :label="optionLabel('styles',item)" :value="item.id" /></el-select></el-form-item>
        <el-form-item label="款号"><el-input v-model="itemForm.styleCode" /></el-form-item><el-form-item label="颜色"><el-input v-model="itemForm.color" /></el-form-item>
        <el-form-item label="尺码"><el-input v-model="itemForm.size" /></el-form-item><el-form-item label="数量"><el-input-number v-model="itemForm.qty" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="单价"><el-input-number v-model="itemForm.unitPrice" :min="0" :precision="4" style="width:100%" /></el-form-item><el-form-item label="金额"><el-input-number v-model="itemForm.amount" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="税率"><el-input-number v-model="itemForm.taxRate" :min="0" :max="1" :precision="4" style="width:100%" /></el-form-item><el-form-item label="行交期"><el-date-picker v-model="itemForm.deliveryDatePlan" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="备注" class="detail-wide"><el-input v-model="itemForm.remark" type="textarea" :rows="2" /></el-form-item>
      </div></el-form>
      <template #footer><el-button @click="itemDialog=false">取消</el-button><el-button type="primary" @click="saveDraftItem">确定</el-button></template>
    </el-dialog>

    <el-dialog v-model="planDialog" :title="planEditingKey ? '编辑收款计划' : '新增收款计划'" width="720px" destroy-on-close>
      <el-form :model="planForm" label-position="left" label-width="110px"><div class="detail-grid-2">
        <el-form-item label="计划单号"><el-input v-model="planForm.planNo" placeholder="如 PP20261120001" /></el-form-item>
        <el-form-item label="节点序号"><el-input-number v-model="planForm.seqNo" :min="1" style="width:100%" /></el-form-item>
        <el-form-item label="节点类型"><el-select v-model="planForm.nodeType" style="width:100%"><el-option v-for="item in planNodeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
        <el-form-item label="占比(%)"><el-input-number v-model="planForm.ratio" :min="0" :max="100" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="计划日期"><el-date-picker v-model="planForm.planDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="计划金额"><el-input-number v-model="planForm.planAmount" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="planForm.status" style="width:100%"><el-option v-for="item in planStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
        <el-form-item label="备注" class="detail-wide"><el-input v-model="planForm.remark" type="textarea" :rows="2" /></el-form-item>
      </div></el-form>
      <template #footer><el-button @click="planDialog=false">取消</el-button><el-button type="primary" @click="saveDraftPlan">确定</el-button></template>
    </el-dialog>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createOne } from '../api/crud'
import { generateCode } from '../api/codeRules'
import { loadLookupData, lookupDefinitions } from '../api/lookups'
import { createOrderItem, createPlan } from '../api/orderDetail'

const router=useRouter(),formRef=ref(null),loading=ref(false),saving=ref(false),codeGenerating=ref(false)
const unitOptions=ref([]),staffOptions=ref([]),deptOptions=ref([]),styleOptions=ref([])
const draftItems=ref([]),draftPlans=ref([])
const itemDialog=ref(false),planDialog=ref(false),itemEditingKey=ref(null),planEditingKey=ref(null)
const itemForm=reactive({}),planForm=reactive({})
let draftSeq=1
const orderTypeOptions=[{label:'内销订单',value:1},{label:'加工单',value:2},{label:'返单',value:3}]
const orderStatusOptions=[{label:'初始',value:1},{label:'待生产',value:2},{label:'生产中',value:3},{label:'部分交付',value:4},{label:'已完成',value:5},{label:'延期',value:6},{label:'暂停',value:7},{label:'取消',value:8}]
const planNodeOptions=[{label:'定金/预付',value:1},{label:'进度款/到货款',value:2},{label:'尾款',value:3},{label:'补款',value:4},{label:'质保金',value:5},{label:'检查/服务费',value:6}]
const planStatusOptions=[{label:'未收付',value:0},{label:'部分',value:1},{label:'已结清',value:2},{label:'逾期',value:3}]
// 返回 YYYY-MM-DD 格式的今天日期，作为表单默认日期。
const today=()=>new Date().toLocaleDateString('sv-SE')
const form=reactive({orderNo:'',unitId:null,orderType:1,deptId:null,salesmanId:null,contractNo:'',orderDate:today(),startDate:null,deliveryDatePlan:null,deliveryDateAct:null,currency:'CNY',qtyTotal:0,amountTotal:0,taxRate:0.13,amountTax:0,costPlan:0,costAct:0,depositAmount:0,payDatePlan:null,isProducible:0,releaseTime:null,status:1,delayDays:0,remark:''})
const rules={orderNo:[{required:true,message:'请输入订单号',trigger:'blur'}],unitId:[{required:true,message:'请选择客户单位',trigger:'change'}],orderDate:[{required:true,message:'请选择下单日期',trigger:'change'}],deliveryDatePlan:[{required:true,message:'请选择预计交付日期',trigger:'change'}]}
// 把关联记录转换为下拉框显示文本。
const optionLabel=(key,item)=>lookupDefinitions[key].label(item)
// 页面显示辅助函数：统一处理关联选项和枚举文本。
const optionText=(options,value)=>options.find(x=>x.value===value)?.label||value
// 按人民币两位小数格式显示金额。
const money=value=>value==null?'—':'¥'+Number(value).toFixed(2)
// 格式化款式下拉选项显示文本。
const styleLabel=id=>{const item=styleOptions.value.find(x=>Number(x.id)===Number(id));return item?optionLabel('styles',item):'—'}
// 格式化客户单位下拉选项显示文本。
const unitLabel=id=>{const item=unitOptions.value.find(x=>Number(x.id)===Number(id));return item?optionLabel('units',item):''}

onMounted(async()=>{loading.value=true;try{const [units,staffs,depts,styles]=await Promise.all([loadLookupData('units'),loadLookupData('staffs'),loadLookupData('depts'),loadLookupData('styles')]);unitOptions.value=units;staffOptions.value=staffs;deptOptions.value=depts;styleOptions.value=styles}catch(error){ElMessage.error(error?.message||'加载关联数据失败')}finally{loading.value=false}})
// 调用编码服务生成订单号并回填只读字段。
async function fillOrderNo(){if(codeGenerating.value)return;codeGenerating.value=true;try{const r=await generateCode('ORDER');form.orderNo=r.data?.data?.previewCode;ElMessage.success('已生成订单号：'+form.orderNo)}catch(error){ElMessage.error(error?.response?.data?.message||'生成订单号失败')}finally{codeGenerating.value=false}}
// 打开新增订单明细对话框并初始化空白表单。
function openItemCreate(){itemEditingKey.value=null;Object.keys(itemForm).forEach(k=>delete itemForm[k]);Object.assign(itemForm,{_key:'item-'+draftSeq++,itemNo:draftItems.value.length+1,styleId:null,styleCode:'',color:'',size:'',qty:0,unitPrice:0,amount:0,taxRate:0.13,deliveryDatePlan:form.deliveryDatePlan,qtyShipped:0,techDesc:'',remark:''});itemDialog.value=true}
// 打开明细编辑对话框并回填当前行数据。
function openItemEdit(row){itemEditingKey.value=row._key;Object.keys(itemForm).forEach(k=>delete itemForm[k]);Object.assign(itemForm,row);itemDialog.value=true}
// 选择款式后自动回填款式编码。
function syncStyleCode(id){const item=styleOptions.value.find(x=>Number(x.id)===Number(id));if(item)itemForm.styleCode=item.styleCode||''}
// 校验并把当前明细暂存到页面，保存主单时统一提交。
function saveDraftItem(){if(!itemForm.styleId||!itemForm.styleCode){ElMessage.warning('请选择款式并填写款号');return}const copy=JSON.parse(JSON.stringify(itemForm));const idx=draftItems.value.findIndex(x=>x._key===itemEditingKey.value);if(idx>=0)draftItems.value[idx]=copy;else draftItems.value.push(copy);itemDialog.value=false;ElMessage.success('订单明细已加入')}
// 从当前页面临时订单明细中移除选中行。
function removeDraftItem(row){draftItems.value=draftItems.value.filter(x=>x._key!==row._key)}
// 打开新增收款计划对话框并初始化空白表单。
function openPlanCreate(){planEditingKey.value=null;Object.keys(planForm).forEach(k=>delete planForm[k]);Object.assign(planForm,{_key:'plan-'+draftSeq++,planNo:'',seqNo:draftPlans.value.length+1,nodeType:1,ratio:null,planDate:'',planAmount:0,status:0,remark:''});planDialog.value=true}
// 打开编辑收付款计划对话框并回填当前行。
function openPlanEdit(row){planEditingKey.value=row._key;Object.keys(planForm).forEach(k=>delete planForm[k]);Object.assign(planForm,row);planDialog.value=true}
// 校验并把当前收付款计划暂存到页面，保存主单时统一提交。
function saveDraftPlan(){if(!planForm.planNo||!planForm.planDate){ElMessage.warning('请填写计划单号和计划日期');return}const copy=JSON.parse(JSON.stringify(planForm));const idx=draftPlans.value.findIndex(x=>x._key===planEditingKey.value);if(idx>=0)draftPlans.value[idx]=copy;else draftPlans.value.push(copy);planDialog.value=false;ElMessage.success('收款计划已加入')}
// 从当前页面临时收款计划中移除选中行。
function removeDraftPlan(row){draftPlans.value=draftPlans.value.filter(x=>x._key!==row._key)}
// 保存订单、订单明细和收款计划，成功后进入订单详情页。
async function save(){
  if(!form.orderNo)await fillOrderNo();if(!form.orderNo)return
  const valid=await formRef.value?.validate().catch(()=>false);if(!valid)return
  saving.value=true
  try{
    const response=await createOne('/od/orders',form)
    if(response.data?.code!==0)throw new Error(response.data?.message||'保存订单失败')
    const orderId=response.data.data.id,orderNo=response.data.data.orderNo||form.orderNo
    const childResults=await Promise.allSettled([
      ...draftItems.value.map(item=>{const {_key,...data}=item;return createOrderItem({...data,orderId,deptId:form.deptId})}),
      ...draftPlans.value.map(plan=>{const {_key,...data}=plan;return createPlan({...data,deptId:form.deptId,direction:1,sourceType:1,sourceId:orderId,sourceNo:orderNo,partnerType:1,partnerId:form.unitId,partnerName:unitLabel(form.unitId),paidAmount:0})})
    ])
    const failed=childResults.filter(x=>x.status==='rejected').length
    if(failed)ElMessage.warning('订单已保存，但有 '+failed+' 条明细或收款计划提交失败，请在订单详情中补充')
    else ElMessage.success('订单、明细和收款计划已保存')
    await router.push('/orders/'+orderId)
  }catch(error){ElMessage.error(error?.response?.data?.message||error?.message||'保存订单失败')}
  finally{saving.value=false}
}
</script>

<style scoped>
.order-breadcrumb{margin:0 0 7px;color:#2b6cb0;font-size:12px}.detail-section{margin-bottom:14px}.detail-section-title{display:flex;align-items:center;gap:12px;margin-bottom:16px;padding-bottom:12px;border-bottom:1px solid #edf0f4}.detail-section-title>span{width:32px;height:32px;display:grid;place-items:center;border-radius:50%;background:#eaf2fb;color:#2b6cb0;font-size:11px;font-weight:700}.detail-section-title strong{display:block;color:#1f3a5f;font-size:15px}.detail-section-title small{display:block;margin-top:3px;color:#98a2b3;font-size:11px}.detail-section-heading{display:flex;justify-content:space-between;align-items:center;margin-bottom:14px}.detail-section-title.compact{margin:0;padding:0;border:0}.detail-grid-3{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:0 18px}.detail-grid-2{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:0 18px;max-height:60vh;overflow-y:auto}.detail-wide{grid-column:1/-1}.detail-grid-3 :deep(.el-form-item__label),.detail-grid-2 :deep(.el-form-item__label){justify-content:flex-start}.detail-grid-3 :deep(.el-form-item),.detail-grid-2 :deep(.el-form-item){margin-bottom:16px}.order-submit-bar{position:sticky;bottom:0;z-index:2;display:flex;justify-content:flex-end;gap:9px;padding:13px 16px;border:1px solid #e4e7ed;border-radius:8px;background:rgba(255,255,255,.96);box-shadow:0 -4px 16px rgba(31,58,95,.08)}@media(max-width:1050px){.detail-grid-3{grid-template-columns:repeat(2,minmax(0,1fr))}}@media(max-width:680px){.detail-grid-3,.detail-grid-2{grid-template-columns:1fr}}
</style>
