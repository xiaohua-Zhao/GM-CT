<template>
  <section class="production-plan-page">
    <div class="page-heading">
      <div>
        <p class="order-breadcrumb">生产管理 / 生产计划</p>
        <h1 class="page-title">生产计划</h1>
        <p class="page-desc">从评审通过且样衣已放行的订单中创建生产计划，一个订单只能建立一张有效计划。</p>
      </div>
      <el-button type="primary" @click="openCreate">新增生产计划</el-button>
    </div>

    <section class="content-panel filter-panel">
      <el-input v-model="filters.keyword" clearable placeholder="生产计划单号" style="width:220px" @keyup.enter="search" />
      <el-select v-model="filters.status" clearable placeholder="计划状态" style="width:150px">
        <el-option v-for="item in planStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-button type="primary" plain @click="search">查询</el-button>
      <el-button @click="resetFilters">重置</el-button>
    </section>

    <section class="content-panel table-panel">
      <el-table v-loading="loading" :data="rows" border stripe row-key="id">
        <el-table-column prop="prodPlanNo" label="生产计划单号" width="165" />
        <el-table-column label="订单号" width="150"><template #default="{ row }">{{ orderNoMap[row.orderId] || row.orderId || '—' }}</template></el-table-column>
        <el-table-column label="客户" min-width="160"><template #default="{ row }">{{ customerName(row.orderId) }}</template></el-table-column>
        <el-table-column label="归属部门" width="135"><template #default="{ row }">{{ deptName(row.deptId) }}</template></el-table-column>
        <el-table-column prop="prodQty" label="生产数量" width="100" />
        <el-table-column prop="startDate" label="计划开工" width="115" />
        <el-table-column prop="endDate" label="计划完工" width="115" />
        <el-table-column label="计划状态" width="110"><template #default="{ row }"><el-tag :type="planStatusType(row.status)">{{ optionText(planStatusOptions, row.status) }}</el-tag></template></el-table-column>
        <el-table-column label="计划人" width="125"><template #default="{ row }">{{ staffName(row.plannerId) }}</template></el-table-column>
        <el-table-column prop="releaseTime" label="下达时间" width="165" />
        <el-table-column label="操作" fixed="right" width="150" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="removePlan(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="current" v-model:page-size="size" class="pager" background layout="total, sizes, prev, pager, next, jumper" :page-sizes="[10,20,50,100]" :total="total" @current-change="load" @size-change="search" />
    </section>

    <el-dialog v-model="planVisible" :title="editingId ? '编辑生产计划' : '新增生产计划'" width="720px" destroy-on-close>
      <el-form :model="form" label-position="left" label-width="110px">
        <div class="plan-form-grid">
          <el-form-item label="生产计划号"><el-input v-model="form.prodPlanNo" readonly :placeholder="editingId ? '' : '保存后自动生成'" /></el-form-item>
          <el-form-item label="订单" required>
            <el-input :model-value="selectedOrderLabel" readonly placeholder="请从弹框选择订单">
              <template #append><el-button :disabled="!!editingId" @click="openOrderPicker">选择订单</el-button></template>
            </el-input>
          </el-form-item>
          <el-form-item label="生产工序" required>
            <el-select v-model="form.processStage" style="width:100%" placeholder="请选择一个生产工序">
              <el-option v-for="item in processStageOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="归属部门"><el-select v-model="form.deptId" clearable filterable style="width:100%"><el-option v-for="item in deptOptions" :key="item.id" :label="item.deptName" :value="item.id" /></el-select></el-form-item>
          <el-form-item label="计划人"><el-select v-model="form.plannerId" clearable filterable style="width:100%"><el-option v-for="item in staffOptions" :key="item.id" :label="item.staffName + '（' + item.staffCode + '）'" :value="item.id" /></el-select></el-form-item>
          <el-form-item label="生产数量"><el-input v-model="form.prodQty" inputmode="numeric" @input="onIntegerInput('prodQty')" @blur="normalizeNumber('prodQty')" /></el-form-item>
          <el-form-item label="计划状态"><el-select v-model="form.status" style="width:100%"><el-option v-for="item in planStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
          <el-form-item label="计划开工" required><el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="计划完工" required><el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="下达人"><el-select v-model="form.releaseBy" clearable filterable style="width:100%"><el-option v-for="item in staffOptions" :key="item.id" :label="item.staffName + '（' + item.staffCode + '）'" :value="item.id" /></el-select></el-form-item>
          <el-form-item label="下达时间"><el-date-picker v-model="form.releaseTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" /></el-form-item>
          <el-form-item label="备注" class="plan-wide"><el-input v-model="form.remark" type="textarea" :rows="3" maxlength="200" show-word-limit /></el-form-item>
        </div>
      </el-form>
      <template #footer><el-button @click="planVisible=false">取消</el-button><el-button type="primary" :loading="saving" @click="savePlan">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="orderPickerVisible" title="选择可生产订单" width="980px" destroy-on-close>
      <div class="picker-filters">
        <el-input v-model="orderFilters.keyword" clearable placeholder="订单号 / 客户名称" style="width:210px" @keyup.enter="searchEligibleOrders" />
        <el-select v-model="orderFilters.unitId" clearable filterable placeholder="客户单位" style="width:180px"><el-option v-for="item in unitOptions" :key="item.id" :label="item.unitName" :value="item.id" /></el-select>
        <el-date-picker v-model="orderFilters.orderDateStart" type="date" value-format="YYYY-MM-DD" placeholder="下单开始" style="width:140px" />
        <el-date-picker v-model="orderFilters.orderDateEnd" type="date" value-format="YYYY-MM-DD" placeholder="下单结束" style="width:140px" />
        <el-date-picker v-model="orderFilters.deliveryDateStart" type="date" value-format="YYYY-MM-DD" placeholder="交期开始" style="width:140px" />
        <el-date-picker v-model="orderFilters.deliveryDateEnd" type="date" value-format="YYYY-MM-DD" placeholder="交期结束" style="width:140px" />
        <el-button type="primary" plain @click="searchEligibleOrders">查询</el-button>
      </div>
      <el-table v-loading="orderLoading" :data="eligibleRows" border stripe highlight-current-row row-key="id" height="360px" @current-change="selectOrder">
        <el-table-column width="48"><template #default="{ row }"><el-radio :model-value="selectedOrder?.id" :value="row.id" @change="selectedOrder = row">&nbsp;</el-radio></template></el-table-column>
        <el-table-column prop="orderNo" label="订单号" width="155" />
        <el-table-column label="客户" min-width="170"><template #default="{ row }">{{ unitName(row.unitId) }}</template></el-table-column>
        <el-table-column prop="orderDate" label="下单日期" width="110" />
        <el-table-column prop="deliveryDatePlan" label="预计交期" width="110" />
        <el-table-column prop="qtyTotal" label="数量" width="90" />
        <el-table-column label="金额" width="115"><template #default="{ row }">{{ money(row.amountTotal) }}</template></el-table-column>
        <el-table-column label="状态" width="125"><template #default="{ row }">{{ optionText(orderStatusOptions, row.status) }}</template></el-table-column>
      </el-table>
      <el-pagination v-model:current-page="orderCurrent" v-model:page-size="orderSize" class="pager" background layout="total, prev, pager, next" :total="orderTotal" @current-change="loadEligibleOrders" />
      <template #footer><el-button @click="orderPickerVisible=false">取消</el-button><el-button type="primary" :disabled="!selectedOrder" @click="confirmOrder">确定选择</el-button></template>
    </el-dialog>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPlanPage, getEligibleOrders, getPlanTasks, createPlan, updatePlan, deletePlan } from '../api/productionPlans'
