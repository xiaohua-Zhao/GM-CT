<template>
  <section class="content-panel code-rule-panel">
    <div class="panel-heading">
      <div>
        <h3>编码规则</h3>
        <p>配置员工、部门、单位、供应商、款式、物料、工序、订单及采购单的编码格式。</p>
      </div>
      <div class="panel-heading-actions">
        <el-button type="primary" @click="openCreate">新增规则</el-button>
      </div>
    </div>

    <div class="table-toolbar">
      <el-input v-model="keyword" class="search-input" clearable placeholder="搜索规则名称" @keyup.enter="search" />
      <el-button type="primary" plain @click="search">查询</el-button>
      <el-button @click="resetSearch">重置</el-button>
      <div class="table-toolbar-actions"><span class="record-count">共 {{ total }} 条</span></div>
    </div>

    <el-table v-loading="loading" :data="rows" row-key="id" border empty-text="暂无编码规则">
      <el-table-column prop="ruleName" label="规则名称" min-width="130" />
      <el-table-column prop="bizType" label="业务类型" width="115" />
      <el-table-column prop="prefix" label="前缀" width="80" />
      <el-table-column prop="dateFormat" label="日期格式" width="110" />
      <el-table-column prop="sequenceLength" label="流水位数" width="90" />
      <el-table-column prop="previewCode" label="下一编码" min-width="155">
        <template #default="{ row }"><span class="next-code">{{ row.previewCode || '—' }}</span></template>
      </el-table-column>
      <el-table-column prop="currentValue" label="当前序号" width="95" />
      <el-table-column prop="resetCycle" label="重置周期" width="95" />
      <el-table-column prop="allowManual" label="手工编码" width="90">
        <template #default="{ row }"><el-tag :type="row.allowManual === 1 ? 'success' : 'info'" effect="light">{{ row.allowManual === 1 ? '允许' : '禁止' }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="enabled" label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.enabled === 1 ? 'success' : 'info'" effect="light">{{ row.enabled === 1 ? '启用' : '停用' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="230" align="center">
        <template #default="{ row }">
          <el-button link type="primary" @click="showPreview(row)">预览</el-button>
          <el-button link type="warning" @click="openReset(row)">重置序号</el-button>
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="current" v-model:page-size="size" class="pager" background
      layout="total, sizes, prev, pager, next, jumper" :page-sizes="[10,20,50]" :total="total"
      @current-change="load" @size-change="search" />
  </section>

  <el-dialog v-model="dialogVisible" :title="editingId ? '编辑编码规则' : '新增编码规则'" width="780px" destroy-on-close>
    <p class="form-hint">编码由“前缀 + 日期 + 流水号”组成；流水位数为 0 时不补零。</p>
    <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
      <div class="rule-form-grid">
        <el-form-item label="规则编码" prop="ruleCode"><el-input v-model="form.ruleCode" /></el-form-item>
        <el-form-item label="规则名称" prop="ruleName"><el-input v-model="form.ruleName" /></el-form-item>
        <el-form-item label="业务类型" prop="bizType"><el-input v-model="form.bizType" placeholder="如 STAFF" /></el-form-item>
        <el-form-item label="目标表" prop="targetTable"><el-input v-model="form.targetTable" /></el-form-item>
        <el-form-item label="目标字段" prop="targetColumn"><el-input v-model="form.targetColumn" /></el-form-item>
        <el-form-item label="编码前缀"><el-input v-model="form.prefix" /></el-form-item>
        <el-form-item label="日期格式"><el-input v-model="form.dateFormat" placeholder="如 yyyyMMdd，可空" /></el-form-item>
        <el-form-item label="分隔符"><el-input v-model="form.separator" placeholder="可空" /></el-form-item>
        <el-form-item label="流水号长度"><el-input-number v-model="form.sequenceLength" :min="0" :max="20" style="width:100%" /></el-form-item>
        <el-form-item label="起始序号"><el-input-number v-model="form.sequenceStart" :min="1" style="width:100%" /></el-form-item>
        <el-form-item label="递增步长"><el-input-number v-model="form.sequenceStep" :min="1" style="width:100%" /></el-form-item>
        <el-form-item label="重置周期">
          <el-select v-model="form.resetCycle" style="width:100%">
            <el-option label="不重置" value="NONE" /><el-option label="按年" value="YEAR" />
            <el-option label="按月" value="MONTH" /><el-option label="按日" value="DAY" />
          </el-select>
        </el-form-item>
        <el-form-item label="允许手工编码">
          <el-select v-model="form.allowManual" style="width:100%"><el-option label="允许" :value="1" /><el-option label="禁止" :value="0" /></el-select>
        </el-form-item>
        <el-form-item label="是否启用">
          <el-select v-model="form.enabled" style="width:100%"><el-option label="启用" :value="1" /><el-option label="停用" :value="0" /></el-select>
        </el-form-item>
        <el-form-item label="备注" class="rule-wide"><el-input v-model="form.remark" type="textarea" :rows="3" /></el-form-item>
      </div>
    </el-form>
    <template #footer>
      <div class="dialog-footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" :loading="saving" @click="save">保存</el-button></div>
    </template>
  </el-dialog>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createOne, deleteOne, updateOne } from '../api/crud'
