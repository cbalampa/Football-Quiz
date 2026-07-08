package com.football_quiz;

import com.football_quiz.dto.AnswerResponse;
import com.football_quiz.game.GameSession;
import com.football_quiz.game.GameSessionService;
import com.football_quiz.game.GameStatus;
import com.football_quiz.model.Difficulty;
import com.football_quiz.model.Question;
import com.football_quiz.repository.GameSessionRepository;
import com.football_quiz.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameSessionServiceTest {
    @Mock
    private GameSessionRepository gameSessionRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private GameSessionService gameSessionService;

    private Question hardQuestion;

    @BeforeEach
    void setup() {
        hardQuestion = new Question(
                "Hard question",
                List.of(
                        "Answer A",
                        "Answer B",
                        "Answer C",
                        "Answer D"
                ),
                0,
                Difficulty.HARD
        );

        when(gameSessionRepository.save(any(GameSession.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void submitAnswer_hardCorrectFromZero_shouldGive10Points() {
        GameSession session = new GameSession(
                Instant.now(),
                List.of(1L)
        );

        when(gameSessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(questionRepository.findById(1L)).thenReturn(Optional.of(hardQuestion));

        gameSessionService.submitAnswer(1L, 0);

        assertThat(session.getScore()).isEqualTo(10);
        assertThat(session.getCorrectAnswers()).isEqualTo(1);
    }

    @Test
    void submitAnswer_hardWrong_shouldReduceScore() {
        GameSession session = new GameSession(
                Instant.now(),
                List.of(1L)
        );

        session.setScore(30);

        when(gameSessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(questionRepository.findById(1L)).thenReturn(Optional.of(hardQuestion));

        gameSessionService.submitAnswer(1L, 1);

        assertThat(session.getScore()).isEqualTo(20);
    }

    @Test
    void submitAnswer_hardWrong_shouldNotGoBelowZero() {
        GameSession session = new GameSession(
                Instant.now(),
                List.of(1L)
        );

        session.setScore(1);

        when(gameSessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(questionRepository.findById(1L)).thenReturn(Optional.of(hardQuestion));

        gameSessionService.submitAnswer(1L, 1);

        assertThat(session.getScore()).isZero();
    }

    @Test
    void submitAnswer_lastQuestion_shouldFinishGame() {
        GameSession session = new GameSession(
                Instant.now(),
                List.of(1L)
        );

        when(gameSessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(questionRepository.findById(1L)).thenReturn(Optional.of(hardQuestion));

        AnswerResponse response = gameSessionService.submitAnswer(1L, 0);

        assertThat(session.getStatus()).isEqualTo(GameStatus.FINISHED);
        assertThat(response.status()).isEqualTo(GameStatus.FINISHED);
        assertThat(response.question()).isNull();
    }
}