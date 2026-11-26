<template>
  <section class="order-flow-page" v-loading="loading">
    <div class="page-heading">
      <div>
        <p class="order-breadcrumb">订单管理 / {{ pageTitle }}</p>
        <h1 class="page-title">{{ pageTitle }}</h1>
        <p class="page-desc">订单录入、评审流转、信息与进度展示；打样、排产、报工、质检、发货、收款在本页只回显结果。</p>
      </div>
      <div class="page-actions">
        <el-button @click="router.push('/orders')">返回订单列表</el-button>
        <el-button v-if="mode === 'detail' && form.editable" type="primary" @click="router.push('/orders/' + orderId + '/edit')">编辑订单</el-button>
        <el-button v-if="formEditable && mode !== 'detail'" type="primary" :loading="saving" @click="save">保存草稿</el-button>
        <el-button v-if="canReview" type="warning" @click="openReview">提交评审</el-button>
        <el-button v-if="canPause" @click="pause">暂停</el-button>
        <el-button v-if="canResume" type="success" @click="resume">恢复</el-button>
        <el-button v-if="canClose" type="danger" plain @click="openClose">关闭订单</el-button>
      </div>
    </div>

    <section class="content-panel flow-panel">
      <el-alert v-if="form.status === 8" title="订单已关闭，全链路锁定" :description="form.closeReason || '订单已关闭'" type="error" show-icon :closable="false" />
      <el-steps v-else :active="activeStep" finish-status="success" align-center>
        <el-step v-for="node in progress" :key="node.name" :title="node.name" :description="progressText(node.state)" />
      </el-steps>
    </section>

    <el-form ref="formRef" :model="form" label-position="left" label-width="112px" :disabled="!formEditable">
      <section class="content-panel detail-section">
        <div class="detail-section-title"><span>01</span><div><strong>订单基础信息</strong><small>客户、业务归属和交期信息</small></div></div>
        <div class="detail-grid-3">
          <el-form-item label="订单号"><el-input :model-value="form.orderNo || (mode === 'create' ? '保存后生成' : '')" readonly /></el-form-item>
          <el-form-item label="客户单位" required>
            <el-select v-model="form.unitId" filterable clearable style="width:100%">
              <el-option v-for="item in unitOptions" :key="item.id" :label="item.unitName || item.unitCode" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="订单类型">
            <el-select v-model="form.orderType" style="width:100%"><el-option v-for="item in orderTypeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
          </el-form-item>
          <el-form-item label="合同号"><el-input v-model="form.contractNo" maxlength="40" show-word-limit /></el-form-item>
          <el-form-item label="归属部门">
            <el-select v-model="form.deptId" filterable clearable style="width:100%"><el-option v-for="item in deptOptions" :key="item.id" :label="item.deptName" :value="item.id" /></el-select>
          </el-form-item>
          <el-form-item label="业务员">
            <el-select v-model="form.salesmanId" filterable clearable style="width:100%"><el-option v-for="item in staffOptions" :key="item.id" :label="item.staffName + '（' + item.staffCode + '）'" :value="item.id" /></el-select>
          </el-form-item>
          <el-form-item label="下单日期" required><el-date-picker v-model="form.orderDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="开始日期"><el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="预计交期" required><el-date-picker v-model="form.deliveryDatePlan" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="实际交期"><el-date-picker v-model="form.deliveryDateAct" type="date" value-format="YYYY-MM-DD" style="width:100%" disabled /></el-form-item>
          <el-form-item label="预计首款"><el-date-picker v-model="form.payDatePlan" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          <el-form-item label="币种"><el-input v-model="form.currency" maxlength="3" /></el-form-item>
        </div>
      </section>

      <section class="content-panel detail-section">
        <div class="detail-section-title"><span>02</span><div><strong>金额与生产控制</strong><small>录入核价字段，汇总和流程字段由系统回写</small></div></div>
        <div class="detail-grid-3">
          <el-form-item label="税率(%)"><el-input-number v-model="taxRatePercent" :min="0" :max="100" :precision="2" style="width:100%" /></el-form-item>
          <el-form-item label="预计成本"><el-input :model-value="money(form.costPlan)" readonly /></el-form-item>
          <el-form-item label="物料成本"><el-input :model-value="money(form.materialCost)" readonly /></el-form-item>
          <el-form-item label="用工成本"><el-input :model-value="money(form.laborCost)" readonly /></el-form-item>
          <el-form-item label="订单数量"><el-input :model-value="numberText(form.qtyTotal)" readonly /></el-form-item>
          <el-form-item label="订单金额"><el-input :model-value="money(form.amountTotal)" readonly /></el-form-item>
          <el-form-item label="价税合计"><el-input :model-value="money(form.amountTax)" readonly /></el-form-item>
          <el-form-item label="实际成本"><el-input :model-value="money(form.costAct)" readonly /></el-form-item>
          <el-form-item label="已收定金"><el-input :model-value="money(form.depositAmount)" readonly /></el-form-item>
          <el-form-item label="允许生产"><el-tag :type="form.isProducible === 1 ? 'success' : 'info'">{{ form.isProducible === 1 ? '已放行' : '未放行' }}</el-tag></el-form-item>
          <el-form-item label="订单状态"><el-tag :type="statusType(form.status)">{{ optionText(statusOptions, form.status) }}</el-tag></el-form-item>
          <el-form-item label="放行时间"><el-input :model-value="form.releaseTime || '—'" readonly /></el-form-item>
          <el-form-item label="逾期天数"><el-input :model-value="String(form.delayDays ?? 0)" readonly /></el-form-item>
          <el-form-item label="评审结论"><el-tag :type="reviewType(form.reviewResult)">{{ optionText(reviewOptions, form.reviewResult) }}</el-tag></el-form-item>
          <el-form-item label="毛利额"><el-input :model-value="money(grossProfit)" readonly :class="profitClass(grossProfit)" /></el-form-item>
          <el-form-item label="毛利率"><el-input :model-value="percent(grossMarginRate)" readonly :class="profitClass(grossProfit)" /></el-form-item>
          <el-form-item label="可做性提示"><el-tag :type="viableNow ? 'success' : 'danger'">{{ viableNow ? '满足评审条件' : '不满足可做条件' }}</el-tag></el-form-item>
          <el-form-item v-if="form.status === 8" label="关闭原因类型"><el-input :model-value="optionText(closeTypeOptions, form.closeReasonType)" readonly /></el-form-item>
          <el-form-item v-if="form.status === 8" label="关闭原因" class="detail-wide"><el-input :model-value="form.closeReason || '—'" type="textarea" :rows="2" readonly /></el-form-item>
          <el-form-item label="备注" class="detail-wide"><el-input v-model="form.remark" type="textarea" :rows="3" maxlength="300" show-word-limit /></el-form-item>
        </div>
      </section>

      <section class="content-panel detail-section">
        <div class="detail-section-heading">
          <div class="detail-section-title compact"><span>03</span><div><strong>订单明细</strong><small>一款一色一码一行，数量和金额由明细汇总</small></div></div>
          <el-button v-if="formEditable" type="primary" plain @click="addItem">添加一行</el-button>
        </div>
        <el-table :data="items" border stripe row-key="_key" empty-text="订单至少需要一条明细">
          <el-table-column prop="itemNo" label="行号" width="70" />
          <el-table-column label="款式" min-width="180">
            <template #default="{ row }">
              <el-select v-if="formEditable" v-model="row.styleId" filterable style="width:100%" @change="syncStyleCode(row)">
                <el-option v-for="item in styleOptions" :key="item.id" :label="item.styleCode + ' ' + item.styleName" :value="item.id" />
              </el-select>
              <span v-else>{{ styleText(row.styleId) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="款号" width="120"><template #default="{ row }"><el-input v-model="row.styleCode" readonly /></template></el-table-column>
          <el-table-column label="颜色" width="110">
            <template #default="{ row }"><el-select v-model="row.color" filterable allow-create clearable style="width:100%"><el-option v-for="item in colorOptions" :key="item.dictCode" :label="item.dictName" :value="item.dictName" /></el-select></template>
          </el-table-column>
          <el-table-column label="尺码" width="100">
            <template #default="{ row }"><el-select v-model="row.size" filterable allow-create clearable style="width:100%"><el-option v-for="item in sizeOptions" :key="item.dictCode" :label="item.dictName" :value="item.dictName" /></el-select></template>
          </el-table-column>
          <el-table-column label="数量" width="125"><template #default="{ row }"><el-input v-model="row.qty" inputmode="decimal" @input="syncItemAmount(row)" @blur="normalizeItemNumber(row, 'qty')" /></template></el-table-column>
          <el-table-column label="单价" width="130"><template #default="{ row }"><el-input v-model="row.unitPrice" inputmode="decimal" @input="syncItemAmount(row)" @blur="normalizeItemNumber(row, 'unitPrice')" /></template></el-table-column>
          <el-table-column label="金额" width="115"><template #default="{ row }">{{ money(row.amount) }}</template></el-table-column>
          <el-table-column label="行税率" width="155"><template #default="{ row }"><el-input v-model="row.taxRate" inputmode="decimal" @blur="normalizeItemNumber(row, 'taxRate')" /></template></el-table-column>
          <el-table-column label="行交期" width="145"><template #default="{ row }"><el-date-picker v-model="row.deliveryDatePlan" type="date" value-format="YYYY-MM-DD" style="width:100%" /></template></el-table-column>
          <el-table-column prop="qtyShipped" label="已发" width="85" />
          <el-table-column label="工艺要求" min-width="160"><template #default="{ row }"><el-input v-model="row.techDesc" maxlength="300" /></template></el-table-column>
          <el-table-column label="备注" min-width="130"><template #default="{ row }"><el-input v-model="row.remark" maxlength="200" /></template></el-table-column>
          <el-table-column v-if="formEditable" label="操作" width="80" fixed="right"><template #default="{ $index, row }"><el-button link type="danger" :disabled="Number(row.qtyShipped || 0) > 0" @click="removeItem($index)">删除</el-button></template></el-table-column>
        </el-table>
      </section>

      <section class="content-panel detail-section">
        <div class="detail-section-title"><span>04</span><div><strong>收款节点</strong><small>数据来自财务收付款计划，本页只读</small></div></div>
        <el-table :data="plans" border stripe empty-text="暂无收款节点">
          <el-table-column label="节点类型" width="120"><template #default="{ row }">{{ optionText(nodeTypeOptions, row.nodeType) }}</template></el-table-column>
          <el-table-column prop="planDate" label="计划日期" width="120" />
          <el-table-column label="计划金额" width="130"><template #default="{ row }">{{ money(row.planAmount) }}</template></el-table-column>
          <el-table-column label="已收金额" width="130"><template #default="{ row }">{{ money(row.paidAmount) }}</template></el-table-column>
          <el-table-column label="节点状态" width="120"><template #default="{ row }">{{ optionText(planStatusOptions, row.status) }}</template></el-table-column>
        </el-table>
      </section>
    </el-form>

    <el-dialog v-model="reviewVisible" title="提交可做性评审" width="560px" destroy-on-close>
      <el-alert :title="viableNow ? '当前毛利满足可做条件' : '当前毛利不满足可做条件'" :type="viableNow ? 'success' : 'error'" :description="viabilityText" show-icon :closable="false" style="margin-bottom:16px" />
      <el-form label-width="100px">
        <el-form-item label="评审结论"><el-radio-group v-model="reviewForm.reviewResult"><el-radio :value="1">可做</el-radio><el-radio :value="2">不可做</el-radio></el-radio-group></el-form-item>
        <template v-if="reviewForm.reviewResult === 2">
          <el-form-item label="关闭原因"><el-select v-model="reviewForm.closeReasonType" style="width:100%"><el-option v-for="item in closeTypeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
          <el-form-item label="原因说明"><el-input v-model="reviewForm.closeReason" type="textarea" :rows="4" maxlength="200" show-word-limit /></el-form-item>
        </template>
      </el-form>
      <template #footer><el-button @click="reviewVisible=false">取消</el-button><el-button type="primary" :loading="saving" @click="doReview">确认提交</el-button></template>
    </el-dialog>

    <el-dialog v-model="closeVisible" title="关闭订单" width="540px" destroy-on-close>
      <el-form label-width="100px">
        <el-form-item label="关闭原因"><el-select v-model="closeForm.closeReasonType" style="width:100%"><el-option v-for="item in closeTypeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
        <el-form-item label="原因说明"><el-input v-model="closeForm.closeReason" type="textarea" :rows="4" maxlength="200" show-word-limit /></el-form-item>
      </el-form>
      <template #footer><el-button @click="closeVisible=false">取消</el-button><el-button type="danger" :loading="saving" @click="doClose">确认关闭</el-button></template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { closeOrderFlow, createOrderFlow, getOrderFlow, pauseOrderFlow, previewOrderCost, resumeOrderFlow, reviewOrderFlow, updateOrderFlow } from '../api/orderDetail'
import { loadLookupData } from '../api/lookups'

const props = defineProps({
  mode: { type: String, default: 'detail' },
  orderId: { type: Number, default: null }
})
const router = useRouter()
const loading = ref(false)
const saving = ref(false)
const items = ref([])
const plans = ref([])
const progress = ref([])
const metrics = ref({ viable: false, grossProfit: 0, grossMarginRate: 0, costCompareBase: 0, minGrossMargin: 0 })
const unitOptions = ref([])
const staffOptions = ref([])
const deptOptions = ref([])
const styleOptions = ref([])
const commonOptions = ref([])
const reviewVisible = ref(false)
const closeVisible = ref(false)
const reviewForm = reactive({ reviewResult: 1, closeReasonType: 1, closeReason: '' })
const closeForm = reactive({ closeReasonType: 3, closeReason: '' })
const form = reactive({})
let keySeed = 1

const orderTypeOptions = [{ label: '内销订单', value: 1 }, { label: '加工单', value: 2 }, { label: '返单', value: 3 }]
const statusOptions = [{ label: '初始', value: 1 }, { label: '待样衣/待排产', value: 2 }, { label: '生产中', value: 3 }, { label: '部分交付', value: 4 }, { label: '已完成', value: 5 }, { label: '延期', value: 6 }, { label: '暂停', value: 7 }, { label: '关闭', value: 8 }]
const reviewOptions = [{ label: '未评审', value: 0 }, { label: '可做', value: 1 }, { label: '不可做', value: 2 }]
const closeTypeOptions = [{ label: '预计成本高于订单价值', value: 1 }, { label: '交期无法满足', value: 2 }, { label: '客户取消', value: 3 }, { label: '其他', value: 4 }]
const nodeTypeOptions = [{ label: '定金', value: 1 }, { label: '进度款', value: 2 }, { label: '尾款', value: 3 }, { label: '全额', value: 4 }]
const planStatusOptions = [{ label: '待执行', value: 0 }, { label: '部分完成', value: 1 }, { label: '已完成', value: 2 }, { label: '逾期', value: 3 }]

const isCreate = computed(() => props.mode === 'create')
const pageTitle = computed(() => isCreate.value ? '新增订单' : props.mode === 'edit' ? '编辑订单' : '订单详情')
const formEditable = computed(() => isCreate.value || (props.mode === 'edit' && form.editable === true))
const canReview = computed(() => !isCreate.value && form.status === 1)
const canPause = computed(() => !isCreate.value && [2, 3, 4].includes(form.status))
const canResume = computed(() => !isCreate.value && form.status === 7)
const canClose = computed(() => !isCreate.value && [1, 2].includes(form.status))
const taxRatePercent = computed({
  get: () => Number(((form.taxRate ?? 0.13) * 100).toFixed(2)),
  set: value => { form.taxRate = Number(value || 0) / 100; recalcTotals() }
})
const grossProfit = computed(() => Number(form.amountTotal || 0) - Number(form.costPlan || 0))
const grossMarginRate = computed(() => Number(form.amountTotal || 0) === 0 ? 0 : grossProfit.value / Number(form.amountTotal))
const viableNow = computed(() => Number(form.amountTotal || 0) > 0 && Number(form.costPlan || 0) < Number(form.amountTotal || 0) && grossMarginRate.value >= Number(metrics.value.minGrossMargin || 0))
const activeStep = computed(() => progress.value.filter(node => node.state === 'done').length)
const viabilityText = computed(() => '订单金额 ' + money(form.amountTotal) + '，预计成本 ' + money(form.costPlan) + '，其中物料成本 ' + money(form.materialCost) + '、用工成本 ' + money(form.laborCost) + '，毛利额 ' + money(grossProfit.value) + '，毛利率 ' + percent(grossMarginRate.value) + '，最低毛利率 ' + percent(metrics.value.minGrossMargin || 0))
const colorOptions = computed(() => commonOptions.value.filter(item => String(item.dictType).toUpperCase() === 'COLOR'))
const sizeOptions = computed(() => commonOptions.value.filter(item => String(item.dictType).toUpperCase() === 'SIZE'))

// 初始化空白订单。
function resetForm() {
  Object.keys(form).forEach(key => delete form[key])
  Object.assign(form, { orderNo: '', unitId: null, orderType: 1, contractNo: '', deptId: null, salesmanId: null, orderDate: today(), startDate: '', deliveryDatePlan: '', deliveryDateAct: '', payDatePlan: '', currency: 'CNY', qtyTotal: 0, amountTotal: 0, taxRate: 0.13, amountTax: 0, costPlan: 0, materialCost: 0, laborCost: 0, costAct: 0, depositAmount: 0, isProducible: 0, releaseTime: '', status: 1, delayDays: 0, reviewResult: 0, closeReasonType: null, closeReason: '', remark: '', editable: true })
  items.value = [createBlankItem(1)]
  plans.value = []
  progress.value = [{ name: '订单创建', state: 'current' }, { name: '可做性评审', state: 'pending' }, { name: '样衣确认 / 放行', state: 'pending' }, { name: '生产与交付', state: 'pending' }, { name: '订单完成', state: 'pending' }]
}
// 创建一行空白明细。
function createBlankItem(itemNo) {
  return { _key: 'item-' + keySeed++, id: null, itemNo, styleId: null, styleCode: '', color: '', size: '', qty: 1, unitPrice: 0, amount: 0, taxRate: form.taxRate ?? 0.13, deliveryDatePlan: form.deliveryDatePlan || '', qtyShipped: 0, techDesc: '', remark: '' }
}
// 追加一条明细行。
function addItem() { items.value.push(createBlankItem(items.value.length + 1)); scheduleCostPreview() }
// 删除一条尚未发货的明细行。
function removeItem(index) { if (Number(items.value[index].qtyShipped || 0) > 0) return ElMessage.warning('已发货明细不能删除'); items.value.splice(index, 1); renumberItems(); recalcTotals(); scheduleCostPreview() }
// 重新生成页面行号。
function renumberItems() { items.value.forEach((item, index) => { item.itemNo = index + 1 }) }
// 选择款式后回填款号。
function syncStyleCode(row) { const style = styleOptions.value.find(item => Number(item.id) === Number(row.styleId)); row.styleCode = style?.styleCode || ''; scheduleCostPreview() }
// 输入失焦后把数量、单价、税率规范为合法数字。
function normalizeItemNumber(row, field) {
  let value = Number(row[field])
  if (!Number.isFinite(value)) value = 0
  if (field === 'qty') value = Math.max(0, value)
  if (field === 'unitPrice') value = Math.max(0, value)
  if (field === 'taxRate') value = Math.min(1, Math.max(0, value))
  row[field] = value
  if (field === 'qty' || field === 'unitPrice') syncItemAmount(row)
}
// 按数量和单价重新计算行金额与订单汇总。
function syncItemAmount(row) { row.amount = Number((Number(row.qty || 0) * Number(row.unitPrice || 0)).toFixed(2)); recalcTotals(); scheduleCostPreview() }
// 重新计算订单数量、金额和价税合计。
let costTimer = null
// 延迟调用后端成本规则，避免连续输入时重复请求。
function scheduleCostPreview() { clearTimeout(costTimer); costTimer = setTimeout(refreshCostPreview, 300) }
// 调用后端按款式 BOM 和工序工价计算预计成本。
async function refreshCostPreview() {
  const payload = items.value.filter(item => item.styleId).map(item => ({ styleId: item.styleId, qty: item.qty }))
  try {
    const response = await previewOrderCost(payload)
    if (response.data?.code === 0) {
      form.materialCost = Number(response.data.data.materialCost || 0)
      form.laborCost = Number(response.data.data.laborCost || 0)
      form.costPlan = Number(response.data.data.totalCost || 0)
    }
  } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '成本核算失败') }
}
// 重新计算订单数量、金额和价税合计。
function recalcTotals() {
  const qty = items.value.reduce((sum, item) => sum + Number(item.qty || 0), 0)
  const amount = items.value.reduce((sum, item) => sum + Number(item.amount || 0), 0)
  form.qtyTotal = Number(qty.toFixed(2))
  form.amountTotal = Number(amount.toFixed(2))
  form.amountTax = Number((form.amountTotal * (1 + Number(form.taxRate || 0))).toFixed(2))
}
// 并行加载客户、部门、业务员、款式和颜色尺码字典。
async function loadOptions() {
  const [units, staffs, depts, styles, commons] = await Promise.all([loadLookupData('units'), loadLookupData('staffs'), loadLookupData('depts'), loadLookupData('styles'), loadLookupData('commons')])
  unitOptions.value = units; staffOptions.value = staffs; deptOptions.value = depts; styleOptions.value = styles; commonOptions.value = commons
}
// 加载订单聚合详情。
async function loadDetail() {
  if (!props.orderId) return
  const response = await getOrderFlow(props.orderId)
  if (response.data?.code !== 0) throw new Error(response.data?.message || '加载订单失败')
  const data = response.data.data
  Object.keys(form).forEach(key => delete form[key])
  Object.assign(form, data.order || {})
  items.value = (data.items || []).map((item, index) => ({ ...item, _key: item.id ? 'saved-' + item.id : 'new-' + keySeed++ }))
  plans.value = data.plans || []
  metrics.value = data.metrics || metrics.value
  progress.value = data.progress || []
  renumberItems()
}
// 保存订单头和全部明细。
async function save() {
  if (!form.unitId) return ElMessage.warning('请选择客户单位')
  if (!form.orderDate || !form.deliveryDatePlan) return ElMessage.warning('请选择下单日期和预计交期')
  if (!items.value.length) return ElMessage.warning('订单至少需要一条明细')
  const invalid = items.value.find(item => !item.styleId || !item.styleCode || Number(item.qty) <= 0 || Number(item.unitPrice) < 0)
  if (invalid) return ElMessage.warning('请完整填写明细款式、数量和单价')
  saving.value = true
  try {
    recalcTotals(); renumberItems()
    const payload = { ...form, items: items.value.map(({ _key, grossProfit, grossMarginRate, ...item }) => ({ ...item, qty: Number(item.qty || 0), unitPrice: Number(item.unitPrice || 0), amount: Number(item.amount || 0), taxRate: Number(item.taxRate ?? form.taxRate ?? 0) })) }
    const response = isCreate.value ? await createOrderFlow(payload) : await updateOrderFlow(props.orderId, payload)
    if (response.data?.code !== 0) throw new Error(response.data?.message || '保存订单失败')
    ElMessage.success('订单保存成功')
    await router.replace('/orders/' + response.data.data.id)
  } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '保存订单失败') }
  finally { saving.value = false }
}
// 打开可做性评审对话框。
function openReview() { reviewForm.reviewResult = 1; reviewForm.closeReasonType = 1; reviewForm.closeReason = ''; reviewVisible.value = true }
// 提交评审结论。
async function doReview() {
  if (reviewForm.reviewResult === 2 && !reviewForm.closeReason.trim()) return ElMessage.warning('不可做时必须填写关闭原因')
  saving.value = true
  try {
    const response = await reviewOrderFlow(props.orderId, reviewForm)
    if (response.data?.code !== 0) throw new Error(response.data?.message || '评审失败')
    ElMessage.success('评审完成'); reviewVisible.value = false; await loadDetail()
  } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '评审失败') }
  finally { saving.value = false }
}
// 暂停订单。
async function pause() { try { await ElMessageBox.confirm('确认暂停当前订单吗？', '暂停订单', { type: 'warning' }); const r = await pauseOrderFlow(props.orderId); if (r.data?.code !== 0) throw new Error(r.data?.message); ElMessage.success('订单已暂停'); await loadDetail() } catch (error) { if (error !== 'cancel' && error !== 'close') ElMessage.error(error?.response?.data?.message || error.message || '暂停失败') } }
// 恢复订单到暂停前状态。
async function resume() { try { const r = await resumeOrderFlow(props.orderId); if (r.data?.code !== 0) throw new Error(r.data?.message); ElMessage.success('订单已恢复'); await loadDetail() } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '恢复失败') } }
// 打开关闭订单对话框。
function openClose() { closeForm.closeReasonType = 3; closeForm.closeReason = ''; closeVisible.value = true }
// 关闭订单。
async function doClose() {
  if (!closeForm.closeReason.trim()) return ElMessage.warning('请填写关闭原因')
  saving.value = true
  try { const response = await closeOrderFlow(props.orderId, closeForm); if (response.data?.code !== 0) throw new Error(response.data?.message || '关闭失败'); ElMessage.success('订单已关闭'); closeVisible.value = false; await loadDetail() }
  catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '关闭失败') }
  finally { saving.value = false }
}
// 返回今天的 YYYY-MM-DD。
function today() { return new Date().toLocaleDateString('sv-SE') }
// 将枚举值转换为中文。
function optionText(options, value) { return options.find(item => item.value === value)?.label || '—' }
// 格式化金额。
function money(value) { return '¥' + Number(value || 0).toFixed(2) }
// 格式化百分比。
function percent(value) { return (Number(value || 0) * 100).toFixed(2) + '%' }
// 格式化数量。
function numberText(value) { return Number(value || 0).toFixed(2) }
// 返回毛利颜色样式。
function profitClass(value) { return Number(value || 0) < 0 ? 'profit-loss' : Number(value || 0) === 0 ? 'profit-even' : 'profit-good' }
// 返回订单状态标签颜色。
function statusType(status) { return ({ 1: 'info', 2: 'warning', 3: 'primary', 4: 'warning', 5: 'success', 6: 'danger', 7: 'warning', 8: 'danger' })[status] || 'info' }
// 返回评审标签颜色。
function reviewType(result) { return result === 1 ? 'success' : result === 2 ? 'danger' : 'info' }
// 返回款式显示文本。
function styleText(styleId) { const style = styleOptions.value.find(item => Number(item.id) === Number(styleId)); return style ? style.styleCode + ' ' + style.styleName : '—' }
// 返回进度节点说明。
function progressText(state) { return ({ done: '已完成', current: '进行中', pending: '待处理', closed: '已关闭' })[state] || '' }

