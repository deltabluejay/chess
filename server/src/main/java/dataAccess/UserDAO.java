package dataAccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {
    void clear();

    boolean createUser(UserData userData);

    UserData getUser(UserData userData);

    UserData getUser(String username);
}
