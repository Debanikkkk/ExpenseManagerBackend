package com.example.demo.repository.workspace;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.workspace.Workspace;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
}
