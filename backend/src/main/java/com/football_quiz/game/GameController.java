package com.football_quiz.game;

import com.football_quiz.dto.GameStateResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameSessionService gameSessionService;

    public GameController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @GetMapping("/{id}")
    public GameStateResponse getGameState(@ PathVariable Long id) {
        return gameSessionService.getGameState(id);
    }

    @PostMapping("/start")
    public StartGameResponse startGame() {
        GameSession session = gameSessionService.startGame();
        return new StartGameResponse(session.getId());
    }

    public record StartGameResponse(Long sessionId) {
    }
}
