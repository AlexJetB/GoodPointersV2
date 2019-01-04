package com.baker.goodpointersv2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Baker baker.alex.j@gmail.com
 * Started 5 November 2018
 *  PointerVisualizer is a custom view class which helps "visualize" pointers
 * and nonPointer variables within C++ through the use of simple shapes and text.
 * pointer type variables - [color] Circle
 * non-pointer type variables - Stacked rectangles
 *                              upper = Address as [color] rect
 *                              lower = Value as [color] rect
 * Information for each is initialized in
 *       NonPointer.java
 *       Pointer.java
 *
 */
public class PointerVisualizer extends View {

    private float densityScalar; // Necessary for different device pixel densities
    private Paint valPaint;
    private Paint addPaint;
    private Paint circlePaint;
    private Paint textPaint;
    private Paint darkText;
    private Paint linePaint;

    // These two ArrayLists will hold our pointer and non-pointer variables from the parser
    List<Pointer> ptrList;
    List<NonPointer> nonPtrList;

    /**
     * This is the default constructor.
     * @param context Context of the application
     */
    public PointerVisualizer(Context context) {
        super(context);
        init();
    }

    /**
     * This is the non-default constructor.
     * @param context The context of the application.
     * @param attrs Context attributes
     */
    public PointerVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * This is the overriden onMeasure. It's typically a good idea
     * to override it within a custom view.
     * @param widthMeasureSpec The width constraint of the view
     * @param heightMeasureSpec The height constraint of the view
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    /**
     * All initialization for the view's canvas properties are.
     */
    private void init() {
        // Set the densityScalar
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        densityScalar = metrics.density;

        // Value paint
        valPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        valPaint.setColor(Color.parseColor("#3A5FCD"));
        valPaint.setStyle(Paint.Style.FILL);
        // Address paint
        addPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        addPaint.setColor(Color.parseColor("#DA4747"));
        addPaint.setStyle(Paint.Style.FILL);

        // Circle Paint Characteristics
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.parseColor("#DA4747"));

        // Text Paint Characteristics
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(densityScalar * 13);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // DarkText Paint Characteristics
        darkText = new Paint(Paint.ANTI_ALIAS_FLAG);
        darkText.setColor(Color.BLACK);
        darkText.setTextSize(densityScalar * 13);
        darkText.setTextAlign(Paint.Align.CENTER);

        // Line Paint Characteristics
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(densityScalar*2);

