// src/store/drive.ts
import { create } from 'zustand';
import { type Folder, listFolders, createFolderApi } from '../api/folders';

type DriveState = {
  currentFolderId: number | null;
  folders: Folder[];
  loading: boolean;
  error: string | null;
  selectedFolderIds: number[];
  selectedFileIds: number[];

  // actions
  loadRoot: () => Promise<void>;
  openFolder: (folderId: number | null) => Promise<void>;
  createFolder: (name: string) => Promise<void>;

  toggleFolderSelection: (id: number, multi: boolean) => void;
  toggleFileSelection: (id: number, multi: boolean) => void;
  clearSelection: () => void;
};

export const useDriveStore = create<DriveState>((set, get) => ({
  currentFolderId: null,
  folders: [],
  loading: false,
  error: null,

  selectedFolderIds: [],
  selectedFileIds: [],

  async loadRoot() {
    await get().openFolder(null);
  },

  async openFolder(folderId) {
    set({ loading: true, error: null, currentFolderId: folderId });

    try {
      const folders = await listFolders(folderId);
      set({ folders });
    } catch (e: any) {
      set({ error: e.message || 'Failed to load folders' });
    } finally {
      set({ loading: false });
    }
  },

  async createFolder(name: string) {
    const parentId = get().currentFolderId;
    try {
      await createFolderApi({ name, parentId: parentId ?? null });
      await get().openFolder(get().currentFolderId);
    } catch (e: any) {
      set({ error: e.message || 'Folder creation failed' });
    }
  },

  toggleFolderSelection(id, multi) {
    set(state => {
      if (!multi) {
        const already = state.selectedFolderIds.includes(id);
        return { selectedFolderIds: already ? [] : [id] };
      }
      const exists = state.selectedFolderIds.includes(id);
      return {
        selectedFolderIds: exists
          ? state.selectedFolderIds.filter(x => x !== id)
          : [...state.selectedFolderIds, id],
      };
    });
  },

  toggleFileSelection(id, multi) {
    set(state => {
      if (!multi) {
        const already = state.selectedFileIds.includes(id);
        return { selectedFileIds: already ? [] : [id] };
      }
      const exists = state.selectedFileIds.includes(id);
      return {
        selectedFileIds: exists
          ? state.selectedFileIds.filter(x => x !== id)
          : [...state.selectedFileIds, id],
      };
    });
  },

  clearSelection() {
    set({ selectedFolderIds: [], selectedFileIds: [] });
  },
}));
