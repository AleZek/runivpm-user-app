package com.ids.idsuserapp.percorso.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import com.ids.idsuserapp.R;
import com.ids.idsuserapp.percorso.Checkpoint;

import java.util.ArrayList;

public class PinView extends SubsamplingScaleImageView {
    private static final String TAG = PinView.class.getName();
    private MapPin singlePin = null;
    private ArrayList<MapPin> multiplePins;
    private ArrayList<DrawPin> drawnPins;
    private Context context;
    private float density;
    private Paint paint;
    private ArrayList<PointF> path;

    /**
     * Constructor
     *
     * @param context
     */
    public PinView(Context context) {
        this(context, null);
        this.context = context;

        initDrawing();
    }

    /**
     * Constructor
     *
     * @param context
     * @param attr
     */
    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        initialise();

        initDrawing();
    }

    /**
     * Initialize drawing tools
     */
    private void initDrawing() {
        density = getResources().getDisplayMetrics().densityDpi;
        drawnPins = new ArrayList<>();
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    private void initialise() {
    }

    public void setImage(Bitmap image) {
        setImage(ImageSource.bitmap(image));
    }

    /**
     * Get the single pin
     *
     * @return
     */
    public MapPin getSinglePin() {
        return singlePin;
    }

    /**
     * Set a single pin
     *
     * @param pin
     */
    public void setSinglePin(MapPin pin) {
        resetPins();
        this.singlePin = pin;
        initialise();
        invalidate();
    }

    /**
     * Get multiple pins
     *
     * @return
     */
    public ArrayList<MapPin> getMultiplePins() {
        return multiplePins;
    }

    /**
     * Set an array of pins
     *
     * @param mapPins
     */
    public void setMultiplePins(ArrayList<MapPin> mapPins) {
        resetPins();
        this.multiplePins = mapPins;
        initialise();
        invalidate();
    }

    public void setPath(com.ids.idsuserapp.percorso.Path path) {
        ArrayList<PointF> points = new ArrayList<>(path.size());
        for (Checkpoint node : path) {
            points.add(new PointF(node.getX(), node.getY()));
        }
        this.path = points;
        initialise();
        invalidate();
    }

    public void resetPath() {
        this.path = null;
        initialise();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during       setup.
        if (!isReady()) {
            return;
        }

        // The path has to be drawn before the pin, unless it will cover the pin
        if (path != null) {
            drawPath(canvas, path);
        }

        if (singlePin == null && multiplePins != null) {
            for (MapPin pin : multiplePins) {
                drawPin(canvas, pin);
            }
        } else if (singlePin != null) {
            drawPin(canvas, singlePin);
        }
    }

    private void drawPath(Canvas canvas, ArrayList<PointF> points) {
        int strokeWidth = (int) (density / 35f);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        // Convert absolute coordinates to coordinates relative to image
        ArrayList<PointF> sPoints = new ArrayList<>();
        for (PointF point : points) {
            sPoints.add(sourceToViewCoord(point));
        }

        Path path = new Path();
        boolean first = true;
        for (PointF point : sPoints) {
            if (first) {
                first = false;
                path.moveTo(point.x, point.y);
            } else {
                path.lineTo(point.x, point.y);
            }
        }

        paint.setStrokeWidth(strokeWidth * 2);
        paint.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        canvas.drawPath(path, paint);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        canvas.drawPath(path, paint);
    }

    private void drawPin(Canvas canvas, MapPin pin) {
        //Bitmap bmpPin = Utils.getBitmapFromAsset(context, mPin.getPinImgSrc());
        int markerResource = R.drawable.marker_icon_google;
        if (pin.getColor() != null) {
            switch (pin.getColor()) {
                case RED: {
                    markerResource = R.drawable.marker_icon_google;
                    break;
                }
                case BLUE: {
                    markerResource = R.drawable.marker_icon_google_blue;
                    break;
                }
                case LOCATION: {
                    markerResource = R.drawable.position_placer;
                    break;
                }
                default: {
                    markerResource = R.drawable.marker_icon_google;
                }
            }
        }

        Bitmap bmpPin = BitmapFactory.decodeResource(this.getResources(), markerResource);

        float w = (density / 1200f) * bmpPin.getWidth();
        float h = (density / 1200f) * bmpPin.getHeight();
        bmpPin = Bitmap.createScaledBitmap(bmpPin, (int) w, (int) h, true);

        PointF vPin = sourceToViewCoord(pin.getPoint());
        //in my case value of point are at center point of pin image, so we need to adjust it here

        float vX = vPin.x - (bmpPin.getWidth() / 2);
        float vY = vPin.y - bmpPin.getHeight();

        canvas.drawBitmap(bmpPin, vX, vY, paint);

        //add added pin to an Array list to get touched pin
        DrawPin dPin = new DrawPin();
        dPin.setStartX(pin.getX() - w / 2);
        dPin.setEndX(pin.getX() + w / 2);
        dPin.setStartY(pin.getY() - h / 2);
        dPin.setEndY(pin.getY() + h / 2);
        dPin.setId(pin.getId());
        drawnPins.add(dPin);
    }

    public int getPinIdByPoint(PointF point) {

        for (int i = drawnPins.size() - 1; i >= 0; i--) {
            DrawPin dPin = drawnPins.get(i);
            if (point.x >= dPin.getStartX() && point.x <= dPin.getEndX()) {
                if (point.y >= dPin.getStartY() && point.y <= dPin.getEndY()) {
                    return dPin.getId();
                }
            }
        }
        return -1; //negative no means no pin selected
    }

    public void resetPins() {
        singlePin = null;
        multiplePins = null;
    }
}