import { getOrderFlow } from '../api/orderDetail'
import { loadLookupData } from '../api/lookups'

const loading = ref(false)
const saving = ref(false)
const orderLoading = ref(false)
const rows = ref([])
const total = ref(0)
const current = ref(1)
const size = ref(10)
const eligibleRows = ref([])
const orderTotal = ref(0)
const orderCurrent = ref(1)
const orderSize = ref(10)
const planVisible = ref(false)
const orderPickerVisible = ref(false)
const editingId = ref(null)
const selectedOrder = ref(null)
const orderInfo = ref(null)
const unitOptions = ref([])
const staffOptions = ref([])
const deptOptions = ref([])
const form = reactive({})
const filters = reactive({ keyword: '', status: null })
const orderFilters = reactive({ keyword: '', unitId: null, orderDateStart: '', orderDateEnd: '', deliveryDateStart: '', deliveryDateEnd: '' })
const planStatusOptions = [{ label: '待下达', value: 1 }, { label: '已下达', value: 2 }, { label: '生产中', value: 3 }, { label: '已完工', value: 4 }, { label: '暂停', value: 5 }, { label: '取消', value: 6 }]
const processStageOptions = [{ label: '裁剪', value: 1 }, { label: '缝制', value: 2 }, { label: '剪线', value: 3 }, { label: '整烫', value: 4 }, { label: '包装', value: 5 }, { label: '质检', value: 6 }]
const orderStatusOptions = [{ label: '初始', value: 1 }, { label: '已审核', value: 2 }, { label: '生产中', value: 3 }, { label: '部分交付', value: 4 }, { label: '已完成', value: 5 }, { label: '延期', value: 6 }, { label: '暂停', value: 7 }, { label: '已关闭', value: 8 }, { label: '待复议', value: 9 }, { label: '样衣生成中', value: 10 }, { label: '待生产', value: 11 }]
const orderNoMap = ref({})
const orderMap = ref({})

