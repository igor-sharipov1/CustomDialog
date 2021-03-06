package com.example.zadanie2;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import java.util.Arrays;

public class CustomDialog extends Dialog implements View.OnTouchListener {

    private int parentX, parentY;
    private int parentWidth, parentHeight;
    private ImageView arrow;
    private View v;
    private LinearLayout ll;
    private int dialogBoxHeight, dialogBoxWidth;
    private int dialogBoxY, dialogBoxX;
    private int arrowX, arrowY;
    private int  arrowWidth = 65, arrowHeight = 65;
    private View insideView;
    private double spaceLeft ;
    private double spaceRight ;
    private double spaceTop ;
    private double spaceBottom ;
    int endOfScreen;
    final public static int FILL = 0;
    final public static int VERTICAL = 1;
    final public static int HORIZONTAL = 2;
    final public static int TOP = 3;
    final public static int RIGHT = 4;
    final public static int BOTTOM = 5;
    final public static int LEFT = 6;

    private int arrowCornerOffset = 0;

    public void setArrowCornerOffset(int offset){
        this.arrowCornerOffset = offset;
    }

    private int gravity = FILL;

    private boolean dismissOnOutsideTap = true;

    int marginY = 30;
    int marginX = 20;

    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    public void setDismissOnOutsideTap(boolean flag){
        dismissOnOutsideTap = flag;
    }

    public void setGravity(int grav){
        this.gravity = grav;
    }

