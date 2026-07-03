<template>
  <section class="page-stack">
    <header class="page-heading">
      <div>
        <h1>黑名单</h1>
        <p>维护客户、税号、营业执照、电话、邮箱等风险对象，命中后阻断客户与联系人保存。</p>
      </div>
      <div class="heading-actions">
        <button v-if="hasPermission('customer:blacklist:create')" class="primary-action" type="button" @click="openCreate">
          新增黑名单
        </button>
        <button class="secondary-action" type="button" @click="loadRows">刷新</button>
      </div>
    </header>

    <div v-if="errorMessage" class="error-banner">{{ errorMessage }}</div>
    <div v-if="toastMessage" class="success-banner">{{ toastMessage }}</div>

    <section class="toolbar query-panel">
      <label>
        <span>关键词</span>
        <input v-model="query.keyword" placeholder="目标值、原因" @keyup.enter="loadRows" />
      </label>
      <label>
        <span>目标类型</span>
        <select v-model="query.targetType">
          <option value="">全部类型</option>
          <option v-for="option in targetTypeOptions" :key="option.value" :value="option.value">{{ option.label }}</option>
        </select>
      </label>
      <label>
        <span>状态</span>
        <select v-model="query.status">
          <option value="">全部状态</option>
          <option v-for="option in roleStatusOptions" :key="option.value" :value="option.value">{{ option.label }}</option>
        </select>
      </label>
      <div class="query-actions">
        <button type="button" @click="loadRows">查询</button>
        <button type="button" @click="resetQuery">重置</button>
      </div>
    </section>

    <article class="panel table-panel">
      <div class="panel__head">
        <div>
          <h2>黑名单列表</h2>
          <p>来自 /api/v1/customer/blacklists/page</p>
        </div>
        <span class="page-status">{{ loading ? '加载中' : `${total} 条` }}</span>
      </div>
      <table class="data-table dense-table">
        <thead>
          <tr>
            <th>类型</th>
            <th>目标值</th>
            <th>风险等级</th>
            <th>状态</th>
            <th>关联客户</th>
            <th>原因</th>
            <th>有效期</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rows" :key="row.blacklistId">
            <td>{{ targetTypeText(row.targetType) }}</td>
            <td><code>{{ row.targetValue }}</code></td>
            <td>{{ riskLevelText(row.riskLevel) }}</td>
            <td>
              <span class="status-tag" :data-tone="row.status === 'ACTIVE' ? 'danger' : 'muted'">
                {{ row.status === 'ACTIVE' ? '启用' : '停用' }}
              </span>
            </td>
            <td>{{ row.sourceCustomerId || '-' }}</td>
            <td>{{ row.reason }}</td>
            <td>{{ formatRange(row.effectiveTime, row.expireTime) }}</td>
            <td class="table-actions">
              <button v-if="hasPermission('customer:blacklist:update')" type="button" @click="openEdit(row)">编辑</button>
              <button v-if="hasPermission('customer:blacklist:delete')" type="button" @click="removeRow(row)">删除</button>
            </td>
          </tr>
          <tr v-if="!loading && rows.length === 0">
            <td colspan="8" class="empty-cell">当前筛选条件下没有黑名单数据。</td>
          </tr>
        </tbody>
      </table>
      <footer class="pagination-bar">
        <span>第 {{ query.pageNum }} 页 / 共 {{ pageCount }} 页</span>
        <select v-model.number="query.pageSize" @change="changePageSize">
          <option :value="10">10 条/页</option>
          <option :value="20">20 条/页</option>
          <option :value="50">50 条/页</option>
          <option :value="100">100 条/页</option>
        </select>
        <button type="button" :disabled="query.pageNum <= 1 || loading" @click="changePage(query.pageNum - 1)">上一页</button>
        <button type="button" :disabled="query.pageNum >= pageCount || loading" @click="changePage(query.pageNum + 1)">下一页</button>
      </footer>
    </article>

    <div v-if="drawerOpen" class="crud-backdrop" @click.self="closeDrawer">
      <section class="crud-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ form.blacklistId ? '编辑黑名单' : '新增黑名单' }}</h2>
            <p>启用状态下，客户与联系人保存会实时校验并阻断命中对象。</p>
          </div>
          <button type="button" @click="closeDrawer">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submitForm">
          <label>
            <span>目标类型</span>
            <select v-model="form.targetType" required>
              <option v-for="option in targetTypeOptions" :key="option.value" :value="option.value">{{ option.label }}</option>
            </select>
          </label>
          <label><span>目标值</span><input v-model="form.targetValue" required /></label>
          <label>
            <span>风险等级</span>
            <select v-model="form.riskLevel">
              <option v-for="option in riskLevelOptions" :key="option.value" :value="option.value">{{ option.label }}</option>
            </select>
          </label>
          <label>
            <span>状态</span>
            <select v-model="form.status">
              <option v-for="option in roleStatusOptions" :key="option.value" :value="option.value">{{ option.label }}</option>
            </select>
          </label>
          <label><span>关联客户 ID</span><input v-model="form.sourceCustomerId" /></label>
          <label><span>生效时间</span><input v-model="form.effectiveTime" type="datetime-local" /></label>
          <label><span>失效时间</span><input v-model="form.expireTime" type="datetime-local" /></label>
          <label class="form-wide"><span>原因</span><textarea v-model="form.reason" rows="4" required /></label>
          <footer class="crud-actions">
            <button type="button" @click="closeDrawer">取消</button>
            <button class="primary-action" type="submit" :disabled="saving">{{ saving ? '保存中' : '保存' }}</button>
          </footer>
        </form>
      </section>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import type { BlacklistTargetType, CustomerBlacklist, CustomerBlacklistPayload } from '@myow/api';
