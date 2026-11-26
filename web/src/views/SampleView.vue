<template>
  <section class="sample-page">
    <div class="page-heading">
      <div>
        <p class="order-breadcrumb">生产管理 / 样衣管理</p>
        <h1 class="page-title">样衣管理</h1>
        <p class="page-desc">登记订单样衣、提交客户确认；客户确认通过后自动放行订单，允许创建生产计划。</p>
      </div>
      <el-button type="primary" @click="openCreate">新增样衣</el-button>
    </div>

    <section class="content-panel filter-panel">
      <el-input v-model="keyword" clearable placeholder="样衣编号" style="width:220px" @keyup.enter="search" />
      <el-select v-model="statusFilter" clearable placeholder="样衣状态" style="width:150px">
        <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-button type="primary" plain @click="search">查询</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </section>

    <section class="content-panel table-panel">
      <el-table v-loading="loading" :data="rows" border stripe row-key="id">
        <el-table-column prop="sampleNo" label="样衣编号" width="150" />
        <el-table-column label="订单号" width="150"><template #default="{ row }">{{ orderNo(row.orderId) }}</template></el-table-column>
        <el-table-column label="款式" min-width="170"><template #default="{ row }">{{ styleName(row.styleId) }}</template></el-table-column>
        <el-table-column label="类型" width="95"><template #default="{ row }">{{ optionText(sampleTypeOptions, row.sampleType) }}</template></el-table-column>
        <el-table-column prop="roundNo" label="轮次" width="75" />
        <el-table-column prop="qty" label="数量" width="75" />
        <el-table-column label="打样人" width="120"><template #default="{ row }">{{ staffName(row.makerId) }}</template></el-table-column>
        <el-table-column prop="sendDate" label="寄出日期" width="110" />
        <el-table-column prop="expectBackDate" label="预计回复" width="110" />
        <el-table-column label="确认结果" width="100"><template #default="{ row }">{{ optionText(confirmOptions, row.confirmResult) }}</template></el-table-column>
        <el-table-column label="状态" width="110"><template #default="{ row }"><el-tag :type="statusType(row.status)">{{ optionText(statusOptions, row.status) }}</el-tag></template></el-table-column>
        <el-table-column label="操作" fixed="right" width="310" align="center">
          <template #default="{ row }">
            <el-button v-if="row.status === 1 || row.status === 2 || row.status === 3" link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button v-if="row.status === 1 || row.status === 2" link type="warning" @click="send(row)">提交确认</el-button>
            <el-button v-if="row.status === 2 || row.status === 3" link type="success" @click="openConfirm(row)">客户确认</el-button>
            <el-button link @click="openLogs(row)">记录</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="current" v-model:page-size="size" class="pager" background layout="total, sizes, prev, pager, next, jumper" :page-sizes="[10,20,50,100]" :total="total" @current-change="load" @size-change="search" />
    </section>

    <el-dialog v-model="formVisible" :title="editingId ? '编辑样衣' : '新增样衣'" width="760px" destroy-on-close>
      <el-form :model="form" label-position="left" label-width="110px">
        <div class="sample-form-grid">
          <el-form-item label="样衣编号"><el-input v-model="form.sampleNo" readonly :placeholder="editingId ? '' : '保存后自动生成'" /></el-form-item>
          <el-form-item label="订单" required><el-select v-model="form.orderId" filterable clearable style="width:100%" @change="loadOrderStyles"><el-option v-for="item in orderOptions" :key="item.id" :label="item.orderNo + ' · ' + unitName(item.unitId)" :value="item.id" /></el-select></el-form-item>
          <el-form-item label="款式" required><el-select v-model="form.styleId" filterable style="width:100%"><el-option v-for="item in availableStyles" :key="item.id" :label="item.styleCode + ' ' + item.styleName" :value="item.id" /></el-select></el-form-item>
          <el-form-item label="样衣类型"><el-select v-model="form.sampleType" style="width:100%"><el-option v-for="item in sampleTypeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
          <el-form-item label="打样轮次"><el-input v-model="form.roundNo" inputmode="numeric" /></el-form-item>
          <el-form-item label="数量"><el-input v-model="form.qty" inputmode="numeric" /></el-form-item>
          <el-form-item label="打样人"><el-select v-model="form.makerId" filterable clearable style="width:100%"><el-option v-for="item in staffOptions" :key="item.id" :label="item.staffName + '（' + item.staffCode + '）'" :value="item.id" /></el-select></el-form-item>
          <el-form-item label="打样费用"><el-input v-model="form.fee" inputmode="decimal" /></el-form-item>
          <el-form-item label="寄出日期"><el-date-picker v-model="form.sendDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="预计回复"><el-date-picker v-model="form.expectBackDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="快递单号"><el-input v-model="form.courierNo" maxlength="40" /></el-form-item>
          <el-form-item label="备注" class="sample-wide"><el-input v-model="form.remark" type="textarea" :rows="3" maxlength="200" show-word-limit /></el-form-item>
        </div>
      </el-form>
      <template #footer><el-button @click="formVisible=false">取消</el-button><el-button type="primary" :loading="saving" @click="save">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="confirmVisible" title="客户样衣确认" width="520px" destroy-on-close>
      <el-form label-width="100px">
        <el-form-item label="确认结果"><el-radio-group v-model="confirmForm.confirmResult"><el-radio :value="1">通过</el-radio><el-radio :value="2">不通过</el-radio></el-radio-group></el-form-item>
        <el-form-item label="确认日期"><el-date-picker v-model="confirmForm.confirmDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="客户意见"><el-input v-model="confirmForm.remark" type="textarea" :rows="3" maxlength="200" show-word-limit /></el-form-item>
      </el-form>
      <el-alert v-if="confirmForm.confirmResult === 1" title="确认通过后，订单将自动放行，允许创建生产计划" type="success" show-icon :closable="false" />
      <template #footer><el-button @click="confirmVisible=false">取消</el-button><el-button type="primary" :loading="saving" @click="submitConfirm">确认</el-button></template>
    </el-dialog>

    <el-dialog v-model="logsVisible" title="样衣跟单记录" width="680px" destroy-on-close>
      <el-timeline><el-timeline-item v-for="item in logs" :key="item.id" :timestamp="item.logTime" placement="top"><el-card shadow="never">{{ item.content || '—' }}</el-card></el-timeline-item></el-timeline>
      <el-empty v-if="!logs.length" description="暂无跟单记录" />
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createSample, deleteSample, getEligibleSampleOrders, getSampleLogs, getSamplePage, sendSample, confirmSample, updateSample } from '../api/samples'
import { getOrderFlow } from '../api/orderDetail'
import { loadLookupData } from '../api/lookups'

