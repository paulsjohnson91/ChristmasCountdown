package christmas.peej.com.christmascountdown;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.plattysoft.leonids.ParticleSystem;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private static final int SHAKE_THRESHOLD = 800;
    private long lastUpdate;
    private boolean color = false;
    private View view;
    private float x,y,z;
    private float last_x = 0;
    private float last_y = 0;
    private float last_z = 0;
    private ParticleSystem ps;
    private boolean particles = false;
    private long particleStart = 0;
    private int currentToGoStat = 0;
    private float x1,x2;
    static final int MIN_DISTANCE = 600;
    private String daysLeft = "";
    private String hoursLeft = "";
    private String minutesLeft = "";
    private String secondsLeft = "";
    private int backgroundInt = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        setContentView(R.layout.activity_fullscreen);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        Toast.makeText(this,"Swipe for hours, minutes and seconds",Toast.LENGTH_SHORT).show();
        countdown();
    }


    public void countdown(){
        final LocalDate fromDate = new LocalDate(System.currentTimeMillis());
        LocalDate newYear = new LocalDate(2017,12,25);
        String formatted = "" + Days.daysBetween(fromDate, newYear);


        final TextView tv_countdown = (TextView) findViewById(R.id.fullscreen_content);
//        int hoursToGo = 0;
//        int minutesToGo = 0;
//        int secondsToGo = 30;
//
//        int millisToGo = secondsToGo*1000+minutesToGo*1000*60+hoursToGo*1000*60*60;
        final String secondsleft = ("" + Seconds.secondsBetween(fromDate,newYear)).substring(2,("" + Seconds.secondsBetween(fromDate,newYear)).length()-1);
        int secondsRemaining = Integer.parseInt(("" + Seconds.secondsBetween(fromDate,newYear)).substring(2,("" + Seconds.secondsBetween(fromDate,newYear)).length()-1));
        new CountDownTimer(Integer.parseInt(("" + Seconds.secondsBetween(fromDate,newYear)).substring(2,("" + Seconds.secondsBetween(fromDate,newYear)).length()-1))*1000,1000) {

            @Override
            public void onTick(long millis) {
                LocalDateTime fromDate1 = new LocalDateTime(System.currentTimeMillis());
                LocalDateTime newYear1 = new LocalDateTime(Integer.parseInt(DateTime.now().year().getAsString()),12,25,0,0);
                int daysDaysLeft = Days.daysBetween(fromDate1, newYear1).getDays();
                int daysHoursLeft = Hours.hoursBetween(fromDate1, newYear1).getHours()%24;
                int daysMinutesLeft  = Minutes.minutesBetween(fromDate1, newYear1).getMinutes()%60;
                int daysSecondsLeft = Seconds.secondsBetween(fromDate1,newYear1).getSeconds()%60;
                String output = "";


                daysLeft = daysDaysLeft + " days";
                String daysHoursMinutes = "";
                String daysSeconds = "";
                if(daysHoursLeft==1){
                    daysHoursMinutes += daysHoursLeft + " hour, ";
                }
                else{
                    daysHoursMinutes += daysHoursLeft + " hours, ";
                }
                if(daysMinutesLeft==1){
                    daysHoursMinutes += daysMinutesLeft + " minute";
                }
                else{
                    daysHoursMinutes += daysMinutesLeft + " minutes";
                }
                if(daysSecondsLeft ==1){
                    daysSeconds += daysSecondsLeft + " second";
                }
                else{
                    daysSeconds += daysSecondsLeft + " seconds";
                }

                hoursLeft = Hours.hoursBetween(fromDate1, newYear1).getHours() + " hours";

                minutesLeft = Minutes.minutesBetween(fromDate1, newYear1).getMinutes() + " minutes";
                secondsLeft = Seconds.secondsBetween(fromDate1, newYear1).getSeconds() + " seconds";
                switch (Math.abs(currentToGoStat)%4){
                    case 0:
                        if(daysHoursLeft==1){
                            daysHoursMinutes = daysHoursLeft + " hour, ";
                        }
                        else{
                            daysHoursMinutes = daysHoursLeft + " hours, ";
                        }
                        if(daysMinutesLeft==1){
                            daysHoursMinutes += daysMinutesLeft + " minute";
                        }
                        else{
                            daysHoursMinutes += daysMinutesLeft + " minutes";
                        }
                        if(daysSecondsLeft ==1){
                            daysSeconds = daysSecondsLeft + " second";
                        }
                        else{
                            daysSeconds = daysSecondsLeft + " seconds";
                        }
                        tv_countdown.setText(daysLeft);
                        ((TextView)findViewById(R.id.hoursMinutesText)).setText(daysHoursMinutes);
                        ((TextView)findViewById(R.id.secondsText)).setText(daysSeconds);
                        break;
                    case 1:
                        if(daysMinutesLeft==1){
                            daysHoursMinutes = daysMinutesLeft + " minute";
                        }
                        else{
                            daysHoursMinutes = daysMinutesLeft + " minutes";
                        }
                        if(daysSecondsLeft ==1){
                            daysSeconds = daysSecondsLeft + " second";
                        }
                        else{
                            daysSeconds = daysSecondsLeft + " seconds";
                        }
                        tv_countdown.setText(hoursLeft);
                        ((TextView)findViewById(R.id.hoursMinutesText)).setText(daysHoursMinutes);
                        ((TextView)findViewById(R.id.secondsText)).setText(daysSeconds);
                        break;
                    case 2:
                        if(daysSecondsLeft ==1){
                            daysHoursMinutes = daysSecondsLeft + " second";
                        }
                        else{
                            daysHoursMinutes = daysSecondsLeft + " seconds";
                        }
                        daysSeconds = "";
                        tv_countdown.setText(minutesLeft);
                        ((TextView)findViewById(R.id.hoursMinutesText)).setText(daysHoursMinutes);
                        ((TextView)findViewById(R.id.secondsText)).setText(daysSeconds);
                        break;
                    case 3:
                        daysSeconds = "";
                        daysHoursMinutes = "";
                        tv_countdown.setText(secondsLeft);
                        ((TextView)findViewById(R.id.hoursMinutesText)).setText(daysHoursMinutes);
                        ((TextView)findViewById(R.id.secondsText)).setText(daysSeconds);
                        //output = newYear1.
                        break;
                    default:
                        output = daysLeft;
                        break;
                }

                //tv_countdown.setText(output);
                //((TextView)findViewById(R.id.toGoText)).setText("To Go!");
            }

            @Override
            public void onFinish() {
                tv_countdown.setText("Merry Christmas!");
            }
        }.start();
    }

    public void setWallpaper(View view){
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(this, CountdownWallpaper.class));
        startActivity(intent);
    }

    public void seeFact(View view){
        Intent intent = new Intent(this, TransparentFact.class);
        startActivity(intent);
        //Toast.makeText(this,"Test",Toast.LENGTH_SHORT).show();
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event){
//        //Log.d("Motion", "event");
////        Toast.makeText(this, "Device was shuffed", Toast.LENGTH_SHORT)
////                .show();
//        int action = MotionEventCompat.getActionMasked(event);
//        String DEBUG_TAG = "swipeevent";
//        switch(action) {
//            case (MotionEvent.ACTION_DOWN) :
//                x1 = event.getX();
//                //Log.d(DEBUG_TAG,"Action was DOWN");
//                return true;
//            case (MotionEvent.ACTION_MOVE) :
//                //Log.d(DEBUG_TAG,"Action was MOVE");
//                return true;
//            case (MotionEvent.ACTION_UP) :
//                x2 = event.getX();
//                float deltaX = x2 - x1;
//                if (Math.abs(deltaX) > MIN_DISTANCE)
//                {
//                    if(deltaX>0){
//                        currentToGoStat ++;
//                    }
//                    else{
//                        currentToGoStat --;
//                    }
//                    String output = "";
//                    switch (Math.abs(currentToGoStat)%4){
//                        case 0:
//                            output = daysLeft;
//                            break;
//                        case 1:
//                            output = hoursLeft;
//                            break;
//                        case 2:
//                            output = minutesLeft;
//                            break;
//                        case 3:
//                            output = secondsLeft;
//                            break;
//                        default:
//                            output = daysLeft;
//                            break;
//                    }
//                    final TextView tv_countdown = (TextView) findViewById(R.id.fullscreen_content);
//                    tv_countdown.setText(output);
//
//                }
//                else
//                {
//                    // consider as something else - a screen tap for example
//                }
//                //Log.d(DEBUG_TAG,"Action was UP");
//                return true;
//            case (MotionEvent.ACTION_CANCEL) :
//                //Log.d(DEBUG_TAG,"Action was CANCEL");
//                return true;
//
//            case (MotionEvent.ACTION_OUTSIDE) :
//                //Log.d(DEBUG_TAG,"Movement occurred outside bounds " +
//                  //      "of current screen element");
//                return true;
//            default :
//                return super.onTouchEvent(event);
//        }
//    }



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
                ps.stopEmitting();
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
            particles = true;
//            Toast.makeText(this, "Device was shuffed", Toast.LENGTH_SHORT)
//                    .show();


            ps = new ParticleSystem(this, 100, R.drawable.snowflake, 3000);
            ps.setAcceleration(0.00013f, 90)
                    .setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f)
                    .setFadeOut(200, new AccelerateInterpolator())
                    .emitWithGravity(findViewById(R.id.topofscreen), Gravity.BOTTOM, 30);


