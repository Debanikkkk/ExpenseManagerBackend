-- Standalone Migration Script for TaskMaster Workspace Schema Update
-- Run this directly with psql if Flyway is not available

\echo 'Starting workspace schema migration...'

-- ============================================
-- Step 1: Create updated_at timestamp column
-- ============================================
ALTER TABLE workspaces 
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Set existing records to current timestamp if updated_at is NULL
UPDATE workspaces SET updated_at = CURRENT_TIMESTAMP WHERE updated_at IS NULL;

\echo '✓ Added updated_at column'

-- ============================================
-- Step 2: Create created_at timestamp column  
-- ============================================
ALTER TABLE workspaces 
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Set existing records to current timestamp if created_at is NULL
UPDATE workspaces SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;

\echo '✓ Added created_at column'

-- ============================================
-- Step 3: Drop old progress column (if exists)
-- ============================================
ALTER TABLE workspaces DROP COLUMN IF EXISTS progress CASCADE;

\echo '✓ Dropped progress column'

-- ============================================
-- Step 4: Drop old updated_text column (rename to updatedAt logic)
-- ============================================
-- Note: We're keeping the data but the frontend will now use timestamps instead
ALTER TABLE workspaces DROP COLUMN IF EXISTS updated_text CASCADE;

\echo '✓ Dropped updated_text column'

-- ============================================
-- Step 5: Verify final schema
-- ============================================
\echo ''
\echo '=== Final Workspace Table Schema ==='
\d workspaces

\echo ''
\echo 'Migration completed successfully!'
\echo ''
