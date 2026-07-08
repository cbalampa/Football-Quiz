package com.football_quiz.dto;

import com.football_quiz.game.GameStatus;

public record GameResultResponse(
        int score,
        int correctAnswers,
        int totalQuestions,
        GameStatus status
) {
}
