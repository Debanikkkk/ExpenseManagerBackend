import { TaskDTO, ITaskService } from './Task';

/**
 * Controller for Task CRUD operations
 */
export class TaskController {
  private taskService: ITaskService;

  constructor(taskService: ITaskService) {
    this.taskService = taskService;
  }

  /**
   * Create a new task
   */
  public async create(dto: TaskDTO): Promise<TaskCreateUpdateResponse> {
    const created = await this.taskService.create(dto);
    return {
      id: created.id,
      workspaceId: created.workspaceId,
      title: created.title,
      status: created.status,
      priority: created.priority,
      due: created.due,
    };
  }

  /**
   * Get a task by ID
   */
  public async getById(id: number): Promise<TaskCreateUpdateResponse | null> {
    const task = await this.taskService.getById(id);
    if (!task) return null;
    return {
      id: task.id,
      workspaceId: task.workspaceId,
      title: task.title,
      status: task.status,
      priority: task.priority,
      due: task.due,
    };
  }

  /**
   * Update an existing task
   */
  public async update(id: number, dto: TaskDTO): Promise<TaskCreateUpdateResponse | null> {
    const updated = await this.taskService.update(id, dto);
    if (!updated) return null;
    return {
      id: updated.id,
      workspaceId: updated.workspaceId,
      title: updated.title,
      status: updated.status,
      priority: updated.priority,
      due: updated.due,
    };
  }

  /**
   * Delete a task by ID
   */
  public async delete(id: number): Promise<boolean> {
    return await this.taskService.delete(id);
  }

  /**
   * Get all tasks
   */
  public async getAll(): Promise<TaskCreateUpdateResponse[]> {
    const tasks = await this.taskService.getAll();
    return tasks.map(task => ({
      id: task.id,
      workspaceId: task.workspaceId,
      title: task.title,
      status: task.status,
      priority: task.priority,
      due: task.due,
    }));
  }

  /**
   * Get tasks by workspace ID
   */
  public async getByWorkspaceId(workspaceId: number): Promise<TaskCreateUpdateResponse[]> {
    const tasks = await this.taskService.getByWorkspaceId(workspaceId);
    return tasks.map(task => ({
      id: task.id,
      workspaceId: task.workspaceId,
      title: task.title,
      status: task.status,
      priority: task.priority,
      due: task.due,
    }));
  }
}