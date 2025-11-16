export function cn(...a: any[]) { return a.filter(Boolean).join(' '); }
export const fmtBytes = (b?: number) =>
  typeof b !== 'number' ? '-' :
  b < 1024 ? `${b} B` :
  b < 1024**2 ? `${(b/1024).toFixed(1)} KB` :
  b < 1024**3 ? `${(b/1024**2).toFixed(1)} MB` :
                `${(b/1024**3).toFixed(1)} GB`;
