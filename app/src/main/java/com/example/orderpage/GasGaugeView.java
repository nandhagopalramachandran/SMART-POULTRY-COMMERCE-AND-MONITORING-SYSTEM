package com.example.orderpage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class GasGaugeView extends View {

    private static final int DEFAULT_WIDTH = 200; // Default width of the gauge
    private static final int DEFAULT_HEIGHT = 200; // Default height of the gauge
    private static final int MAX_VALUE = 100; // Maximum value of the gas sensor
    private static final int START_ANGLE = 135; // Start angle of the arc
    private static final int SWEEP_ANGLE = 270; // Sweep angle of the arc
    private static final int STROKE_WIDTH = 20; // Width of the arc

    private int gasValue = 0; // Current value of the gas sensor
    private Paint arcPaint; // Paint object for drawing the arc
    private Paint textPaint; // Paint object for drawing the text
    private String indicatorText = "";
    private RectF arcRect; // RectF object defining the bounds of the arc
    private int arcColor; // Color of the arc

    public GasGaugeView(Context context) {
        super(context);
        init();
    }

    public GasGaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GasGaugeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GasGaugeView(Context context, int arcColor) {
        super(context);
        this.arcColor = arcColor;
        init();
    }

    public void setArcColor(int color) {
        this.arcColor = color;
        arcPaint.setColor(color);
        invalidate();
    }

    private void init() {
        // Initialize Paint object for drawing the arc
        arcPaint = new Paint();
        arcPaint.setColor(arcColor);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(STROKE_WIDTH);
        arcPaint.setAntiAlias(true);

        // Initialize Paint object for drawing the text
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(70); // Text size
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);// Text alignment

        // Initialize RectF object defining the bounds of the arc
        arcRect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Calculate the bounds of the arc based on the view's width and height
        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height);
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = size / 2 - STROKE_WIDTH / 2;
        arcRect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        // Draw the arc representing the gas sensor value
        float sweepAngle = ((float) gasValue / MAX_VALUE) * SWEEP_ANGLE;
        canvas.drawArc(arcRect, START_ANGLE, sweepAngle, false, arcPaint);

        // Draw the value text (percentage) at the center of the gauge
        String percentageText = String.valueOf((int) ((float) gasValue / MAX_VALUE * 100)) + "%";
        canvas.drawText(percentageText, centerX, centerY, textPaint);

        // Calculate the position to draw the indicator text
        float textWidth = textPaint.measureText(indicatorText);
        float x = centerX;
        float y = centerY + textPaint.getTextSize() * 1.5f; // Draw below the value text

        // Draw the indicator text below the percentage
        canvas.drawText(indicatorText, x, y, textPaint);
    }

    // Method to set the gas sensor value
    public void setGasValue(int value) {
        if (value >= 0 && value <= MAX_VALUE) {
            this.gasValue = value;
            invalidate(); // Redraw the view
        }
    }

    public void setIndicatorText(String text) {
        this.indicatorText = text;
        invalidate();
    }

}
