import { cva } from 'class-variance-authority';
import type { VariantProps } from 'class-variance-authority';
import { cn } from '../../lib/format';
import React from 'react';

const styles = cva(
  'inline-flex items-center justify-center rounded-xl px-4 py-2 font-medium transition-all focus:outline-none focus:ring-2 focus:ring-offset-2',
  {
    variants: {
      variant: {
        primary: 'bg-brand-600 text-white hover:bg-brand-700 focus:ring-brand-600',
        ghost: 'bg-transparent hover:bg-black/5 dark:hover:bg-white/10',
        danger: 'bg-red-600 text-white hover:bg-red-700 focus:ring-red-600',
      },
      size: { sm: 'text-sm', md: 'text-base' },
    },
    defaultVariants: { variant: 'primary', size: 'md' },
  }
);

type Props = React.ButtonHTMLAttributes<HTMLButtonElement> & VariantProps<typeof styles>;
export default function Button({ className, variant, size, ...props }: Props) {
  return <button className={cn(styles({ variant, size }), className)} {...props} />;
}
