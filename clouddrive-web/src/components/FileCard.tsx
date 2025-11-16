import { Link } from 'react-router-dom';
import Button from './ui/Button';
import { fmtBytes } from '../lib/format';
import api from '../lib/api';

type FileRow = {
  id: number;
  originalName: string;
  mimeType: string;
  sizeBytes: number;
  s3Key: string;
  hasThumbnail?: boolean;
  createdAt: string;
};

export default function FileCard({ f, onDelete }: { f: FileRow; onDelete: (id:number)=>void }) {
  const del = async () => {
    if (!confirm(`Delete ${f.originalName}?`)) return;
    await api.delete(`/api/files/${f.id}`);
    onDelete(f.id);
  };

  return (
    <div className="glass rounded-2xl p-4 flex items-center justify-between">
      <div className="flex items-center gap-4">
        <div className="h-12 w-12 rounded-xl bg-brand-500/10 flex items-center justify-center">{/* icon */}</div>
        <div>
          <Link to={`/files/${f.id}`} className="font-medium hover:underline">{f.originalName}</Link>
          <div className="text-sm opacity-70">{f.mimeType} • {fmtBytes(f.sizeBytes)}</div>
        </div>
      </div>
      <div className="flex gap-2">
        <Button variant="ghost" onClick={del}>Delete</Button>
      </div>
    </div>
  );
}
