// Agri Market Dashboard - JavaScript

// 1. Smooth navigation
document.querySelectorAll("nav a").forEach(function(link) {
    link.addEventListener("click", function() {
        document.querySelectorAll("nav a").forEach(function(item) {
            item.classList.remove("active");
        });
        this.classList.add("active");
    });
});

// 2. Crop lot form
const crop = document.getElementById("crop");
const quantity = document.getElementById("quantity");
const locationInput = document.getElementById("location");
const quality = document.getElementById("quality");

function createLot() {
    if (crop.value === "" || quantity.value === "" || location.value === "") {
        alert("Please fill Crop, Quantity and Location.");
        return;
    }

    alert(
        "Crop Lot Created Successfully!\n\n" +
        "Crop: " + crop.value + "\n" +
        "Quantity: " + quantity.value + " Quintal\n" +
        "Location: " + locationInput.value + "\n" +
        "Quality: " + quality.value
    );
}

// 3. Demo market data
const marketData = {
    Wheat: 2450,
    Rice: 3100,
    Maize: 2200,
    Potato: 1800
};

function showMarketPrice() {
    const selectedCrop = crop.value;

    if (marketData[selectedCrop]) {
        alert(
            selectedCrop +
            " current demo market price: ₹" +
            marketData[selectedCrop] +
            " / Quintal"
        );
    }
}

// 4. Recommendation
function showRecommendation() {
    if (crop.value === "") {
        alert("Please select a crop first.");
        return;
    }

    const price = marketData[crop.value] || 0;

    alert(
        "Recommendation for " + crop.value + ":\n\n" +
        "Expected demo price: ₹" + price + " / Quintal\n" +
        "Compare buyers before selling.\n" +
        "Check transport cost and net realization."
    );
}

// 5. Buyer selection
function selectBuyer(name, price) {
    alert(
        "Buyer Selected: " + name + "\n" +
        "Offer: ₹" + price + " / Quintal"
    );
}

// 6. Offer confirmation
function confirmOffer() {
    alert("Offer submitted successfully!\nDemo mode: No real transaction.");
}

// 7. Page load
window.addEventListener("load", function() {
    console.log("Agri Market Dashboard loaded successfully.");
});


// 8. Login modal (frontend demo)
function openLogin() {
    const modal = document.getElementById("loginModal");
    modal.style.display = "flex";
    modal.setAttribute("aria-hidden", "false");
    document.getElementById("loginUser").focus();
}

function closeLogin() {
    const modal = document.getElementById("loginModal");
    modal.style.display = "none";
    modal.setAttribute("aria-hidden", "true");
}

function loginUser() {
    const username = document.getElementById("loginUser").value.trim();
    const password = document.getElementById("loginPassword").value.trim();
    const message = document.getElementById("loginMessage");

    if (!username || !password) {
        message.textContent = "⚠️ Please enter username and password.";
        message.style.color = "#d9534f";
        return;
    }

    message.textContent = "✅ Login successful! Welcome " + username;
    message.style.color = "#087d4d";
}

document.getElementById("loginModal").addEventListener("click", function(event) {
    if (event.target === this) closeLogin();
});

document.addEventListener("keydown", function(event) {
    if (event.key === "Escape") closeLogin();
});

// 9. Smart Help / How to Use
function getHelp() {
    const input = document.getElementById("helpInput").value.toLowerCase().trim();
    const answer = document.getElementById("helpAnswer");

    if (!input) {
        answer.innerHTML = "💡 Please type a question. Example: <b>How can I create a crop lot?</b>";
        return;
    }

    if (input.includes("crop lot") || input.includes("crop") || input.includes("lot")) {
        answer.innerHTML = "🌱 <b>Create Crop Lot:</b><br>Go to the <b>Create Crop Lot</b> section → select your crop → enter quantity → enter location → select quality → click <b>Create Lot</b>.";
        return;
    }

    if (input.includes("price") || input.includes("market")) {
        answer.innerHTML = "📈 <b>Market Price:</b><br>Open the Market section and select your crop to view the available demo market price information.";
        return;
    }

    if (input.includes("buyer") || input.includes("sell")) {
        answer.innerHTML = "🏆 <b>Find Buyer:</b><br>Go to the Buyers section, compare buyer prices and ratings, then select a suitable buyer.";
        return;
    }

    if (input.includes("offer") || input.includes("expected price")) {
        answer.innerHTML = "🤝 <b>Make an Offer:</b><br>In the existing Offer section, enter your expected price and click <b>Send Offer</b>.";
        return;
    }

    if (input.includes("transport") || input.includes("logistics")) {
        answer.innerHTML = "🚚 <b>Transaction & Logistics:</b><br>Use the existing Transaction & Logistics section to follow buyer confirmation, transportation, crop pickup and payment stages.";
        return;
    }

    if (input.includes("login") || input.includes("sign in")) {
        answer.innerHTML = "🔐 <b>Login:</b><br>Click the Login button in the top bar and enter your username and password.";
        return;
    }

    answer.innerHTML = "🤖 <b>Try asking:</b><br>• How can I create a crop lot?<br>• How can I check market price?<br>• How can I find a buyer?<br>• How can I make an offer?<br>• How can I track logistics?";
}

