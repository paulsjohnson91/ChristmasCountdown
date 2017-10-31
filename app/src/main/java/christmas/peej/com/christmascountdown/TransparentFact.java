package christmas.peej.com.christmascountdown;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;

public class TransparentFact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);



        setContentView(R.layout.activity_transparent_fact);
        TextView tv = (TextView) findViewById(R.id.factOfTheDay);
        LocalDateTime fromDate1 = new LocalDateTime(System.currentTimeMillis());
        LocalDateTime newYear1 = new LocalDateTime(2017,12,25,0,0);
        int daysremaining = Days.daysBetween(fromDate1, newYear1).getDays();
        if(daysremaining>=FactClass.facts.length){
            daysremaining= daysremaining%FactClass.facts.length;
        }
        Log.d("length",FactClass.facts.length+"");
        Log.d("Day chosen",daysremaining + "");
        tv.setText(FactClass.facts[daysremaining]);


    }

    public void closeActivity(View view){
        finish();
    }
}
