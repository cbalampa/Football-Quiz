package com.football_quiz.repository;

import com.football_quiz.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSessionRepository extends JpaRepository<Question, Long> {
}
