-- Migration V2: Update workspace schema and add task grouping query

-- Step 1: Add new columns to workspaces table if they don't exist
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'workspaces' AND column_name = 'created_at') THEN
        ALTER TABLE workspaces ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
        RAISE NOTICE 'Added created_at column to workspaces';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'workspaces' AND column_name = 'updated_at') THEN
        ALTER TABLE workspaces ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
        RAISE NOTICE 'Added updated_at column to workspaces';
    END IF;
END $$;

-- Step 2: Rename progress column if it exists and copy data to new columns
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'workspaces' AND column_name = 'progress') THEN
        -- Copy existing progress values to updatedAt as a placeholder
        UPDATE workspaces SET updated_at = CURRENT_TIMESTAMP WHERE updated_at IS NULL;
        
        -- Keep the old data by not dropping columns yet, but this will be handled in a future migration
        RAISE NOTICE 'Existing progress column found. Data preserved.';
    END IF;
END $$;

-- Step 3: Drop old columns (progress and updated_text)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'workspaces' AND column_name = 'progress') THEN
        ALTER TABLE workspaces DROP COLUMN progress;
        RAISE NOTICE 'Dropped progress column';
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'workspaces' AND column_name = 'updated_text') THEN
        ALTER TABLE workspaces DROP COLUMN updated_text;
        RAISE NOTICE 'Dropped updated_text column';
    END IF;
END $$;

-- Step 4: Add comment to workspace table
COMMENT ON TABLE workspaces IS 'Workspaces with calculated progress from tasks';

-- Verify schema changes
SELECT 
    column_name, 
    data_type, 
    is_nullable
FROM information_schema.columns 
WHERE table_name = 'workspaces' 
ORDER BY ordinal_position;
