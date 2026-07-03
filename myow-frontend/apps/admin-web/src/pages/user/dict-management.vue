<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>字典管理</h1>
        <p>维护字典集合和集合下的数据项，供状态、枚举和表单选项统一使用。</p>
      </div>
      <button class="primary-action" type="button" @click="openDictCreate">新增集合</button>
    </header>

    <div v-if="toastMessage" class="success-banner">{{ toastMessage }}</div>
    <div v-if="errorMessage" class="error-banner">{{ errorMessage }}</div>

    <section class="dict-layout">
      <aside class="panel dict-list-panel">
        <div class="panel__head compact-head">
          <div>
            <h2>字典集合</h2>
            <p>{{ dictTotal }} 个集合</p>
          </div>
        </div>
        <section class="dict-search">
          <input v-model="dictQuery.dictCode" placeholder="编码" @keyup.enter="loadDicts" />
          <input v-model="dictQuery.dictName" placeholder="名称" @keyup.enter="loadDicts" />
          <button type="button" :disabled="dictLoading" @click="loadDicts">查询</button>
        </section>
        <div class="dict-list">
          <button
            v-for="dict in dicts"
            :key="dict.dictId"
            class="dict-item"
            :class="{ active: selectedDict?.dictId === dict.dictId }"
            type="button"
            @click="selectDict(dict)"
          >
            <strong>{{ dict.dictName || '-' }}</strong>
            <code>{{ dict.dictCode || '-' }}</code>
          </button>
          <p v-if="!dictLoading && dicts.length === 0" class="empty-cell">暂无字典集合</p>
          <p v-if="dictLoading" class="empty-cell">加载中...</p>
        </div>
        <footer class="dict-list-actions">
          <button type="button" :disabled="!selectedDict" @click="openDictEdit">编辑集合</button>
          <button type="button" :disabled="!selectedDict" @click="removeDict">删除集合</button>
        </footer>
      </aside>

      <article class="panel table-panel">
        <div class="panel__head">
          <div>
            <h2>{{ selectedDict?.dictName || '字典数据项' }}</h2>
            <p>{{ selectedDict ? selectedDict.dictCode : '请先选择左侧字典集合' }}</p>
          </div>
          <div class="heading-actions">
            <button type="button" :disabled="dataLoading || !selectedDict" @click="loadDictData">刷新</button>
            <button class="primary-action" type="button" :disabled="!selectedDict" @click="openDataCreate">新增数据项</button>
          </div>
        </div>

        <section class="toolbar query-panel">
          <label><span>标签</span><input v-model="dataQuery.dataLabel" placeholder="展示名称" @keyup.enter="loadDictData" /></label>
          <label><span>值</span><input v-model="dataQuery.dataValue" placeholder="业务值" @keyup.enter="loadDictData" /></label>
          <label>
            <span>状态</span>
            <select v-model="dataQuery.disabledFlag">
              <option value="">全部</option>
              <option value="false">启用</option>
              <option value="true">停用</option>
            </select>
          </label>
          <div class="query-actions">
            <button type="button" :disabled="dataLoading || !selectedDict" @click="loadDictData">查询</button>
            <button type="button" :disabled="dataLoading" @click="resetDataQuery">重置</button>
          </div>
        </section>

        <table class="data-table">
          <thead>
            <tr>
              <th>标签</th>
              <th>值</th>
              <th>排序</th>
              <th>状态</th>
              <th>备注</th>
              <th>更新时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="row in dataRows" :key="row.dictDataId">
              <td><strong>{{ row.dataLabel || '-' }}</strong></td>
              <td><code>{{ row.dataValue || '-' }}</code></td>
              <td>{{ row.sort ?? 0 }}</td>
              <td>
                <span class="status-tag" :data-tone="row.disabledFlag ? 'amber' : 'green'">
                  {{ row.disabledFlag ? '停用' : '启用' }}
                </span>
              </td>
              <td>{{ row.remark || '-' }}</td>
              <td>{{ formatTime(row.updateTime || row.createTime) }}</td>
              <td class="table-actions">
                <button type="button" @click="openDataEdit(row)">编辑</button>
                <button type="button" @click="removeData(row)">删除</button>
              </td>
            </tr>
            <tr v-if="!dataLoading && dataRows.length === 0">
              <td class="empty-cell" colspan="7">{{ selectedDict ? '暂无字典数据项' : '请选择字典集合' }}</td>
            </tr>
            <tr v-if="dataLoading">
              <td class="empty-cell" colspan="7">加载中...</td>
            </tr>
          </tbody>
        </table>
        <footer class="pagination-bar">
          <span>第 {{ dataQuery.pageNum }} / {{ dataPageCount }} 页，共 {{ dataTotal }} 条</span>
          <button type="button" :disabled="dataQuery.pageNum <= 1" @click="changeDataPage(dataQuery.pageNum - 1)">上一页</button>
          <button type="button" :disabled="dataQuery.pageNum >= dataPageCount" @click="changeDataPage(dataQuery.pageNum + 1)">下一页</button>
        </footer>
      </article>
    </section>

    <div v-if="dictDrawerOpen" class="crud-backdrop" @click.self="closeDictDrawer">
      <section class="crud-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ editingDictId ? '编辑字典集合' : '新增字典集合' }}</h2>
            <p>字典编码全局唯一，保存后供数据项归属使用。</p>
          </div>
          <button type="button" @click="closeDictDrawer">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submitDict">
          <label><span>字典编码</span><input v-model="dictForm.dictCode" required placeholder="user_status" /></label>
          <label><span>字典名称</span><input v-model="dictForm.dictName" required placeholder="用户状态" /></label>
          <label class="form-wide"><span>备注</span><textarea v-model="dictForm.remark" rows="4" /></label>
          <footer class="crud-actions">
            <button type="button" @click="closeDictDrawer">取消</button>
            <button class="primary-action" type="submit" :disabled="saving">{{ saving ? '保存中...' : '保存' }}</button>
          </footer>
        </form>
      </section>
    </div>

    <div v-if="dataDrawerOpen" class="crud-backdrop" @click.self="closeDataDrawer">
      <section class="crud-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ editingDataId ? '编辑字典数据项' : '新增字典数据项' }}</h2>
            <p>{{ selectedDict?.dictName }} / {{ selectedDict?.dictCode }}</p>
          </div>
          <button type="button" @click="closeDataDrawer">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submitData">
          <label><span>数据标签</span><input v-model="dataForm.dataLabel" required placeholder="正常" /></label>
          <label><span>数据值</span><input v-model="dataForm.dataValue" required placeholder="NORMAL" /></label>
          <label><span>排序</span><input v-model.number="dataForm.sort" type="number" min="0" /></label>
          <label>
            <span>状态</span>
            <select v-model="dataForm.disabledFlag">
              <option :value="false">启用</option>
              <option :value="true">停用</option>
            </select>
          </label>
          <label class="form-wide"><span>备注</span><textarea v-model="dataForm.remark" rows="4" /></label>
          <footer class="crud-actions">
            <button type="button" @click="closeDataDrawer">取消</button>
            <button class="primary-action" type="submit" :disabled="saving">{{ saving ? '保存中...' : '保存' }}</button>
          </footer>
        </form>
      </section>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import type { ApiId } from '@myow/api';
