-- ============================================================
-- SIH 26132
-- Flyway V5 - Fix audit_logs schema
-- ============================================================

-- 1. Change IP address from INET to VARCHAR
ALTER TABLE audit_logs
    ALTER COLUMN ip_address TYPE VARCHAR(100)
    USING ip_address::TEXT;

-- 2. Change details from JSONB to TEXT
ALTER TABLE audit_logs
    ALTER COLUMN details TYPE TEXT
    USING details::TEXT;

-- 3. Change user_agent from TEXT to VARCHAR(1000)
ALTER TABLE audit_logs
    ALTER COLUMN user_agent TYPE VARCHAR(1000);

-- 4. Add user_id because AuditLog entity contains:
--    @JoinColumn(name = "user_id")
ALTER TABLE audit_logs
ADD COLUMN IF NOT EXISTS user_id UUID;

-- 5. Add foreign key
ALTER TABLE audit_logs
    ADD CONSTRAINT fk_audit_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE SET NULL;

-- 6. Add index
CREATE INDEX idx_audit_logs_user
    ON audit_logs(user_id);