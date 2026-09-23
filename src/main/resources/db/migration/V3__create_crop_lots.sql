-- =========================================================
-- V3: Create Crop Lots
-- SIH 26132 - Agri Market Decision
-- =========================================================

CREATE TABLE crop_lots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    farmer_id UUID NOT NULL,
    crop_id UUID NOT NULL,

    district VARCHAR(100) NOT NULL,
    season VARCHAR(50) NOT NULL,
    market_type VARCHAR(100),
    quality_grade VARCHAR(10),

    quantity_quintal NUMERIC(12,2) NOT NULL,
    production_tonnes NUMERIC(12,2),

    current_market_price NUMERIC(12,2),
    min_price NUMERIC(12,2),
    max_price NUMERIC(12,2),

    demand_index NUMERIC(8,2),
    supply_index NUMERIC(8,2),
    price_trend NUMERIC(8,2),

    arrival_volume_tonnes NUMERIC(12,2),
    buyer_demand_tonnes NUMERIC(12,2),

    storage_capacity_used_pct NUMERIC(8,2),

    transport_distance_km NUMERIC(10,2),
    transport_cost NUMERIC(12,2),

    buyer_offered_price NUMERIC(12,2),
    buyer_rating NUMERIC(4,2),
    payment_reliability_pct NUMERIC(8,2),

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_crop_lots_farmer
        FOREIGN KEY (farmer_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_crop_lots_crop
        FOREIGN KEY (crop_id)
        REFERENCES crops(id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_crop_lots_quantity
        CHECK (quantity_quintal > 0),

    CONSTRAINT chk_crop_lots_buyer_rating
        CHECK (
            buyer_rating IS NULL
            OR buyer_rating BETWEEN 1 AND 5
        ),

    CONSTRAINT chk_crop_lots_storage
        CHECK (
            storage_capacity_used_pct IS NULL
            OR storage_capacity_used_pct BETWEEN 0 AND 100
        ),

    CONSTRAINT chk_crop_lots_payment
        CHECK (
            payment_reliability_pct IS NULL
            OR payment_reliability_pct BETWEEN 0 AND 100
        )
);


-- =========================================================
-- Indexes
-- =========================================================

CREATE INDEX idx_crop_lots_farmer
    ON crop_lots(farmer_id);

CREATE INDEX idx_crop_lots_crop
    ON crop_lots(crop_id);

CREATE INDEX idx_crop_lots_district
    ON crop_lots(district);