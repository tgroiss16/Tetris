package components;

import android.R.color;
import android.widget.TextView;

import com.example.tetris.GameActivity;
import com.example.tetris.R;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import engine.PieceGenerator;
import pieces.IPiece;
import pieces.JPiece;
import pieces.LPiece;
import pieces.OPiece;
import pieces.Piece;
import pieces.SPiece;
import pieces.TPiece;
import pieces.ZPiece;

public class GameState extends Component
{
    private final static int STATE_STARTABLE = 0;
    private final static int STATE_RUNNING = 1;
    private final static int STATE_PAUSED = 2;
    private final static int STATE_FINISHED = 3;

    private static GameState instance;

    // References
    private PieceGenerator pieceGenerator;
    private Board board;
    private GregorianCalendar date;
    private SimpleDateFormat formatter;
    private int hourOffset;

    // Game State
    private String playerName;
    private int activeIndex, previewIndex;
    private Piece[] activePieces;
    private Piece[] previewPieces;
    private boolean scheduleSpawn;
    private long spawnTime;
    private int stateOfTheGame;
    private long score;
    private int lines;
    private int clearedLines;
    private int level;
    private int maxLevel;
    private long gameTime; // += (systemtime - currenttime) at start of cycle
    private long currentTime; // = systemtime at start of cycle
    private long nextDropTime;
    private long nextPlayerDropTime;
    private long nextPlayerMoveTime;
    private int[] dropIntervals; // = (1 / gamespeed)
    private long playerDropInterval;
    private long playerMoveInterval;
    private int singleLineScore;
    private int doubleLineScore;
    private int trippleLineScore;
    private int multiTetrisScore;
    private boolean multitetris;
    private int quadLineScore;
    private int hardDropBonus;
    private int softDropBonus;
    private int spawnDelay;
    private int pieceStartX;
    private long actions;
    private int songtime;

    private long popupTime;
    private String popupString;
    private int popupAttack;
    private int popupSustain;
    private int popupDecay;
    private int softDropDistance;

    private GameState(GameActivity gameActivity)
    {
        super(gameActivity);

        actions = 0;
        board = new Board(host);
        date = new GregorianCalendar();
        formatter = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        date.setTimeInMillis(60000);

        if (formatter.format(date.getTime()).startsWith("23")) {
            hourOffset = 1;
        } else if (formatter.format(date.getTime()).startsWith("01")) {
            hourOffset = -1;
        } else {
            hourOffset = 0;
        }

        dropIntervals = host.getResources().getIntArray(R.array.intervals);
        singleLineScore = host.getResources().getInteger(R.integer.single_line_score);
        doubleLineScore = host.getResources().getInteger(R.integer.double_line_score);
        trippleLineScore = host.getResources().getInteger(R.integer.tripple_line_score);
        multiTetrisScore = host.getResources().getInteger(R.integer.multi_tetris_score);
        quadLineScore = host.getResources().getInteger(R.integer.quad_line_score);
        softDropDistance = 0;
        spawnDelay = host.getResources().getInteger(R.integer.spawn_delay);
        pieceStartX = host.getResources().getInteger(R.integer.piece_start_x);
        popupAttack = host.getResources().getInteger(R.integer.popup_attack);
        popupSustain = host.getResources().getInteger(R.integer.popup_sustain);
        popupDecay = host.getResources().getInteger(R.integer.popup_decay);
        popupString = "";
        popupTime = -(popupAttack + popupSustain + popupDecay);
        clearedLines = 0;
        level = 0;
        score = 0;
        songtime = 0;
        maxLevel = host.getResources().getInteger(R.integer.levels);
        lines = 0;

        nextDropTime = host.getResources().getIntArray(R.array.intervals)[0];

        playerDropInterval = (int) (1000.0f / host.getResources()
            .getInteger(R.integer.soft_drop_speed));
        playerMoveInterval = (int) (1000.0f / host.getResources()
            .getInteger(R.integer.move_speed));
        nextPlayerDropTime = (int) (1000.0f / host.getResources()
            .getInteger(R.integer.soft_drop_speed));
        nextPlayerMoveTime = (int) (1000.0f / host.getResources()
            .getInteger(R.integer.move_speed)); // Movement for the cycle
        gameTime = 0;
        pieceGenerator = new PieceGenerator();

        // Initialize Pieces
        activePieces = new Piece[7];
        previewPieces = new Piece[7];

        activePieces[0] = new IPiece(host);
        activePieces[1] = new JPiece(host);
        activePieces[2] = new LPiece(host);
        activePieces[3] = new OPiece(host);
        activePieces[4] = new SPiece(host);
        activePieces[5] = new TPiece(host);
        activePieces[6] = new ZPiece(host);

        // Starting pieces
        activeIndex = pieceGenerator.next();
        previewIndex = pieceGenerator.next();
        activePieces[activeIndex].setActive(true);

        stateOfTheGame = STATE_STARTABLE;
        scheduleSpawn = false;
        spawnTime = 0;
    }

