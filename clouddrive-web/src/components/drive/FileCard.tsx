// src/components/drive/FileCard.tsx
import React from 'react';
import type { FileItem } from '../../api/files';
import { useDriveStore } from '../../store/drive';

type Props = {
  file: FileItem;
  onOpenPreview: () => void;
};

const FileCard: React.FC<Props> = ({ file, onOpenPreview }) => {
  const { selectedFileIds, toggleFileSelection } = useDriveStore(s => ({
    selectedFileIds: s.selectedFileIds,
    toggleFileSelection: s.toggleFileSelection,
  }));

  const isSelected = selectedFileIds.includes(file.id);

  const prettySize = (() => {
    const kb = file.sizeBytes / 1024;
    if (kb < 1024) return `${kb.toFixed(1)} KB`;
    const mb = kb / 1024;
    return `${mb.toFixed(1)} MB`;
  })();

  const isPreviewable = file.mimeType.startsWith('image/') || file.mimeType === 'application/pdf';

  return (
    <div
      className={`group relative flex cursor-pointer items-center justify-between rounded-2xl border px-4 py-3
        bg-white/95 backdrop-blur
        transition-all duration-150
        ${
          isSelected
            ? 'border-violet-500/80 ring-2 ring-violet-300/70'
            : 'border-slate-200/80 hover:border-violet-300/80 hover:bg-violet-50/70'
        }`}
      onClick={e => {
        const multi = e.metaKey || e.ctrlKey;
        toggleFileSelection(file.id, multi);
      }}
      onDoubleClick={() => {
        if (isPreviewable) onOpenPreview();
      }}
    >
      <div className="flex items-center gap-3 overflow-hidden">
        <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-slate-900 text-white shadow-sm">
          <span className="material-symbols-outlined text-[18px]">
            description
          </span>
        </div>
        <div className="min-w-0">
          <p className="truncate text-sm font-medium text-slate-900">
            {file.originalName}
          </p>
          <p className="text-[11px] text-slate-400">
            {file.mimeType} · {prettySize}
          </p>
        </div>
      </div>

      {isPreviewable && (
        <button
          type="button"
          onClick={e => {
            e.stopPropagation();
            onOpenPreview();
          }}
          className="inline-flex items-center gap-1 rounded-full bg-slate-900 px-3 py-1 text-xs font-medium text-white opacity-0 shadow-sm transition-opacity group-hover:opacity-100"
        >
          <span className="material-symbols-outlined text-[14px]">
            visibility
          </span>
          Preview
        </button>
      )}
    </div>
  );
};

export default FileCard;
