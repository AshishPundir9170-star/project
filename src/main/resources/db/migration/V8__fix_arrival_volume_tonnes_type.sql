ALTER TABLE crop_lots
ALTER COLUMN arrival_volume_tonnes
TYPE NUMERIC(12,2)
USING arrival_volume_tonnes::NUMERIC(12,2);