package com.football_quiz.game;

import com.football_quiz.dto.AnswerRequest;
import com.football_quiz.dto.AnswerResponse;
import com.football_quiz.dto.GameResultResponse;
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

    @GetMapping("/{id}/result")
    public GameResultResponse getGameResult(@PathVariable Long id) {
        return gameSessionService.getGameResult(id);
    }

    @PostMapping("/start")
    public StartGameResponse startGame() {
        GameSession session = gameSessionService.startGame();
        return new StartGameResponse(session.getId());
    }

    @PostMapping("/{id}/answer")
    public AnswerResponse submitAnswer(@PathVariable Long id, @RequestBody AnswerRequest request) {
        return gameSessionService.submitAnswer(
                id,
                request.selectedAnswerIndex()
        );
    }

    public record StartGameResponse(Long sessionId) {
    }
}
