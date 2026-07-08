package com.football_quiz.dto;

import com.football_quiz.model.Difficulty;
import java.util.List;

public record QuestionResponse(
   Long id,
   String text,
   List<String> options,
   Difficulty difficulty
) {
}
