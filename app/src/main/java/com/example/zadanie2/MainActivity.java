package com.example.zadanie2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    RelativeLayout myLayout;
    ImageView iv;
    int x;
    int y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myLayout = findViewById(R.id.main);
        myLayout.setOnTouchListener(this);
        iv = findViewById(R.id.imageView);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(iv);
                //dialog.setDismissOnOutsideTap = false;
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (iv.getDrawable() != null) {
                ((ViewGroup) v).removeView(iv);
            }
            int x = (int) event.getX();
            int y = (int) event.getY();
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(x - iv.getMeasuredWidth()/2, y - iv.getMeasuredHeight()/2 , 0, 0);
            iv.setLayoutParams(lp);
            iv.setImageDrawable(getResources().getDrawable(
                    R.drawable.ic_baseline_settings_applications_24));
            ((ViewGroup) v).addView(iv);
        }
        return false;
    }
}