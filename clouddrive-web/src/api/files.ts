// src/api/files.ts
import api from '../lib/api';

export type FileItem = {
  id: number;
  s3Key: string;
  originalName: string;
  mimeType: string;
  sizeBytes: number;
  checksumSha256?: string | null;
  createdAt: string;
  // backend doesn't send folderId yet, keep it optional
  folderId?: number | null;
};

export async function listFiles(page = 0, size = 200): Promise<FileItem[]> {
  const { data } = await api.get<FileItem[]>('/api/files', {
    params: { page, size },
  });
  return data;
}

export async function uploadFile(
  file: File,
  folder: string = 'uploads'
): Promise<FileItem> {
  const form = new FormData();
  form.append('file', file);

  const { data } = await api.post<FileItem>(
    '/api/files/upload',
    form,
    {
      params: { folder },
      headers: { 'Content-Type': 'multipart/form-data' },
    }
  );

  return data;
}