import { confirmDelete } from '@/composables/use-confirm-action';
import {
  createDict,
  createDictData,
  deleteDict,
  deleteDictData,
  type DictDataRecord,
  type DictRecord,
  pageDictData,
  pageDicts,
  updateDict,
  updateDictData
} from '@/services/user-service';

const dictLoading = ref(false);
const dataLoading = ref(false);
const saving = ref(false);
const dictDrawerOpen = ref(false);
const dataDrawerOpen = ref(false);
const editingDictId = ref<ApiId | ''>('');
const editingDataId = ref<ApiId | ''>('');
const dicts = ref<DictRecord[]>([]);
const dataRows = ref<DictDataRecord[]>([]);
const selectedDict = ref<DictRecord | null>(null);
const dictTotal = ref(0);
const dataTotal = ref(0);
const toastMessage = ref('');
const errorMessage = ref('');

const dictQuery = reactive({ dictCode: '', dictName: '', pageNum: 1, pageSize: 200 });
const dataQuery = reactive({ dataLabel: '', dataValue: '', disabledFlag: '', pageNum: 1, pageSize: 20 });
const dictForm = reactive({ dictCode: '', dictName: '', remark: '' });
const dataForm = reactive({ dataLabel: '', dataValue: '', sort: 0, disabledFlag: false, remark: '' });

