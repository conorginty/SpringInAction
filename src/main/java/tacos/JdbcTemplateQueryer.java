package tacos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// This class is designed to show how much simpler it is to query a DB with JdbcTemplate

/* Notice there aren’t any statements or connections being created. And, after the method is finished, there isn’t
any cleanup of those objects. Finally, there isn’t any handling of exceptions that can’t properly be handled in a
catch block. All we're doing is making a query and mapping the results to the corresponding Ingredient */
public class JdbcTemplateQueryer {
    private JdbcTemplate jdbcTemplate;

    public Optional<Ingredient> findById(String id) {
        /* jdbcTemplate.query() accepts the SQL for the query as well as an implementation of Spring’s RowMapper for
        the purpose of mapping each row in the result set to an object. */
        List<Ingredient> results = jdbcTemplate.query(
            "select id, name, type from Ingredient where id=?",
            this::mapRowToIngredient,
            id
        );

        return results.size() == 0 ?
            Optional.empty() :
            Optional.of(results.get(0));
    }

    private Ingredient mapRowToIngredient(ResultSet row, int rowNum) throws SQLException {
        return new Ingredient(
            row.getString("id"),
            row.getString("name"),
            Ingredient.Type.valueOf(row.getString("type"))
        );
    }

    // Although we use a method reference in the query method, here's how it would look explicitly using a RowMapper
    public Ingredient findByIdUsingRowMapper(String id) {
        return jdbcTemplate.queryForObject(
            "select id, name, type from Ingredient where id=?",
            new RowMapper<Ingredient>() {
                public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Ingredient(
                        rs.getString("id"),
                        rs.getString("name"),
                        Ingredient.Type.valueOf(rs.getString("type")));

                }
            },
            id
        );
    }
}
