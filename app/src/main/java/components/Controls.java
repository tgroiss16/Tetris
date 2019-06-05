package components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import com.example.tetris.GameActivity;
import com.example.tetris.R;


import java.util.Objects;

import pieces.Piece;

public class Controls extends Component
{
    private int[] lineThresholds;

    private boolean playerSoftDrop;
    private boolean clearPlayerSoftDrop;
    private boolean playerHardDrop;
    private boolean leftMove;
    private boolean rightMove;
    private boolean continuousSoftDrop;
    private boolean continuousLeftMove;
    private boolean continuousRightMove;
    private boolean leftRotation;
    private boolean rightRotation;
    private boolean buttonVibrationEnabled;
    private boolean eventVibrationEnabled;
    private int initialHorizontalIntervalFactor;
    private int initialVerticalIntervalFactor;

    public Controls(GameActivity gameActivity)
    {
        super(gameActivity);

        lineThresholds = host.getResources().getIntArray(R.array.line_thresholds);
        if (PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_horizontal_acceleration", true)) {
            initialHorizontalIntervalFactor = 2;
        } else {
            initialHorizontalIntervalFactor = 1;
        }

        if (PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_soft_drop_acceleration", true)) {
            initialVerticalIntervalFactor = 2;
        } else {
            initialVerticalIntervalFactor = 1;
        }

        playerSoftDrop = false;
        leftMove = false;
        rightMove = false;
        leftRotation = false;
        rightRotation = false;
        clearPlayerSoftDrop = false;
        continuousSoftDrop = false;
        continuousLeftMove = false;
        continuousRightMove = false;
    }


    public void rotateLeftPressed()
    {
        leftRotation = true;
        host.game.action();

    }

    public void rotateRightPressed()
    {
        rightRotation = true;
        host.game.action();
    }

    public void downButtonPressed()
    {
        host.game.action();
        playerSoftDrop = true;


        host.game.setNextPlayerDropTime(host.game.getTime());

    }




    public void leftButtonPressed()
    {
        host.game.action();
        leftMove = true;
        rightMove = false;
        host.game.setNextPlayerMoveTime(host.game.getTime());

    }

    public void rightButtonPressed()
    {
        host.game.action();
        rightMove = true;
        host.game.setNextPlayerMoveTime(host.game.getTime());
    }

    public void cycle() {
        long gameTime = host.game.getTime();
        Piece active = host.game.getActivePiece();
        Board board = host.game.getBoard();
        int maxLevel = host.game.getMaxLevel();

        // Left rotation
        if (leftRotation) {
            leftRotation = false;
            active.turnLeft(board);
        }

        // Right rotation
        if (rightRotation) {
            rightRotation = false;
            active.turnRight(board);
        }

        // Reset move time
        if ((!leftMove && !rightMove) && (!continuousLeftMove && !continuousRightMove)) {
            host.game.setNextPlayerMoveTime(gameTime);
        }

        // Left move
        if (leftMove) {
            leftMove = false;

            if (active.moveLeft(board)) {
                // Successful move
                host.game.setNextPlayerMoveTime(host.game.getNextPlayerMoveTime() + initialHorizontalIntervalFactor * host.game.getMoveInterval());
                leftMove = false;
            } else {
                host.game.setNextPlayerMoveTime(gameTime);
                leftMove = false;
            }

        }

        // Right Move
        if (rightMove) {
            continuousRightMove = true;
            rightMove = false;
            if (active.moveRight(board)) {


                rightMove = false;
                host.game.setNextPlayerMoveTime(host.game.getNextPlayerMoveTime() + initialHorizontalIntervalFactor * host.game.getMoveInterval());
            } else {
                rightMove = false;
                host.game.setNextPlayerMoveTime(gameTime); // First interval is doubled
            }

        }

        // Hard Drop
        if (playerHardDrop) {
            board.interruptClearAnimation();
            int hardDropDistance = active.hardDrop(false, board);
            host.game.clearLines(true, hardDropDistance);
            host.game.pieceTransition(eventVibrationEnabled);
            board.invalidate();
            playerHardDrop = false;

            if ((host.game.getLevel() < maxLevel) && (host.game.getClearedLines() > lineThresholds[Math.min(host.game.getLevel(), maxLevel - 1)])) {
                host.game.nextLevel();
            }
            host.game.setNextDropTime(gameTime + host.game.getAutoDropInterval());
            host.game.setNextPlayerDropTime(gameTime);

        } else if (playerSoftDrop) {
            playerSoftDrop = false;
            continuousSoftDrop = false;

            if (!active.drop(board)) {
                host.game.clearLines(false, 0);
                host.game.pieceTransition(eventVibrationEnabled);
                board.invalidate();
            } else {
                host.game.incSoftDropCounter();
            }

            if ((host.game.getLevel() < maxLevel) && (host.game.getClearedLines() > lineThresholds[Math.min(host.game.getLevel(), maxLevel - 1)])) {
                host.game.nextLevel();
            }

            host.game.setNextDropTime(host.game.getNextPlayerDropTime() + host.game.getAutoDropInterval());
            host.game.setNextPlayerDropTime(host.game.getNextPlayerDropTime() + initialVerticalIntervalFactor * host.game.getSoftDropInterval());

            // Continuous Soft Drop
        } else if (gameTime >= host.game.getNextDropTime()) {
            // Autodrop if faster than playerDrop
            if (!active.drop(board)) {
                // Piece finished
                host.game.clearLines(false, 0);
                host.game.pieceTransition(eventVibrationEnabled);
                board.invalidate();
            }

            if ((host.game.getLevel() < maxLevel) && (host.game.getClearedLines() > lineThresholds[Math.min(host.game.getLevel(), maxLevel - 1)])) {
                host.game.nextLevel();
            }

            host.game.setNextDropTime(host.game.getNextDropTime() + host.game.getAutoDropInterval());
            host.game.setNextPlayerDropTime(host.game.getNextDropTime() + host.game.getSoftDropInterval());
        }

         else if (gameTime >= host.game.getNextDropTime()) {
            if (!active.drop(board)) {

                host.game.clearLines(false, 0);
                host.game.pieceTransition(eventVibrationEnabled);
                board.invalidate();
            }

            if ((host.game.getLevel() < maxLevel) && (host.game.getClearedLines() > lineThresholds[Math.min(host.game.getLevel(), maxLevel - 1)])) {
                host.game.nextLevel();
            }

            host.game.setNextDropTime(host.game.getNextDropTime() + host.game.getAutoDropInterval());
            host.game.setNextPlayerDropTime(host.game.getNextDropTime());
        } else {
            host.game.setNextPlayerDropTime(gameTime);
        }
    }
}