const dataPageCount = computed(() => Math.max(1, Math.ceil(dataTotal.value / dataQuery.pageSize)));

onMounted(async () => {
  await loadDicts();
});

async function loadDicts() {
  await runWithFeedback(async () => {
    dictLoading.value = true;
    const result = await pageDicts({
      pageNum: dictQuery.pageNum,
      pageSize: dictQuery.pageSize,
      dictCode: dictQuery.dictCode.trim() || undefined,
      dictName: dictQuery.dictName.trim() || undefined
    });
    dicts.value = result.list ?? [];
    dictTotal.value = Number(result.total ?? dicts.value.length);
    if (!selectedDict.value || !dicts.value.some((item) => item.dictId === selectedDict.value?.dictId)) {
      await selectDict(dicts.value[0] ?? null);
    }
  }, false);
  dictLoading.value = false;
}

async function selectDict(dict: DictRecord | null) {
  selectedDict.value = dict;
  dataRows.value = [];
  dataTotal.value = 0;
  dataQuery.pageNum = 1;
  if (dict) {
    await loadDictData();
  }
}

async function loadDictData() {
  if (!selectedDict.value) return;
  await runWithFeedback(async () => {
    dataLoading.value = true;
    const result = await pageDictData({
      dictId: selectedDict.value?.dictId,
      dataLabel: dataQuery.dataLabel.trim() || undefined,
      dataValue: dataQuery.dataValue.trim() || undefined,
      disabledFlag: dataQuery.disabledFlag === '' ? undefined : dataQuery.disabledFlag === 'true',
      pageNum: dataQuery.pageNum,
      pageSize: dataQuery.pageSize
    });
    dataRows.value = result.list ?? [];
    dataTotal.value = Number(result.total ?? dataRows.value.length);
  }, false);
  dataLoading.value = false;
}

function resetDataQuery() {
  Object.assign(dataQuery, { dataLabel: '', dataValue: '', disabledFlag: '', pageNum: 1 });
  void loadDictData();
}

function changeDataPage(pageNum: number) {
  dataQuery.pageNum = pageNum;
  void loadDictData();
}

function openDictCreate() {
  editingDictId.value = '';
  Object.assign(dictForm, { dictCode: '', dictName: '', remark: '' });
  dictDrawerOpen.value = true;
}

function openDictEdit() {
  if (!selectedDict.value) return;
  editingDictId.value = selectedDict.value.dictId;
  Object.assign(dictForm, {
    dictCode: selectedDict.value.dictCode ?? '',
    dictName: selectedDict.value.dictName ?? '',
    remark: selectedDict.value.remark ?? ''
  });
  dictDrawerOpen.value = true;
}

function closeDictDrawer() {
  dictDrawerOpen.value = false;
}

async function submitDict() {
  await runWithFeedback(async () => {
    saving.value = true;
    if (editingDictId.value) {
      await updateDict({ dictId: editingDictId.value, ...dictForm });
      showToast('字典集合已更新');
    } else {
      await createDict(dictForm);
      showToast('字典集合已创建');
    }
    closeDictDrawer();
    await loadDicts();
  });
  saving.value = false;
}

