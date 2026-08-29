import { TaskStatus, TaskPriority } from '../entity/task';

/**
 * DTO for creating/updating a task
 */
export interface TaskDTO {
  workspaceId: number;
  title: string;
  status: 'TODO' | 'IN_PROGRESS' | 'DONE';
  priority: 'HIGH' | 'MEDIUM' | 'LOW';
  due: string;
}

/**
 * Service interface for Task operations
 */
export interface ITaskService {
  create(data: TaskDTO): Promise<Task>;
  getById(id: number): Promise<Task | null>;
  update(id: number, data: TaskDTO): Promise<Task | null>;
  delete(id: number): Promise<boolean>;
  getAll(): Promise<Task[]>;
  getByWorkspaceId(workspaceId: number): Promise<Task[]>;
}

/**
 * DTO for response in create/update operations
 */
export interface TaskCreateUpdateResponse {
  id: number;
  workspaceId: number;
  title: string;
  status: 'TODO' | 'IN_PROGRESS' | 'DONE';
  priority: 'HIGH' | 'MEDIUM' | 'LOW';
  due: string;
}