ALTER TABLE public.crop_lots
    ADD COLUMN state VARCHAR(100) NOT NULL DEFAULT 'Uttar Pradesh',

    ADD COLUMN temperature_c DOUBLE PRECISION DEFAULT 25.0,

    ADD COLUMN humidity_pct DOUBLE PRECISION DEFAULT 60.0,

    ADD COLUMN rainfall_mm DOUBLE PRECISION DEFAULT 0.0,

    ADD COLUMN fpo_member VARCHAR(20) DEFAULT 'Yes',

    ADD COLUMN demand_urgency VARCHAR(30) DEFAULT 'Medium',

    ADD COLUMN buyer_verified VARCHAR(20) DEFAULT 'Yes';


ALTER TABLE public.crop_lots
    ADD CONSTRAINT chk_crop_lots_temperature
        CHECK (temperature_c IS NULL OR temperature_c BETWEEN -50 AND 70),

    ADD CONSTRAINT chk_crop_lots_humidity
        CHECK (humidity_pct IS NULL OR humidity_pct BETWEEN 0 AND 100),

    ADD CONSTRAINT chk_crop_lots_rainfall
        CHECK (rainfall_mm IS NULL OR rainfall_mm >= 0);