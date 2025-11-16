import React from 'react';
export default function Input(props: React.InputHTMLAttributes<HTMLInputElement>) {
  return (
    <input
      {...props}
      className="w-full rounded-xl border border-black/10 dark:border-white/10 bg-white/70 dark:bg-white/10 px-3 py-2 outline-none focus:ring-2 focus:ring-brand-500"
    />
  );
}
