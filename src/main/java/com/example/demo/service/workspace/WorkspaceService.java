package com.example.demo.service.workspace;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.workspace.WorkspaceCreateRequest;
import com.example.demo.dto.workspace.WorkspaceDashboardResponse;
import com.example.demo.entity.task.TaskStatus;
import com.example.demo.entity.workspace.Workspace;
import com.example.demo.repository.task.TaskRepository;
import com.example.demo.repository.workspace.WorkspaceRepository;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final TaskRepository taskRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository, TaskRepository taskRepository) {
        this.workspaceRepository = workspaceRepository;
        this.taskRepository = taskRepository;
    }

    public List<Workspace> getAll() {
        return workspaceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<WorkspaceDashboardResponse> getDashboardData() {
        List<Workspace> workspaces = workspaceRepository.findAll();
        
        // Group tasks by workspaceId and status from native query
        // Returns: [workspace_id, status, count]
        List<Object[]> taskProgressRaw = taskRepository.groupTasksByWorkspaceAndStatus();
        
        // Convert to Map<Long, Map<Enum<TaskStatus>, Long>>
        Map<Long, Map<TaskStatus, Long>> taskProgressMap = new HashMap<>();
        
        for (Object[] row : taskProgressRaw) {
            Long workspaceId = (Long) row[0];
            Object statusObj = row[1];
            Long count = (Long) row[2];
            
            // Convert status enum from database
            TaskStatus status = TaskStatus.valueOf(statusObj.toString());
            
            taskProgressMap.putIfAbsent(workspaceId, new HashMap<>());
            taskProgressMap.get(workspaceId).put(status, count);
        }
        
        return workspaces.stream().map(workspace -> {
            Map<TaskStatus, Long> taskCounts = taskProgressMap.getOrDefault(workspace.getId(), new HashMap<>());
            
            long totalTasks = taskCounts.values().stream().mapToLong(Long::longValue).sum();
            long completedTasks = taskCounts.getOrDefault(TaskStatus.DONE, 0L);
            
            int progress = (int) ((totalTasks > 0 ? (double) completedTasks / totalTasks * 100 : 0));
            
            return new WorkspaceDashboardResponse(
                workspace.getId(),
                workspace.getIcon(),
                workspace.getIconBg(),
                workspace.getTitle(),
                workspace.getDescription(),
                progress,
                // workspace.getUpdatedText(),
                workspace.getCreatedAt(),
                workspace.getUpdatedAt()
            );
        }).collect(Collectors.toList());
    }

    public Optional<Workspace> getById(Long id) {
        return workspaceRepository.findById(id);
    }

    public Workspace create(WorkspaceCreateRequest request) {
        Workspace workspace = new Workspace();
        workspace.setIcon(request.icon());
        workspace.setIconBg(request.iconBg());
        workspace.setTitle(request.title());
        workspace.setDescription(request.description());
        workspace.setCreatedAt(LocalDateTime.now());
        workspace.setUpdatedAt(LocalDateTime.now());
        return workspaceRepository.save(workspace);
    }

    public Optional<Workspace> update(Long id, WorkspaceCreateRequest request) {
        return workspaceRepository.findById(id)
                .map(existing -> {
                    existing.setIcon(request.icon());
                    existing.setIconBg(request.iconBg());
                    existing.setTitle(request.title());
                    existing.setDescription(request.description());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return workspaceRepository.save(existing);
                });
    }

    public boolean delete(Long id) {
        if (!workspaceRepository.existsById(id)) {
            return false;
        }
        workspaceRepository.deleteById(id);
        return true;
    }
}
