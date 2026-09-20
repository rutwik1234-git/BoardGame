package com.javaproject.database;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.javaproject.beans.BoardGame;
import com.javaproject.beans.Review;

@Repository
public class DatabaseAccess {

    @Autowired
    protected NamedParameterJdbcTemplate jdbc;

    // --- BoardGame Operations ---

    public List<BoardGame> getBoardGames() {
        String query = "SELECT * FROM boardgames";
        return jdbc.query(query, new BeanPropertyRowMapper<>(BoardGame.class));
    }

    public BoardGame getBoardGame(Long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM boardgames WHERE id = :id";
        namedParameters.addValue("id", id);

        List<BoardGame> games = jdbc.query(query, namedParameters, new BeanPropertyRowMapper<>(BoardGame.class));
        return games.isEmpty() ? null : games.get(0);
    }

    public BoardGame getBoardGameById(Long id) {
        return getBoardGame(id);
    }

    public Long addBoardGame(BoardGame game) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String query = "INSERT INTO boardgames (name, min_players, max_players) VALUES (:name, :minPlayers, :maxPlayers)";
        namedParameters.addValue("name", game.getName());
        namedParameters.addValue("minPlayers", game.getMinPlayers());
        namedParameters.addValue("maxPlayers", game.getMaxPlayers());

        jdbc.update(query, namedParameters, keyHolder);
        return (keyHolder.getKey() != null) ? keyHolder.getKey().longValue() : 1L;
    }

    // --- Review Operations ---

    public List<Review> getReviews(Long gameId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM reviews WHERE game_id = :gameId";
        namedParameters.addValue("gameId", gameId);

        return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<>(Review.class));
    }

    public Review getReview(Long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM reviews WHERE id = :id";
        namedParameters.addValue("id", id);

        List<Review> reviews = jdbc.query(query, namedParameters, new BeanPropertyRowMapper<>(Review.class));
        return reviews.isEmpty() ? null : reviews.get(0);
    }

    public int addReview(Review review) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "INSERT INTO reviews (game_id, text) VALUES (:gameId, :text)";
        namedParameters.addValue("gameId", review.getGameId());
        namedParameters.addValue("text", review.getText());

        return jdbc.update(query, namedParameters);
    }

    public int editReview(Review review) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "UPDATE reviews SET text = :text WHERE id = :id";
        namedParameters.addValue("text", review.getText());
        namedParameters.addValue("id", review.getId());

        return jdbc.update(query, namedParameters);
    }

    public int deleteReview(Long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "DELETE FROM reviews WHERE id = :id";
        namedParameters.addValue("id", id);

        return jdbc.update(query, namedParameters);
    }

    // --- Security & Roles ---

    public List<String> getAuthorities() {
        String query = "SELECT authority FROM sec_authority";
        return jdbc.queryForList(query, new MapSqlParameterSource(), String.class);
    }
}
