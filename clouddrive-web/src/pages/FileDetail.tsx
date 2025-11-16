import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import api from '../lib/api';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';

type FileRow = {
  id: number;
  originalName: string;
  mimeType: string;
  sizeBytes: number;
  s3Key: string;
};

export default function FileDetail() {
  const { id } = useParams();
  const [f,setF]=useState<FileRow|null>(null);
  const [name,setName]=useState('');
  const [saving,setSaving]=useState(false);

  useEffect(() => {
    (async () => {
      const { data } = await api.get(`/api/files/${id}`);
      setF(data); setName(data.originalName);
    })();
  }, [id]);

  const rename = async () => {
    setSaving(true);
    try {
      const { data } = await api.patch(`/api/files/${id}`, { originalName: name });
      setF(data);
    } finally { setSaving(false); }
  };

  if (!f) return <div className="opacity-70">Loading…</div>;

  return (
    <div className="max-w-xl space-y-6">
      <div className="glass rounded-2xl p-6">
        <div className="text-sm opacity-70">File name</div>
        <div className="mt-2 flex gap-3">
          <Input value={name} onChange={e=>setName(e.target.value)} />
          <Button onClick={rename} disabled={saving}>{saving?'Saving…':'Save'}</Button>
        </div>
      </div>

      <div className="glass rounded-2xl p-6">
        <div className="text-sm opacity-70">Details</div>
        <div className="mt-2 text-sm">
          <div><b>Type:</b> {f.mimeType}</div>
          <div><b>S3 Key:</b> <code className="opacity-80">{f.s3Key}</code></div>
        </div>
      </div>
    </div>
  );
}