import { confirmDelete } from '@/composables/use-confirm-action';
import { useDictOptions } from '@/composables/use-dict-options';
import { usePermission } from '@/composables/use-permission';
import {
  createCustomerBlacklist,
  deleteCustomerBlacklist,
  pageCustomerBlacklists,
  updateCustomerBlacklist
} from '@/services/customer-service';

const { hasPermission } = usePermission();

const { options: targetTypeOptions } = useDictOptions<BlacklistTargetType>('customer_blacklist_target_type', [
  { label: '客户 ID', value: 'CUSTOMER_ID' },
  { label: '税号', value: 'TAX_NO' },
  { label: '营业执照', value: 'LICENSE_NO' },
  { label: '电话', value: 'PHONE' },
  { label: '邮箱', value: 'EMAIL' }
]);
const { options: riskLevelOptions } = useDictOptions('risk_level', [
  { label: '低', value: 'LOW' },
  { label: '中', value: 'MEDIUM' },
  { label: '高', value: 'HIGH' },
  { label: '严重', value: 'CRITICAL' }
]);
const { options: roleStatusOptions } = useDictOptions('customer_role_status', [
  { label: '启用', value: 'ACTIVE' },
  { label: '停用', value: 'DISABLED' }
]);

const loading = ref(false);
const saving = ref(false);
const errorMessage = ref('');
const toastMessage = ref('');
const rows = ref<CustomerBlacklist[]>([]);
const total = ref(0);
const query = reactive({
  keyword: '',
  targetType: '' as BlacklistTargetType | '',
  status: '',
  pageNum: 1,
  pageSize: 20
});
const pageCount = computed(() => Math.max(1, Math.ceil(total.value / query.pageSize)));

const drawerOpen = ref(false);
const form = reactive<CustomerBlacklistPayload>({
  targetType: 'TAX_NO',
  targetValue: '',
  riskLevel: 'HIGH',
  reason: '',
  sourceCustomerId: undefined,
  status: 'ACTIVE',
  effectiveTime: '',
  expireTime: ''
});

onMounted(() => {
  void loadRows();
});

async function loadRows() {
  loading.value = true;
  errorMessage.value = '';
  try {
    const page = await pageCustomerBlacklists({
      keyword: query.keyword || undefined,
      targetType: query.targetType || undefined,
      status: query.status || undefined,
      pageNum: query.pageNum,
      pageSize: query.pageSize
    });
    rows.value = page.list ?? [];
    total.value = Number(page.total ?? rows.value.length);
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '黑名单数据加载失败';
  } finally {
    loading.value = false;
  }
}

function resetQuery() {
  query.keyword = '';
  query.targetType = '';
  query.status = '';
  query.pageNum = 1;
  void loadRows();
}

function changePage(pageNum: number) {
  query.pageNum = Math.max(1, pageNum);
  void loadRows();
}

function changePageSize() {
  query.pageNum = 1;
  void loadRows();
}

function openCreate() {
  Object.assign(form, {
    blacklistId: undefined,
    targetType: 'TAX_NO',
    targetValue: '',
    riskLevel: 'HIGH',
    reason: '',
    sourceCustomerId: undefined,
    status: 'ACTIVE',
    effectiveTime: '',
    expireTime: ''
  });
  drawerOpen.value = true;
}

function openEdit(row: CustomerBlacklist) {
  Object.assign(form, {
    blacklistId: row.blacklistId,
    targetType: row.targetType,
    targetValue: row.targetValue,
    riskLevel: row.riskLevel || 'HIGH',
    reason: row.reason,
    sourceCustomerId: row.sourceCustomerId,
    status: row.status || 'ACTIVE',
    effectiveTime: toDatetimeLocal(row.effectiveTime),
    expireTime: toDatetimeLocal(row.expireTime)
  });
  drawerOpen.value = true;
}

async function submitForm() {
  saving.value = true;
  try {
    const payload = {
      ...form,
      effectiveTime: form.effectiveTime || undefined,
      expireTime: form.expireTime || undefined
    };
    if (form.blacklistId) {
      await updateCustomerBlacklist(payload);
      showToast('黑名单已更新');
    } else {
      await createCustomerBlacklist(payload);
      showToast('黑名单已创建');
    }
    closeDrawer();
    await loadRows();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '黑名单保存失败';
  } finally {
    saving.value = false;
  }
}

async function removeRow(row: CustomerBlacklist) {
  if (!confirmDelete(`黑名单 ${row.targetValue}`, '删除黑名单会解除该对象的风险拦截。若只是临时放行，建议先停用或调整有效期。')) return;
  await deleteCustomerBlacklist(row.blacklistId);
  showToast('黑名单已删除');
  await loadRows();
}

function closeDrawer() {
  drawerOpen.value = false;
}

function showToast(message: string) {
  toastMessage.value = message;
  window.setTimeout(() => {
    toastMessage.value = '';
  }, 2200);
}

function targetTypeText(value?: string) {
  return targetTypeOptions.value.find((option) => option.value === value)?.label || value || '-';
}

function riskLevelText(value?: string) {
  return riskLevelOptions.value.find((option) => option.value === value)?.label || value || '-';
}

function formatRange(start?: string, end?: string) {
  if (!start && !end) return '长期';
  return `${formatTime(start) || '立即'} 至 ${formatTime(end) || '长期'}`;
}

function formatTime(value?: string) {
  if (!value) return '';
  return value.replace('T', ' ').slice(0, 16);
}

function toDatetimeLocal(value?: string) {
  if (!value) return '';
  return value.slice(0, 16);
}
</script>
