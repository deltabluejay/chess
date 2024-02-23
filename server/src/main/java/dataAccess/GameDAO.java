package dataAccess;

import java.util.List;
import model.GameData;

public interface GameDAO {
    void clear();

    List<GameData> list();

    int create(String gameName);
}
