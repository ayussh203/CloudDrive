export default function Logo({ className = "h-8 w-8" }: { className?: string }) {
  return (
    <div className={`${className} rounded-2xl bg-gradient-to-br from-brand-500 to-brand-700 relative`}>
      {/* CD monogram */}
      <svg viewBox="0 0 48 48" className="absolute inset-0 m-1 text-white/95" fill="none" stroke="currentColor" strokeWidth="3">
        <path d="M16 14a10 10 0 100 20" />
        <path d="M32 34a10 10 0 100-20" />
      </svg>
    </div>
  );
}
