package ru.yamoney.test.testtools2.user_cashe.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.yamoney.test.testtools2.user_cashe.User;
import ru.yamoney.test.testtools2.user_cashe.UserStatus;

import java.util.List;

/**
 * Created by def on 22.11.15.
 */
public class UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getUsers(String host) {
        String SQL = "SELECT data\n" +
                " FROM users WHERE data->>'host'=?;\n";
        return jdbcTemplate.query(SQL, new Object[] {host}, new UserMapper());
    }

    @Transactional
    public User getUser(String host, UserStatus status) {
        String SQL = "SELECT data" +
                " FROM users WHERE data->>'host'=? and data->>'status'=? limit 1;\n";
        User user = jdbcTemplate.queryForObject(SQL, new Object[] { host, status.getValue() + "" }, new UserMapper());
        user = setStatus(user.getAccount(), UserStatus.BUSY);
        return user;
    }


    private User getUser(String account) {
        String SQL = "SELECT data\n" +
                "  FROM users WHERE data->>'account'=? limit 1;\n";
        return jdbcTemplate.queryForObject(SQL, new Object[] {account}, new UserMapper());
    }

    public void delUser(User user) {
        String SQL = "DELETE FROM users WHERE data->>'account'=?;\n";
        jdbcTemplate.update(SQL, new Object[] {user.getAccount()});
    }

    public void delUser(String account) {
        String SQL = "DELETE FROM users WHERE data->>'account'=?;\n";
        jdbcTemplate.update(SQL, new Object[] {account});
    }

    private User storeUser(User user) {
        String SQL = "UPDATE users\n" +
                " SET data=?::jsonb \n" +
                " WHERE data->>'account'=?";

        jdbcTemplate.update(SQL, new Object[]{
                user.getJsonString(), user.getAccount()
        });
        return user;
    }

    @Transactional
    public User setStatus(String account, UserStatus userStatus) {
        User user = getUser(account);
        user.setStatus(userStatus);
        storeUser(user);
        return user;
    }

    public User addUser(String account, String userName, String host, String phone){
        User user = new User();
        user.setStatus(UserStatus.NEW);
        user.setAccount(account);
        user.setHost(host);
        user.setUserName(userName);
        user.setPhone(phone);
        String SQL = "INSERT into users (data) values (?::jsonb);";
        jdbcTemplate.update(SQL, new Object[]{user.getJsonString()});
        return user;
    }
}