const loading = ref(false)
const saving = ref(false)
const rows = ref([])
const total = ref(0)
const current = ref(1)
const size = ref(10)
const keyword = ref('')
const statusFilter = ref(null)
const formVisible = ref(false)
const confirmVisible = ref(false)
const logsVisible = ref(false)
const editingId = ref(null)
const activeSample = ref(null)
const logs = ref([])
const form = reactive({})
const confirmForm = reactive({ confirmResult: 1, confirmDate: '', remark: '' })
const orderOptions = ref([])
const orderItems = ref([])
const styleOptionsList = ref([])
const staffOptions = ref([])
const unitOptions = ref([])
const orderNoMap = ref({})
const statusOptions = [{ label: '制作中', value: 1 }, { label: '已寄出', value: 2 }, { label: '客户确认中', value: 3 }, { label: '已通过', value: 4 }, { label: '未通过', value: 5 }, { label: '作废', value: 6 }]
const sampleTypeOptions = [{ label: '初样', value: 1 }, { label: '改样', value: 2 }, { label: '产前样', value: 3 }, { label: '船样', value: 4 }]
const confirmOptions = [{ label: '待确认', value: 0 }, { label: '通过', value: 1 }, { label: '不通过', value: 2 }]
const availableStyles = computed(() => orderItems.value.length ? styleOptionsList.value.filter(style => orderItems.value.some(item => Number(item.styleId) === Number(style.id))) : styleOptionsList.value)