async function removeDict() {
  if (!selectedDict.value) return;
  const target = `字典集合 ${selectedDict.value.dictName || selectedDict.value.dictCode || selectedDict.value.dictId}`;
  if (!confirmDelete(target, '删除字典集合会影响该集合下的所有枚举维护；如果存在数据项或业务引用，后端将拒绝删除。')) return;
  await runWithFeedback(async () => {
    await deleteDict(selectedDict.value!.dictId);
    showToast('字典集合已删除');
    selectedDict.value = null;
    await loadDicts();
  });
}

function openDataCreate() {
  if (!selectedDict.value) return;
  editingDataId.value = '';
  Object.assign(dataForm, { dataLabel: '', dataValue: '', sort: 0, disabledFlag: false, remark: '' });
  dataDrawerOpen.value = true;
}

function openDataEdit(row: DictDataRecord) {
  editingDataId.value = row.dictDataId;
  Object.assign(dataForm, {
    dataLabel: row.dataLabel ?? '',
    dataValue: row.dataValue ?? '',
    sort: row.sort ?? 0,
    disabledFlag: Boolean(row.disabledFlag),
    remark: row.remark ?? ''
  });
  dataDrawerOpen.value = true;
}

function closeDataDrawer() {
  dataDrawerOpen.value = false;
}

async function submitData() {
  if (!selectedDict.value) return;
  await runWithFeedback(async () => {
    saving.value = true;
    const payload = { dictId: selectedDict.value!.dictId, ...dataForm };
    if (editingDataId.value) {
      await updateDictData({ dictDataId: editingDataId.value, ...payload });
      showToast('字典数据项已更新');
    } else {
      await createDictData(payload);
      showToast('字典数据项已创建');
    }
    closeDataDrawer();
    await loadDictData();
  });
  saving.value = false;
}

async function removeData(row: DictDataRecord) {
  const target = `字典数据项 ${row.dataLabel || row.dataValue || row.dictDataId}`;
  if (!confirmDelete(target, '删除数据项会影响历史业务值的解释。已经被业务引用的数据项建议停用，而不是直接删除。')) return;
  await runWithFeedback(async () => {
    await deleteDictData(row.dictDataId);
    showToast('字典数据项已删除');
    await loadDictData();
  });
}

async function runWithFeedback(task: () => Promise<void>, clearToast = true) {
  if (clearToast) toastMessage.value = '';
  errorMessage.value = '';
  try {
    await task();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '操作失败';
  } finally {
    saving.value = false;
    dictLoading.value = false;
    dataLoading.value = false;
  }
}

function showToast(message: string) {
  toastMessage.value = message;
}

function formatTime(value?: string) {
  return value ? value.replace('T', ' ').slice(0, 19) : '-';
}
</script>

<style scoped>
.dict-layout {
  display: grid;
  grid-template-columns: minmax(260px, 320px) minmax(0, 1fr);
  gap: 16px;
  align-items: start;
}

.dict-list-panel {
  min-height: 560px;
}

.compact-head {
  margin-bottom: 12px;
}

.dict-search {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1fr) auto;
  gap: 8px;
  margin-bottom: 12px;
}

.dict-search input {
  min-width: 0;
}

.dict-list {
  display: grid;
  gap: 8px;
  max-height: 430px;
  overflow: auto;
}

.dict-item {
  display: grid;
  gap: 4px;
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  background: #fff;
  color: #0f172a;
  text-align: left;
  cursor: pointer;
}

.dict-item.active {
  border-color: #2563eb;
  background: #eff6ff;
}

.dict-item code {
  color: #64748b;
  font-size: 12px;
}

.dict-list-actions {
  display: flex;
  gap: 8px;
  margin-top: 14px;
}

@media (max-width: 900px) {
  .dict-layout {
    grid-template-columns: 1fr;
  }

  .dict-list-panel {
    min-height: auto;
  }

  .dict-search {
    grid-template-columns: 1fr;
  }
}
</style>
