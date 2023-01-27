package com.stinkytooters.stinkytootersbot.service.user.data;

import com.stinkytooters.stinkytootersbot.api.internal.exception.DaoException;
import com.stinkytooters.stinkytootersbot.api.internal.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Named
public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final RowMapper<UserData> userRowMapper = new UserRowMapper();

    private static String INSERT_USER = "insert into %s.users (usr_name) values (lower(:name))";
    private static String SELECT_USER_BY_NAME = "select * from %s.users where usr_name = lower(:name)";
    private static String SELECT_ALL_USERS = "select * from %s.users";
    private static String DELETE_USER = "delete from %s.users where usr_name = lower(:name)";

    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Inject
    public UserDao(@Value("${schema}") String schema,
                      NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = Objects.requireNonNull(namedJdbcTemplate, "NamedJdbcTemplate is required.");
        populateSchema(schema);
    }

    public void insertUser(User user) {
        int inserted = namedJdbcTemplate.update(INSERT_USER, getParameters(user));
        if (inserted != 1) {
            throw new DaoException("Inserted (" + inserted + ") users. Expected 1.");
        }
    }

    public Optional<UserData> getUser(User user) {
        try {
            return Optional.ofNullable(namedJdbcTemplate.queryForObject(SELECT_USER_BY_NAME, getParameters(user), userRowMapper));
        } catch (DataAccessException ex) {
            String message = String.format("Could not find user by username: (%s)", user.getName());
            logger.info(message);
            return Optional.empty();
        }
    }

    public List<UserData> getAllUsers() {
        return namedJdbcTemplate.query(SELECT_ALL_USERS, Collections.emptyMap(), userRowMapper);
    }

    public void deleteUserByName(User user) {
        int changed = namedJdbcTemplate.update(DELETE_USER, getParameters(user));
        if (changed != 1) {
            throw new DaoException("Deleted (" + changed + ") users. Expected 1, rolling back.");
        }
    }

    private MapSqlParameterSource getParameters(User user) {
        return new MapSqlParameterSource()
                .addValue("name", user.getName(), Types.VARCHAR);
    }

    private static class UserRowMapper implements RowMapper<UserData> {

        @Override
        public UserData mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserData userData = new UserData();
            userData.setId(rs.getLong("usr_id"));
            userData.setName(rs.getString("usr_name"));
            return userData;
        }
    }

    private void populateSchema(String schema) {
        INSERT_USER = String.format(INSERT_USER, schema);
        SELECT_USER_BY_NAME = String.format(SELECT_USER_BY_NAME, schema);
        SELECT_ALL_USERS = String.format(SELECT_ALL_USERS, schema);
        DELETE_USER = String.format(DELETE_USER, schema);
    }
}
