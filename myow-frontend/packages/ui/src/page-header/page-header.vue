<template>
  <header class="myow-page-header">
    <div>
      <h1>{{ title }}</h1>
      <p v-if="description">{{ description }}</p>
    </div>
    <div class="myow-page-header__actions"><slot name="actions" /></div>
  </header>
</template>

<script setup lang="ts">
defineProps<{ title: string; description?: string }>();
</script>

<style scoped>
.myow-page-header { display: flex; align-items: flex-start; justify-content: space-between; gap: 16px; margin-bottom: 16px; }
.myow-page-header h1 { margin: 0; font-size: 20px; }
.myow-page-header p { margin: 4px 0 0; color: #64748b; }
.myow-page-header__actions { display: flex; gap: 8px; }
</style>
