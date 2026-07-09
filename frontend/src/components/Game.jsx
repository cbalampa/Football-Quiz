import { useState } from "react";
import {
    startGame,
    getGameState,
    submitAnswer
} from "../api/gameApi";

function Game() {
    const [sessionId, setSessionId] = useState(null);
    const [gameState, setGameState] = useState(null);
    const [gameFinished, setGameFinished] = useState(false);
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

    async function handleAnswer(selectedAnswerIndex) {
        try {
            setError(null);

            const response = await submitAnswer(
                sessionId,
                selectedAnswerIndex
            );

            if (response.status === "FINISHED") {
                setGameFinished(true);
                setGameState({
                    ...gameState,
                    score: response.score,
                    status: response.status,
                    question: null
                });

                return;
            }

            setGameState({
                ...gameState,
                score: response.score,
                status: response.status,
                question: response.question
            });

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

            {gameState && !gameFinished && (
                <div>
                    <p>
                        Score: {gameState.score}
                    </p>

                    <p>
                        Time: {gameState.remainingSeconds}
                    </p>

                    <h2>
                        {gameState.question.text}
                    </h2>

                    {
                        gameState.question.options.map(
                            (option, index) => (
                                <button
                                    key={index}
                                    onClick={() =>
                                        handleAnswer(index)
                                    }
                                >
                                    {option}
                                </button>
                            )
                        )
                    }
                </div>
            )}

            {gameFinished && (
                <div>
                    <h2>
                        Game Finished
                    </h2>

                    <p>
                        Final Score: {gameState.score}
                    </p>
                </div>
            )}
        </div>
    );
}

export default Game;