document.getElementById("helpInput").addEventListener("keydown", function(event) {
    if (event.key === "Enter") getHelp();
});


/* =========================================================
   SIH 26132 - REAL CROP LOT + ML INTEGRATION
   ADD THIS BLOCK AT THE VERY END OF script.js
   ========================================================= */

(function () {

    const API_BASE_URL = "https://sih26132-backend.onrender.com";

    // -----------------------------------------------------
    // PUT YOUR REAL CROP UUIDs HERE
    // Get them from PostgreSQL:
    //
    // SELECT id, name FROM crops;
    // -----------------------------------------------------

    const cropIdMap = {

       Wheat: "4cc387c2-6704-4572-97ec-afe5d4ebdeb8",
    Rice: "69aa829d-486b-477d-b1c5-1124d24e6572",
    Potato: "6ab7ef83-209f-4b46-9ace-3b5da48a7821",
    Onion: "e3a9a3ca-a45e-4eda-8533-159123b8204a",
    Mango: "bd753521-4c8f-47d4-9ba1-0fb86cb73fd1",
    cucumber: "e11c2e10-9649-4948-8f8f-ff3ed35c8a2c"
    };


    // -----------------------------------------------------
    // Get JWT token
    // -----------------------------------------------------

    function getJWTToken() {

        const possibleKeys = [
            "token",
            "jwt",
            "accessToken",
            "access_token",
            "authToken",
            "jwtToken"
        ];

        for (const key of possibleKeys) {

            const value = localStorage.getItem(key);

            if (value && value.trim() !== "") {
                return value;
            }
        }

        return null;
    }


    // -----------------------------------------------------
    // Authorization headers
    // -----------------------------------------------------

    function getAuthHeaders() {

        const token = getJWTToken();

        const headers = {
            "Content-Type": "application/json"
        };

        if (token) {

            headers["Authorization"] =
                token.startsWith("Bearer ")
                    ? token
                    : "Bearer " + token;
        }

        return headers;
    }


    // -----------------------------------------------------
    // Create REAL Crop Lot
    // -----------------------------------------------------

    async function createRealCropLot(event) {

        if (event) {
            event.preventDefault();
        }

        const cropElement =
            document.getElementById("crop");

        const quantityElement =
            document.getElementById("quantity");

        const locationElement =
            document.getElementById("location");

        const qualityElement =
            document.getElementById("quality");


        if (
            !cropElement ||
            !quantityElement ||
            !locationElement
        ) {

            alert(
                "Crop lot form elements were not found."
            );

            return;
        }


        const cropName =
            cropElement.value.trim();

        const quantityValue =
            quantityElement.value.trim();

        const districtValue =
            locationElement.value.trim();

        const qualityValue =
            qualityElement
                ? qualityElement.value.trim()
                : "A";


        // -------------------------------------------------
        // Validation
        // -------------------------------------------------

        if (
            !cropName ||
            cropName === "Select Crop" ||
            !quantityValue ||
            !districtValue ||
            !qualityValue ||
            qualityValue === "Select Quality"
        ) {

            alert(
                "Please fill Crop, Quantity, Location and Quality."
            );

            return;
        }


        const quantityNumber =
            parseFloat(quantityValue);


        if (
            isNaN(quantityNumber) ||
            quantityNumber <= 0
        ) {

            alert(
                "Please enter a valid quantity."
            );

            return;
        }


        // -------------------------------------------------
        // Crop UUID
        // -------------------------------------------------

        const cropId =
            cropIdMap[cropName];


        if (
            !cropId ||
            cropId.includes("PUT-")
        ) {

            alert(
                "Crop UUID is not configured for " +
                cropName +
                ".\n\n" +
                "Please add the real UUID in cropIdMap."
            );

            console.error(
                "Missing Crop UUID:",
                cropName
            );

            return;
        }


        // -------------------------------------------------
        // Convert quality names to ML grades
        // -------------------------------------------------

        let qualityGrade = "A";

        if (qualityValue === "Premium") {
            qualityGrade = "A";
        }
        else if (qualityValue === "Good") {
            qualityGrade = "B";
        }
        else if (qualityValue === "Average") {
            qualityGrade = "C";
        }


        // -------------------------------------------------
        // Crop Lot request
        // -------------------------------------------------

        const cropLotData = {

            state: "Uttar Pradesh",

            district: districtValue,

            season: "Kharif",

            marketType: "Mandi",

            qualityGrade: qualityGrade,

            quantityQuintal:
                quantityNumber,

            productionTonnes:
                quantityNumber / 10,

            currentMarketPrice: 0,

            minPrice: 0,

            maxPrice: 0,

            demandIndex: 50,

            supplyIndex: 50,

            priceTrend: 0,

            arrivalVolumeTonnes:
                quantityNumber / 10,

            buyerDemandTonnes:
                quantityNumber / 10,

            storageCapacityUsedPct: 50,

            transportDistanceKm: 20,

            transportCost: 0,

            buyerOfferedPrice: 0,

            buyerRating: 4,

            paymentReliabilityPct: 90,

            temperatureC: 25,

            humidityPct: 60,

            rainfallMm: 0,

            fpoMember: "Yes",

            demandUrgency: "Medium",

            buyerVerified: "Yes"
        };


        console.log(
            "Sending Crop Lot:",
            cropLotData
        );


        // -------------------------------------------------
        // POST Crop Lot
        // -------------------------------------------------

        try {

            const response =
                await fetch(
                    API_BASE_URL +
                    "/api/crop-lots?cropId=" +
                    encodeURIComponent(cropId),
                    {
                        method: "POST",
                        headers: getAuthHeaders(),
                        body:
                            JSON.stringify(
                                cropLotData
                            )
                    }
                );


            if (!response.ok) {

                const errorText =
                    await response.text();

                console.error(
                    "Crop lot creation failed:",
                    response.status,
                    errorText
                );


                if (response.status === 401) {

                    alert(
                        "Authentication required.\n\n" +
                        "Please login first."
                    );

                }
                else if (response.status === 403) {

                    alert(
                        "Access denied.\n\n" +
                        "Your account does not have permission."
                    );

                }
                else {

                    alert(
                        "Crop lot creation failed.\n\n" +
                        "HTTP Status: " +
                        response.status +
                        "\n\n" +
                        errorText
                    );
                }

                return;
            }


            const createdLot =
                await response.json();


            console.log(
                "Crop Lot Created:",
                createdLot
            );


            const cropLotId =
                createdLot.id;


            if (!cropLotId) {

                alert(
                    "Crop lot was created, but the backend did not return its ID."
                );

                return;
            }


            // -------------------------------------------------
            // Success message
            // -------------------------------------------------

            alert(
                "Crop Lot Created Successfully!\n\n" +
                "Crop: " +
                cropName +
                "\n" +
                "Quantity: " +
                quantityNumber +
                " Quintal\n" +
                "District: " +
                districtValue +
                "\n\n" +
                "Running AI predictions..."
            );


            // -------------------------------------------------
            // Run 3 ML models
            // -------------------------------------------------

            await getCropLotMLPredictions(
                cropLotId
            );


        }
        catch (error) {

            console.error(
                "Crop Lot API Error:",
                error
            );

            alert(
                "Unable to connect to Spring Boot.\n\n" +
                "Make sure Spring Boot is running on port 8080."
            );
        }
    }


    // -----------------------------------------------------
    // Get all 3 ML predictions
    // -----------------------------------------------------

    async function getCropLotMLPredictions(
        cropLotId
    ) {

        showMLLoading();


        try {

            const response =
                await fetch(
                    API_BASE_URL +
                    "/api/crop-lots/" +
                    encodeURIComponent(
                        cropLotId
                    ) +
                    "/predictions",
                    {
                        method: "GET",
                        headers:
                            getAuthHeaders()
                    }
                );


            if (!response.ok) {

                const errorText =
                    await response.text();

                console.error(
                    "Prediction error:",
                    response.status,
                    errorText
                );

                showMLMessage(
                    "❌ ML prediction failed. HTTP " +
                    response.status
                );

                return;
            }


            const result =
                await response.json();


            console.log("======================================");
console.log("COMPLETE ML RESPONSE:", response);
// Display ML predictions on dashboard
const mlResults = document.getElementById("mlResults");

if (mlResults) {
    mlResults.style.display = "block";
}

const priceResult = document.getElementById("priceResult");
const saleWindowResult = document.getElementById("saleWindowResult");
const buyerMatchResult = document.getElementById("buyerMatchResult");

if (priceResult) {
    priceResult.textContent =
        JSON.stringify(response.pricePrediction);
}

if (saleWindowResult) {
    saleWindowResult.textContent =
        JSON.stringify(response.saleWindowPrediction);
}

if (buyerMatchResult) {
    buyerMatchResult.textContent =
        JSON.stringify(response.buyerMatchPrediction);
}

console.log("PRICE:", JSON.stringify(response.pricePrediction, null, 2));
console.log("SALE WINDOW:", JSON.stringify(response.saleWindowPrediction, null, 2));
console.log("BUYER MATCH:", JSON.stringify(response.buyerMatchPrediction, null, 2));
console.log("======================================");
                result
            ;


            // -------------------------------------------------
            // Display three predictions
            // -------------------------------------------------

            displayPricePrediction(
                result.pricePrediction
            );


            displaySaleWindowPrediction(
                result.saleWindowPrediction
            );


            displayBuyerMatchPrediction(
                result.buyerMatchPrediction
            );


            showMLMessage(
                "✅ AI predictions generated successfully."
            );


        }
        catch (error) {

            console.error(
                "Prediction API Error:",
                error
            );

            showMLMessage(
                "❌ Unable to connect to ML prediction service."
            );
        }
    }


    // -----------------------------------------------------
    // Loading message
    // -----------------------------------------------------

    function showMLLoading() {

        const resultBox =
            document.getElementById(
                "mlResults"
            );

        const message =
            document.getElementById(
                "predictionMessage"
            );


        if (resultBox) {
            resultBox.style.display = "block";
        }


        if (message) {

            message.innerText =
                "🔄 AI is analyzing your crop lot...";
        }
    }


    // -----------------------------------------------------
    // ML status message
    // -----------------------------------------------------

    function showMLMessage(text) {

        const message =
            document.getElementById(
                "predictionMessage"
            );


        if (message) {
            message.innerText = text;
        }
    }


    // -----------------------------------------------------
    // PRICE PREDICTION
    // -----------------------------------------------------

    function displayPricePrediction(
        prediction
    ) {

        const element =
            document.getElementById(
                "priceResult"
            );


        if (!element) {
            return;
        }


        if (
            prediction === null ||
            prediction === undefined
        ) {

            element.innerText =
                "Price prediction unavailable.";

            return;
        }


        let value = prediction;


        if (
            typeof prediction === "object"
        ) {

            value =
                prediction.prediction ??
                prediction.predictedPrice ??
                prediction.price ??
                prediction.value ??
                JSON.stringify(
                    prediction
                );
        }


        const numericValue =
            parseFloat(value);


        if (!isNaN(numericValue)) {

            element.innerText =
                "₹" +
                numericValue.toFixed(2) +
                " / Quintal";

        }
        else {

            element.innerText =
                String(value);
        }
    }


    // -----------------------------------------------------
    // BEST SALE WINDOW
    // -----------------------------------------------------

    function displaySaleWindowPrediction(
        prediction
    ) {

        const element =
            document.getElementById(
                "saleWindowResult"
            );


        if (!element) {
            return;
        }


        if (
            prediction === null ||
            prediction === undefined
        ) {

            element.innerText =
                "Sale window unavailable.";

            return;
        }


        let value = prediction;


        if (
            typeof prediction === "object"
        ) {

            value =
                prediction.prediction ??
                prediction.saleWindow ??
                prediction.bestSaleWindow ??
                prediction.window ??
                prediction.label ??
                JSON.stringify(
                    prediction
                );
        }


        element.innerText =
            String(value);
    }


    // -----------------------------------------------------
    // BUYER MATCH SCORE
    // -----------------------------------------------------

    function displayBuyerMatchPrediction(
        prediction
    ) {

        const element =
            document.getElementById(
                "buyerMatchResult"
            );


        if (!element) {
            return;
        }


        if (
            prediction === null ||
            prediction === undefined
        ) {

            element.innerText =
                "Buyer matching unavailable.";

            return;
        }


        let value = prediction;


        if (
            typeof prediction === "object"
        ) {

            value =
                prediction.prediction ??
                prediction.matchScore ??
                prediction.buyerMatchScore ??
                prediction.score ??
                prediction.value ??
                JSON.stringify(
                    prediction
                );
        }


        const numericValue =
            parseFloat(value);


        if (!isNaN(numericValue)) {

            element.innerText =
                "Match Score: " +
                numericValue.toFixed(2);

        }
        else {

            element.innerText =
                String(value);
        }
    }


    // -----------------------------------------------------
    // CONNECT EXISTING CROP LOT FORM
    // -----------------------------------------------------

    window.addEventListener(
        "load",
        function () {

            const forms =
                document.querySelectorAll(
                    "form"
                );


            let cropLotForm = null;


            // Find form containing #crop
            forms.forEach(
                function (form) {

                    if (
                        form.querySelector(
                            "#crop"
                        )
                    ) {

                        cropLotForm = form;
                    }
                }
            );


            if (!cropLotForm) {

                console.warn(
                    "Crop lot form not found."
                );

                return;
            }


            // -------------------------------------------------
            // Intercept existing form submit
            // -------------------------------------------------

            cropLotForm.addEventListener(
                "submit",
                function (event) {

                    event.preventDefault();

                    createRealCropLot(
                        event
                    );

                }
            );


            console.log(
                "✅ SIH 26132 Real Crop Lot + ML integration enabled."
            );

        }
    );

})();

