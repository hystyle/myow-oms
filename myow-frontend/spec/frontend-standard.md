# MYOW Frontend Standard

## Scope

Apply this standard to all code under `myow-frontend/apps/*` and `myow-frontend/packages/*`.

The current frontend stack is Vue 3, Vite, TypeScript, npm workspaces, Pinia, Vue Router, Axios, and Element Plus.

## Naming

- Project and package directory names use lowercase kebab-case, such as `admin-web`, `client-web`, and `page-header`.
- Directories and ordinary files use lowercase kebab-case, such as `user-list.vue`, `status-tag.ts`, and `smart-logo.png`.
- Do not use camelCase or PascalCase for file and directory names.
- Vue component files also use kebab-case. This keeps imports, routes, and filesystem paths consistent across macOS, Linux, and CI.
- Child components that are tightly coupled to a parent component use the parent name as a prefix, such as `order-list-filter.vue`, `order-list-table.vue`, and `order-list-status-tag.vue`.
- Avoid unclear abbreviations. Prefer `user-profile-options.vue` over shortened names.

## Formatting

- Vue template attributes use double quotes.
- TypeScript and JavaScript string literals use single quotes.
- End TypeScript and JavaScript statements with semicolons.
- Split component tags across multiple lines when attributes become hard to scan.
- Keep templates declarative. Move complex expressions into `computed`, functions, or composables.

## Vue Components

- Use `<script setup lang="ts">` for Vue single-file components.
- Prefer Composition API everywhere. Do not mix Options API into new components.
- Group state and functions by business concern instead of putting all variables first and all functions later.
- Keep related query, table, form, modal, drawer, and batch-action state near the functions that operate on it.
- Template refs must end with `Ref`, and the matching template `ref` attribute must use the same name.
- Add short comments for important state groups and public functions. Avoid comments that only repeat the identifier.
- Use `defineProps`, `defineEmits`, and `defineExpose` near the top of the script after imports.
- Expose only the methods the parent component truly needs.

Recommended component skeleton:

```vue
<script setup lang="ts">
import { reactive, ref } from 'vue';

const emit = defineEmits<{
  reload: [];
}>();

const formRef = ref();

// Query state
const queryForm = reactive({
  keyword: '',
  pageNum: 1,
  pageSize: 20
});

function reloadList() {
  emit('reload');
}

// Form state
const formVisible = ref(false);

function openForm() {
  formVisible.value = true;
}

defineExpose({
  openForm
});
</script>
```

## Views And Pages

- Organize pages by business module.
- List pages end with `-list.vue`, such as `role-list.vue`.
- Form pages end with `-form.vue`, such as `role-form.vue`.
- Modal components end with `-modal.vue`, such as `role-form-modal.vue` or `role-detail-modal.vue`.
- Drawer components end with `-drawer.vue`, such as `role-detail-drawer.vue`.
- Module-local reusable components live in a `components` directory under the module.
- Module-local styles live next to the module, using the existing project style convention.

Recommended page layout:

```text
src/pages/
  role/
    role-list.vue
    role-add-form.vue
    role-update-form-modal.vue
    components/
      role-title-modal.vue
```

## Router And Stores

- Split router and store files by business module. Do not put all routes or all stores in one large file.
- Router modules should mirror the page/module structure as much as possible.
- Pinia stores should be named by domain concern, such as `user-session`, `permission`, or `tenant`.
- Keep store state minimal and serializable. Put request orchestration in services or composables when it is not shared state.

## API, Services, And Requests

- API functions belong in `packages/api` when they are shared by multiple apps.
- App-specific service orchestration belongs in `apps/<app-name>/src/services`.
- Use one API module per business domain.
- Export named functions or typed service objects; avoid large anonymous default objects.
- Request payload and response types should be explicit TypeScript interfaces or types.
- Do not call `fetch` or `axios` directly from views. Use the shared request layer or a service wrapper.

## Constants And Dictionaries

- Constants and dictionaries are grouped by business module.
- Constant names use uppercase snake case, such as `EMPLOYEE_STATUS`.
- Prefer object values with both machine value and display label, such as `{ value, label }`.
- Keep dictionaries close to the module that owns them unless they are truly shared across apps.
- Shared constants belong in `packages/shared`.

Example:

```ts
export const EMPLOYEE_STATUS = {
  NORMAL: {
    value: 1,
    label: '正常'
  },
  DISABLED: {
    value: 2,
    label: '禁用'
  }
} as const;
```

## Assets

- Static assets use lowercase kebab-case.
- Shared assets belong in the shared package or a shared app-level assets directory.
- Business-module assets should stay under that module when they are not reused elsewhere.
- Avoid placing unrelated assets in a single flat directory.

## Composables

- Extract a composable when a logical concern is reused or when a page component becomes difficult to scan.
- Composable files use `use-*.ts`, such as `use-table-query.ts`.
- Composable function names use camelCase and start with `use`, such as `useTableQuery`.
- A composable should own one clear concern, not an entire page workflow.

## Quality Gates

- Run `npm run typecheck` before merging frontend changes.
- Run `npm run build` for changes that affect app entry points, Vite config, shared packages, routing, or generated API types.
- Keep lint/type errors at zero. Do not leave TODO comments unless they include a concrete follow-up owner or issue.
