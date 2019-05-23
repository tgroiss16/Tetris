package components;



import com.example.tetris.GameActivity;

public abstract class Component
{
    GameActivity host;

    Component(GameActivity gameActivity)
    {
        host = gameActivity;
    }

    public void reconnect(GameActivity gameActivity)
    {
        host = gameActivity;
    }

    public void disconnect()
    {
        host = null;
    }
}
