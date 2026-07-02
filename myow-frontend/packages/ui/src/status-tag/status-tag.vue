<template>
  <span class="myow-status-tag" :data-tone="tone">{{ label }}</span>
</template>

<script setup lang="ts">
withDefaults(defineProps<{ label: string; tone?: 'green' | 'blue' | 'amber' | 'red' | 'gray' }>(), { tone: 'gray' });
</script>

<style scoped>
.myow-status-tag { display: inline-flex; align-items: center; height: 22px; padding: 0 8px; border-radius: 4px; font-size: 12px; font-weight: 600; }
.myow-status-tag[data-tone="green"] { color: #065f46; background: #d1fae5; }
.myow-status-tag[data-tone="blue"] { color: #1e40af; background: #dbeafe; }
.myow-status-tag[data-tone="amber"] { color: #92400e; background: #fef3c7; }
.myow-status-tag[data-tone="red"] { color: #991b1b; background: #fee2e2; }
.myow-status-tag[data-tone="gray"] { color: #475569; background: #e2e8f0; }
</style>
