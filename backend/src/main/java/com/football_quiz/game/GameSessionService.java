package com.football_quiz.game;

import com.football_quiz.model.Question;
import com.football_quiz.repository.GameSessionRepository;
import com.football_quiz.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GameSessionService {
    private final GameSessionRepository gameSessionRepository;
    private final QuestionRepository questionRepository;

    public GameSessionService(
            GameSessionRepository gameSessionRepository,
            QuestionRepository questionRepository
    ) {
        this.gameSessionRepository = gameSessionRepository;
        this.questionRepository = questionRepository;
    }

    public GameSession startGame() {
        List<Long> questionOrder = new ArrayList<>(
                questionRepository.findAll()
                        .stream()
                        .map(Question::getId)
                        .toList()
        );

        Collections.shuffle(questionOrder);

        GameSession gameSession = new GameSession(
                Instant.now(),
                questionOrder
        );

        return gameSessionRepository.save(gameSession);
    }
}