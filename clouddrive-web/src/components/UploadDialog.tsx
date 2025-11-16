import { useState } from 'react';
import Button from './ui/Button';
import api from '../lib/api';

type Props = { onUploaded: () => void };

export default function UploadDialog({ onUploaded }: Props) {
  const [open,setOpen]=useState(false);
  const [file,setFile]=useState<File|undefined>(undefined);
  const [loading,setLoading]=useState(false);

  const upload = async () => {
    if (!file) return;
    setLoading(true);
    try {
      const form = new FormData();
      form.append('file', file);
      // optional: form.append('folder','uploads');
      await api.post('/api/files/upload', form, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      onUploaded();
      setOpen(false); setFile(undefined);
    } catch (e:any) {
      alert(e?.response?.data?.message || 'Upload failed');
    } finally { setLoading(false); }
  };

  return (
    <>
      <Button variant="primary" onClick={()=>setOpen(true)}>Upload</Button>
      {open && (
        <div className="fixed inset-0 z-20 bg-black/30 flex items-center justify-center p-4">
          <div className="glass rounded-2xl w-full max-w-md p-6">
            <h3 className="text-lg font-semibold mb-3">Upload file</h3>
            <input type="file" onChange={e=>setFile(e.target.files?.[0])} />
            <div className="mt-5 flex gap-3">
              <Button onClick={upload} disabled={!file || loading}>{loading?'Uploading…':'Upload'}</Button>
              <Button variant="ghost" onClick={()=>setOpen(false)}>Cancel</Button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
