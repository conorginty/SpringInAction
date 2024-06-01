package tacos;

import java.sql.*;
import java.util.Optional;

// This class is designed to show how cumbersome it is to query a DB without JdbcTemplate

/* Although there are a couple of lines here that query the database for ingredients, it’s surrounded by code that
creates a connection, creates a statement, and cleans up by closing the connection, statement, and result set.
To make matters worse, any number of things could go wrong when creating the connection or the statement, or when
performing the query. This requires that you catch a SQLException, which may or may not be helpful in figuring out
what went wrong or how to address the problem.
SQLException is a checked exception, which requires handling in a catch block. But the most common problems, such
as failure to create a connection to the database or a mistyped query, can’t possibly be addressed in a catch block
and are likely to be rethrown for handling upstream.*/
public class NoJdbcTemplateQueryer {

    private final DataSource dataSource = new DataSource();

    public NoJdbcTemplateQueryer() throws SQLException {
    }

    public Optional<Ingredient> findById(String id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(
                "select id, name, type from Ingredient where id=?"
            );
            statement.setString(1, id);
            resultSet = statement.executeQuery();
            Ingredient ingredient = null;
            if (resultSet.next()) {
                ingredient = new Ingredient(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    Ingredient.Type.valueOf(resultSet.getString("type"))
                );
            }
            return Optional.of(ingredient);
        } catch (SQLException e) {
            e.printStackTrace();
            // ??? What should be done here ???
        } finally {
            // Finally, attempt to close the connection from the innermost to outermost point
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return Optional.empty();
    }

    private class DataSource {
        private final Connection connection = DriverManager.getConnection("jdbc:jdbc:mysql://localhost:3306/");

        private DataSource() throws SQLException {
        }

        public Connection getConnection() {
            return this.connection;
        }
    }
}
