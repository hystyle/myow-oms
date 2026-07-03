import { computed, onMounted, ref, toValue, watch, type MaybeRefOrGetter } from 'vue';
import { pageDictData, pageDicts } from '@/services/user-service';

export interface DictOption<T = string> {
  label: string;
  value: T;
}

export function useDictOptions<T = string>(
  dictCode: MaybeRefOrGetter<string>,
  fallback: Array<DictOption<T>> = [],
  transform: (value: string) => T = (value) => value as T
) {
  const loadedOptions = ref<Array<DictOption<T>>>([]);
  const loading = ref(false);
  const loadFailed = ref(false);

  const options = computed(() => loadedOptions.value.length > 0 ? loadedOptions.value : fallback);

  async function loadOptions() {
    loading.value = true;
    loadFailed.value = false;
    try {
      const currentDictCode = toValue(dictCode);
      if (!currentDictCode) {
        loadedOptions.value = [];
        loadFailed.value = false;
        return;
      }
      const dictPage = await pageDicts({ dictCode: currentDictCode, pageNum: 1, pageSize: 1 });
      const dict = dictPage.list?.[0];
      if (!dict?.dictId) {
        loadedOptions.value = [];
        loadFailed.value = true;
        return;
      }
      const dataPage = await pageDictData({ dictId: dict.dictId, disabledFlag: false, pageNum: 1, pageSize: 500 });
      loadedOptions.value = (dataPage.list ?? []).map((item) => ({
        label: item.dataLabel || item.dataValue || '',
        value: transform(String(item.dataValue ?? ''))
      }));
    } catch {
      loadedOptions.value = [];
      loadFailed.value = true;
    } finally {
      loading.value = false;
    }
  }

  onMounted(() => {
    void loadOptions();
  });
  watch(() => toValue(dictCode), () => {
    void loadOptions();
  });

  return {
    options,
    loading,
    loadFailed,
    reload: loadOptions
  };
}
