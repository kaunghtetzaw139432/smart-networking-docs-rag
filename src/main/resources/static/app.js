const form = document.getElementById("faq-form");
const messageInput = document.getElementById("message");
const answer = document.getElementById("answer");
const statusLabel = document.getElementById("status");
const chips = document.querySelectorAll(".chip");

async function askQuestion(question) {
    const trimmed = question.trim();

    if (!trimmed) {
        statusLabel.textContent = "Enter a question";
        answer.textContent = "The backend expects a non-empty message.";
        return;
    }

    statusLabel.textContent = "Loading...";
    answer.textContent = "Fetching answer...";

    try {
        const url = `/faq?message=${encodeURIComponent(trimmed)}`;
        const response = await fetch(url, {
            method: "GET",
            headers: {
                Accept: "text/plain"
            }
        });

        if (!response.ok) {
            throw new Error(`Request failed with status ${response.status}`);
        }

        const text = await response.text();
        statusLabel.textContent = "Answer received";
        answer.textContent = text || "The backend returned an empty response.";
    } catch (error) {
        statusLabel.textContent = "Request failed";
        answer.textContent = error.message;
    }
}

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    await askQuestion(messageInput.value);
});

chips.forEach((chip) => {
    chip.addEventListener("click", async () => {
        const question = chip.dataset.question || "";
        messageInput.value = question;
        await askQuestion(question);
    });
});
