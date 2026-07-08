package com.football_quiz.game;

import com.football_quiz.dto.GameStateResponse;
import com.football_quiz.dto.QuestionResponse;
import com.football_quiz.model.Question;
import com.football_quiz.repository.GameSessionRepository;
import com.football_quiz.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    public GameStateResponse getGameState(Long id) {
        GameSession session = gameSessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found"));

        long elapsedSeconds = Duration.between(
                session.getStartTime(),
                Instant.now()
        ).getSeconds();

        if (elapsedSeconds >= 60) {
            session.setStatus(GameStatus.FINISHED);
            gameSessionRepository.save(session);

            return new GameStateResponse(
                    session.getScore(),
                    session.getCurrentQuestionIndex(),
                    0,
                    session.getStatus(),
                    null
            );
        }

        Long questionId = session.getQuestionOrder()
                .get(session.getCurrentQuestionIndex());

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        QuestionResponse questionResponse = new QuestionResponse(
                question.getId(),
                question.getText(),
                question.getOptions(),
                question.getDifficulty()
        );

        return new GameStateResponse(
                session.getScore(),
                session.getCurrentQuestionIndex(),
                60 - elapsedSeconds,
                session.getStatus(),
                questionResponse
        );
    }
}