
// Age Calculation
function calculateAge(dobString) {
    const dob = new Date(dobString);
    const today = new Date();
    let age = today.getFullYear() - dob.getFullYear();
    const monthDiff = today.getMonth() - dob.getMonth();
    const dayDiff = today.getDate() - dob.getDate();

    if (monthDiff < 0 || (monthDiff === 0 && dayDiff < 0)) {
        age--;
    }
    return age;
}

// Update age display dynamically
function updateAgeDisplay() {
    const dobInput = document.getElementById("dob");
    const ageDisplay = document.getElementById("ageDisplay");

    if (!dobInput.value) {
        ageDisplay.textContent = "";
        return;
    }

    const age = calculateAge(dobInput.value);
    if (age >= 18) {
        ageDisplay.innerHTML = `<strong>Age:</strong> ${age} — ✅ You are eligible to register.`;
        ageDisplay.style.color = "green";
    } else {
        ageDisplay.innerHTML = `<strong>Age:</strong> ${age} — ❌ You are not eligible to register.`;
        ageDisplay.style.color = "red";
    }
}

// Validate password format
function validatePassword(password) {
    const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return passwordPattern.test(password);
}

// Send OTP
function sendOtp() {
    const email = document.getElementById("email").value;
    if (!email) {
        alert("Please enter your email first.");
        return;
    }

    fetch(`http://localhost:8080/api/otp/send?email=${email}`, {
        method: "POST"
    })
    .then(res => res.text())
    .then(msg => {
        document.getElementById("otpStatus").innerText = "✅ OTP sent successfully!";
        document.getElementById("otpStatus").style.color = "green";
    })
    .catch(err => {
        console.error("OTP send error:", err);
        document.getElementById("otpStatus").innerText = "❌ Failed to send OTP.";
        document.getElementById("otpStatus").style.color = "red";
    });
}

// Register form submission
document.getElementById("dob").addEventListener("change", updateAgeDisplay);

document.getElementById("registerform").addEventListener("submit", function (event) {
    event.preventDefault();

    const rawAadhar = document.getElementById("aadharno").value.trim();
    const aadharno = rawAadhar.replace(/\s+/g, "");

    if (!/^\d{12}$/.test(aadharno)) {
        alert("Aadhar number must be exactly 12 numeric digits.");
        return;
    }

    const dob = document.getElementById("dob").value;
    if (!dob) {
        alert("Please enter your Date of Birth.");
        return;
    }

    const age = calculateAge(dob);
    if (age < 18) {
        alert("You must be at least 18 years old to register.");
        return;
    }

    const password = document.getElementById("password").value;
    if (!validatePassword(password)) {
        alert("Password must be at least 8 characters long and include an uppercase letter, a lowercase letter, a digit, and a special character.");
        return;
    }

    const email = document.getElementById("email").value;
    const otp = document.getElementById("otp").value;
    if (!otp) {
        alert("Please enter the OTP sent to your email.");
        return;
    }

    const studentData = {
        aadharno: aadharno,
        dob: dob,
        email: email,
        name: document.getElementById("name").value,
        password: password
    };

    // First verify OTP
    fetch(`http://localhost:8080/api/otp/verify?email=${email}&otp=${otp}`, {
        method: "POST"
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Invalid OTP. Please check and try again.");
        }

        // OTP verified — proceed with registration
        return fetch("http://localhost:8080/api/v1/student", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(studentData)
        });
    })
    .then(res => {
        if (!res.ok) {
            throw new Error("Failed to register student.");
        }
        return res.text();
    })
    .then(data => {
        alert("registered successfully! please fill the form");
        console.log(data);
        document.getElementById("registerform").reset();
        document.getElementById("ageDisplay").innerText = "";
        document.getElementById("otpStatus").innerText = "";
        window.location.href = "regi.html";
    })
    .catch(error => {
        console.error("Error:", error);
        alert(error.message || "Registration failed.");
    });
});
