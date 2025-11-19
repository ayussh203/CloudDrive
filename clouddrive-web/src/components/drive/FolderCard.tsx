// src/components/drive/FolderCard.tsx
import React from 'react';
import type { Folder } from '../../api/folders';
import { useDriveStore } from '../../store/drive';

type Props = {
  folder: Folder;
  onOpen: () => void;
};

const FolderCard: React.FC<Props> = ({ folder, onOpen }) => {
  const { selectedFolderIds, toggleFolderSelection } = useDriveStore(s => ({
    selectedFolderIds: s.selectedFolderIds,
    toggleFolderSelection: s.toggleFolderSelection,
  }));

  const isSelected = selectedFolderIds.includes(folder.id);

  return (
    <div
      className={`group flex cursor-pointer items-center justify-between rounded-2xl border px-4 py-3
        bg-white/90 backdrop-blur hover:bg-violet-50/70
        transition-all duration-150
        ${
          isSelected
            ? 'border-violet-500/80 ring-2 ring-violet-300/70'
            : 'border-slate-200/80 hover:border-violet-300/80 hover:bg-slate-50/80'
        }`}
      onClick={e => {
        const multi = e.metaKey || e.ctrlKey;
        toggleFolderSelection(folder.id, multi);
      }}
      onDoubleClick={onOpen}
    >
      <div className="flex items-center gap-3 overflow-hidden">
        <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-gradient-to-tr from-amber-400 via-orange-500 to-pink-500 text-white shadow-sm">
          <span className="material-symbols-outlined text-[20px]">folder</span>
        </div>
        <div className="min-w-0">
          <p className="truncate text-sm font-medium text-slate-900">
            {folder.name}
          </p>
          <p className="text-[11px] uppercase tracking-[0.16em] text-slate-400">
            Folder
          </p>
        </div>
      </div>

      <span className="material-symbols-outlined text-slate-300 group-hover:text-violet-400 text-[18px]">
        chevron_right
      </span>
    </div>
  );
};

export default FolderCard;
