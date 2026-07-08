package com.football_quiz.game;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameSessionService gameSessionService;

    public GameController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @PostMapping("/start")
    public StartGameResponse startGame() {
        GameSession session = gameSessionService.startGame();
        return new StartGameResponse(session.getId());
    }

    public record StartGameResponse(Long sessionId) {
    }
}