import { getCodeRulesPage, previewCode, resetCodeSequence } from '../api/codeRules'

const rows=ref([])
const total=ref(0)
const current=ref(1)
const size=ref(10)
const keyword=ref('')
const loading=ref(false)
const saving=ref(false)
const dialogVisible=ref(false)
const editingId=ref(null)
const formRef=ref(null)
const form=reactive({})
const rules={
  ruleCode:[{required:true,message:'请输入规则编码',trigger:'blur'}],
  ruleName:[{required:true,message:'请输入规则名称',trigger:'blur'}],
  bizType:[{required:true,message:'请输入业务类型',trigger:'blur'}],
  targetTable:[{required:true,message:'请输入目标表',trigger:'blur'}],
  targetColumn:[{required:true,message:'请输入目标字段',trigger:'blur'}]
}

// 加载编码规则列表并逐条查询下一编码预览。
async function load(){
  loading.value=true
  try{
    const response=await getCodeRulesPage({current:current.value,size:size.value,keyword:keyword.value})
    const payload=response.data
    if(payload?.code!==0) throw new Error(payload?.message||'查询失败')
    total.value=payload.data?.total||0
    rows.value=(payload.data?.records||[]).map(item=>({...item,previewCode:'',currentValue:null}))
    await Promise.all(rows.value.map(async row=>{
      try{
        const result=await previewCode(row.bizType)
        if(result.data?.code===0){
          row.previewCode=result.data.data.previewCode
          row.currentValue=result.data.data.currentValue
        }
      }catch{
        row.previewCode='规则不可预览'
      }
    }))
  }catch(error){
    ElMessage.error(error?.response?.data?.message||error?.message||'查询失败')
  }finally{
    loading.value=false
  }
}
// 回到第一页并按当前关键字重新查询编码规则。
function search(){current.value=1;load()}
// 清空编码规则搜索条件并刷新列表。
function resetSearch(){keyword.value='';current.value=1;load()}
// 重置编码规则表单，并在编辑时回填当前规则。
function resetForm(row){
  Object.keys(form).forEach(k=>delete form[k])
  const defaults={sequenceLength:4,sequenceStart:1,sequenceStep:1,resetCycle:'NONE',allowManual:1,enabled:1,separator:''}
  Object.assign(form,defaults,row||{})
}
// 打开新增编码规则对话框并初始化默认值。
function openCreate(){editingId.value=null;resetForm();dialogVisible.value=true}
// 打开编辑编码规则对话框并回填当前规则。
function openEdit(row){editingId.value=row.id;resetForm(row);dialogVisible.value=true}
// 校验编码规则表单后新增或修改规则并刷新列表。
async function save(){
  const valid=await formRef.value?.validate().catch(()=>false)
  if(!valid)return
  saving.value=true
  try{
    if(editingId.value) await updateOne('/sys/code-rules',editingId.value,form)
    else await createOne('/sys/code-rules',form)
    ElMessage.success(editingId.value?'修改成功':'新增成功')
    dialogVisible.value=false
    await load()
  }catch(error){
    ElMessage.error(error?.response?.data?.message||'保存失败')
  }finally{saving.value=false}
}
// 二次确认后删除编码规则并刷新列表。
async function remove(row){
  try{
    await ElMessageBox.confirm('确认删除编码规则“'+row.ruleName+'”吗？','删除确认',{type:'warning'})
    await deleteOne('/sys/code-rules',row.id)
    ElMessage.success('删除成功')
    await load()
  }catch(error){if(error!=='cancel'&&error!=='close')ElMessage.error(error?.response?.data?.message||'删除失败')}
}
// 查询并展示当前规则的下一个编码、序号和周期标识。
async function showPreview(row){
  try{
    const response=await previewCode(row.bizType)
    const data=response.data?.data
    ElMessageBox.alert('下一编码：'+data.previewCode+'<br>当前序号：'+data.currentValue+'<br>周期标识：'+data.periodKey,'编码预览',{confirmButtonText:'知道了',dangerouslyUseHTMLString:true})
  }catch(error){ElMessage.error(error?.response?.data?.message||'预览失败')}
}
// 提示输入下一序号并重置当前编码规则序列。
async function openReset(row){
  try{
    const result=await ElMessageBox.prompt('请输入下一次要生成的流水序号','重置 '+row.ruleName,{
      inputPattern:/^[1-9][0-9]*$/,inputErrorMessage:'请输入大于0的整数',confirmButtonText:'确认重置',cancelButtonText:'取消'
    })
    await resetCodeSequence(row.id,{nextValue:Number(result.value),reason:'管理员手工重置'})
    ElMessage.success('序列已重置')
    await load()
  }catch(error){if(error!=='cancel'&&error!=='close')ElMessage.error(error?.response?.data?.message||'重置失败')}
}
onMounted(load)
</script>

<style scoped>
.next-code{font-family:Consolas,monospace;color:#2b6cb0;font-weight:700}
.rule-form-grid{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:0 18px;max-height:58vh;overflow-y:auto;padding:2px}
.rule-wide{grid-column:1/-1}
@media(max-width:720px){.rule-form-grid{grid-template-columns:1fr}.rule-wide{grid-column:auto}}
</style>
