document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("passwordLoginForm");
    const sendOtpBtn = document.getElementById("sendOtpBtn");
    const loginBtn = document.getElementById("loginBtn");
    const otpInput = document.getElementById("passwordOtp");

    sendOtpBtn.addEventListener("click", function () {
        const aadharno = document.getElementById("aadharPassword").value;
        const password = document.getElementById("password").value;
        const voterId = document.getElementById("voterId").value;

        if (!aadharno || !password || !voterId) {
            alert("Please enter Aadhaar, Password, and Voter ID.");
            return;
        }

        // Trigger OTP request from backend
        fetch("http://localhost:8080/api/v1/student/login/request-otp", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ aadharno })
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                alert("OTP sent to your registered email.");
                otpInput.disabled = false;
                loginBtn.disabled = false;
            } else {
                alert(data.message || "Failed to send OTP.");
            }
        })
        .catch(() => {
            alert("Error sending OTP.");
        });
    });

    loginForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const aadharno = document.getElementById("aadharPassword").value;
        const password = document.getElementById("password").value;
        const otp = document.getElementById("passwordOtp").value;
        const voterId = document.getElementById("voterId").value;

        if (!otp || !aadharno || !password || !voterId) {
            alert("Please enter all required fields including OTP.");
            return;
        }

        const requestBody = { aadharno, password, otp, voterId };
        console.log("Sending login request:", requestBody); // Debug log

        // Send login request with Aadhaar, Voter ID, Password, and OTP
        fetch("http://localhost:8080/api/v1/student/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(requestBody)
        })
        .then(res => {
            if (!res.ok) {
                return res.json().then(err => { throw err; });
            }
            return res.json();
        })
        .then(data => {
            if (data.success) {
                alert("Login successful!");

                // Store user info in localStorage
                const { firstName, lastName, photo, voterId } = data;
                localStorage.setItem("aadharno", aadharno);
                localStorage.setItem("voterId", voterId);
                localStorage.setItem("name", `${firstName} ${lastName}`);
                localStorage.setItem("photoUrl", photo);

                // Redirect to face verification page
                window.location.href = "face.html";
            } else {
                alert(data.message || "Invalid credentials or OTP.");
            }
        })
        .catch(err => {
            console.error("Login error:", err);
            alert(err.message || "Login failed. Please check your inputs and try again.");
        });
    });
});
