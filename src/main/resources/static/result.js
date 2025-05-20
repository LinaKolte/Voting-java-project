document.addEventListener("DOMContentLoaded", async () => {
    try {
        const response = await fetch("http://localhost:8080/api/v1/votes/results");
        const results = await response.json();

        // Update existing vote-count elements by matching candidate name
        results.forEach((candidate) => {
            const allCandidates = document.querySelectorAll(".candidate-name");

            allCandidates.forEach((nameElement) => {
                if (nameElement.textContent.trim().toUpperCase() === candidate.candidateName.toUpperCase()) {
                    const card = nameElement.closest(".card-body");
                    const voteCount = card.querySelector(".vote-count");
                    voteCount.textContent = candidate.totalVotes;
                }
            });
        });

        // Update winner section
        const winner = results[0];
        document.getElementById("winner-name").textContent = winner.candidateName;
        document.getElementById("winner-votes").textContent = `with ${winner.totalVotes} votes`;

        const candidateImages = {
            "MASTER OTU ADDO": "feR.png",
            "MADAM AFUA KOBI": "icon-male-clipart-1-removebg-preview.png",
            "MASTER ISA AHMED": "icon-male-clipart-1-removebg-preview.png",
            "SADIQ FUSEINI": "icon-male-clipart-1-removebg-preview.png"
        };

        const winnerImg = document.getElementById("winner-img");
        if (winnerImg) {
            winnerImg.src = candidateImages[winner.candidateName] || "default.png";
        }

    } catch (error) {
        console.error("Error fetching vote results:", error);
    }
});
