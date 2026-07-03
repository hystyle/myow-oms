<template>
  <section class="page-stack">
    <header class="page-heading">
      <div><h1>站点配置</h1><p>维护系统级配置项、开关和敏感值。</p></div>
      <button class="primary-action" type="button" @click="openCreate">新增配置</button>
    </header>

    <div v-if="toastMessage" class="success-banner">{{ toastMessage }}</div>
    <div v-if="errorMessage" class="error-banner">{{ errorMessage }}</div>

    <section class="config-layout">
      <aside class="panel tree-panel">
        <h2>配置分组</h2>
        <button v-for="group in groups" :key="group" class="tree-item" :class="{ active: current === group }" type="button" @click="selectGroup(group)">{{ group }}</button>
      </aside>
      <article class="panel table-panel">
        <div class="panel__head">
          <div><h2>{{ current }}</h2><p>敏感配置默认脱敏，查看明文需权限和审计。</p></div>
          <div class="heading-actions">
            <button class="secondary-action" type="button" :disabled="loading" @click="loadConfigs">查询</button>
            <button class="secondary-action" type="button" @click="refreshCache">刷新缓存</button>
          </div>
        </div>
        <section class="toolbar query-panel">
          <label><span>关键词</span><input v-model="query.keyword" placeholder="配置键、站点代码" @keyup.enter="loadConfigs" /></label>
        </section>
        <table class="data-table">
          <thead><tr><th>站点</th><th>配置键</th><th>配置值</th><th>类型</th><th>更新时间</th><th>操作</th></tr></thead>
          <tbody>
            <tr v-for="row in rows" :key="row.id">
              <td>{{ row.name }}</td>
              <td><code>{{ row.code }}</code></td>
              <td>{{ isSecret(row) ? '******' : attr(row, 'configValue') }}</td>
              <td>{{ attr(row, 'configType') || '-' }}</td>
              <td>{{ formatTime(row.updateTime || row.createTime) }}</td>
              <td class="table-actions">
                <button type="button" @click="openEdit(row)">编辑</button>
                <button v-if="isSecret(row)" type="button" @click="reveal(row)">查看明文</button>
                <button type="button" @click="remove(row)">删除</button>
              </td>
            </tr>
            <tr v-if="!loading && rows.length === 0"><td class="empty-cell" colspan="6">暂无配置项</td></tr>
            <tr v-if="loading"><td class="empty-cell" colspan="6">加载中...</td></tr>
          </tbody>
        </table>
        <footer class="pagination-bar">
          <span>第 {{ query.pageNum }} / {{ pageCount }} 页，共 {{ total }} 条</span>
          <button type="button" :disabled="query.pageNum <= 1" @click="changePage(query.pageNum - 1)">上一页</button>
          <button type="button" :disabled="query.pageNum >= pageCount" @click="changePage(query.pageNum + 1)">下一页</button>
        </footer>
      </article>
    </section>

    <div v-if="drawerOpen" class="crud-backdrop" @click.self="closeDrawer">
      <section class="crud-drawer">
        <header class="crud-drawer__head">
          <div><h2>{{ editingId ? '编辑配置' : '新增配置' }}</h2><p>站点代码和配置键组合唯一。</p></div>
          <button type="button" @click="closeDrawer">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submit">
          <label><span>站点代码</span><input v-model="form.siteCode" required placeholder="ADMIN / CLIENT / GLOBAL" /></label>
          <label><span>配置键</span><input v-model="form.configKey" required placeholder="site.name" /></label>
          <label><span>配置类型</span><select v-model="form.configType"><option value="STRING">字符串</option><option value="NUMBER">数字</option><option value="BOOLEAN">布尔</option><option value="SECRET">敏感值</option><option value="JSON">JSON</option></select></label>
          <label><span>配置值</span><input v-model="form.configValue" /></label>
          <label class="form-wide"><span>备注</span><textarea v-model="form.remark" rows="4" /></label>
          <footer class="crud-actions">
            <button type="button" @click="closeDrawer">取消</button>
            <button class="primary-action" type="submit" :disabled="saving">{{ saving ? '保存中...' : '保存' }}</button>
          </footer>
        </form>
      </section>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import type { SystemRecord } from '@myow/api';
