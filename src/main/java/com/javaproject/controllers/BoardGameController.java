package com.javaproject.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.javaproject.beans.BoardGame;
import com.javaproject.beans.ErrorMessage;
import com.javaproject.beans.Review;
import com.javaproject.database.DatabaseAccess;

@RestController
@RequestMapping("/boardgames")
public class BoardGameController {

    private DatabaseAccess da;

    public BoardGameController(DatabaseAccess da) {
        this.da = da;
    }

    /**
     * Retrieve all boardgames
     */
    @GetMapping
    public List<BoardGame> getBoardGames() {
        return da.getBoardGames();
    }

    /**
     * Retrieve a specific boardgame by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardGame(@PathVariable Long id) {
        BoardGame game = da.getBoardGameById(id);
        if (game != null) {
            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessage("Board game with ID " + id + " not found."));
        }
    }
}