/* ============================================================
   REAL BACKEND LOGIN INTEGRATION
   SIH 26132
   ADD THIS AT THE VERY END OF script.js
   ============================================================ */

(function () {

    const AUTH_API = "https://sih26132-backend.onrender.com";

    async function realBackendLogin() {

        const usernameElement = document.getElementById("loginUser");
        const passwordElement = document.getElementById("loginPassword");
        const messageElement = document.getElementById("loginMessage");

        if (!usernameElement || !passwordElement || !messageElement) {
            console.error("Login elements not found.");
            return;
        }

        const identifier = usernameElement.value.trim();
        const password = passwordElement.value.trim();

        if (!identifier || !password) {
            messageElement.textContent =
                "⚠️ Please enter email/phone and password.";
            messageElement.style.color = "#d9534f";
            return;
        }

        messageElement.textContent = "🔄 Logging in...";
        messageElement.style.color = "#555";

        try {

            const response = await fetch(
                AUTH_API + "/api/auth/login",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        identifier: identifier,
                        password: password
                    })
                }
            );

            const data = await response.json();

            if (!response.ok) {

                console.error("Login failed:", data);

                messageElement.textContent =
                    "❌ " +
                    (data.message || "Invalid email/phone or password");

                messageElement.style.color = "#d9534f";

                return;
            }

            /*
             * Backend AuthResponse contains:
             * token
             * tokenType
             * userId
             * fullName
             * role
             * preferredLanguage
             */

            if (!data.token) {

                console.error(
                    "Login response does not contain token:",
                    data
                );

                messageElement.textContent =
                    "❌ Login succeeded but JWT token was not received.";

                messageElement.style.color = "#d9534f";

                return;
            }

            // Save JWT for Crop Lot / ML API calls
            localStorage.setItem("token", data.token);

            // Save user information for frontend use
            if (data.userId) {
                localStorage.setItem(
                    "userId",
                    data.userId
                );
            }

            if (data.fullName) {
                localStorage.setItem(
                    "fullName",
                    data.fullName
                );
            }

            if (data.role) {
                localStorage.setItem(
                    "role",
                    data.role
                );
            }

            if (data.preferredLanguage) {
                localStorage.setItem(
                    "preferredLanguage",
                    data.preferredLanguage
                );
            }

            if (data.tokenType) {
                localStorage.setItem(
                    "tokenType",
                    data.tokenType
                );
            }

            console.log("✅ Backend login successful");
            console.log("✅ JWT token saved in localStorage");
            console.log("👤 User:", data.fullName);
            console.log("🔐 Role:", data.role);

            messageElement.textContent =
                "✅ Login successful! Welcome " +
                (data.fullName || identifier);

            messageElement.style.color = "#087d4d";

            /*
             * Close login modal after successful login.
             * Small delay allows user to see success message.
             */
            setTimeout(function () {

                if (typeof closeLogin === "function") {
                    closeLogin();
                }

            }, 700);

        } catch (error) {

            console.error("Backend login error:", error);

            messageElement.textContent =
                "❌ Cannot connect to backend. Make sure Spring Boot is running.";

            messageElement.style.color = "#d9534f";
        }
    }


    /*
     * Replace the old frontend-only login function
     * with the real backend login function.
     *
     * We do NOT modify the original function above.
     */
    window.loginUser = realBackendLogin;


    /*
     * Optional logout helper
     */
    window.logoutUser = function () {

        localStorage.removeItem("token");
        localStorage.removeItem("userId");
        localStorage.removeItem("fullName");
        localStorage.removeItem("role");
        localStorage.removeItem("preferredLanguage");
        localStorage.removeItem("tokenType");

        console.log("✅ Logged out");

        if (typeof openLogin === "function") {
            openLogin();
        }
    };


    /*
     * Check login state when page loads
     */
    window.addEventListener("load", function () {

        const token = localStorage.getItem("token");

        if (token) {
            console.log(
                "🔐 Existing JWT found. User is authenticated."
            );
        } else {
            console.log(
                "ℹ️ No JWT found. Please login before creating a crop lot."
            );
        }

    });

})();
// ============================================================
// REAL BACKEND LOGIN - JWT FIX
// ============================================================

