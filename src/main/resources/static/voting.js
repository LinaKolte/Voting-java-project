document.addEventListener("DOMContentLoaded", function () {
    const storedAadharNo = localStorage.getItem("aadharno");

    document.querySelectorAll(".vote-btn button").forEach(button => {
        button.addEventListener("click", async function () {
            console.log("Button Clicked!"); // Debugging line

            if (!storedAadharNo) {
                alert("Error: Aadhaar number not found. Please log in again.");
                return;
            }

            const candidateVoted = this.closest(".card").querySelector(".candidate-name").textContent.trim();

            try {
                const response = await fetch("http://localhost:8080/api/v1/student/cast", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ aadharno: storedAadharNo, candidateVoted })
                });

                if (!response.ok) {
                    throw new Error("Failed to cast vote.");
                }

                const result = await response.text();
                alert(result || "Vote cast successfully! ✅");

                // 🏆 Redirect to results page after voting
                setTimeout(() => {
                    window.location.href = "result1.html";
                }, 2000); // 2-second delay before redirect

            } catch (error) {
                alert("Error casting vote. Please try again."); // Error Popup
            }
        });
    });
});