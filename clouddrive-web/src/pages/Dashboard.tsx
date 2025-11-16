import { useEffect, useState } from 'react';
import { useAuthGuard } from '../lib/auth';
import api from '../lib/api';
import UploadDialog from '../components/UploadDialog';
import FileCard from '../components/FileCard';

type FileRow = {
  id: number;
  originalName: string;
  mimeType: string;
  sizeBytes: number;
  s3Key: string;
  createdAt: string;
};

export default function Dashboard() {
  useAuthGuard();
  const [files,setFiles]=useState<FileRow[]>([]);
  const [loading,setLoading]=useState(true);

  const load = async () => {
    setLoading(true);
    try {
      const { data } = await api.get('/api/files', { params: { page:0, size:50 } });
      setFiles(data);
    } finally { setLoading(false); }
  };

  useEffect(() => { load(); }, []);

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-semibold">Your files</h1>
        <UploadDialog onUploaded={load} />
      </div>

      {loading ? (
        <div className="opacity-70">Loading…</div>
      ) : files.length === 0 ? (
        <div className="glass rounded-2xl p-8 text-center opacity-75">
          No files yet. Click <span className="font-medium">Upload</span> to add your first file.
        </div>
      ) : (
        <div className="grid gap-3">
          {files.map(f => <FileCard key={f.id} f={f} onDelete={(id)=>setFiles(prev=>prev.filter(x=>x.id!==id))} />)}
        </div>
      )}
    </div>
  );
}
