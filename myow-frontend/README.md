# MYOW Frontend

This directory is an independent frontend workspace inside the current backend repository.
It is intentionally isolated so it can be moved to a standalone frontend repository later.

## Structure

```text
myow-frontend/
├── apps/
│   ├── admin-web/
│   └── client-web/
├── packages/
│   ├── api/
│   ├── shared/
│   └── ui/
├── package.json
└── tsconfig.base.json
```

## Commands

Use Node 20+.

```bash
npm install
npm run dev:admin
npm run dev:client
npm run build
```

The current Windows PowerShell environment may block `npm.ps1`; use `npm.cmd` if needed.

## Standards

- [Frontend standard](./spec/frontend-standard.md)
