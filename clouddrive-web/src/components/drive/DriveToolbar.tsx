// src/components/drive/DriveToolbar.tsx
import React, { useState } from 'react';
import { useDriveStore } from '../../store/drive';

const DriveToolbar: React.FC = () => {
  const { currentFolderId, createFolder, loading } = useDriveStore();
  const [creating, setCreating] = useState(false);
  const [name, setName] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) return;
    await createFolder(name.trim());
    setName('');
    setCreating(false);
  };

  return (
    <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
      <div className="space-y-0.5">
        <p className="text-[11px] font-semibold uppercase tracking-[0.18em] text-violet-500">
          Drive
        </p>
        <p className="text-xs text-slate-500">
          {currentFolderId === null ? 'My Drive' : `Inside folder #${currentFolderId}`}
        </p>
      </div>

      <div className="flex items-center gap-2">
        {creating ? (
          <form onSubmit={handleSubmit} className="flex items-center gap-2">
            <input
              autoFocus
              value={name}
              onChange={e => setName(e.target.value)}
              placeholder="Folder name"
              className="rounded-xl border border-slate-200 bg-white px-3 py-1.5 text-xs md:text-sm text-slate-900 focus:outline-none focus:ring-2 focus:ring-violet-500"
            />
            <button
              type="submit"
              disabled={loading}
              className="rounded-lg bg-slate-900 px-3 py-1.5 text-xs md:text-sm font-semibold text-white hover:bg-slate-800 disabled:opacity-60"
            >
              Create
            </button>
            <button
              type="button"
              onClick={() => {
                setCreating(false);
                setName('');
              }}
              className="rounded-lg border border-slate-200 px-3 py-1.5 text-xs md:text-sm text-slate-600 hover:bg-slate-50"
            >
              Cancel
            </button>
          </form>
        ) : (
          <button
            type="button"
            onClick={() => setCreating(true)}
            className="inline-flex items-center gap-1.5 rounded-xl bg-slate-900 px-3.5 py-2 text-xs md:text-sm font-semibold text-white shadow-[0_14px_30px_rgba(15,23,42,0.45)] hover:bg-slate-800"
          >
            <span className="material-symbols-outlined text-sm">create_new_folder</span>
            New folder
          </button>
        )}
      </div>
    </div>
  );
};

export default DriveToolbar;
