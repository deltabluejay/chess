package dataAccess;

import java.util.List;
import model.GameData;

public interface GameDAO {
    void clear();

    List<GameData> list();

    boolean exists(int gameID);

    int create(String gameName);

    void join(String username, String playerColor, int gameID);
}
