package com.football_quiz.game;

import com.football_quiz.dto.AnswerResponse;
import com.football_quiz.dto.GameStateResponse;
import com.football_quiz.dto.QuestionResponse;
import com.football_quiz.model.Difficulty;
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

        QuestionResponse questionResponse = buildQuestionResponse(questionId);

        return new GameStateResponse(
                session.getScore(),
                session.getCurrentQuestionIndex(),
                60 - elapsedSeconds,
                session.getStatus(),
                questionResponse
        );
    }

    private int calculateScore(int currentScore, boolean correct, Difficulty difficulty) {
        if (correct) {
            return switch (difficulty) {
                case EASY, NORMAL -> currentScore + 5;

                case HARD -> {
                    int score = currentScore + 5;
                    yield score * 2;
                }
            };
        }

        if (difficulty == Difficulty.HARD) {
            return Math.max(
                    0,
                    (int) Math.floor(currentScore / 1.5)
            );
        }

        return currentScore;
    }

    public AnswerResponse submitAnswer(Long sessionId, int selectedAnswerIndex) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found"));

        if (session.getStatus() == GameStatus.FINISHED) {
            throw new IllegalStateException("Game already finished");
        }

        long elapsedSeconds = Duration.between(
                session.getStartTime(),
                Instant.now()
        ).getSeconds();

        if (elapsedSeconds >= 60) {
            session.setStatus(GameStatus.FINISHED);
            gameSessionRepository.save(session);

            return new AnswerResponse(
                    false,
                    session.getScore(),
                    session.getStatus(),
                    null
            );
        }

        Long questionId = session.getQuestionOrder().get(session.getCurrentQuestionIndex());

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        boolean correct = selectedAnswerIndex == question.getCorrectAnswerIndex();

        int newScore = calculateScore(
                session.getScore(),
                correct,
                question.getDifficulty()
        );

        session.setScore(newScore);

        if (correct) {
            session.setCorrectAnswers(session.getCorrectAnswers() + 1);
        }

        session.setCurrentQuestionIndex(session.getCurrentQuestionIndex() + 1);
        boolean finished = session.getCurrentQuestionIndex() >= session.getQuestionOrder().size();

        if (finished) {
            session.setStatus(GameStatus.FINISHED);
            gameSessionRepository.save(session);

            return new AnswerResponse(
                    correct,
                    session.getScore(),
                    session.getStatus(),
                    null
            );
        }

        gameSessionRepository.save(session);

        Long nextQuestionId =
                session.getQuestionOrder()
                        .get(session.getCurrentQuestionIndex());

        return new AnswerResponse(
                correct,
                session.getScore(),
                session.getStatus(),
                buildQuestionResponse(nextQuestionId)
        );
    }

    private QuestionResponse buildQuestionResponse(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        return new QuestionResponse(
                question.getId(),
                question.getText(),
                question.getOptions(),
                question.getDifficulty()
        );
    }
}