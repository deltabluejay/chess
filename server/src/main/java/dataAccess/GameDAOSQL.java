package dataAccess;

import model.GameData;
import service.AlreadyTakenError;

import java.util.List;

public class GameDAOSQL implements GameDAO {
    @Override
    public void clear() {

    }

    @Override
    public List<GameData> list() {
        return null;
    }

    @Override
    public boolean exists(int gameID) {
        return false;
    }

    @Override
    public int create(String gameName) {
        return 0;
    }

    @Override
    public void join(String username, String playerColor, int gameID) throws AlreadyTakenError {

    }
}