// 加载生产计划列表。
async function load() {
  loading.value = true
  try {
    const response = await getPlanPage({ current: current.value, size: size.value, ...filters })
    if (response.data?.code !== 0) throw new Error(response.data?.message || '查询失败')
    rows.value = response.data.data?.records || []
    total.value = response.data.data?.total || 0
    await loadOrderLabels(rows.value)
  } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '查询生产计划失败') }
  finally { loading.value = false }
}
// 加载计划列表关联订单号和客户名称。
async function loadOrderLabels(list) {
  for (const row of list) {
    if (!row.orderId || orderNoMap.value[row.orderId]) continue
    try { const r = await getOrderFlow(row.orderId); const order = r.data?.data?.order; orderNoMap.value[row.orderId] = order?.orderNo || String(row.orderId); orderMap.value[row.orderId] = order } catch {}
  }
}
// 回到第一页重新查询生产计划。
function search() { current.value = 1; load() }
// 清空生产计划筛选条件。
function resetFilters() { filters.keyword = ''; filters.status = null; search() }
// 加载客户、业务员和部门下拉数据。
async function loadOptions() {
  const [units, staffs, depts] = await Promise.all([loadLookupData('units'), loadLookupData('staffs'), loadLookupData('depts')])
  unitOptions.value = units; staffOptions.value = staffs; deptOptions.value = depts
}
// 打开新增生产计划弹框。
function openCreate() { editingId.value = null; selectedOrder.value = null; orderInfo.value = null; resetForm(); planVisible.value = true }
// 打开生产计划编辑弹框。
async function openEdit(row) {
  editingId.value = row.id
  Object.keys(form).forEach(key => delete form[key])
  Object.assign(form, row)
  // 生产工序直接取计划表保存的值；历史计划没有该字段时，回退到工序任务上的阶段。
  form.processStage = row.prodProcess ?? null
  selectedOrder.value = { id: row.orderId, orderNo: orderNoMap.value[row.orderId] || String(row.orderId) }
  try { const r = await getOrderFlow(row.orderId); orderInfo.value = r.data?.data?.order || null; selectedOrder.value = { ...selectedOrder.value, ...orderInfo.value } } catch {}
  if (form.processStage == null) {
    try { const r = await getPlanTasks(row.id); form.processStage = r.data?.data?.[0]?.processStage ?? null } catch {}
  }
  planVisible.value = true
}
// 重置新增生产计划表单。
function resetForm() {
  Object.keys(form).forEach(key => delete form[key])
  Object.assign(form, { prodPlanNo: '', orderId: null, processStage: null, deptId: null, prodQty: 0, startDate: '', endDate: '', status: 1, plannerId: null, releaseBy: null, releaseTime: '', remark: '' })
}
// 打开可生产订单选择弹框。
function openOrderPicker() {
  orderFilters.keyword = ''; orderFilters.unitId = null; orderFilters.orderDateStart = ''; orderFilters.orderDateEnd = ''; orderFilters.deliveryDateStart = ''; orderFilters.deliveryDateEnd = ''
  orderCurrent.value = 1; selectedOrder.value = null; orderPickerVisible.value = true; loadEligibleOrders()
}
// 查询评审通过、已放行且尚未排产的订单。
async function loadEligibleOrders() {
  orderLoading.value = true
  try {
    const response = await getEligibleOrders({ current: orderCurrent.value, size: orderSize.value, ...orderFilters })
    if (response.data?.code !== 0) throw new Error(response.data?.message || '查询可生产订单失败')
    eligibleRows.value = response.data.data?.records || []
    orderTotal.value = response.data.data?.total || 0
  } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '查询可生产订单失败') }
  finally { orderLoading.value = false }
}
// 重置可生产订单查询条件。
function searchEligibleOrders() { orderCurrent.value = 1; loadEligibleOrders() }
// 选中订单行。
function selectOrder(row) { if (row) selectedOrder.value = row }
// 确认选择订单并回填计划表单。
function confirmOrder() {
  if (!selectedOrder.value) return ElMessage.warning('请选择订单')
  const order = selectedOrder.value
  form.orderId = order.id
  form.orderNo = order.orderNo
  form.deptId = order.deptId || form.deptId
  form.prodQty = order.qtyTotal != null ? Math.trunc(Number(order.qtyTotal)) : form.prodQty
  form.startDate = order.startDate || form.startDate
  form.endDate = order.deliveryDatePlan || form.endDate
  orderPickerVisible.value = false
}
// 生产数量只允许输入整数，实时过滤小数点和非数字字符。
function onIntegerInput(field) { form[field] = String(form[field] ?? '').replace(/\D/g, '') }
// 规范化生产数量为不小于 0 的整数。
function normalizeNumber(field) { const value = Number(form[field]); form[field] = Number.isFinite(value) && value >= 0 ? Math.floor(value) : 0 }
// 保存生产计划。
async function savePlan() {
  if (!form.orderId) return ElMessage.warning('请通过弹框选择订单')
  if (!form.processStage) return ElMessage.warning('请选择一个生产工序')
  if (!form.startDate || !form.endDate) return ElMessage.warning('请选择计划开工和完工日期')
  if (form.endDate < form.startDate) return ElMessage.warning('计划完工日不能早于开工日')
  normalizeNumber('prodQty')
  saving.value = true
  try {
    // 后端按 processStages 生成/同步工序任务，单选时只传当前工序。
    const { processStage, ...rest } = form
    const payload = { ...rest, processStages: processStage ? [processStage] : [] }
    const response = editingId.value ? await updatePlan(editingId.value, payload) : await createPlan(payload)
    if (response.data?.code !== 0) throw new Error(response.data?.message || '保存生产计划失败')
    ElMessage.success('生产计划保存成功'); planVisible.value = false; await load()
  } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '保存生产计划失败') }
  finally { saving.value = false }
}
// 删除生产计划。
async function removePlan(row) {
  try { await ElMessageBox.confirm('确认删除该生产计划吗？', '删除确认', { type: 'warning' }); const r = await deletePlan(row.id); if (r.data?.code !== 0) throw new Error(r.data?.message); ElMessage.success('删除成功'); await load() }
  catch (error) { if (error !== 'cancel' && error !== 'close') ElMessage.error(error?.response?.data?.message || error.message || '删除失败') }
}
// 返回客户单位名称。
function unitName(id) { return unitOptions.value.find(item => Number(item.id) === Number(id))?.unitName || '—' }
// 返回部门名称。
function deptName(id) { return deptOptions.value.find(item => Number(item.id) === Number(id))?.deptName || '—' }
// 返回员工姓名。
function staffName(id) { const item = staffOptions.value.find(x => Number(x.id) === Number(id)); return item ? item.staffName : '—' }
// 客户名称查询辅助。
function customerName(orderId) { const order = orderMap.value[orderId]; return order ? unitName(order.unitId) : '—' }
// 返回枚举中文。
function optionText(options, value) { return options.find(x => x.value === value)?.label || '—' }
// 格式化金额。
function money(value) { return '¥' + Number(value || 0).toFixed(2) }
// 返回计划状态颜色。
function planStatusType(status) { return ({ 1:'info',2:'warning',3:'primary',4:'success',5:'warning',6:'danger' })[status] || 'info' }
// 当前选中订单显示文本。
const selectedOrderLabel = ref('')
// 更新选中订单显示文本。
function updateSelectedOrderLabel() { selectedOrderLabel.value = selectedOrder.value ? ((selectedOrder.value.orderNo || '') + (selectedOrder.value.unitId ? ' · ' + unitName(selectedOrder.value.unitId) : '')) : '' }
// 监听选择订单变化。
import { watch } from 'vue'
watch(selectedOrder, updateSelectedOrderLabel, { immediate: true })

onMounted(() => { loadOptions(); load() })
</script>

<style scoped>
.order-breadcrumb{margin:0 0 7px;color:#2b6cb0;font-size:12px}.filter-panel,.picker-filters{display:flex;flex-wrap:wrap;gap:10px}.filter-panel{margin-bottom:14px}.picker-filters{margin-bottom:14px}.table-panel{padding:0}.pager{margin:16px}.plan-form-grid{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:0 18px}.plan-wide{grid-column:1/-1}.form-tip{margin-top:4px;color:#909399;font-size:12px;line-height:1.4}@media(max-width:800px){.plan-form-grid{grid-template-columns:1fr}.plan-wide{grid-column:auto}}
</style>
