// src/components/drive/UploadDropzone.tsx
import React, { useRef, useState, type DragEvent } from 'react';

type Props = {
  onUpload: (files: File[]) => Promise<void> | void;
};

const UploadDropzone: React.FC<Props> = ({ onUpload }) => {
  const inputRef = useRef<HTMLInputElement | null>(null);
  const [isDragging, setIsDragging] = useState(false);

  const handleFiles = async (fileList: FileList | File[]) => {
    const files = Array.from(fileList);
    if (files.length === 0) return;
    await onUpload(files);
  };

  const onDrop = async (e: DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(false);
    if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
      await handleFiles(e.dataTransfer.files);
      e.dataTransfer.clearData();
    }
  };

  const onDragOver = (e: DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    e.stopPropagation();
    if (!isDragging) setIsDragging(true);
  };

  const onDragLeave = (e: DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(false);
  };

  const openPicker = () => {
    inputRef.current?.click();
  };

  return (
    <div
      onDrop={onDrop}
      onDragOver={onDragOver}
      onDragLeave={onDragLeave}
      className={`relative flex flex-col items-center justify-center rounded-3xl border
        border-dashed px-8 py-16 bg-white/80 backdrop-blur-xl
        shadow-[0_18px_45px_rgba(148,163,184,0.28)]
        transition-all duration-200
        ${
          isDragging
            ? 'border-violet-500/80 bg-violet-50/80'
            : 'border-slate-200/80 hover:border-violet-400/60 hover:bg-slate-50/80'
        }`}
    >
      <input
        ref={inputRef}
        type="file"
        multiple
        className="hidden"
        onChange={async e => {
          if (e.target.files) {
            await handleFiles(e.target.files);
            e.target.value = '';
          }
        }}
      />

      <div className="flex flex-col items-center gap-3 text-center">
        <div className="inline-flex h-11 w-11 items-center justify-center rounded-2xl bg-gradient-to-tr from-violet-500 via-fuchsia-500 to-amber-400 text-white shadow-md">
          <span className="material-symbols-outlined text-[22px]">upload</span>
        </div>

        <div className="space-y-1">
          <p className="text-sm md:text-base font-medium text-slate-900">
            Drop files here to upload
          </p>
          <p className="text-xs md:text-sm text-slate-500">
            Or{' '}
            <button
              type="button"
              onClick={openPicker}
              className="font-semibold text-violet-600 hover:text-violet-700"
            >
              browse from your device
            </button>
            .
          </p>
        </div>

        <p className="mt-2 text-[11px] uppercase tracking-[0.16em] text-slate-400">
          Secure uploads · S3 backed · Private by default
        </p>
      </div>
    </div>
  );
};

export default UploadDropzone;
