-- ============================================================
-- SIH 26132 - Initial Database Schema
-- PostgreSQL 18
-- PostGIS
-- pgvector
-- Flyway V1
-- ============================================================

-- ------------------------------------------------------------
-- Extensions
-- ------------------------------------------------------------

CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS vector;


-- ============================================================
-- 1. ROLES
-- ============================================================

CREATE TABLE roles (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                       code VARCHAR(50) NOT NULL UNIQUE,
                       name VARCHAR(100) NOT NULL,

                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ============================================================
-- 2. USERS
-- ============================================================

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                       full_name VARCHAR(150) NOT NULL,
                       phone VARCHAR(20),
                       email VARCHAR(255),
                       password_hash VARCHAR(255) NOT NULL,

                       role_id UUID NOT NULL,

                       preferred_language VARCHAR(20) NOT NULL DEFAULT 'en',

                       is_active BOOLEAN NOT NULL DEFAULT TRUE,

                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_users_role
                           FOREIGN KEY (role_id)
                               REFERENCES roles(id)
                               ON DELETE RESTRICT,

                       CONSTRAINT uq_users_email
                           UNIQUE (email),

                       CONSTRAINT uq_users_phone
                           UNIQUE (phone)
);


-- ============================================================
-- 3. FARMS
-- ============================================================

CREATE TABLE farms (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                       user_id UUID NOT NULL,

                       farm_name VARCHAR(150) NOT NULL,
                       area_hectares NUMERIC(10,2),

                       soil_type VARCHAR(100),
                       irrigation_type VARCHAR(100),

                       description TEXT,

                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_farms_user
                           FOREIGN KEY (user_id)
                               REFERENCES users(id)
                               ON DELETE CASCADE,

                       CONSTRAINT chk_farm_area
                           CHECK (area_hectares IS NULL OR area_hectares > 0)
);


-- ============================================================
-- 4. FARM LOCATIONS
-- ============================================================

