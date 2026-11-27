<template>
  <section class="order-list-page">
    <div class="page-heading">
      <div>
        <p class="order-breadcrumb">订单管理 / 订单列表</p>
        <h1 class="page-title">订单列表</h1>
        <p class="page-desc">按客户、业务员、状态和交期查询货物订单，流程详情只读展示下游结果。</p>
      </div>
      <el-button type="primary" @click="router.push('/orders/new')">新增订单</el-button>
    </div>

    <section class="content-panel filter-panel">
      <el-input v-model="filters.keyword" clearable placeholder="订单号 / 客户名称 / 合同号" style="width:230px" @keyup.enter="search" />
      <el-select v-model="filters.unitId" clearable filterable placeholder="客户单位" style="width:180px">
        <el-option v-for="item in unitOptions" :key="item.id" :label="item.unitName" :value="item.id" />
      </el-select>
      <el-select v-model="filters.salesmanId" clearable filterable placeholder="业务员" style="width:170px">
        <el-option v-for="item in staffOptions" :key="item.id" :label="item.staffName + '（' + item.staffCode + '）'" :value="item.id" />
      </el-select>
      <el-select v-model="filters.status" clearable placeholder="订单状态" style="width:160px">
        <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-select v-model="filters.deptId" clearable filterable placeholder="归属部门" style="width:160px">
        <el-option v-for="item in deptOptions" :key="item.id" :label="item.deptName" :value="item.id" />
      </el-select>
      <el-date-picker v-model="filters.deliveryStart" type="date" value-format="YYYY-MM-DD" placeholder="交期开始" style="width:145px" />
      <el-date-picker v-model="filters.deliveryEnd" type="date" value-format="YYYY-MM-DD" placeholder="交期结束" style="width:145px" />
      <el-button type="primary" plain @click="search">查询</el-button>
      <el-button @click="resetFilters">重置</el-button>
    </section>

    <section class="content-panel table-panel">
      <el-table v-loading="loading" :data="rows" border stripe row-key="id">
        <el-table-column label="订单号" width="155"><template #default="{ row }"><el-button link type="primary" @click="router.push('/orders/' + row.id)">{{ row.orderNo }}</el-button></template></el-table-column>
        <el-table-column label="客户" width="160">
          <template #default="{ row }">{{ unitLabel(row.unitId) }}</template>
        </el-table-column>
        <el-table-column label="订单类型" width="100">
          <template #default="{ row }">{{ optionText(orderTypeOptions, row.orderType) }}</template>
        </el-table-column>
        <el-table-column label="业务员" width="135">
          <template #default="{ row }">{{ staffLabel(row.salesmanId) }}</template>
        </el-table-column>
        <el-table-column prop="orderDate" label="下单日期" width="110" />
        <el-table-column prop="deliveryDatePlan" label="预计交期" width="110" />
        <el-table-column prop="qtyTotal" label="数量" width="90" />
        <el-table-column label="订单金额" width="115">
          <template #default="{ row }">{{ money(row.amountTotal) }}</template>
        </el-table-column>
        <el-table-column label="预计成本" width="115">
          <template #default="{ row }">{{ money(row.costPlan) }}</template>
        </el-table-column>
        <el-table-column label="毛利额" width="110">
          <template #default="{ row }"><span :class="profitClass(row.grossProfit)">{{ money(row.grossProfit) }}</span></template>
        </el-table-column>
        <el-table-column label="毛利率" width="95">
          <template #default="{ row }"><span :class="profitClass(row.grossProfit)">{{ percent(row.grossMarginRate) }}</span></template>
        </el-table-column>
        <el-table-column label="评审" width="90">
          <template #default="{ row }">{{ optionText(reviewOptions, row.reviewResult) }}</template>
        </el-table-column>
        <el-table-column label="已收定金" width="110">
          <template #default="{ row }">{{ money(row.depositAmount) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="125">
          <template #default="{ row }"><el-tag :type="statusType(row.status)" effect="light">{{ optionText(statusOptions, row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="放行" width="90">
          <template #default="{ row }"><el-tag :type="row.isProducible === 1 ? 'success' : 'info'">{{ row.isProducible === 1 ? '已放行' : '未放行' }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="delayDays" label="逾期天数" width="95" />
        <el-table-column label="操作" fixed="right" width="285" align="center">
          <template #default="{ row }">
            <el-button v-if="row.status === 1 || row.status === 9" link type="warning" @click="openAudit(row)">{{ row.status === 9 ? '重新审核' : '订单审核' }}</el-button>
            <el-button v-if="row.status === 2" link type="primary" @click="openSamples(row)">制作样衣</el-button>
            <el-button v-if="row.status === 11" link type="primary" @click="openPlan(row)">生产计划</el-button>
            <el-button v-if="row.status !== 5 && row.status !== 8" link type="danger" @click="openClose(row)">关闭订单</el-button>
            <span v-else>—</span>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="current" v-model:page-size="size" class="pager" background layout="total, sizes, prev, pager, next, jumper" :page-sizes="[10,20,50,100]" :total="total" @current-change="load" @size-change="search" />
    </section>

    <el-dialog v-model="closeVisible" title="关闭订单" width="520px" destroy-on-close>
      <el-form label-width="100px">
        <el-form-item label="关闭原因"><el-select v-model="closeForm.closeReasonType" style="width:100%"><el-option v-for="item in closeTypeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
        <el-form-item label="原因说明"><el-input v-model="closeForm.closeReason" type="textarea" :rows="4" maxlength="200" show-word-limit /></el-form-item>
      </el-form>
      <template #footer><el-button @click="closeVisible=false">取消</el-button><el-button type="danger" :loading="closing" @click="submitClose">确认关闭</el-button></template>
    </el-dialog>

    <el-dialog v-model="auditVisible" title="订单审核" width="520px" destroy-on-close>
      <el-form label-width="100px">
        <el-form-item label="审核结果"><el-radio-group v-model="auditForm.auditResult"><el-radio value="pass">通过</el-radio><el-radio value="reject">不通过，待复议</el-radio></el-radio-group></el-form-item>
        <el-form-item label="审核备注"><el-input v-model="auditForm.auditRemark" type="textarea" :rows="4" maxlength="200" show-word-limit /></el-form-item>
      </el-form>
      <template #footer><el-button @click="auditVisible=false">取消</el-button><el-button type="primary" :loading="actionSaving" @click="submitAudit">确认审核</el-button></template>
    </el-dialog>

    <el-dialog v-model="samplesVisible" title="制作样衣" width="740px" destroy-on-close>
      <el-alert title="请选择需要制作样衣的订单明细" type="info" show-icon :closable="false" style="margin-bottom:14px" />
      <el-checkbox-group v-model="selectedItemIds"><el-table :data="orderItems" border stripe><el-table-column width="55"><template #default="{ row }"><el-checkbox :value="row.id" /></template></el-table-column><el-table-column prop="itemNo" label="行号" width="70" /><el-table-column label="款式" min-width="180"><template #default="{ row }">{{ row.styleCode || row.styleId }}</template></el-table-column><el-table-column prop="color" label="颜色" width="100" /><el-table-column prop="size" label="尺码" width="80" /><el-table-column prop="qty" label="数量" width="90" /></el-table></el-checkbox-group>
      <template #footer><el-button @click="samplesVisible=false">取消</el-button><el-button type="primary" :loading="actionSaving" @click="submitSamples">生成样衣</el-button></template>
    </el-dialog>

    <el-dialog v-model="planVisible" title="创建生产计划" width="520px" destroy-on-close>
      <el-form label-width="100px"><el-form-item label="工序" required><el-select v-model="planForm.processId" filterable style="width:100%"><el-option v-for="item in processOptions" :key="item.id" :label="item.processCode + ' ' + item.processName" :value="item.id" /></el-select></el-form-item><el-form-item label="计划数量" required><el-input v-model="planForm.planQty" inputmode="decimal" /></el-form-item></el-form>
      <template #footer><el-button @click="planVisible=false">取消</el-button><el-button type="primary" :loading="actionSaving" @click="submitPlan">保存计划</el-button></template>
    </el-dialog>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { auditOrder, closeOrderFlow, createOrderProductionPlan, createOrderSamples, getOrderFlow, getOrderList } from '../api/orderDetail'
