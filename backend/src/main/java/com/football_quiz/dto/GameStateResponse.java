package com.football_quiz.dto;


import com.football_quiz.game.GameStatus;

public record GameStateResponse(
        int score,
        int currentQuestionIndex,
        long remainingSeconds,
        GameStatus status,
        QuestionResponse question
) {
}