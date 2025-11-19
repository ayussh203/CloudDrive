// src/pages/HomePage.tsx
import React from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';

const fadeUp = {
  hidden: { opacity: 0, y: 24 },
  visible: { opacity: 1, y: 0 },
};

type CardProps = {
  icon: string;
  title: string;
  badge?: string;
  description: string;
};

const FeatureCard: React.FC<CardProps> = ({ icon, title, badge, description }) => (
  <motion.div
    variants={fadeUp}
    initial="hidden"
    whileInView="visible"
    viewport={{ once: false, amount: 0.4 }} // animate every time it comes into view
    transition={{ duration: 0.45, ease: 'easeOut' }}
    className="relative overflow-hidden rounded-3xl bg-white/80 shadow-[0_18px_45px_rgba(15,23,42,0.10)] 
               border border-white/60 backdrop-blur-md px-6 py-5 md:px-7 md:py-6 flex items-start gap-4"
  >
    <div className="h-11 w-11 flex items-center justify-center rounded-2xl bg-gradient-to-br 
                    from-[#8b5cf6] via-[#ec4899] to-[#f97316] text-white text-xl shrink-0">
      {icon}
    </div>
    <div className="space-y-1">
      <div className="flex items-center gap-2">
        <h3 className="font-semibold text-slate-900 text-lg md:text-xl">{title}</h3>
        {badge && (
          <span className="text-[0.7rem] uppercase tracking-wide rounded-full bg-violet-50 
                           text-violet-600 px-2 py-0.5 font-semibold">
            {badge}
          </span>
        )}
      </div>
      <p className="text-slate-600 text-sm md:text-base leading-relaxed">{description}</p>
    </div>
  </motion.div>
);

const HomePage: React.FC = () => {
  const nav = useNavigate();

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#f5f3ff] via-[#faf5ff] to-[#fdf2ff] text-slate-900">
      {/* NAVBAR */}
      <header className="sticky top-0 z-30 bg-gradient-to-b from-white/90 to-white/60 backdrop-blur border-b border-white/70">
        <div className="mx-auto max-w-6xl px-4 sm:px-6 lg:px-8 flex items-center justify-between h-16">
          <div className="flex items-center gap-2">
            <div className="h-9 w-9 rounded-2xl bg-gradient-to-tr from-[#4f46e5] via-[#8b5cf6] to-[#ec4899] 
                            flex items-center justify-center text-white font-semibold text-sm shadow-lg">
              CD
            </div>
            <span className="font-semibold text-lg tracking-tight">CloudDrive</span>
          </div>

          <nav className="flex items-center gap-3 text-sm">
            <button
              type="button"
              onClick={() => nav('/login')}
              className="px-4 py-2 rounded-full text-slate-700 hover:text-slate-900 hover:bg-slate-100 
                         transition-colors"
            >
              Log in
            </button>
            <button
              type="button"
              onClick={() => nav('/register')}
              className="px-4 py-2 rounded-full bg-slate-900 text-white text-sm font-semibold shadow-md 
                         hover:bg-slate-800 transition-colors"
            >
              Get started
            </button>
          </nav>
        </div>
      </header>

      <main className="mx-auto max-w-6xl px-4 sm:px-6 lg:px-8 pt-12 pb-20">
        {/* HERO */}
        <section className="grid md:grid-cols-[minmax(0,1.1fr)_minmax(0,0.9fr)] gap-10 items-center mb-16">
          <div className="space-y-6">
            <motion.p
              variants={fadeUp}
              initial="hidden"
              animate="visible"
              transition={{ duration: 0.4, ease: 'easeOut' }}
              className="text-xs md:text-sm font-semibold tracking-[0.22em] text-violet-600 uppercase"
            >
              Modern file sharing
            </motion.p>

            <motion.h1
              variants={fadeUp}
              initial="hidden"
              animate="visible"
              transition={{ duration: 0.5, ease: 'easeOut', delay: 0.05 }}
              className="text-4xl md:text-5xl lg:text-[3.5rem] font-bold tracking-tight text-slate-900 leading-tight"
            >
              Store. <span className="text-slate-900">Share.</span>{' '}
              <span className="bg-gradient-to-r from-[#8b5cf6] via-[#ec4899] to-[#f97316] bg-clip-text text-transparent">
                Delight.
              </span>
            </motion.h1>

            <motion.p
              variants={fadeUp}
              initial="hidden"
              animate="visible"
              transition={{ duration: 0.5, ease: 'easeOut', delay: 0.1 }}
              className="max-w-xl text-slate-600 text-sm md:text-base leading-relaxed"
            >
              CloudDrive is your fast, secure home for files. Create passworded share links with custom
              URLs, track clicks, and preview PDFs &amp; images inline — all in one minimal workspace.
            </motion.p>

            <motion.div
              variants={fadeUp}
              initial="hidden"
              animate="visible"
              transition={{ duration: 0.45, ease: 'easeOut', delay: 0.16 }}
              className="flex flex-wrap items-center gap-3"
            >
              <button
                type="button"
                onClick={() => nav('/register')}
                className="px-6 py-3 rounded-full bg-slate-900 text-white text-sm md:text-base font-semibold 
                           shadow-[0_14px_30px_rgba(15,23,42,0.5)] hover:-translate-y-[1px] active:translate-y-0 
                           transition-transform"
              >
                Create free account
              </button>
              <button
                type="button"
                onClick={() => nav('/login')}
                className="px-5 py-3 rounded-full border border-slate-200 bg-white/70 text-sm md:text-base 
                           text-slate-800 font-medium hover:bg-slate-50 transition-colors"
              >
                Log in
              </button>
            </motion.div>
          </div>

          {/* Hero preview cards */}
          <div className="space-y-4">
            <FeatureCard
              icon="📁"
              title="Beautiful Drive"
              badge="New"
              description="Organise your files into clean folders, drag & drop to move, multi-select items, and run quick actions in one click."
            />
            <FeatureCard
              icon="🔗"
              title="Share links"
              badge="New"
              description="Create share links that can expire automatically, protect them with a password, and customise the link text."
            />
            <FeatureCard
              icon="📊"
              title="Short URLs & analytics"
              badge="New"
              description="Turn any share link into a tiny URL and see how many people clicked, which country they are from, and what device they used."
            />
          </div>
        </section>

        {/* WHY CLOUDDRIVE */}
        <section className="space-y-8">
          <motion.h2
            variants={fadeUp}
            initial="hidden"
            whileInView="visible"
            viewport={{ once: false, amount: 0.4 }}
            transition={{ duration: 0.45, ease: 'easeOut' }}
            className="text-2xl md:text-3xl font-semibold tracking-tight text-slate-900"
          >
            Why CloudDrive?
          </motion.h2>

          <div className="grid md:grid-cols-3 gap-5">
            <FeatureCard
              icon="⚡"
              title="Instant uploads"
              description="Drop a file and it’s uploaded in seconds. No stalls, no mystery errors — just a smooth progress bar."
            />
            <FeatureCard
              icon="🛡️"
              title="Safe by default"
              description="Your files stay private by default. Only people with your share link (and optional password) can access them."
            />
            <FeatureCard
              icon="🧭"
              title="Organise anything"
              description="Create nested folders for clients, projects, or semesters. Move things around without breaking your links."
            />
            <FeatureCard
              icon="👀"
              title="Inline previews"
              description="Open PDFs and images directly in the browser with zoom, paginate, and quick download — no extra apps needed."
            />
            <FeatureCard
              icon="✨"
              title="Custom slugs"
              description="Replace ugly, random URLs with clean, memorable names like /cv/ayush or /portfolio/design-review."
            />
            <FeatureCard
              icon="📈"
              title="Actionable analytics"
              description="See which links are performing, which countries are engaging, and which devices visitors use — all at a glance."
            />
          </div>
        </section>
      </main>

      {/* FOOTER */}
      <footer className="border-t border-slate-200 bg-white/80">
        <div className="mx-auto max-w-6xl px-4 sm:px-6 lg:px-8 py-6 flex flex-col md:flex-row items-center justify-between gap-4 text-xs md:text-sm text-slate-500">
          <span>© 2025 CloudDrive. All rights reserved.</span>
          <div className="flex flex-wrap items-center gap-4">
            <span className="text-slate-400">Built for effortless file sharing.</span>
            <a href="#" className="hover:text-slate-700">
              Product
            </a>
            <a href="#" className="hover:text-slate-700">
              Security
            </a>
            <a href="#" className="hover:text-slate-700">
              Status
            </a>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default HomePage;