onMounted(async () => {
  loading.value = true
  try {
    resetForm(); await loadOptions(); if (!isCreate.value) await loadDetail(); recalcTotals()
  } catch (error) { ElMessage.error(error?.response?.data?.message || error.message || '加载订单页面失败') }
  finally { loading.value = false }
})
</script>

<style scoped>
.order-breadcrumb{margin:0 0 7px;color:#2b6cb0;font-size:12px}.flow-panel{margin-bottom:14px;padding:20px}.detail-section{margin-bottom:14px}.detail-section-title{display:flex;align-items:center;gap:12px;margin-bottom:16px;padding-bottom:12px;border-bottom:1px solid #edf0f4}.detail-section-title>span{width:32px;height:32px;display:grid;place-items:center;border-radius:50%;background:#eaf2fb;color:#2b6cb0;font-size:11px;font-weight:700}.detail-section-title strong{display:block;color:#1f3a5f;font-size:15px}.detail-section-title small{display:block;margin-top:3px;color:#98a2b3;font-size:11px}.detail-section-title.compact{margin:0;padding:0;border:0}.detail-section-heading{display:flex;justify-content:space-between;align-items:center;margin-bottom:14px}.detail-grid-3{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:0 18px}.detail-wide{grid-column:1/-1}.detail-grid-3 :deep(.el-form-item__label){justify-content:flex-start}.profit-good :deep(.el-input__inner){color:#1d8a55;font-weight:700}.profit-even :deep(.el-input__inner){color:#b7791f;font-weight:700}.profit-loss :deep(.el-input__inner){color:#c53030;font-weight:700}@media(max-width:1050px){.detail-grid-3{grid-template-columns:repeat(2,minmax(0,1fr))}}@media(max-width:680px){.detail-grid-3{grid-template-columns:1fr}}
</style>
