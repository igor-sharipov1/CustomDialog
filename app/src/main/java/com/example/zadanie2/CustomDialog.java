package com.example.zadanie2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class CustomDialog extends Dialog implements View.OnTouchListener {

    private int parentX, parentY;
    private int parentWidth, parentHeight;
    private ImageView arrow;
    private View v;
    private int dialogBoxHeight, dialogBoxWidth;
    private int dialogBoxY, dialogBoxX;
    private int arrowX, arrowY;
    private int  arrowWidth = 75, arrowHeight = 50;
    private CardView cv = new CardView(getContext());

    private boolean dismissOnOutsideTap = true;

    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    public void setDismissOnOutsideTap(boolean flag){
        dismissOnOutsideTap = flag;
    }

    public void setArrowSize(int width, int height){
        arrowWidth = width;
        arrowHeight = height;
    }

    public void setParentView(View parent){
        this.arrow = new ImageView(parent.getContext());
        this.parentX = (int) parent.getX();
        this.parentY = (int) parent.getY();
        this.parentWidth = parent.getWidth();
        this.parentHeight = parent.getHeight();
    }



    public void setDialogView(View v){
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.v = v;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(v);
        setDialogBox();
        v.setOnTouchListener(this);
    }

    private void setDialogBox(){
        arrowX = parentX + this.parentWidth / 2 - arrowWidth / 2;
        arrowY = parentY + this.parentHeight;

        WindowManager.LayoutParams wmlp = this.getWindow().getAttributes();
        wmlp.gravity = Gravity.FILL_VERTICAL | Gravity.FILL_HORIZONTAL;
        wmlp.x = 0 ;   //x position
        wmlp.y = 0 ;

        getCardView();
        dialogBoxHeight = cv.getMinimumHeight();
        dialogBoxWidth = cv.getMinimumWidth();

        int ostatokHeight = Resources.getSystem().getDisplayMetrics().heightPixels - (arrowY+arrowHeight);

        if (ostatokHeight < dialogBoxHeight){
            setDialogBoxAbove();
        }
        else {
            setDialogBoxUnder(arrowX, arrowY);
        }
    }



    private  void setDialogBoxUnder(int arrowX, int arrowY){

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(arrowX, arrowY, 0, 0);
        arrow.setLayoutParams(lp);
        Bitmap bitmap = getBitmapFromVectorDrawable(getContext(), R.drawable.ic_baseline_arrow_drop_down);
        arrow.setImageBitmap(Bitmap.createScaledBitmap(bitmap, arrowWidth, arrowHeight, false));;
        ((ViewGroup) v).addView(arrow);

        dialogBoxY = parentY + parentHeight + arrowHeight* 20 / 23 ;

        setDialogHorizontally();

        cv.setY(dialogBoxY);
    }

    private void setDialogBoxAbove(){

        arrowX = parentX + this.parentWidth / 2 - arrowWidth / 2;
        arrowY = parentY - arrowHeight;


        dialogBoxY = parentY - dialogBoxHeight - arrowHeight* 20 / 23;

        setDialogHorizontally();
        cv.setY(dialogBoxY);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(arrowX, arrowY, 0, 0);

        arrow.setLayoutParams(lp);

        Bitmap bitmap = getBitmapFromVectorDrawable(getContext(), R.drawable.ic_baseline_arrow_drop_down);
        arrow.setImageBitmap(Bitmap.createScaledBitmap(bitmap,  arrowWidth, arrowHeight, false));;

        arrow.animate().rotation(180f).setDuration(0).start();

        ((ViewGroup) v).addView(arrow);

    }

    private void setDialogHorizontally(){
        int endOfScreen = Resources.getSystem().getDisplayMetrics().widthPixels;

        int marginX = 15;

        if(parentX - dialogBoxWidth/2 < 0){
            dialogBoxX = 0;
            cv.setX(dialogBoxX+marginX);
        }
        else if(parentX + dialogBoxWidth/3*2 > endOfScreen){
            dialogBoxX = endOfScreen - dialogBoxWidth;
            cv.setX(dialogBoxX-marginX);
        }
        else{
            dialogBoxX = arrowX - dialogBoxWidth/2 + arrowWidth/2;
            cv.setX(dialogBoxX-marginX);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int y = (int) event.getY();
            int x = (int) event.getX();
            if (dismissOnOutsideTap) {
                if (touchYOutsideDialogBorders(y)) {
                    this.dismiss();
                }
                if (touchXOutsideDialogBorders(x)){
                    this.dismiss();
                }
            }
        }
        return false;
    }


    private boolean touchYOutsideDialogBorders(int y){
        return (y < dialogBoxY) || (y > dialogBoxY + dialogBoxHeight);
    }

    private boolean touchXOutsideDialogBorders(int x){
        return (x < dialogBoxX) || (x > dialogBoxX + dialogBoxWidth);
    }


    private void getCardView(){
        for(int index = 0; index < ((ViewGroup) v).getChildCount(); index++) {
            LinearLayout ll = (LinearLayout) ((ViewGroup) v).getChildAt(index);
            for(int index2 = 0; index2 < ((ViewGroup) ll).getChildCount(); index2++) {
                cv = (CardView) ((ViewGroup) ll).getChildAt(index2);
            }
        }
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}