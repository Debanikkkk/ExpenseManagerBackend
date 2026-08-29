package com.example.demo.service.workspace;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.workspace.WorkspaceCreateRequest;
import com.example.demo.entity.workspace.Workspace;
import com.example.demo.repository.workspace.WorkspaceRepository;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    public List<Workspace> getAll() {
        return workspaceRepository.findAll();
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
        workspace.setProgress(request.progress());
        workspace.setUpdatedText(request.updatedText());
        return workspaceRepository.save(workspace);
    }

    public Optional<Workspace> update(Long id, WorkspaceCreateRequest request) {
        return workspaceRepository.findById(id)
                .map(existing -> {
                    existing.setIcon(request.icon());
                    existing.setIconBg(request.iconBg());
                    existing.setTitle(request.title());
                    existing.setDescription(request.description());
                    existing.setProgress(request.progress());
                    existing.setUpdatedText(request.updatedText());
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
