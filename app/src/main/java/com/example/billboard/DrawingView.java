package com.example.billboard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class DrawingView extends View {
    private CustomPath mDrawPath;
    private Bitmap mCanvasBitmap;
    private Paint mDrawPaint;
    private Paint mCanvasPaint;
    private float mBrushSize;
    private int color;
    private Canvas canvas;
    private final ArrayList mPaths;
    private final ArrayList mUndo;
    private HashMap _$_findViewCache;

    private final void setupDrawing() {
        this.mDrawPaint = new Paint();
        this.mDrawPath = new CustomPath(this.color, this.mBrushSize);
        Paint var10000 = this.mDrawPaint;
        Intrinsics.checkNotNull(var10000);
        var10000.setColor(-16777216);
        var10000 = this.mDrawPaint;
        Intrinsics.checkNotNull(var10000);
        var10000.setStyle(Style.STROKE);
        var10000 = this.mDrawPaint;
        Intrinsics.checkNotNull(var10000);
        var10000.setStrokeJoin(Join.ROUND);
        var10000 = this.mDrawPaint;
        Intrinsics.checkNotNull(var10000);
        var10000.setStrokeCap(Cap.ROUND);
        this.mCanvasPaint = new Paint(4);
    }

    public final void clickOnUndo() {
        if (this.mPaths.size() > 0) {
            this.mUndo.add(this.mPaths.remove(this.mPaths.size() - 1));
            this.invalidate();
        }

    }

    public final void setColor(@NotNull String newColor) {
        Intrinsics.checkNotNullParameter(newColor, "newColor");
        this.color = Color.parseColor(newColor);
        Paint var10000 = this.mDrawPaint;
        Intrinsics.checkNotNull(var10000);
        var10000.setColor(this.color);
    }

    protected void onDraw(@NotNull Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        super.onDraw(canvas);
        Bitmap var10001 = this.mCanvasBitmap;
        Intrinsics.checkNotNull(var10001);
        canvas.drawBitmap(var10001, 0.0F, 0.0F, this.mCanvasPaint);
        Iterator var3 = this.mPaths.iterator();

        Paint var10000;
        Paint var10002;
        Path var5;
        while(var3.hasNext()) {
            CustomPath path = (CustomPath)var3.next();
            var10000 = this.mDrawPaint;
            Intrinsics.checkNotNull(var10000);
            var10000.setStrokeWidth(path.getBrushThickness());
            var10000 = this.mDrawPaint;
            Intrinsics.checkNotNull(var10000);
            var10000.setColor(path.getColour());
            var5 = (Path)path;
            var10002 = this.mDrawPaint;
            Intrinsics.checkNotNull(var10002);
            canvas.drawPath(var5, var10002);
        }

        CustomPath var4 = this.mDrawPath;
        Intrinsics.checkNotNull(var4);
        if (!var4.isEmpty()) {
            var10000 = this.mDrawPaint;
            Intrinsics.checkNotNull(var10000);
            CustomPath var6 = this.mDrawPath;
            Intrinsics.checkNotNull(var6);
            var10000.setStrokeWidth(var6.getBrushThickness());
            var10000 = this.mDrawPaint;
            Intrinsics.checkNotNull(var10000);
            var6 = this.mDrawPath;
            Intrinsics.checkNotNull(var6);
            var10000.setColor(var6.getColour());
            var6 = this.mDrawPath;
            Intrinsics.checkNotNull(var6);
            var5 = (Path)var6;
            var10002 = this.mDrawPaint;
            Intrinsics.checkNotNull(var10002);
            canvas.drawPath(var5, var10002);
        }

    }

    private ArrayList<Pair<Float, Float>> coordinatesList = new ArrayList<>();

    public boolean onTouchEvent(@Nullable MotionEvent event) {
        label49: {
            Float touchx = event != null ? event.getX() : null;
            Float touchy = event != null ? event.getY() : null;
//            Log.d("Cord", touchx.toString());
//            Log.d("Cord", touchy.toString());

//            Pair<Float, Float> coordinates = new Pair<>(touchx, touchy);
//            if (!coordinatesList.contains(coordinates)) {
//                coordinatesList.add(coordinates);
//            }
//            Log.d("abc", String.valueOf(coordinatesList));

            Integer var4 = event != null ? event.getAction() : null;
            boolean var5 = false;
            CustomPath var7;
            float var8;
            if (var4 != null) {
                if (var4 == 0) {
                    var7 = this.mDrawPath;
                    Intrinsics.checkNotNull(var7);
                    var7.setColour(this.color);
                    var7 = this.mDrawPath;
                    Intrinsics.checkNotNull(var7);
                    var7.setBrushThickness(this.mBrushSize);
                    var7 = this.mDrawPath;
                    Intrinsics.checkNotNull(var7);
                    Intrinsics.checkNotNull(touchx);
                    var8 = touchx;
                    Intrinsics.checkNotNull(touchy);
                    var7.reset();
                    var7.moveTo(var8, touchy);
                    break label49;
                }
            }

            byte var6 = 2;
            if (var4 != null) {
                if (var4 == var6) {
                    var7 = this.mDrawPath;
                    Intrinsics.checkNotNull(var7);
                    Intrinsics.checkNotNull(touchx);
                    var8 = touchx;
                    Intrinsics.checkNotNull(touchy);
                    var7.lineTo(var8, touchy);
                    break label49;
                }
            }

            var6 = 1;
            if (var4 == null) {
                return false;
            }

            if (var4 != var6) {
                return false;
            }

            ArrayList var10000 = this.mPaths;
            CustomPath var10001 = this.mDrawPath;
            Intrinsics.checkNotNull(var10001);
            var10000.add(var10001);
            this.mDrawPath = new CustomPath(this.color, this.mBrushSize);
        }

        this.invalidate();
        return true;
    }



    public final void setSizeForBrush(float newSize) {
        Resources var10003 = this.getResources();
        Intrinsics.checkNotNullExpressionValue(var10003, "resources");
        this.mBrushSize = TypedValue.applyDimension(1, newSize, var10003.getDisplayMetrics());
        Paint var10000 = this.mDrawPaint;
        Intrinsics.checkNotNull(var10000);
        var10000.setStrokeWidth(this.mBrushSize);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mCanvasBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Bitmap var10003 = this.mCanvasBitmap;
        Intrinsics.checkNotNull(var10003);
        this.canvas = new Canvas(var10003);
    }

    public DrawingView(@NotNull Context context, @NotNull AttributeSet attrs) {
        super(context, attrs);
        this.color = -16777216;
        this.mPaths = new ArrayList();
        this.mUndo = new ArrayList();
        this.setupDrawing();
    }

    public View _$_findCachedViewById(int var1) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }

        View var2 = (View)this._$_findViewCache.get(var1);
        if (var2 == null) {
            var2 = this.findViewById(var1);
            this._$_findViewCache.put(var1, var2);
        }

        return var2;
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }

    }

    public final class CustomPath extends Path {
        private int colour;
        private float brushThickness;

        public final int getColour() {
            return this.colour;
        }

        public final void setColour(int var1) {
            this.colour = var1;
        }

        public final float getBrushThickness() {
            return this.brushThickness;
        }

        public final void setBrushThickness(float var1) {
            this.brushThickness = var1;
        }

        public CustomPath(int colour, float brushThickness) {
            this.colour = colour;
            this.brushThickness = brushThickness;
        }
    }
}
