from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import pandas as pd
import numpy as np
import joblib
import os


# ============================================================
# FASTAPI APPLICATION
# ============================================================

app = FastAPI(
    title="SIH26132 ML Service",
    description="AI/ML prediction service for SIH26132",
    version="1.0.0"
)


# ============================================================
# MODEL PATHS
# ============================================================

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
MODEL_DIR = os.path.join(BASE_DIR, "models")


PRICE_MODEL_PATH = os.path.join(
    MODEL_DIR,
    "2 expected_market_ke_price_model.pkl"
)

SALE_WINDOW_MODEL_PATH = os.path.join(
    MODEL_DIR,
    "2 best_sale_window_ke_model.pkl"
)

BUYER_MODEL_PATH = os.path.join(
    MODEL_DIR,
    "2 buyer_match_ke_model.pkl"
)


# ========================
====================================
# LOAD MODELS
# ============================================================

def load_model(path, model_name):
    try:
        if not os.path.exists(path):
            print(f"WARNING: {model_name} not found:")
            print(path)
            return None

        model = joblib.load(path)

        print(f"SUCCESS: {model_name} loaded")

        return model

    except Exception as e:
        print(f"ERROR loading {model_name}: {e}")
        return None


price_model = load_model(
    PRICE_MODEL_PATH,
    "Expected Market Price Model"
)

sale_window_model = load_model(
    SALE_WINDOW_MODEL_PATH,
    "Best Sale Window Model"
)

buyer_model = load_model(
    BUYER_MODEL_PATH,
    "Buyer Matching Model"
)


# ============================================================
# INPUT DATA MODEL
# ============================================================

class FarmerInput(BaseModel):

    # --------------------------------------------------------
    # CATEGORICAL FEATURES
    # --------------------------------------------------------

    State: str
    District: str
    Crop: str
    Season: str
    Market_Type: str
    Quality_Grade: str
    FPO_Member: str
    Demand_Urgency: str
    Buyer_Verified: str

    # --------------------------------------------------------
    # NUMERICAL FEATURES
    # --------------------------------------------------------

    Temperature_C: float
    Humidity_pct: float
    Rainfall_mm: float

    Production_tonnes: float
    Lot_Quantity_tonnes: float

    Current_Market_Price_Rs_per_quintal: float
    Min_Price_Rs_per_quintal: float
    Max_Price_Rs_per_quintal: float

    Demand_Index: float
    Supply_Index: float
    Price_Trend: float

    Arrival_Volume_tonnes: float
    Buyer_Demand_tonnes: float

    Storage_Capacity_Used_pct: float

    Transport_Distance_km: float
    Transport_Cost_Rs: float

    Buyer_Offered_Price_Rs_per_quintal: float
    Buyer_Rating_1_5: float
    Payment_Reliability_pct: float


# ============================================================
# FEATURE PREPARATION
# ============================================================

def prepare_input(data: FarmerInput):

    # Convert request into dictionary
    row = data.model_dump()

    # --------------------------------------------------------
    # ENGINEERED FEATURE 1
    # Demand Supply Ratio
    # --------------------------------------------------------

    row["Demand_Supply_Ratio"] = (
        row["Buyer_Demand_tonnes"]
        /
        (row["Arrival_Volume_tonnes"] + 1)
    )

    # --------------------------------------------------------
    # ENGINEERED FEATURE 2
    # Price vs Market Range %
    # --------------------------------------------------------

    row["Price_vs_Market_Range_pct"] = (
        (
            row["Current_Market_Price_Rs_per_quintal"]
            -
            row["Min_Price_Rs_per_quintal"]
        )
        /
        (
            row["Max_Price_Rs_per_quintal"]
            -
            row["Min_Price_Rs_per_quintal"]
            + 1
        )
    ) * 100

    # --------------------------------------------------------
    # ENGINEERED FEATURE 3
    # Storage Availability %
    # --------------------------------------------------------

    row["Storage_Availability_pct"] = (
        100 - row["Storage_Capacity_Used_pct"]
    )

    # --------------------------------------------------------
    # ENGINEERED FEATURE 4
    # Price Demand Interaction
    # --------------------------------------------------------

    row["Price_Demand_Interaction"] = (
        row["Current_Market_Price_Rs_per_quintal"]
        *
        row["Demand_Index"]
    )

    # --------------------------------------------------------
    # ENGINEERED FEATURE 5
    # Quality Demand Interaction
    # --------------------------------------------------------

    quality_mapping = {
        "A": 3,
        "B": 2,
        "C": 1
    }

    quality_value = quality_mapping.get(
        row["Quality_Grade"],
        1
    )

    row["Quality_Demand_Interaction"] = (
        row["Demand_Index"]
        *
        quality_value
    )

    # --------------------------------------------------------
    # DATAFRAME
    # --------------------------------------------------------

    df = pd.DataFrame([row])

    return df


# ============================================================
# ROOT ENDPOINT
# ============================================================

@app.get("/")
def root():

    return {
        "service": "SIH26132 ML Service",
        "status": "running",
        "models": {
            "expected_market_price": price_model is not None,
            "best_sale_window": sale_window_model is not None,
            "buyer_matching": buyer_model is not None
        }
    }


# ============================================================
# MODEL STATUS
# ============================================================

@app.get("/health")
def health():

    return {
        "status": "healthy",
        "price_model_loaded": price_model is not None,
        "sale_window_model_loaded": sale_window_model is not None,
        "buyer_model_loaded": buyer_model is not None
    }


# ============================================================
# 1. EXPECTED MARKET PRICE
# ============================================================

@app.post("/predict/price")
def predict_price(data: FarmerInput):

    if price_model is None:

        raise HTTPException(
            status_code=503,
            detail="Expected Market Price model is not loaded."
        )

    try:

        df = prepare_input(data)

        prediction = price_model.predict(df)

        predicted_price = float(prediction[0])

        return {
            "model": "Expected Market Price",
            "predicted_price_rs_per_quintal": round(
                predicted_price,
                2
            )
        }

    except Exception as e:

        raise HTTPException(
            status_code=500,
            detail=f"Price prediction error: {str(e)}"
        )


# ============================================================
# 2. BEST SALE WINDOW
# ============================================================

@app.post("/predict/sale-window")
def predict_sale_window(data: FarmerInput):

    if sale_window_model is None:

        raise HTTPException(
            status_code=503,
            detail="Best Sale Window model is not loaded."
        )

    try:

        df = prepare_input(data)

        prediction = sale_window_model.predict(df)

        sale_window = prediction[0]

        return {
            "model": "Best Sale Window",
            "predicted_sale_window": str(sale_window)
        }

    except Exception as e:

        raise HTTPException(
            status_code=500,
            detail=f"Sale window prediction error: {str(e)}"
        )


# ============================================================
# 3. BUYER MATCHING
# ============================================================

@app.post("/predict/buyer-match")
def predict_buyer_match(data: FarmerInput):

    if buyer_model is None:

        raise HTTPException(
            status_code=503,
            detail="Buyer Matching model is not loaded."
        )

    try:

        df = prepare_input(data)

        prediction = buyer_model.predict(df)

        match_score = float(prediction[0])

        # Keep score in 0-100 range
        match_score = max(
            0.0,
            min(
                100.0,
                match_score
            )
        )

        return {
            "model": "Buyer Matching",
            "predicted_match_score": round(
                match_score,
                2
            )
        }

    except Exception as e:

        raise HTTPException(
            status_code=500,
            detail=f"Buyer matching prediction error: {str(e)}"
        )