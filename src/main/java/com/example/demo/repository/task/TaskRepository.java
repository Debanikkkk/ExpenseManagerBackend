package com.example.demo.repository.task;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.entity.task.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
    @Query(value = "SELECT t.workspace_id, t.status, COUNT(t) FROM taskS t GROUP BY t.workspace_id, t.status", nativeQuery = true)
    List<Object[]> groupTasksByWorkspaceAndStatus();
}
