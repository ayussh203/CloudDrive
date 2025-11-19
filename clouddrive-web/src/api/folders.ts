// src/api/folders.ts
import api from '../lib/api';

export type Folder = {
  id: number;
  name: string;
  parentId: number | null;
  path: string;
};

export type CreateFolderRequest = {
  name: string;
  parentId: number | null;
};

export async function listFolders(
  parentId: number | null
): Promise<Folder[]> {
  const { data } = await api.get<Folder[]>('/api/folders', {
    params: { parentId },
  });
  return data;
}

export async function createFolderApi(
  req: CreateFolderRequest
): Promise<Folder> {
  const { data } = await api.post<Folder>('/api/folders', req);
  return data;
}
