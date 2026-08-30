// // import { Injectable } from '@nestjs/common';
// import { WorkspaceDTO } from '../controllers/workspace/Workspace';

// @Injectable()
// export class WorkspaceService {
//   private workspaces: Map<number, WorkspaceDTO> = new Map();

//   async create(data: WorkspaceDTO): Promise<WorkspaceDTO> {
//     const id = Date.now();
//     this.workspaces.set(id, data);
//     return data;
//   }

//   async getById(id: number): Promise<WorkspaceDTO | null> {
//     return this.workspaces.get(id) || null;
//   }

//   async update(id: number, data: WorkspaceDTO): Promise<WorkspaceDTO | null> {
//     const existing = this.workspaces.get(id);
//     if (!existing) return null;

//     Object.assign(existing, data);
//     return existing;
//   }

//   async delete(id: number): Promise<boolean> {
//     return this.workspaces.delete(id);
//   }

//   async getAll(): Promise<WorkspaceDTO[]> {
//     return Array.from(this.workspaces.values());
//   }
// }