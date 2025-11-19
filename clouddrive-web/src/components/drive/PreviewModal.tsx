// src/components/drive/PreviewModal.tsx
import React from 'react';
import type { FileItem } from '../../api/files';

type Props = {
  file: FileItem | null;
  onClose: () => void;
};

const PreviewModal: React.FC<Props> = ({ file, onClose }) => {
  if (!file) return null;

  const isImage = file.mimeType.startsWith('image/');

  return (
    <div className="fixed inset-0 z-40 flex items-center justify-center bg-slate-900/40 backdrop-blur-sm">
      <div className="relative w-full max-w-3xl rounded-3xl bg-white/95 p-6 shadow-2xl">
        <button
          type="button"
          className="absolute right-4 top-4 rounded-full bg-slate-900 text-white px-2 py-1 text-xs"
          onClick={onClose}
        >
          Close
        </button>

        <h2 className="mb-3 text-lg font-semibold text-slate-900">
          {file.originalName}
        </h2>

        <p className="mb-4 text-xs text-slate-500">
          {file.mimeType} · {(file.sizeBytes / 1024 / 1024).toFixed(2)} MB
        </p>

        <div className="rounded-2xl border border-slate-200 bg-slate-50/80 p-4 max-h-[60vh] overflow-auto">
          {isImage ? (
            <img
              src={`/s/${file.id}`} // placeholder route; you can adjust later
              alt={file.originalName}
              className="mx-auto max-h-[50vh] rounded-xl object-contain"
            />
          ) : (
            <p className="text-sm text-slate-600">
              Preview for this file type is not implemented yet. You can share /
              download it via share or short-link features in the next stories.
            </p>
          )}
        </div>
      </div>
    </div>
  );
};

export default PreviewModal;