async function realBackendLogin() {

    const usernameElement = document.getElementById("loginUser");
    const passwordElement = document.getElementById("loginPassword");
    const messageElement = document.getElementById("loginMessage");

    if (!usernameElement || !passwordElement || !messageElement) {
        console.error("Login elements not found.");
        return;
    }

    const identifier = usernameElement.value.trim();
    const password = passwordElement.value.trim();

    if (!identifier || !password) {
        messageElement.textContent = "⚠️ Please enter username and password.";
        messageElement.style.color = "#d9534f";
        return;
    }

    try {

        const response = await fetch(
            "https://sih26132-backend.onrender.com",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    identifier: identifier,
                    password: password
                })
            }
        );

        const data = await response.json();

        console.log("Backend Login Response:", data);

        if (!response.ok) {
            messageElement.textContent =
                "❌ Login failed: " +
                (data.message || "Invalid username or password.");

            messageElement.style.color = "#d9534f";
            return;
        }

        // IMPORTANT:
        // Backend returns accessToken, NOT token
        const jwtToken = data.accessToken;

        if (!jwtToken) {
            messageElement.textContent =
                "❌ Login successful but JWT token was not received.";

            messageElement.style.color = "#d9534f";

            console.error("No accessToken received:", data);
            return;
        }

        // Save JWT
        localStorage.setItem("token", jwtToken);

        // Save user information
        if (data.userId) {
            localStorage.setItem("userId", data.userId);
        }

        if (data.fullName) {
            localStorage.setItem("fullName", data.fullName);
        }

        if (data.role) {
            localStorage.setItem("role", data.role);
        }

        if (data.preferredLanguage) {
            localStorage.setItem(
                "preferredLanguage",
                data.preferredLanguage
            );
        }

        if (data.tokenType) {
            localStorage.setItem(
                "tokenType",
                data.tokenType
            );
        }

        messageElement.textContent =
            "✅ Login successful! Welcome " +
            (data.fullName || identifier);

        messageElement.style.color = "#087d4d";

        console.log("✅ JWT saved successfully.");

        // Close login modal
        setTimeout(function () {

            if (typeof closeLogin === "function") {
                closeLogin();
            }

        }, 700);

    } catch (error) {

        console.error("Login error:", error);

        messageElement.textContent =
            "❌ Cannot connect to backend.";

        messageElement.style.color = "#d9534f";
    }
}


// Replace the old frontend-only login function
window.loginUser = realBackendLogin;
// ============================================================
// SIH 26132 - DEBUG CROP LOT ID
// ADD ONLY - DO NOT REMOVE EXISTING CODE
// ============================================================

(function () {

    console.log("==============================================");
    console.log("SIH 26132 Crop Lot ID Debugger Loaded");
    console.log("==============================================");

    window.addEventListener("load", function () {

        const token = localStorage.getItem("token");

        console.log("JWT exists:", !!token);

        console.log("Crop ID Map:", {
            Wheat: "4cc387c2-6704-4572-97ec-afe5d4ebdeb8",
            Rice: "69aa829d-486b-477d-b1c5-1124d24e6572",
            Potato: "6ab7ef83-209f-4b46-9ace-3b5da48a7821",
            Onion: "e3a9a3ca-a45e-4eda-8533-159123b8204a",
            Mango: "bd753521-4c8f-47d4-9ba1-0fb86cb73fd1",
            cucumber: "e11c2e10-9649-4948-8f8f-ff3ed35c8a2c"
        });

    });

})();