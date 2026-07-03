export interface ConfirmActionOptions {
  title: string;
  risk: string;
  confirmText: string;
}

export function confirmImportantAction(options: ConfirmActionOptions) {
  const acknowledged = window.confirm(`${options.title}\n\n${options.risk}`);
  if (!acknowledged) {
    return false;
  }
  return window.confirm(options.confirmText);
}

export function confirmDelete(target: string, risk: string) {
  return confirmImportantAction({
    title: `删除 ${target}`,
    risk,
    confirmText: `确认删除 ${target}？`
  });
}
