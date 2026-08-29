/**
 * DTO for creating/updating a workspace
 */
export interface WorkspaceDTO {
  icon: string;
  iconBg: string;
  title: string;
  description: string;
  progress: number; // 0-100
  updatedText: string;
}

/**
 * Service interface for Workspace operations
 */
export interface IWorkspaceService {
  create(data: WorkspaceDTO): Promise<Workspace>;
  getById(id: number): Promise<Workspace | null>;
  update(id: number, data: WorkspaceDTO): Promise<Workspace | null>;
  delete(id: number): Promise<boolean>;
  getAll(): Promise<Workspace[]>;
}

/**
 * DTO for response in create/update operations
 */
export interface WorkspaceCreateUpdateResponse {
  id: number;
  icon: string;
  iconBg: string;
  title: string;
  description: string;
  progress: number;
  updatedText: string;
}