    public void setArrowSize(int width, int height){
        this.arrowWidth = width;
        this.arrowHeight = height;
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

    public void setInsideView(View v){
        this.insideView = v;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(v);
        setDialogBox();
        setArrowPrimarySettings();
        v.setOnTouchListener(this);
    }


    private void setArrowPrimarySettings(){
        this.arrow = new ImageView(getContext());
        arrow.setPadding(0,0,0,0);
        arrow.setAdjustViewBounds(true);
        arrow.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Bitmap bitmap = getBitmapFromVectorDrawable(getContext(), R.drawable.ic_baseline_arrow_drop_down);
        arrow.setImageBitmap(Bitmap.createScaledBitmap(bitmap, arrowWidth, arrowHeight, false));
    }

    private void setDialogBox(){
        WindowManager.LayoutParams wmlp = this.getWindow().getAttributes();
        wmlp.gravity = Gravity.FILL_VERTICAL | Gravity.FILL_HORIZONTAL;
        wmlp.x = 0 ;   //x position
        wmlp.y = 0 ;

        getLinearLayout();
        insideView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                insideView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                dialogBoxHeight = insideView.getMeasuredHeight(); //height is ready
                dialogBoxWidth = insideView.getMeasuredWidth();
                selectDialogBoxLocation();
            }
        });

    }

    private void selectDialogBoxLocation(){
        double screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels - (arrowY+arrowHeight);
        double screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels - (arrowX+arrowWidth);

        spaceLeft = (parentX - marginX - dialogBoxWidth - arrowHeight) ;
        spaceRight = (screenWidth - parentX - parentWidth - marginX - dialogBoxWidth- arrowHeight)  ;
        spaceTop = (parentY - marginY - arrowHeight - dialogBoxHeight) ;
        spaceBottom = (screenHeight - parentY - parentHeight - marginY- arrowHeight - dialogBoxHeight) ;

        switch(this.gravity){
            case FILL:
                selectGravityFill();
                selectDialogBoxLocation();
                break;
            case VERTICAL:
                selectGravityVertical();
                break;
            case HORIZONTAL:
                selectGravityHorizontal();
                break;
            case TOP:
                setDialogBoxAbove();
                break;
            case RIGHT:
                setDialogBoxRight();
                break;
            case BOTTOM:
                setDialogBoxUnder();
                break;
            case LEFT:
                setDialogBoxLeft();
                break;
            default:
                break;
        }
    }


    private void selectGravityFill(){

        double[] arrayScales = {spaceBottom, spaceLeft, spaceRight, spaceTop};

        double max = arrayScales[0];

        for (int i = 1; i < 4; i++){
            if (max < arrayScales[i]) max = arrayScales[i];
        }


        if (max == spaceBottom || max == spaceTop){
            setGravity(VERTICAL);
        }
        else if(max == spaceLeft || max == spaceRight){
            setGravity(HORIZONTAL);
        }
    }

    private void selectGravityVertical(){

        if (spaceBottom < spaceTop){
            setDialogBoxAbove();
        }
        else {
            setDialogBoxUnder();
        }
    }

    private void selectGravityHorizontal(){

        if (spaceRight < spaceLeft){
            setDialogBoxLeft();
        }
        else {
            setDialogBoxRight();
        }
    }

    private  void setDialogBoxLeft(){

        arrow.animate().rotation(90f).setDuration(0).start();

        arrowX = parentX - arrowHeight+ (arrowHeight - arrowWidth) / 2;
        arrowY = parentY + parentHeight / 2 - arrowHeight / 2;

        dialogBoxX = parentX - dialogBoxWidth - arrowHeight* 20/23 ;

        setDialogVertically();

        setArrowParameters();

        insideView.setX(dialogBoxX);
    }

    private  void setDialogBoxRight(){


        arrow.animate().rotation(-90f).setDuration(0).start();

        arrowX = parentX + parentWidth + (arrowHeight - arrowWidth) / 2;
        arrowY = parentY + parentHeight / 2 - arrowHeight / 2;

        dialogBoxX = parentX + parentWidth + arrowHeight * 20/23;

        setDialogVertically();

        setArrowParameters();

        insideView.setX(dialogBoxX);
    }

    private  void setDialogBoxUnder(){

        arrowX = parentX + this.parentWidth / 2 - arrowWidth / 2;
        arrowY = parentY + this.parentHeight;

        dialogBoxY = parentY + parentHeight + arrowHeight* 20/23;

        setDialogHorizontally();

        setArrowParameters();

        insideView.setY(dialogBoxY);
    }

    private void setDialogBoxAbove(){

        arrow.animate().rotation(180f).setDuration(0).start();

        arrowX = parentX + this.parentWidth / 2 - arrowWidth / 2;
        arrowY = parentY - this.arrowHeight;
        dialogBoxY = parentY - dialogBoxHeight - this.arrowHeight* 20/23;


        setDialogHorizontally();
        insideView.setY(dialogBoxY);

        setArrowParameters();

        setDialogHorizontally();

    }

    private void setArrowParameters(){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        if (gravity == CustomDialog.VERTICAL || gravity == CustomDialog.TOP || gravity == CustomDialog.BOTTOM){
            int leftArrowDistance = arrowX  - dialogBoxX;
            int rightArrowDistance = dialogBoxX + dialogBoxWidth - arrowWidth - arrowX;
            if (leftArrowDistance < arrowCornerOffset)
                lp.setMargins(dialogBoxX + arrowCornerOffset, arrowY, 0, 0);
            else if (rightArrowDistance < arrowCornerOffset)
                lp.setMargins(dialogBoxX + dialogBoxWidth - arrowCornerOffset - arrowWidth, arrowY, 0, 0);
            else lp.setMargins(arrowX, arrowY, 0, 0);
        }
        else {
            int rotateArrowSlip = arrowWidth - arrowHeight;
            int topArrowDistance = arrowY - dialogBoxY;
            int bottomArrowDistance = dialogBoxY + dialogBoxHeight - arrowWidth - arrowY;
            if (topArrowDistance < arrowCornerOffset){
                lp.setMargins(arrowX, dialogBoxY  + arrowCornerOffset + rotateArrowSlip / 2, 0, 0);
            }

            else if (bottomArrowDistance < arrowCornerOffset){
                lp.setMargins(arrowX, dialogBoxY + dialogBoxHeight - arrowCornerOffset - arrowWidth + rotateArrowSlip / 2 , 0, 0);
            }

            else lp.setMargins(arrowX, arrowY, 0, 0);
        }

        arrow.setLayoutParams(lp);
        ((ViewGroup) v).addView(arrow);

    }


    private void setDialogHorizontally(){
        int endOfScreen = Resources.getSystem().getDisplayMetrics().widthPixels;

        int marginX = 15;

        if(parentX - dialogBoxWidth/2 < 0){
            dialogBoxX = marginX;
            insideView.setX(dialogBoxX);
        }
        else if(parentX + dialogBoxWidth/3*2 > endOfScreen){
            dialogBoxX = endOfScreen - dialogBoxWidth - marginX;
            insideView.setX(dialogBoxX);
        }
        else{
            dialogBoxX = arrowX - dialogBoxWidth/2 + arrowWidth/2;
            insideView.setX(dialogBoxX);
        }
    }

    private void setDialogVertically(){
        endOfScreen = Resources.getSystem().getDisplayMetrics().heightPixels;


        if(parentY - dialogBoxHeight/2 < 0){
            dialogBoxY = marginY;
            insideView.setY(dialogBoxY);
        }
        else if(parentY + dialogBoxHeight > endOfScreen){
            dialogBoxY = endOfScreen - dialogBoxHeight - 2*marginY;
            insideView.setY(dialogBoxY);
        }
        else{
            dialogBoxY = arrowY - dialogBoxHeight/2 + arrowHeight/2;
            insideView.setY(dialogBoxY);
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


    private void getLinearLayout(){
        for(int index = 0; index < ((ViewGroup) v).getChildCount(); index++) {
            ll = (LinearLayout) ((ViewGroup) v).getChildAt(index);
            ((ViewGroup) ll).addView(insideView);
        }
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);


        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}