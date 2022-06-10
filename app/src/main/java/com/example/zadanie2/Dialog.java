package com.example.zadanie2;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

public class Dialog extends DialogFragment implements View.OnTouchListener {

    private View parent;
    private int parentX, parentY;
    private int parentWidth, parentHeight;
    private ImageView arrow;
    private View v;
    private int dialogBoxHeight;
    private int dialogBoxY, dialogBoxX;
    private int arrowX, arrowY;

    private boolean dismissOnOutsideTap = true;

    public void setDismissOnOutsideTap(boolean flag){
        dismissOnOutsideTap = flag;
    }

    Dialog(View parent){
        this.parent = parent;
        this.arrow = new ImageView(parent.getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.parentX = (int) parent.getX();
        this.parentY = (int) parent.getY();
        this.parentWidth = parent.getWidth();
        this.parentHeight = parent.getHeight();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.dialog, null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setDialogBox();
        v.setOnTouchListener(this);
        return v;
    }


    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    private void setDialogBox(){
        arrowX = parentX + this.parentWidth / 2 - 30;
        arrowY = parentY + this.parentHeight - 42;

        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
        wmlp.gravity = Gravity.FILL_VERTICAL | Gravity.FILL_HORIZONTAL;
        wmlp.x = 0 ;   //x position
        wmlp.y = 0 ;


        CardView cv = v.findViewById(R.id.dialogForm);
        dialogBoxHeight = cv.getMinimumHeight();

        int ostatokHeight = Resources.getSystem().getDisplayMetrics().heightPixels - (arrowY+100);

        if (ostatokHeight < dialogBoxHeight){
            setDialogBoxAbove(cv);
        }
        else {
            setDialogBoxUnder(cv, arrowX, arrowY);
        }
    }

    private  void setDialogBoxUnder(CardView cv, int arrowX, int arrowY){

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(arrowX, arrowY, 0, 0);
        arrow.setLayoutParams(lp);
        arrow.setImageDrawable(getResources().getDrawable(
                R.drawable.ic_baseline_arrow_drop_down));
        ((ViewGroup) v).addView(arrow);

        dialogBoxX = cv.getPaddingStart();
        dialogBoxY = parentY + parentHeight + 30;

        cv.setX(dialogBoxX);
        cv.setY(dialogBoxY);
    }

    private void setDialogBoxAbove(CardView cv){

        arrowX = parentX + this.parentWidth / 2 - 30;
        arrowY = parentY - 45;

        dialogBoxX = cv.getPaddingStart();
        dialogBoxY = parentY - dialogBoxHeight - 30;

        cv.setX(0);
        cv.setY(parentY - dialogBoxHeight - 30);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(arrowX, arrowY, 0, 0);
        arrow.setLayoutParams(lp);
        arrow.setImageDrawable(getResources().getDrawable(
                R.drawable.ic_baseline_arrow_drop_down));

        arrow.animate().rotation(180f).setDuration(0).start();

        ((ViewGroup) v).addView(arrow);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int y = (int) event.getY();
            if (dismissOnOutsideTap) {
                if (touchYOutsideDialogBorders(y)) {
                    getDialog().dismiss();
                }
            }
        }
        return false;
    }


    private boolean touchYOutsideDialogBorders(int y){
        return (y < dialogBoxY) || (y > dialogBoxY + dialogBoxHeight);
    }
}