// 分页加载样衣列表。
async function load() {
  loading.value = true
  try {
    const response = await getSamplePage({ current: current.value, size: size.value, keyword: keyword.value })
    if (response.data?.code !== 0) throw new Error(response.data?.message || '查询失败')
    rows.value = response.data.data?.records || []
    total.value = response.data.data?.total || 0
    if (statusFilter.value != null) rows.value = rows.value.filter(row => row.status === statusFilter.value)
  } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '查询样衣失败') }
  finally { loading.value = false }
}
// 回到第一页查询。
function search() { current.value = 1; load() }
// 清空筛选条件。
function resetSearch() { keyword.value = ''; statusFilter.value = null; search() }
// 加载基础下拉数据。
async function loadOptions() {
  const [styles, staffs, units] = await Promise.all([loadLookupData('styles'), loadLookupData('staffs'), loadLookupData('units')])
  styleOptionsList.value = styles; staffOptions.value = staffs; unitOptions.value = units
}
// 打开新增样衣弹框。
async function openCreate() {
  editingId.value = null; Object.keys(form).forEach(key => delete form[key])
  Object.assign(form, { sampleNo: '', orderId: null, styleId: null, sampleType: 1, roundNo: 1, qty: 1, makerId: null, sendDate: '', expectBackDate: '', fee: 0, courierNo: '', remark: '' })
  orderItems.value = []
  await loadEligibleOrders()
  formVisible.value = true
}
// 打开编辑样衣弹框。
async function openEdit(row) {
  editingId.value = row.id; Object.keys(form).forEach(key => delete form[key]); Object.assign(form, row)
  await loadEligibleOrders()
  await loadOrderStyles(row.orderId)
  formVisible.value = true
}
// 加载可制作样衣的订单。
async function loadEligibleOrders() {
  const response = await getEligibleSampleOrders('')
  if (response.data?.code === 0) {
    orderOptions.value = response.data.data || []
    orderOptions.value.forEach(order => { orderNoMap.value[order.id] = order.orderNo })
  }
}
// 加载所选订单的明细款式。
async function loadOrderStyles(orderId) {
  if (!orderId) { orderItems.value = []; return }
  try { const response = await getOrderFlow(orderId); orderItems.value = response.data?.data?.items || []; orderNoMap.value[orderId] = response.data?.data?.order?.orderNo } catch { orderItems.value = [] }
}
// 保存样衣。
async function save() {
  if (!form.orderId || !form.styleId) return ElMessage.warning('请选择订单和款式')
  form.roundNo = Number(form.roundNo || 1); form.qty = Number(form.qty || 1); form.fee = Number(form.fee || 0)
  saving.value = true
  try {
    const response = editingId.value ? await updateSample(editingId.value, form) : await createSample(form)
    if (response.data?.code !== 0) throw new Error(response.data?.message || '保存样衣失败')
    ElMessage.success('样衣保存成功'); formVisible.value = false; await load()
  } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '保存样衣失败') }
  finally { saving.value = false }
}
// 提交客户确认。
async function send(row) {
  try { await ElMessageBox.confirm('确认将该样衣提交客户确认吗？', '提交确认', { type: 'warning' }); const r = await sendSample(row.id); if (r.data?.code !== 0) throw new Error(r.data?.message); ElMessage.success('已提交客户确认'); await load() }
  catch (error) { if (error !== 'cancel' && error !== 'close') ElMessage.error(error?.response?.data?.message || error.message || '提交失败') }
}
// 打开客户确认弹框。
function openConfirm(row) { activeSample.value = row; confirmForm.confirmResult = 1; confirmForm.confirmDate = today(); confirmForm.remark = ''; confirmVisible.value = true }
// 提交客户确认结果。
async function submitConfirm() {
  saving.value = true
  try { const response = await confirmSample(activeSample.value.id, confirmForm); if (response.data?.code !== 0) throw new Error(response.data?.message || '确认失败'); ElMessage.success(confirmForm.confirmResult === 1 ? '样衣已通过，订单已放行' : '已记录客户不通过'); confirmVisible.value = false; await load() }
  catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '确认失败') }
  finally { saving.value = false }
}
// 查看样衣跟单记录。
async function openLogs(row) { activeSample.value = row; const response = await getSampleLogs(row.id); logs.value = response.data?.data || []; logsVisible.value = true }
// 删除样衣。
async function remove(row) {
  try { await ElMessageBox.confirm('确认删除该样衣吗？', '删除确认', { type: 'warning' }); const r = await deleteSample(row.id); if (r.data?.code !== 0) throw new Error(r.data?.message); ElMessage.success('删除成功'); await load() }
  catch (error) { if (error !== 'cancel' && error !== 'close') ElMessage.error(error?.response?.data?.message || error.message || '删除失败') }
}
// 返回订单号。
function orderNo(id) { return orderNoMap.value[id] || String(id || '—') }
// 返回客户名称。
function unitName(id) { return unitOptions.value.find(item => Number(item.id) === Number(id))?.unitName || '—' }
// 返回款式名称。
function styleName(id) { const item = styleOptionsList.value.find(x => Number(x.id) === Number(id)); return item ? item.styleCode + ' ' + item.styleName : '—' }
// 返回员工姓名。
function staffName(id) { return staffOptions.value.find(x => Number(x.id) === Number(id))?.staffName || '—' }
// 返回枚举中文。
function optionText(options, value) { return options.find(x => x.value === value)?.label || '—' }
// 返回状态颜色。
function statusType(status) { return ({ 1:'primary',2:'warning',3:'warning',4:'success',5:'danger',6:'info' })[status] || 'info' }
// 返回今天日期。
function today() { return new Date().toLocaleDateString('sv-SE') }

onMounted(() => { loadOptions(); load() })
</script>

<style scoped>
.order-breadcrumb{margin:0 0 7px;color:#2b6cb0;font-size:12px}.filter-panel{display:flex;flex-wrap:wrap;gap:10px;margin-bottom:14px}.table-panel{padding:0}.pager{margin:16px}.sample-form-grid{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:0 18px}.sample-wide{grid-column:1/-1}@media(max-width:800px){.sample-form-grid{grid-template-columns:1fr}.sample-wide{grid-column:auto}}
</style>
