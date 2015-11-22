package ru.yamoney.test.testtools2.user_cashe;

import ru.yamoney.test.testtools2.common.DaoContainer;
import ru.yamoney.test.testtools2.user_cashe.db.UserDao;

/**
 * Created by def on 22.11.15.
 */
public class UserManager {
    private UserDao userDao;

    public UserManager(DaoContainer daoContainer){
        this.userDao = daoContainer.getUserDao();
    }

    public User getUser(String host, UserStatus userStatus) {
        return userDao.getUser(host, userStatus);
    }

    public void delUser(String account) {
        userDao.delUser(account);
    }

    public void freeUser(String account) {
        userDao.setStatus(account, UserStatus.USED);
    }

    public User addUser(String account, String host, String phone, String userName) {
        return userDao.addUser(account, userName, host, phone);
    }

}
