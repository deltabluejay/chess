package dataAccess;

import model.AuthData;
import model.UserData;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Iterator;

public class UserDAOMemory implements UserDAO {
    private static List<UserData> userList = new ArrayList<>();

    @Override
    public void clear() {
        userList = new ArrayList<>();
    }

    @Override
    public boolean createUser(UserData userData) {
        userList.add(userData);
        return true;
    }

    @Override
    public UserData getUser(UserData userData) {
        for (UserData user : userList) {
            if (user.equals(userData)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public UserData getUser(String username) {
        for (UserData user : userList) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

}