    public static void destroy()
    {
        if (instance != null) {
            instance.disconnect();
        }

        instance = null;
    }

    public static GameState getInstance(GameActivity gameActivity)
    {
        if (instance == null) {
            instance = new GameState(gameActivity);
        }
        return instance;
    }

    public static GameState getNewInstance(GameActivity gameActivity)
    {
        instance = new GameState(gameActivity);

        return instance;
    }

    public static boolean isFinished()
    {
        if (instance == null) {
            return true;
        }

        return !instance.isResumable();
    }

    public Board getBoard()
    {
        return board;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(String string)
    {
        playerName = string;
    }

    int getAutoDropInterval()
    {
        return dropIntervals[Math.min(level, maxLevel)];
    }

    long getMoveInterval()
    {
        return playerMoveInterval;
    }

    long getSoftDropInterval()
    {
        return playerDropInterval;
    }

    public void setRunning(boolean running)
    {
        if (running) {
            currentTime = System.currentTimeMillis();

            if (stateOfTheGame != STATE_FINISHED) {
                stateOfTheGame = STATE_RUNNING;
            }
        } else {
            if (stateOfTheGame == STATE_RUNNING) {
                stateOfTheGame = STATE_PAUSED;
            }
        }
    }

    void clearLines(boolean playerHardDrop, int hardDropDistance)
    {
        if (host == null) {
            return;
        }

        activePieces[activeIndex].place(board);
        int cleared = board.clearLines(activePieces[activeIndex].getDim());
        clearedLines += cleared;
        long addScore;

        switch (cleared) {
            case 1:
                addScore = singleLineScore;
                multitetris = false;
                lines +=1;
                popupTime = gameTime;
                break;

            case 2:
                addScore = doubleLineScore;
                multitetris = false;
                lines +=2;
                popupTime = gameTime;
                break;

            case 3:
                addScore = trippleLineScore;
                multitetris = false;
                lines +=3;
                popupTime = gameTime;
                break;

            case 4:
                if (multitetris) {
                    addScore = multiTetrisScore;
                } else {
                    addScore = quadLineScore;
                }

                multitetris = true;
                lines +=4;
                popupTime = gameTime;
                break;

            default:
                addScore = 0;


                if ((gameTime - popupTime) < (popupAttack + popupSustain)) {
                    popupTime = gameTime - (popupAttack + popupSustain);
                }

                break;
        }

        if (cleared > 0) {
            // HardDrop/SoftDrop Boni: we comply to Tetrisfriends rules now
            if (playerHardDrop) {
                addScore += hardDropDistance * hardDropBonus;
            } else {
                addScore += softDropDistance * softDropBonus;
            }
        }

        score += addScore; // + tempBonus;
        host.updateScore(score,lines,getLevel());

        if (addScore != 0) {
            popupString = "+" + addScore;
        }
    }

    void pieceTransition(boolean eventVibrationEnabled)
    {
        if (host == null) {
            return;
        }

        scheduleSpawn = true;

        // Delay piece transition only while vibration is playing
        if (eventVibrationEnabled) {
            spawnTime = gameTime + spawnDelay;
        } else {
            spawnTime = gameTime;
        }

        activePieces[activeIndex].reset(host);
        activeIndex = previewIndex;
        previewIndex = pieceGenerator.next();
        activePieces[activeIndex].reset(host);
    }

    private void finishTransition()
    {
        if (host == null) {
            return;
        }

        scheduleSpawn = false;
        activePieces[activeIndex].setActive(true);
        setNextDropTime(gameTime + dropIntervals[Math.min(level, maxLevel)]);
        setNextPlayerDropTime(gameTime);
        setNextPlayerMoveTime(gameTime);
        softDropDistance = 0;

        // Checking for Defeat
        if (!activePieces[activeIndex].setPosition(pieceStartX, 0, false, board)) {
            stateOfTheGame = STATE_FINISHED;
            host.gameOver(score, getTimeString(), lines);
        }
    }

    public boolean isResumable()
    {
        return (stateOfTheGame != STATE_FINISHED);
    }

    String getScoreString()
    {
        return String.valueOf(score);
    }

    Piece getActivePiece()
    {
        return activePieces[activeIndex];
    }

    /*
     * Returns true if controls is allowed to cycle()
     */
    public boolean cycle(long tempTime)
    {
        if (stateOfTheGame != STATE_RUNNING) {
            return false;
        }

        gameTime += (tempTime - currentTime);
        currentTime = tempTime;

        // Instant Placement
        if (scheduleSpawn) {
            if (gameTime >= spawnTime) {
                finishTransition();
            }

            return false;
        }

        return true;
    }

    String getLevelString()
    {
        return String.valueOf(level);
    }

    public String getTimeString()
    {
        date.setTimeInMillis(gameTime + hourOffset * (3600000));

        return formatter.format(date.getTime());
    }

    String getAPMString()
    {
        if (host == null) {
            return "";
        }

        return String.valueOf((int) ((float) actions * (60000.0f / gameTime)));
    }

    @Override
    public void reconnect(GameActivity gameActivity)
    {
        super.reconnect(gameActivity);

        playerDropInterval = (int) (1000.0f / host.getResources().getInteger(R.integer.soft_drop_speed)); // 142, at 7 lines per second
        playerMoveInterval = (int) (1000.0f / host.getResources().getInteger(R.integer.move_speed)); // 250, at 4 squares per second

        pieceGenerator = new PieceGenerator();

        board.reconnect(gameActivity);
        setRunning(true);
    }

    public void disconnect()
    {
        setRunning(false);
        board.disconnect();

        super.disconnect();
    }

    Piece getPreviewPiece()
    {
        return previewPieces[previewIndex];
    }

    public long getTime()
    {
        return gameTime;
    }

    void nextLevel()
    {
        level++;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;

        nextDropTime = host.getResources().getIntArray(R.array.intervals)[level];
        clearedLines = 10 * level;
    }

    int getMaxLevel()
    {
        return maxLevel;
    }

    int getClearedLines()
    {
        return clearedLines;
    }

    void action()
    {
        actions++;
    }

    long getNextPlayerDropTime()
    {
        return nextPlayerDropTime;
    }

    void setNextPlayerDropTime(long time)
    {
        nextPlayerDropTime = time;
    }

    long getNextDropTime()
    {
        return nextDropTime;
    }

    void setNextDropTime(long l)
    {
        nextDropTime = l;
    }

    long getNextPlayerMoveTime()
    {
        return nextPlayerMoveTime;
    }

    void setNextPlayerMoveTime(long time)
    {
        nextPlayerMoveTime = time;
    }

    public long getScore()
    {
        return score;
    }

    public int getAPM()
    {
        return (int) ((float) actions * (60000.0f / gameTime));
    }

    public int getSongtime()
    {
        return songtime;
    }

    public void setSongtime(int songtime)
    {
        this.songtime = songtime;
    }

    String getPopupString()
    {
        return popupString;
    }

    int getPopupAlpha()
    {
        long x = gameTime - popupTime;

        if (x < (popupAttack + popupSustain)) {
            return 255;
        }

        if (x < (popupAttack + popupSustain + popupDecay)) {
            return (int) (255.0f * (1.0f + (((float) (popupAttack + popupSustain - x)) / ((float) popupDecay))));
        }

        return 0;
    }

    float getPopupSize()
    {
        long x = gameTime - popupTime;

        if (x < popupAttack) {
            return (int) (60.0f * (1.0f + (((float) x) / ((float) popupAttack))));
        }

        return 120;
    }

    int getPopupColor()
    {
        if (host == null) {
            return 0;
        }

        if (multitetris) {
            return host.getResources().getColor(R.color.yellow);
        }

        return host.getResources().getColor(color.white);
    }

    void incSoftDropCounter()
    {
        softDropDistance++;
    }
}
