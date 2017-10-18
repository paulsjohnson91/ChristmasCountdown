package christmas.peej.com.christmascountdown;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by i-lau on 18/10/2017.
 */

public class CountdownWallpaper extends WallpaperService implements SensorEventListener {

    private final Handler mHandler = new Handler();
    private static final int NUM_SNOWFLAKES = 150;
    private static final int DELAY = 5;
    private List<SnowFlake> snowflakes;
    private int mSreenHeight;
    private int mScreenWidth;
    private boolean firstParticles = false;
    private boolean particles = false;
    private long particleStart = 0;
    private int currentToGoStat = 0;
    private long lastUpdate;
    private SensorManager sensorManager;

    @Override
    public void onCreate() {
        super.onCreate();
        registerSensors();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        snowflakes = new ArrayList<SnowFlake>();
        Context mContext = getBaseContext();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = mContext.getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;
        mSreenHeight = BitmapFactory.decodeResource(getResources(),R.drawable.christmastree).getHeight();
        for (int i = 0; i < NUM_SNOWFLAKES; i++) {
            snowflakes.add(i,SnowFlake.create(mScreenWidth, mSreenHeight, paint));
        }
    }
    public void registerSensors() {
        Log.d("Registering sensors", "registerSensors()");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new CubeEngine();
    }

    class CubeEngine extends Engine {

        private final Paint mPaint = new Paint();
        private float mOffset;
        private float mTouchX = -1;
        private float mTouchY = -1;
        private long mStartTime;
        private float mCenterX;
        private float mCenterY;

        private final Runnable mDrawCube = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        private boolean mVisible;

        CubeEngine() {
            // Create a Paint to draw the lines for our cube
            final Paint paint = mPaint;
            paint.setColor(0xffffffff);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);

