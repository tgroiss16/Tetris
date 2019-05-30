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
    private Vibrator vibrator;
    private int vibrationDuration;
    private long shortVibrationTime;
    private int[] lineThresholds;

    // Player Controls
    private boolean playerSoftDrop;
    private boolean clearPlayerSoftDrop;
    private boolean playerHardDrop;
    private boolean leftMove;
    private boolean rightMove;
    private boolean continuousSoftDrop;
    private boolean continuousLeftMove;
    private boolean continuousRightMove;
    private boolean clearLeftMove;
    private boolean clearRightMove;
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
        shortVibrationTime = 0;

        vibrator = (Vibrator) host.getSystemService(Context.VIBRATOR_SERVICE);

        buttonVibrationEnabled = PreferenceManager.getDefaultSharedPreferences(host)
            .getBoolean("pref_vibration_button", false);
        eventVibrationEnabled = PreferenceManager.getDefaultSharedPreferences(host)
            .getBoolean("pref_vibration_events", false);

        try {
            vibrationDuration = Integer.parseInt(
                Objects.requireNonNull(PreferenceManager.getDefaultSharedPreferences(host)
                    .getString("pref_button_vibration_duration", "0"))
            );
        } catch (NumberFormatException e) {
            vibrationDuration = 0;
        }

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
        clearLeftMove = false;
        clearRightMove = false;
        clearPlayerSoftDrop = false;
        continuousSoftDrop = false;
        continuousLeftMove = false;
        continuousRightMove = false;
    }

    @SuppressLint("MissingPermission")
    private void vibrateWall()
    {
        if (vibrator == null) {
            return;
        }

        if (!eventVibrationEnabled) {
            return;
        }

        if (((AudioManager) host.getSystemService(Context.AUDIO_SERVICE)).getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
            return;
        }

        vibrator.vibrate(host.game.getMoveInterval() + vibrationDuration);
    }



    private void vibrateBottom()
    {
        if (vibrator == null) {
            return;
        }

        if (!eventVibrationEnabled) {
            return;
        }

        if (((AudioManager) host.getSystemService(Context.AUDIO_SERVICE)).getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
            return;
        }


    }

    private void vibrateShort()
    {
        if (vibrator == null) {
            return;
        }

        if (!buttonVibrationEnabled) {
            return;
        }

        if (((AudioManager) host.getSystemService(Context.AUDIO_SERVICE)).getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
            return;
        }

        if ((host.game.getTime() - shortVibrationTime) > (host.getResources().getInteger(R.integer.short_vibration_interval) + vibrationDuration)) {
            shortVibrationTime = host.game.getTime();
        }
    }

    public void rotateLeftPressed()
    {
        leftRotation = true;
        host.game.action();
        vibrateShort();

    }

    public void rotateRightPressed()
    {
        rightRotation = true;
        host.game.action();
        vibrateShort();
    }

    public void downButtonReleased()
    {
        clearPlayerSoftDrop = true;
        vibrateShort();
    }

    public void downButtonPressed()
    {
        host.game.action();
        playerSoftDrop = true;


        host.game.setNextPlayerDropTime(host.game.getTime());

    }

    public void dropButtonPressed()
    {
        if (!host.game.getActivePiece().isActive()) {
            return;
        }

        host.game.action();
        playerHardDrop = true;

        if (buttonVibrationEnabled & !eventVibrationEnabled) {
            vibrateShort();
        }
    }



    public void leftButtonPressed()
    {
        host.game.action();
        clearLeftMove = false;
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

    public void cycle()
    {
        long gameTime = host.game.getTime();
        Piece active = host.game.getActivePiece();
        Board board = host.game.getBoard();
        int maxLevel = host.game.getMaxLevel();

        // Left rotation
        if (leftRotation) {
            leftRotation = false;
            active.turnLeft(board);
            host.display.invalidatePhantom();
        }

        // Right rotation
        if (rightRotation) {
            rightRotation = false;
            active.turnRight(board);
            host.display.invalidatePhantom();
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
                // Failed move
                vibrateWall();
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
            vibrateBottom();
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
            // Initial Soft Drop
            playerSoftDrop = false;
            continuousSoftDrop = false;

            if (!active.drop(board)) {
                // Piece finished
                vibrateBottom();
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
        } else if (continuousSoftDrop) {
            if (gameTime >= host.game.getNextPlayerDropTime()) {
                if (!active.drop(board)) {
                    // piece finished
                    vibrateBottom();
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
                host.game.setNextPlayerDropTime(host.game.getNextPlayerDropTime() + host.game.getSoftDropInterval());

            } else if (gameTime >= host.game.getNextDropTime()) {
                // Autodrop if faster than playerDrop
                if (!active.drop(board)) {
                    // Piece finished
                    vibrateBottom();
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

            // Cancel continuous SoftDrop
            if (clearPlayerSoftDrop) {
                continuousSoftDrop = false;
                clearPlayerSoftDrop = false;
            }

        } else if (gameTime >= host.game.getNextDropTime()) {
            // Autodrop if no playerDrop
            if (!active.drop(board)) {
                // Piece finished
                vibrateBottom();
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