import { confirmDelete, confirmImportantAction } from '@/composables/use-confirm-action';
import { createSiteConfig, deleteSiteConfig, pageSiteConfigs, refreshSiteConfig, updateSiteConfig } from '@/services/system-service';

const groups = ['GLOBAL', 'ADMIN', 'CLIENT', 'FILE', 'SECURITY'];
const current = ref(groups[0]);
const loading = ref(false);
const saving = ref(false);
const drawerOpen = ref(false);
const editingId = ref('');
const rows = ref<SystemRecord[]>([]);
const total = ref(0);
const toastMessage = ref('');
const errorMessage = ref('');
const revealedIds = ref<Set<string>>(new Set());
const query = reactive({ keyword: '', pageNum: 1, pageSize: 20 });
const form = reactive({ siteCode: 'GLOBAL', configKey: '', configValue: '', configType: 'STRING', remark: '' });

const pageCount = computed(() => Math.max(1, Math.ceil(total.value / query.pageSize)));

onMounted(loadConfigs);

async function loadConfigs() {
  await runWithFeedback(async () => {
    loading.value = true;
    const keywordParts = [query.keyword.trim(), current.value].filter(Boolean);
    const result = await pageSiteConfigs({ ...query, keyword: keywordParts.join(' ') || undefined });
    rows.value = (result.list ?? []).filter((item) => current.value === 'GLOBAL' ? true : item.name === current.value || String(item.code ?? '').includes(current.value.toLowerCase()));
    total.value = Number(result.total ?? rows.value.length);
  }, false);
  loading.value = false;
}

function selectGroup(group: string) {
  current.value = group;
  query.pageNum = 1;
  void loadConfigs();
}

function changePage(pageNum: number) {
  query.pageNum = pageNum;
  void loadConfigs();
}

function openCreate() {
  editingId.value = '';
  Object.assign(form, { siteCode: current.value, configKey: '', configValue: '', configType: 'STRING', remark: '' });
  drawerOpen.value = true;
}

function openEdit(row: SystemRecord) {
  editingId.value = String(row.id);
  Object.assign(form, {
    siteCode: row.name ?? current.value,
    configKey: row.code ?? '',
    configValue: String(attr(row, 'configValue') ?? ''),
    configType: String(attr(row, 'configType') ?? 'STRING'),
    remark: String(attr(row, 'remark') ?? '')
  });
  drawerOpen.value = true;
}

function closeDrawer() {
  drawerOpen.value = false;
}

async function submit() {
  await runWithFeedback(async () => {
    saving.value = true;
    if (editingId.value) {
      await updateSiteConfig({ id: editingId.value, ...form });
      showToast('配置已更新');
    } else {
      await createSiteConfig(form);
      showToast('配置已创建');
    }
    closeDrawer();
    await loadConfigs();
  });
  saving.value = false;
}

async function refreshCache() {
  if (!confirmImportantAction({
    title: `刷新 ${current.value} 配置缓存`,
    risk: '刷新缓存会让后续请求读取最新配置，可能立即影响前端展示或系统行为。',
    confirmText: '确认刷新该配置缓存？'
  })) return;
  await runWithFeedback(async () => {
    await refreshSiteConfig(current.value);
    showToast('配置缓存已刷新');
  });
}

async function remove(row: SystemRecord) {
  if (!confirmDelete(`配置 ${row.code || row.id}`, '删除配置可能导致站点、主题、安全或文件策略回退到默认值。')) return;
  await runWithFeedback(async () => {
    await deleteSiteConfig(row.id);
    showToast('配置已删除');
    await loadConfigs();
  });
}

function reveal(row: SystemRecord) {
  const next = new Set(revealedIds.value);
  next.add(String(row.id));
  revealedIds.value = next;
}

async function runWithFeedback(task: () => Promise<void>, clearToast = true) {
  if (clearToast) toastMessage.value = '';
  errorMessage.value = '';
  try {
    await task();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '操作失败';
  }
}

function showToast(message: string) {
  toastMessage.value = message;
}

function attr(record: SystemRecord, key: string) {
  return record.attributes?.[key];
}

function isSecret(row: SystemRecord) {
  return String(attr(row, 'configType') ?? '').toUpperCase() === 'SECRET' && !revealedIds.value.has(String(row.id));
}

function formatTime(value?: string) {
  return value ? value.replace('T', ' ').slice(0, 19) : '-';
}
</script>