import { loadLookupData } from '../api/lookups'

const router = useRouter()
const loading = ref(false)
const rows = ref([])
const total = ref(0)
const current = ref(1)
const size = ref(10)
const unitOptions = ref([])
const staffOptions = ref([])
const deptOptions = ref([])
const processOptions = ref([])
const auditVisible = ref(false)
const samplesVisible = ref(false)
const planVisible = ref(false)
const actionSaving = ref(false)
const actionRow = ref(null)
const orderItems = ref([])
const selectedItemIds = ref([])
const auditForm = reactive({ auditResult: 'pass', auditRemark: '' })
const planForm = reactive({ processId: null, planQty: 0 })
const filters = reactive({ keyword: '', unitId: null, salesmanId: null, status: null, deptId: null, deliveryStart: '', deliveryEnd: '' })
const orderTypeOptions = [{ label: '内销订单', value: 1 }, { label: '加工单', value: 2 }, { label: '返单', value: 3 }]
const statusOptions = [{ label: '初始', value: 1 }, { label: '已审核', value: 2 }, { label: '生产中', value: 3 }, { label: '部分交付', value: 4 }, { label: '已完成', value: 5 }, { label: '延期', value: 6 }, { label: '暂停', value: 7 }, { label: '已关闭', value: 8 }, { label: '待复议', value: 9 }, { label: '样衣生成中', value: 10 }, { label: '待生产', value: 11 }]
const reviewOptions = [{ label: '未评审', value: 0 }, { label: '可做', value: 1 }, { label: '不可做', value: 2 }]
const closeTypeOptions = [{ label: '预计成本高于订单价值', value: 1 }, { label: '交期无法满足', value: 2 }, { label: '客户取消', value: 3 }, { label: '其他', value: 4 }]
const closeVisible = ref(false)
const closing = ref(false)
const closingRow = ref(null)
const closeForm = reactive({ closeReasonType: 3, closeReason: '' })

