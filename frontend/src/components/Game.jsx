import { useState } from "react";
import { startGame, getGameState } from "../api/gameApi";

function Game() {
    const [sessionId, setSessionId] = useState(null);
    const [gameState, setGameState] = useState(null);
    const [error, setError] = useState(null);

    async function handleStartGame() {
        try {
            setError(null);

            const startResponse = await startGame();

            setSessionId(startResponse.sessionId);

            const state = await getGameState(startResponse.sessionId);

            setGameState(state);
        } catch (error) {
            setError(error.message);
        }
    }

    return (
        <div>
            <h1>Football Quiz</h1>

            {!sessionId && (
                <button onClick={handleStartGame}>
                    Start Game
                </button>
            )}

            {error && (
                <p>{error}</p>
            )}

            {gameState && (
                <div>
                    <p>
                        Score: {gameState.score}
                    </p>

                    <p>
                        Time:
                        {" "}
                        {gameState.remainingSeconds}
                    </p>

                    <h2>
                        {gameState.question.text}
                    </h2>

                    {gameState.question.options.map(
                        (option, index) => (
                            <button key={index}>
                                {option}
                            </button>
                        )
                    )}
                </div>
            )}
        </div>
    );
}

export default Game;