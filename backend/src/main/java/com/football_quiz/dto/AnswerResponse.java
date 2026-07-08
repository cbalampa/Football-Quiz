package com.football_quiz.dto;

import com.football_quiz.game.GameStatus;

public record AnswerResponse(
        boolean correct,
        int score,
        GameStatus status,
        QuestionResponse question
) {
}