            mStartTime = SystemClock.elapsedRealtime();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            // By default we don't get touch events, so enable them.
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawCube);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawCube);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            // store the center of the surface, so we can draw the cube in the
            // right spot
            mCenterX = width / 2.0f;
            mCenterY = height / 2.0f;
            drawFrame();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawCube);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
                                     float yStep, int xPixels, int yPixels) {
            mOffset = xOffset;
            drawFrame();
        }

        /*
         * Store the position of the touch event so we can use it for drawing
         * later
         */
        @Override
        public void onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mTouchX = event.getX();
                mTouchY = event.getY();
            } else {
                mTouchX = -1;
                mTouchY = -1;
            }
            super.onTouchEvent(event);
        }

        /*
         * Draw one frame of the animation. This method gets called repeatedly
         * by posting a delayed Runnable. You can do any drawing you want in
         * here. This example draws a wireframe cube.
         */
        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                Bitmap backgroundImage = BitmapFactory.decodeResource(getResources(),R.drawable.christmastree);
                //c.drawBitmap(backgroundImage, 0, 0, null);
                if (c != null) {
                    // draw something
                    Paint background = new Paint();
                    background.setAntiAlias(true);
                    background.setFilterBitmap(true);
                    background.setDither(true);

                    c.drawBitmap(backgroundImage, null, new RectF(0, 0, c.getWidth(), c.getHeight()), null);
                    //drawCube(c);
                    Rect bounds = new Rect();
                    LocalDateTime fromDate1 = new LocalDateTime(System.currentTimeMillis());
                    LocalDateTime newYear1 = new LocalDateTime(2017,12,25,0,0);
                    String formatted1 = "" + Days.daysBetween(fromDate1, newYear1);
                    String daysLeft = formatted1.substring(1,formatted1.length()-1) + " days";
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    // text color - #3D3D3D
                    // text size in pixels
                    Context mContext = getBaseContext();
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    displayMetrics = mContext.getResources().getDisplayMetrics();
                    int mScreenWidth = displayMetrics.widthPixels;
                    int mSreenHeight = displayMetrics.heightPixels;
                    paint.setTextSize(displayMetrics.density*40);
                    paint.setTypeface(Typeface.create("MONOSPACE", Typeface.NORMAL));
                    paint.setColor(Color.WHITE);
                    paint.getTextBounds(daysLeft, 0, daysLeft.length(), bounds);
                    int x = (backgroundImage.getWidth() - bounds.width())/2;
                    int y = 9*(backgroundImage.getHeight() + bounds.height())/10;

                    c.drawText(daysLeft, x, y, paint);
                    if(firstParticles) {
                        for (SnowFlake snowFlake : snowflakes) {
                            snowFlake.draw(c, mSreenHeight);
                        }
                    }



                    drawTouchPoint(c);
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mDrawCube);
            if (mVisible) {
                mHandler.postDelayed(mDrawCube, 1000 / 25);
            }
        }

        /*
         * Draw a wireframe cube by drawing 12 3 dimensional lines between
         * adjacent corners of the cube
         */
        void drawCube(Canvas c) {
            c.save();
            c.translate(mCenterX, mCenterY);
            c.drawColor(0xff000000);
            drawLine(c, -400, -400, -400, 400, -400, -400);
            drawLine(c, 400, -400, -400, 400, 400, -400);
            drawLine(c, 400, 400, -400, -400, 400, -400);
            drawLine(c, -400, 400, -400, -400, -400, -400);

            drawLine(c, -400, -400, 400, 400, -400, 400);
            drawLine(c, 400, -400, 400, 400, 400, 400);
            drawLine(c, 400, 400, 400, -400, 400, 400);
            drawLine(c, -400, 400, 400, -400, -400, 400);

            drawLine(c, -400, -400, 400, -400, -400, -400);
            drawLine(c, 400, -400, 400, 400, -400, -400);
            drawLine(c, 400, 400, 400, 400, 400, -400);
            drawLine(c, -400, 400, 400, -400, 400, -400);
            c.restore();
        }

        /*
         * Draw a 3 dimensional line on to the screen
         */
        void drawLine(Canvas c, int x1, int y1, int z1, int x2, int y2, int z2) {
            long now = SystemClock.elapsedRealtime();
            float xrot = ((float) (now - mStartTime)) / 1000;
            float yrot = (0.5f - mOffset) * 2.0f;
            float zrot = 0;

            // 3D transformations

            // rotation around X-axis
            float newy1 = (float) (Math.sin(xrot) * z1 + Math.cos(xrot) * y1);
            float newy2 = (float) (Math.sin(xrot) * z2 + Math.cos(xrot) * y2);
            float newz1 = (float) (Math.cos(xrot) * z1 - Math.sin(xrot) * y1);
            float newz2 = (float) (Math.cos(xrot) * z2 - Math.sin(xrot) * y2);

            // rotation around Y-axis
            float newx1 = (float) (Math.sin(yrot) * newz1 + Math.cos(yrot) * x1);
            float newx2 = (float) (Math.sin(yrot) * newz2 + Math.cos(yrot) * x2);
            newz1 = (float) (Math.cos(yrot) * newz1 - Math.sin(yrot) * x1);
            newz2 = (float) (Math.cos(yrot) * newz2 - Math.sin(yrot) * x2);

            // 3D-to-2D projection
            float startX = newx1 / (4 - newz1 / 400);
            float startY = newy1 / (4 - newz1 / 400);
            float stopX = newx2 / (4 - newz2 / 400);
            float stopY = newy2 / (4 - newz2 / 400);

            c.drawLine(startX, startY, stopX, stopY, mPaint);
        }

        /*
         * Draw a circle around the current touch point, if any.
         */
        void drawTouchPoint(Canvas c) {
            if (mTouchX >= 0 && mTouchY >= 0) {
                c.drawCircle(mTouchX, mTouchY, 80, mPaint);
            }
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }

    }


    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);


        long actualTime = System.currentTimeMillis();

        if(particles){
            if(actualTime-particleStart >10000){
                particles = false;
                for (int i = 0; i < NUM_SNOWFLAKES; i++) {
                    snowflakes.get(i).setReset(false);
                }
            }
        }
        //Log.d("a","a value : " + accelationSquareRoot + " act " + (actualTime - lastUpdate));
        if (accelationSquareRoot >= 2 && !particles) //
        {

            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            particleStart = actualTime;

//
//            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            paint.setColor(Color.WHITE);
//            paint.setStyle(Paint.Style.FILL);
//            snowflakes = new ArrayList<SnowFlake>();
//            Context mContext = getBaseContext();
//            DisplayMetrics displayMetrics = new DisplayMetrics();
//            displayMetrics = mContext.getResources().getDisplayMetrics();
//            int mScreenWidth = displayMetrics.widthPixels;
//            mSreenHeight =BitmapFactory.decodeResource(getResources(),R.drawable.christmastree).getHeight();
//            for (int i = 0; i < NUM_SNOWFLAKES; i++) {
//                snowflakes.add(i,SnowFlake.create(mScreenWidth, mSreenHeight, paint));
//            }
            for(SnowFlake s : snowflakes){
                if(!s.isInside(mScreenWidth,mSreenHeight)){
                    s.resetTop(mScreenWidth);
                }
            }


            particles = true;
            firstParticles = true;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }




}