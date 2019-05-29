package components;

import android.R.color;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import com.example.tetris.GameActivity;
import com.example.tetris.R;


public class Display extends Component
{
    private int prevPhantomY;
    private boolean dropPhantom;
    private Paint paint;
    private int gridRowBorder;
    private int gridColumnBorder;
    private int squaresize;
    private int rowOffset;
    private int rows;
    private int columnOffset;
    private int columns;
    private boolean landscapeInitialized;
    private int prevTop;
    private int prevBottom;
    private int prevLeft;
    private int prevRight;
    private int textLeft;
    private int textTop;
    private int textRight;
    private int textBottom;
    private int textLines;
    private int textSizeH;
    private int textEmptySpacing;
    private Paint textPaint;
    private Rect textRect;
    private int textHeight;
    private Paint popupTextPaint;

    public Display(GameActivity gameActivity)
    {
        super(gameActivity);

        invalidatePhantom();
        prevPhantomY = 0;
        landscapeInitialized = false;
        paint = new Paint();
        rows = host.getResources().getInteger(R.integer.rows);
        columns = 9;

        squaresize = 1; // Unknown at this point
        prevTop = 1; // Unknown at this point
        prevBottom = 1; // Unknown at this point
        prevLeft = 1; // Unknown at this point
        prevRight = 1; // Unknown at this point

        rowOffset = host.getResources().getInteger(R.integer.rows_offset);
        columnOffset = host.getResources().getInteger(R.integer.columns_offset);

        textPaint = new Paint();
        textRect = new Rect();
        textPaint.setColor(host.getResources().getColor(color.white));
        textPaint.setAlpha(host.getResources().getInteger(R.integer.text_alpha));
        textPaint.setAntiAlias(true);
        popupTextPaint = new Paint();
        popupTextPaint.setColor(host.getResources().getColor(color.white));
        popupTextPaint.setAntiAlias(true);
        popupTextPaint.setTextSize(120);
        textSizeH = 1;
        textHeight = 2;

        if (PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_show_fps", true)) {
            textLines = 10;
        } else {
            textLines = 8;
        }
    }

    public void doDraw(Canvas canvas, int fps)
    {
        if (canvas == null) {
            return;
        }

        if (!landscapeInitialized) {
            int fpsenabled = 0;

            if (PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_show_fps", true)) {
                fpsenabled = 1;
            }

            host.game.getBoard().invalidate();
            landscapeInitialized = true;
            squaresize = (((canvas.getHeight() - 1) - 2 * rowOffset) / rows);
            int size2 = (((canvas.getHeight() - 1) - 2 * columnOffset) / (columns + 4 + host.getResources()
                .getInteger(R.integer.padding_columns)));

            if (size2 < squaresize) {
                squaresize = size2;
                rowOffset = (((canvas.getHeight() - 1) - squaresize * rows) / 2);
            } else {
                columnOffset = (((canvas.getWidth() - 1) - squaresize * (host.getResources()
                    .getInteger(R.integer.padding_columns) + 4 + columns)) / 2);
            }

            gridRowBorder = rowOffset + squaresize * rows ;
            gridColumnBorder = columnOffset + squaresize * columns;
            prevTop = rowOffset;
            prevBottom = rowOffset + 4 * squaresize;
            prevLeft = gridColumnBorder + host.getResources()
                .getInteger(R.integer.padding_columns) * squaresize;
            prevRight = prevLeft + 4 * squaresize;
            textLeft = prevLeft;
            textTop = prevBottom + 2 * squaresize;
            textRight = (canvas.getWidth() - 1) - columnOffset;
            textBottom = (canvas.getHeight() - 1) - rowOffset - squaresize;
            textSizeH = 1;

            // Adaptive text size setup
            textPaint.setTextSize(textSizeH + 1);

            while (textPaint.measureText("00:00:00") < (textRight - textLeft)) {
                textPaint.getTextBounds("Level:32", 0, 6, textRect);
                textHeight = textRect.height();
                textEmptySpacing = ((textBottom - textTop) - (textLines * (textHeight + 3))) / (3 + fpsenabled);

                if (textEmptySpacing < 10) {
                    break;
                }

                textSizeH++;
                textPaint.setTextSize(textSizeH + 1);
            }

            textPaint.setTextSize(textSizeH);
            textPaint.getTextBounds("Level:32", 0, 6, textRect);
            textHeight = textRect.height() + 3;
            textEmptySpacing = ((textBottom - textTop) - (textLines * (textHeight))) / (3 + fpsenabled);
        }

        // Background
        canvas.drawColor(Color.argb(0, 0, 0, 0), android.graphics.PorterDuff.Mode.CLEAR);

        host.game.getBoard().draw(columnOffset, rowOffset, squaresize, canvas);

        drawActive(columnOffset, rowOffset, squaresize, canvas);

        drawGrid(columnOffset, rowOffset, gridColumnBorder, gridRowBorder, canvas);

    }

    private void drawGrid(int x, int y, int xBorder, int yBorder, Canvas canvas)
    {
        paint.setColor(host.getResources().getColor(color.holo_blue_dark));

        for (int zeilePixel = 0; zeilePixel <= rows; zeilePixel++) {
            canvas.drawLine(x + 150 , y + zeilePixel * squaresize, xBorder+150, y + zeilePixel * squaresize, paint);
        }

        for (int spaltePixel = 0; spaltePixel <= columns; spaltePixel++) {
            canvas.drawLine(x + 150 + spaltePixel * squaresize, y, x +150+ spaltePixel * squaresize, yBorder, paint);
        }

        // Draw border
        paint.setColor(host.getResources().getColor(color.black));
        canvas.drawLine(x + 150, y, x+150, yBorder, paint);
        canvas.drawLine(x+150, y, xBorder+150, y, paint);
        canvas.drawLine(xBorder+150, yBorder, xBorder+150, y, paint);
        canvas.drawLine(xBorder+150, yBorder, x+150, yBorder, paint);
    }


    private void drawActive(int spaltenOffset, int zeilenOffset, int spaltenAbstand, Canvas canvas)
    {
        host.game.getActivePiece().drawOnBoard(spaltenOffset+150, zeilenOffset, spaltenAbstand, canvas);
    }






    void invalidatePhantom()
    {
        dropPhantom = true;
    }
}
