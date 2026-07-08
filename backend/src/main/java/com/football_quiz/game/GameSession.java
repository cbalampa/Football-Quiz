package com.football_quiz.game;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant startTime;

    private int score;

    private int currentQuestionIndex;

    private int correctAnswers;

    @ElementCollection
    @CollectionTable(
            name = "game_session_questions",
            joinColumns = @JoinColumn(name = "game_session_id")
    )
    @Column(name = "question_id")
    private List<Long> questionOrder;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    // Constructors
    public GameSession() {}

    public GameSession(
            Instant startTime,
            List<Long> questionOrder
    ) {
        this.startTime = startTime;
        this.questionOrder = questionOrder;
        this.score = 0;
        this.currentQuestionIndex = 0;
        this.correctAnswers = 0;
        this.status = GameStatus.IN_PROGRESS;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public List<Long> getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(List<Long> questionOrder) {
        this.questionOrder = questionOrder;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }
}