//            if (color) {
//                view.setBackgroundColor(Color.GREEN);
//            } else {
//                view.setBackgroundColor(Color.RED);
//            }
//            color = !color;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void changeTimeType(View view){
        currentToGoStat++;
    }

    public void changeBackground(View view){
        backgroundInt++;
        ConstraintLayout layout =(ConstraintLayout) findViewById(R.id.mainlayout);
        ImageView mainImage = (ImageView) findViewById(R.id.treeimage);
        TextView tv = (TextView) findViewById(R.id.fullscreen_content);
        TextView tv1 = (TextView) findViewById(R.id.secondsText);
        TextView tv2 = (TextView) findViewById(R.id.hoursMinutesText);
        switch(backgroundInt%2){
            case 0:
                mainImage.setImageDrawable(getDrawable(R.drawable.treeonly));
                layout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.backgroundcolour));
                tv.setTextColor(Color.WHITE);
                tv1.setTextColor(Color.WHITE);
                tv2.setTextColor(Color.WHITE);
                break;
            case 1:
                mainImage.setImageDrawable(getDrawable(R.drawable.snowman));
                layout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.backgroundsnowman));
                tv.setTextColor(Color.BLACK);
                tv1.setTextColor(Color.BLACK);
                tv2.setTextColor(Color.BLACK);
                break;
        }
    }

}