        // Initialize containers
        ptrList = new ArrayList<Pointer>();
        nonPtrList = new ArrayList<NonPointer>();
    }

    /**
     * This is the overriden onDraw to draw the pointer vectors and possible lines.
     * @param canvas Canvas of the view
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (ptrList.size() != 0) {
            drawPointers(canvas);
        }
        if (nonPtrList.size() != 0) {
            drawNonPointers(canvas);
        }
        this.invalidate();
    }

    /**
     * Called in onDraw as a way to draw pointers from the list, as well as the lines connecting
     * them to what they're pointing.
     * @param canvas Canvas of the view
     */
    private void drawPointers(Canvas canvas) {
        // This is only testing information for the variables below.

        int ptrSpread = getWidth() / (ptrList.size() + 1);
        int nonPtrSpread = getWidth() / (nonPtrList.size() + 1);
        // Draw Pointers from Vector
        for (int i = 0; i < ptrList.size(); i++) {
            drawPointer(canvas, (i + 1) * ptrSpread,
                    ptrList.get(i).getToDisplay(), ptrList.get(i).getName());
            // Draw a line given the pointer is pointing
            if (ptrList.get(i).isPointing()) {
                int j = findNonPtrByAddress(ptrList.get(i).getpAddress());
                drawArrow(canvas, (i + 1) * ptrSpread, (j + 1) * nonPtrSpread);
            }
        }

    }

    /**
     * Called in onDraw to draw all nonPointer objects from their respective list
     * @param canvas Canvas of the view.
     */
    private void drawNonPointers(Canvas canvas) {
        int nonPtrSpread = getWidth() / (nonPtrList.size() + 1);
        // Draw NonPointers from Vector
        for (int j = 0; j < nonPtrList.size(); j++) {
            drawNonPointer(canvas, (j + 1) * nonPtrSpread,
                    nonPtrList.get(j).getValue(),
                    nonPtrList.get(j).getAddress(),
                    nonPtrList.get(j).getName());
        }
    }

    /**
     * drawNonPointer creates a visualization of a Cpp nonPointer variable using two rectangles,
     * with the top containing the value, the bottom the address, and the name below.
     *
     * @param canvas Canvas to pass in
     * @param x The location of the object from the left of the screen.
     * @param value The value of the nonPointer.
     * @param address The address of the nonPointer.
     * @param name The name of the nonPointer.
     */
    private void drawNonPointer(Canvas canvas, int x, String value, String address, String name) {
        int cornerRadius = 15;
        int top = 3 * getHeight() / 4;
        float width = densityScalar * 40; //Keep in mind that we'll never truly use our width to draw the rectangle.
        float height = width / 2;

        // Set up our rectangles
        RectF addrR = new RectF(
                x - height,
                top,
                x + height,
                top + height
        );
        RectF valuR = new RectF(
                x - height,
                top + height,
                x + height,
                top + height*2
        );

        // Draw rounded rectangles
        canvas.drawRoundRect(
                addrR,
                cornerRadius,
                cornerRadius,
                addPaint
        );
        canvas.drawRoundRect(
                valuR,
                cornerRadius,
                cornerRadius,
                valPaint
        );

        // Draw our text from our strings. Probably need to calculate an offset.
        Log.d("Pointer Visualizer", value + " is the current value.");
        canvas.drawText(value, x, top + densityScalar * 18, textPaint);
        canvas.drawText(address, x, top + densityScalar * 37, textPaint);
        canvas.drawText(name, x, top + densityScalar * 50, darkText);
    }

    /*
        Might want to consider putting a bool here to determine if it's dereferenced
     */
    /**
     * drawPointer places a visualization of a Cpp Pointer variable, which is a round circle
     * with either the address of the object its pointing to, or the value (given it's dereferenced)
     * @param canvas Canvas to pass in.
     * @param x The location of the drawn object from the left of the screen.
     * @param pAddress The address of the variable it is pointing to. Can be changed to value if
     *                 dereferenced.
     */
    private void drawPointer(Canvas canvas, int x, String pAddress, String name) {
        int y = getHeight() / 4;
        float radius = densityScalar * 20;

        canvas.drawCircle(x, y, radius, circlePaint);
        canvas.drawText(pAddress, x, y + densityScalar * 5, textPaint);
        canvas.drawText(name, x, y - densityScalar * 25, darkText);
    }

    /**
     * Function will draw a line with an arrowHead to convey what nonPointer a Pointer variable
     * is pointing to.
     * @param canvas Canvas objecto be passed in.
     * @param startX The start of the line from the top pointer object.
     * @param stopX The end of the line from the top pointer object.
     */
    private void drawArrow(Canvas canvas, float startX, float stopX) {
        float startY = getHeight() / 4 + densityScalar * 20;
        float stopY = 3 * getHeight() / 4;
        float radius = densityScalar * 5;

        canvas.drawLine(startX, startY, stopX, stopY, linePaint);
        canvas.drawCircle(stopX, stopY, radius, linePaint);
    }

    /**
     * May be unnecessary. Consider testing removal, but not needed.
     * @param nonPtrAddress The address of the nonPtr.
     * @return The location of value within list array
     */
    public int findNonPtrByAddress(String nonPtrAddress) {
        int indexPlace = -1;

        for(int i = 0; i < nonPtrList.size(); i++) {
            if (nonPtrList.get(i).getAddress().equals(nonPtrAddress)) {
                indexPlace = i;
            }
        }

        return indexPlace;
    }
}