CREATE TABLE farm_locations (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                farm_id UUID NOT NULL UNIQUE,

                                latitude NUMERIC(9,6) NOT NULL,
                                longitude NUMERIC(9,6) NOT NULL,

                                geom geometry(Point, 4326),

                                address TEXT,
                                village VARCHAR(150),
                                block VARCHAR(150),
                                district VARCHAR(150),
                                state VARCHAR(150),
                                country VARCHAR(100) DEFAULT 'India',

                                created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_farm_locations_farm
                                    FOREIGN KEY (farm_id)
                                        REFERENCES farms(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT chk_latitude
                                    CHECK (latitude BETWEEN -90 AND 90),

                                CONSTRAINT chk_longitude
                                    CHECK (longitude BETWEEN -180 AND 180)
);


-- ============================================================
-- 5. CROPS
-- ============================================================

CREATE TABLE crops (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                       name VARCHAR(150) NOT NULL UNIQUE,
                       scientific_name VARCHAR(200),

                       description TEXT,

                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ============================================================
-- 6. CROP CYCLES
-- ============================================================

CREATE TABLE crop_cycles (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                             farm_id UUID NOT NULL,
                             crop_id UUID NOT NULL,

                             variety VARCHAR(150),
                             growth_stage VARCHAR(100),

                             sowing_date DATE,
                             transplant_date DATE,
                             expected_harvest_date DATE,

                             area_hectares NUMERIC(10,2),

                             status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

                             created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT fk_crop_cycles_farm
                                 FOREIGN KEY (farm_id)
                                     REFERENCES farms(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_crop_cycles_crop
                                 FOREIGN KEY (crop_id)
                                     REFERENCES crops(id)
                                     ON DELETE RESTRICT,

                             CONSTRAINT chk_crop_cycle_area
                                 CHECK (area_hectares IS NULL OR area_hectares > 0),

                             CONSTRAINT chk_crop_cycle_status
                                 CHECK (status IN ('PLANNED', 'ACTIVE', 'COMPLETED', 'CANCELLED'))
);


-- ============================================================
-- 7. CASES
-- ============================================================

CREATE TABLE cases (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                       case_number VARCHAR(50) NOT NULL UNIQUE,

                       crop_cycle_id UUID NOT NULL,

                       created_by_user_id UUID NOT NULL,
                       assigned_to_user_id UUID,

                       status VARCHAR(40) NOT NULL DEFAULT 'NEW',

                       description TEXT,

                       latitude NUMERIC(9,6),
                       longitude NUMERIC(9,6),

                       location geometry(Point, 4326),

                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_cases_crop_cycle
                           FOREIGN KEY (crop_cycle_id)
                               REFERENCES crop_cycles(id)
                               ON DELETE CASCADE,

                       CONSTRAINT fk_cases_created_by
                           FOREIGN KEY (created_by_user_id)
                               REFERENCES users(id)
                               ON DELETE RESTRICT,

                       CONSTRAINT fk_cases_assigned_to
                           FOREIGN KEY (assigned_to_user_id)
                               REFERENCES users(id)
                               ON DELETE SET NULL,

                       CONSTRAINT chk_case_status
                           CHECK (
                               status IN (
                                          'NEW',
                                          'AI_ANALYZED',
                                          'REVIEW_REQUIRED',
                                          'VERIFIED',
                                          'ADVISORY_SENT',
                                          'FOLLOW_UP',
                                          'CLOSED'
                                   )
                               )
);


-- ============================================================
-- 8. CASE IMAGES
-- ============================================================

CREATE TABLE case_images (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                             case_id UUID NOT NULL,

                             object_key VARCHAR(500) NOT NULL,
                             storage_url VARCHAR(1000),

                             original_filename VARCHAR(255),

                             content_type VARCHAR(100),
                             file_size_bytes BIGINT,

                             checksum VARCHAR(128),

                             captured_at TIMESTAMPTZ,

                             image_quality_score NUMERIC(5,4),
                             image_quality_status VARCHAR(30),

                             metadata JSONB,

                             created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT fk_case_images_case
                                 FOREIGN KEY (case_id)
                                     REFERENCES cases(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT chk_image_quality_score
                                 CHECK (
                                     image_quality_score IS NULL
                                         OR image_quality_score BETWEEN 0 AND 1
                                     )
);


-- ============================================================
-- 9. MODEL VERSIONS
-- ============================================================

CREATE TABLE model_versions (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                model_name VARCHAR(150) NOT NULL,
                                model_type VARCHAR(100) NOT NULL,
                                version VARCHAR(100) NOT NULL,

                                artifact_uri VARCHAR(1000),

                                framework VARCHAR(100),

                                metrics JSONB,

                                is_active BOOLEAN NOT NULL DEFAULT FALSE,

                                deployed_at TIMESTAMPTZ,

                                created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT uq_model_version
                                    UNIQUE (model_name, version)
);


-- ============================================================
-- 10. DIAGNOSES
-- ============================================================

CREATE TABLE diagnoses (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                           case_id UUID NOT NULL,

                           model_version_id UUID,

                           condition_name VARCHAR(200) NOT NULL,

                           condition_type VARCHAR(50),

                           confidence NUMERIC(6,5) NOT NULL,

                           alternatives JSONB,
                           explainability JSONB,

                           source VARCHAR(50) NOT NULL DEFAULT 'AI',

                           is_final BOOLEAN NOT NULL DEFAULT FALSE,

                           created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_diagnoses_case
                               FOREIGN KEY (case_id)
                                   REFERENCES cases(id)
                                   ON DELETE CASCADE,

                           CONSTRAINT fk_diagnoses_model
                               FOREIGN KEY (model_version_id)
                                   REFERENCES model_versions(id)
                                   ON DELETE SET NULL,

                           CONSTRAINT chk_diagnosis_confidence
                               CHECK (confidence BETWEEN 0 AND 1)
);


-- ============================================================
-- 11. RISK PREDICTIONS
-- ============================================================

CREATE TABLE risk_predictions (
                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                  case_id UUID NOT NULL,

                                  model_version_id UUID,

                                  risk_score NUMERIC(6,5) NOT NULL,

                                  severity VARCHAR(30),

                                  forecast_start TIMESTAMPTZ,
                                  forecast_end TIMESTAMPTZ,

                                  contributing_factors JSONB,

                                  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                  CONSTRAINT fk_risk_case
                                      FOREIGN KEY (case_id)
                                          REFERENCES cases(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_risk_model
                                      FOREIGN KEY (model_version_id)
                                          REFERENCES model_versions(id)
                                          ON DELETE SET NULL,

                                  CONSTRAINT chk_risk_score
                                      CHECK (risk_score BETWEEN 0 AND 1)
);


-- ============================================================
-- 12. WEATHER OBSERVATIONS
-- ============================================================

CREATE TABLE weather_observations (
                                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                      location geometry(Point, 4326) NOT NULL,

                                      observed_at TIMESTAMPTZ NOT NULL,

                                      temperature_c NUMERIC(6,2),
                                      humidity_percent NUMERIC(6,2),
                                      rainfall_mm NUMERIC(8,2),
                                      wind_speed_kmh NUMERIC(8,2),

                                      leaf_wetness NUMERIC(8,2),

                                      source VARCHAR(100),

                                      raw_data JSONB,

                                      created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                      CONSTRAINT chk_weather_humidity
                                          CHECK (
                                              humidity_percent IS NULL
                                                  OR humidity_percent BETWEEN 0 AND 100
                                              ),

                                      CONSTRAINT chk_weather_rainfall
                                          CHECK (
                                              rainfall_mm IS NULL
                                                  OR rainfall_mm >= 0
                                              )
);


-- ============================================================
-- 13. SENSOR OBSERVATIONS
-- ============================================================

CREATE TABLE sensor_observations (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                     farm_id UUID NOT NULL,

                                     observed_at TIMESTAMPTZ NOT NULL,

                                     sensor_type VARCHAR(100) NOT NULL,

                                     metric VARCHAR(100) NOT NULL,

                                     value NUMERIC(15,5) NOT NULL,

                                     unit VARCHAR(50),

                                     raw_data JSONB,

                                     created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                     CONSTRAINT fk_sensor_farm
                                         FOREIGN KEY (farm_id)
                                             REFERENCES farms(id)
                                             ON DELETE CASCADE
);


-- ============================================================
-- 14. ADVISORIES
-- ============================================================

CREATE TABLE advisories (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                            case_id UUID NOT NULL,

                            language VARCHAR(20) NOT NULL DEFAULT 'en',

                            title VARCHAR(300) NOT NULL,

                            content TEXT NOT NULL,

                            immediate_actions TEXT,
                            preventive_actions TEXT,

                            ipdm_actions TEXT,

                            safe_use_instructions TEXT,

                            when_to_contact_expert TEXT,
                            when_to_recheck TEXT,

                            knowledge_sources JSONB,

                            generated_by VARCHAR(50),

                            status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',

                            created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_advisory_case
                                FOREIGN KEY (case_id)
                                    REFERENCES cases(id)
                                    ON DELETE CASCADE,

                            CONSTRAINT chk_advisory_status
                                CHECK (
                                    status IN ('DRAFT', 'APPROVED', 'SENT', 'ARCHIVED')
                                    )
);


-- ============================================================
-- 15. KNOWLEDGE DOCUMENTS
-- ============================================================

CREATE TABLE knowledge_documents (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                     title VARCHAR(500) NOT NULL,

                                     source VARCHAR(500),
                                     source_url VARCHAR(1000),

                                     publisher VARCHAR(300),

                                     document_type VARCHAR(100),

                                     language VARCHAR(20),

                                     version VARCHAR(100),

                                     checksum VARCHAR(128),

                                     is_active BOOLEAN NOT NULL DEFAULT TRUE,

                                     metadata JSONB,

                                     created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ============================================================
-- 16. KNOWLEDGE CHUNKS
-- ============================================================

CREATE TABLE knowledge_chunks (
                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                  document_id UUID NOT NULL,

                                  chunk_index INTEGER NOT NULL,

                                  content TEXT NOT NULL,

                                  embedding vector,

                                  metadata JSONB,

                                  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                  CONSTRAINT fk_chunks_document
                                      FOREIGN KEY (document_id)
                                          REFERENCES knowledge_documents(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT uq_document_chunk
                                      UNIQUE (document_id, chunk_index)
);


-- ============================================================
-- 17. EXPERT REVIEWS
-- ============================================================

CREATE TABLE expert_reviews (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                case_id UUID NOT NULL,

                                reviewer_id UUID NOT NULL,

                                diagnosis VARCHAR(300),

                                confidence NUMERIC(6,5),

                                reason TEXT,

                                review_status VARCHAR(40) NOT NULL,

                                reviewed_at TIMESTAMPTZ,

                                created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_expert_review_case
                                    FOREIGN KEY (case_id)
                                        REFERENCES cases(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT fk_expert_review_user
                                    FOREIGN KEY (reviewer_id)
                                        REFERENCES users(id)
                                        ON DELETE RESTRICT,

                                CONSTRAINT chk_expert_confidence
                                    CHECK (
                                        confidence IS NULL
                                            OR confidence BETWEEN 0 AND 1
                                        ),

                                CONSTRAINT chk_review_status
                                    CHECK (
                                        review_status IN (
                                                          'PENDING',
                                                          'APPROVED',
                                                          'REJECTED',
                                                          'NEEDS_MORE_INFORMATION'
                                            )
                                        )
);


-- ============================================================
-- 18. FOLLOWUPS
-- ============================================================

CREATE TABLE followups (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                           case_id UUID NOT NULL,

                           scheduled_at TIMESTAMPTZ NOT NULL,

                           completed_at TIMESTAMPTZ,

                           status VARCHAR(30) NOT NULL DEFAULT 'SCHEDULED',

                           notes TEXT,

                           outcome TEXT,

                           next_action TEXT,

                           created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_followup_case
                               FOREIGN KEY (case_id)
                                   REFERENCES cases(id)
                                   ON DELETE CASCADE,

                           CONSTRAINT chk_followup_status
                               CHECK (
                                   status IN (
                                              'SCHEDULED',
                                              'COMPLETED',
                                              'MISSED',
                                              'CANCELLED'
                                       )
                                   )
);


-- ============================================================
-- 19. NOTIFICATIONS
-- ============================================================

CREATE TABLE notifications (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                               user_id UUID NOT NULL,

                               related_case_id UUID,

                               type VARCHAR(100) NOT NULL,

                               title VARCHAR(300) NOT NULL,

                               body TEXT NOT NULL,

                               channel VARCHAR(30) NOT NULL,

                               status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

                               scheduled_at TIMESTAMPTZ,
                               sent_at TIMESTAMPTZ,
                               read_at TIMESTAMPTZ,

                               created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_notification_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_notification_case
                                   FOREIGN KEY (related_case_id)
                                       REFERENCES cases(id)
                                       ON DELETE SET NULL,

                               CONSTRAINT chk_notification_channel
                                   CHECK (
                                       channel IN (
                                                   'IN_APP',
                                                   'SMS',
                                                   'EMAIL',
                                                   'PUSH'
                                           )
                                       ),

                               CONSTRAINT chk_notification_status
                                   CHECK (
                                       status IN (
                                                  'PENDING',
                                                  'SENT',
                                                  'FAILED',
                                                  'READ'
                                           )
                                       )
);


-- ============================================================
-- 20. HOTSPOTS
-- ============================================================

CREATE TABLE hotspots (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                          location geometry(Point, 4326) NOT NULL,

                          detected_at TIMESTAMPTZ NOT NULL,

                          condition_name VARCHAR(200) NOT NULL,

                          hotspot_score NUMERIC(6,5),

                          severity VARCHAR(30),

                          case_count INTEGER NOT NULL DEFAULT 0,

                          state VARCHAR(150),
                          district VARCHAR(150),
                          block VARCHAR(150),

                          status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

                          metadata JSONB,

                          created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT chk_hotspot_score
                              CHECK (
                                  hotspot_score IS NULL
                                      OR hotspot_score BETWEEN 0 AND 1
                                  ),

                          CONSTRAINT chk_hotspot_case_count
                              CHECK (case_count >= 0)
);


-- ============================================================
-- 21. FEEDBACK
-- ============================================================

CREATE TABLE feedback (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                          case_id UUID,

                          user_id UUID NOT NULL,

                          feedback_type VARCHAR(100) NOT NULL,

                          rating INTEGER,

                          comment TEXT,

                          verified_label VARCHAR(300),

                          actual_condition VARCHAR(300),

                          model_was_correct BOOLEAN,

                          created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_feedback_case
                              FOREIGN KEY (case_id)
                                  REFERENCES cases(id)
                                  ON DELETE SET NULL,

                          CONSTRAINT fk_feedback_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT chk_feedback_rating
                              CHECK (
                                  rating IS NULL
                                      OR rating BETWEEN 1 AND 5
                                  )
);


-- ============================================================
-- 22. AUDIT LOGS
-- ============================================================

CREATE TABLE audit_logs (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                            actor_user_id UUID,

                            action VARCHAR(100) NOT NULL,

                            entity_type VARCHAR(100),
                            entity_id UUID,

                            details JSONB,

                            ip_address INET,
                            user_agent TEXT,

                            created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_audit_actor
                                FOREIGN KEY (actor_user_id)
                                    REFERENCES users(id)
                                    ON DELETE SET NULL
);


-- ============================================================
-- INDEXES
-- ============================================================

CREATE INDEX idx_users_role
    ON users(role_id);

CREATE INDEX idx_farms_user
    ON farms(user_id);

CREATE INDEX idx_crop_cycles_farm
    ON crop_cycles(farm_id);

CREATE INDEX idx_crop_cycles_crop
    ON crop_cycles(crop_id);

CREATE INDEX idx_cases_crop_cycle
    ON cases(crop_cycle_id);

CREATE INDEX idx_cases_created_by
    ON cases(created_by_user_id);

CREATE INDEX idx_cases_assigned_to
    ON cases(assigned_to_user_id);

CREATE INDEX idx_cases_status
    ON cases(status);

CREATE INDEX idx_case_images_case
    ON case_images(case_id);

CREATE INDEX idx_diagnoses_case
    ON diagnoses(case_id);

CREATE INDEX idx_risk_predictions_case
    ON risk_predictions(case_id);

CREATE INDEX idx_weather_observations_time
    ON weather_observations(observed_at);

CREATE INDEX idx_sensor_observations_farm_time
    ON sensor_observations(farm_id, observed_at);

CREATE INDEX idx_advisories_case
    ON advisories(case_id);

CREATE INDEX idx_knowledge_chunks_document
    ON knowledge_chunks(document_id);

CREATE INDEX idx_expert_reviews_case
    ON expert_reviews(case_id);

CREATE INDEX idx_expert_reviews_reviewer
    ON expert_reviews(reviewer_id);

CREATE INDEX idx_followups_case
    ON followups(case_id);

CREATE INDEX idx_followups_scheduled
    ON followups(scheduled_at);

CREATE INDEX idx_notifications_user
    ON notifications(user_id);

CREATE INDEX idx_notifications_status
    ON notifications(status);

CREATE INDEX idx_hotspots_detected
    ON hotspots(detected_at);

CREATE INDEX idx_feedback_case
    ON feedback(case_id);

CREATE INDEX idx_audit_logs_actor
    ON audit_logs(actor_user_id);

CREATE INDEX idx_audit_logs_created
    ON audit_logs(created_at);


-- ============================================================
-- SPATIAL INDEXES
-- ============================================================

CREATE INDEX idx_farm_locations_geom
    ON farm_locations
    USING GIST (geom);

CREATE INDEX idx_cases_location
    ON cases
    USING GIST (location);

CREATE INDEX idx_weather_location
    ON weather_observations
    USING GIST (location);

CREATE INDEX idx_hotspots_location
    ON hotspots
    USING GIST (location);


-- ============================================================
-- INITIAL ROLES
-- ============================================================

INSERT INTO roles (code, name)
VALUES
    ('FARMER', 'Farmer'),
    ('EXTENSION_OFFICER', 'Extension Officer'),
    ('AGRICULTURE_OFFICIAL', 'Agriculture Official'),
    ('EXPERT', 'Agriculture Expert'),
    ('ADMIN', 'Administrator')
    ON CONFLICT (code) DO NOTHING;