const API_BASE_URL = "http://localhost:8080/api/game";

export async function startGame() {
    const response = await fetch(`${API_BASE_URL}/start`, {
        method: "POST"
    });

    if (!response.ok) {
        throw new Error("Failed to start game");
    }

    return response.json();
}

export async function getGameState(sessionId) {
    const response = await fetch(`${API_BASE_URL}/${sessionId}`);

    if (!response.ok) {
        throw new Error("Failed to get game state");
    }

    return response.json();
}

export async function submitAnswer(sessionId, selectedAnswerIndex) {
    const response = await fetch(
        `${API_BASE_URL}/${sessionId}/answer`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                selectedAnswerIndex
            })
        }
    );

    if (!response.ok) {
        throw new Error("Failed to submit answer");
    }

    return response.json();
}

export async function getGameResult(sessionId) {
    const response = await fetch(
        `${API_BASE_URL}/${sessionId}/result`
    );

    if (!response.ok) {
        throw new Error("Failed to get game result");
    }

    return response.json();
}