// 按当前筛选条件加载订单分页数据。
async function load() {
  loading.value = true
  try {
    const response = await getOrderList({ current: current.value, size: size.value, ...filters })
    const payload = response.data
    if (payload?.code !== 0) throw new Error(payload?.message || '查询订单失败')
    rows.value = payload.data?.records || []
    total.value = payload.data?.total || 0
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || error.message || '查询订单失败')
  } finally { loading.value = false }
}
// 回到第一页重新查询。
function search() { current.value = 1; load() }
// 清空全部筛选条件。
function resetFilters() { Object.assign(filters, { keyword: '', unitId: null, salesmanId: null, status: null, deptId: null, deliveryStart: '', deliveryEnd: '' }); search() }
// 并行加载客户、业务员和部门筛选数据。
async function loadOptions() {
  try {
    const [units, staffs, depts, processes] = await Promise.all([loadLookupData('units'), loadLookupData('staffs'), loadLookupData('depts'), loadLookupData('processes')])
    unitOptions.value = units; staffOptions.value = staffs; deptOptions.value = depts; processOptions.value = processes
  } catch (error) { ElMessage.error(error?.message || '加载筛选数据失败') }
}
// 返回客户单位显示文本。
function unitLabel(id) { const item = unitOptions.value.find(x => Number(x.id) === Number(id)); return item?.unitName || '—' }
// 返回业务员显示文本。
function staffLabel(id) { const item = staffOptions.value.find(x => Number(x.id) === Number(id)); return item ? item.staffName + '（' + item.staffCode + '）' : '—' }
// 将枚举代码转换为中文。
function optionText(options, value) { return options.find(x => x.value === value)?.label || '—' }
// 格式化金额。
function money(value) { return value == null ? '¥0.00' : '¥' + Number(value).toFixed(2) }
// 将毛利率小数转为百分比。
function percent(value) { return (Number(value || 0) * 100).toFixed(2) + '%' }
// 返回毛利显示颜色。
function profitClass(value) { return Number(value || 0) < 0 ? 'profit-loss' : Number(value || 0) === 0 ? 'profit-even' : 'profit-good' }
// 返回订单状态标签颜色。
function statusType(status) { return ({ 1: 'info', 2: 'warning', 3: 'primary', 4: 'warning', 5: 'success', 6: 'danger', 7: 'warning', 8: 'danger', 9: 'warning', 10: 'primary', 11: 'warning' })[status] || 'info' }
// 打开订单审核弹框。
function openAudit(row) { actionRow.value = row; auditForm.auditResult = 'pass'; auditForm.auditRemark = ''; auditVisible.value = true }
// 提交订单审核并刷新列表。
async function submitAudit() { actionSaving.value = true; try { const r = await auditOrder(actionRow.value.id, auditForm); if (r.data?.code !== 0) throw new Error(r.data?.message || '审核失败'); ElMessage.success('订单审核完成'); auditVisible.value = false; await load() } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '审核失败') } finally { actionSaving.value = false } }
// 加载订单明细并打开制作样衣弹框。
async function openSamples(row) { actionRow.value = row; selectedItemIds.value = []; try { const r = await getOrderFlow(row.id); orderItems.value = r.data?.data?.items || []; samplesVisible.value = true } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '加载订单明细失败') } }
// 批量生成样衣并刷新列表。
async function submitSamples() { if (!selectedItemIds.value.length) return ElMessage.warning('请至少选择一条订单明细'); actionSaving.value = true; try { const r = await createOrderSamples(actionRow.value.id, { selectedItemIds: selectedItemIds.value }); if (r.data?.code !== 0) throw new Error(r.data?.message || '生成样衣失败'); ElMessage.success('样衣生成成功'); samplesVisible.value = false; await load() } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '生成样衣失败') } finally { actionSaving.value = false } }
// 打开订单生产计划弹框。
async function openPlan(row) { actionRow.value = row; planForm.processId = null; try { const r = await getOrderFlow(row.id); planForm.planQty = Number(r.data?.data?.order?.qtyTotal || row.qtyTotal || 0); planVisible.value = true } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '加载订单信息失败') } }
// 创建生产计划并刷新列表。
async function submitPlan() { if (!planForm.processId) return ElMessage.warning('请选择工序'); actionSaving.value = true; try { const r = await createOrderProductionPlan(actionRow.value.id, { processId: planForm.processId, planQty: Number(planForm.planQty || 0) }); if (r.data?.code !== 0) throw new Error(r.data?.message || '创建生产计划失败'); ElMessage.success('生产计划创建成功'); planVisible.value = false; await load() } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '创建生产计划失败') } finally { actionSaving.value = false } }
// 打开订单关闭弹框。
function openClose(row) { closingRow.value = row; closeForm.closeReasonType = 3; closeForm.closeReason = ''; closeVisible.value = true }
// 提交订单关闭申请并刷新列表。
async function submitClose() {
  if (!closeForm.closeReason.trim()) return ElMessage.warning('请填写关闭原因')
  closing.value = true
  try {
    const response = await closeOrderFlow(closingRow.value.id, closeForm)
    if (response.data?.code !== 0) throw new Error(response.data?.message || '关闭订单失败')
    ElMessage.success('订单已关闭'); closeVisible.value = false; await load()
  } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '关闭订单失败') }
  finally { closing.value = false }
}

onMounted(() => { loadOptions(); load() })
</script>

<style scoped>
.order-breadcrumb{margin:0 0 7px;color:#2b6cb0;font-size:12px}.filter-panel{display:flex;flex-wrap:wrap;gap:10px;margin-bottom:14px}.table-panel{padding:0}.pager{margin:16px}.profit-good{color:#1d8a55;font-weight:700}.profit-even{color:#b7791f;font-weight:700}.profit-loss{color:#c53030;font-weight:700}
</style>
