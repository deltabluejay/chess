package dataAccess;

import model.UserData;

import java.sql.SQLException;

public class UserDAOSQL implements UserDAO {
    @Override
    public void clear() {
        return;
    }

    @Override
    public void createUser(UserData userData) {

    }

    @Override
    public UserData getUser(UserData userData) {
        return null;
    }

    @Override
    public UserData getUser(String username) {
        return null;
    }
}
