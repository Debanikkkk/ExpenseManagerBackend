-- Migration V3: Ensure task statuses are properly set up for progress calculation
-- NOTE: progress is NOT stored on workspaces (dropped in V2) - it is calculated
-- on read via the dashboard API. This migration only normalizes data and adds
-- a supporting index; it does not write to a progress column.

-- Step 1: Check if there are any invalid status values and normalize them
DO $$
DECLARE
    invalid_statuses TEXT[];
BEGIN
    -- Get distinct status values from tasks table
    SELECT array_agg(DISTINCT status) INTO invalid_statuses
    FROM tasks 
    WHERE status NOT IN ('TODO', 'IN_PROGRESS', 'DONE');
    
    IF invalid_statuses IS NOT NULL THEN
        RAISE WARNING 'Found invalid task statuses: %', invalid_statuses;
        
        -- Normalize invalid statuses to TODO
        UPDATE tasks 
        SET status = 'TODO'
        WHERE status NOT IN ('TODO', 'IN_PROGRESS', 'DONE');
        
        RAISE NOTICE 'Normalized invalid statuses to TODO';
    END IF;
END $$;

-- Step 2: Add index on workspace_id for better performance (if not exists)
CREATE INDEX IF NOT EXISTS idx_tasks_workspace_id ON tasks(workspace_id);

-- Step 3: Touch updated_at for workspaces whose tasks were normalized in Step 1.
-- Progress itself is NOT persisted (calculated on read from the dashboard API),
-- so this loop only logs the computed value for verification and refreshes
-- updated_at as a record of the recalculation pass.
DO $$
DECLARE
    v_workspace_id INTEGER;
    v_total INTEGER;
    v_completed INTEGER;
    v_progress NUMERIC;
BEGIN
    FOR v_workspace_id IN
        SELECT id FROM workspaces WHERE id IS NOT NULL
    LOOP
        SELECT COUNT(*), COUNT(*) FILTER (WHERE status = 'DONE')
        INTO v_total, v_completed
        FROM tasks 
        WHERE workspace_id = v_workspace_id;
        
        IF v_total > 0 THEN
            v_progress := ROUND((v_completed::NUMERIC / v_total::NUMERIC) * 100, 2);
        ELSE
            v_progress := 0.0;
        END IF;
        
        RAISE NOTICE 'Workspace % computed progress: %', v_workspace_id, v_progress;
        
        UPDATE workspaces 
        SET updated_at = CURRENT_TIMESTAMP
        WHERE id = v_workspace_id;
    END LOOP;
END $$;

-- Step 4: Verify final schema
SELECT 
    'tasks' as table_name,
    COUNT(*) as total_tasks
FROM tasks

UNION ALL

SELECT 
    'workspaces' as table_name,
    COUNT(*) as total_workspaces
FROM workspaces;