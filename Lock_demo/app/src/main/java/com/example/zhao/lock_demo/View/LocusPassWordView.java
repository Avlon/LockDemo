package com.example.zhao.lock_demo.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.zhao.lock_demo.R;

/**
 * Created by zhao on 2016/8/8.
 */

public class LocusPassWordView extends View {
    private class Circle{                   //储存每个圆的信息
        Point Center;
        float Radiuse;
        boolean isPressed;
        public Circle(){
            Center=new Point();
            Radiuse=0;
            isPressed=false;
        }
    }
    private class Point{
        float X;
        float Y;
    }
    private boolean isChecking;
    private boolean onceDraw;
    private float WidthSpace,HeightSpace;
    private String PassWord;                //储存生成的密码
    private ImageView[] PussWordButton;
    private Point TouchPoint;            //触摸坐标
    private Circle[] PussWordView;          //储存每个按钮的状态以及信息
    private Paint paint;
    private Bitmap UnPressed;
    private Bitmap Pressed;
    public LocusPassWordView(Context context, AttributeSet attrs){
        super(context,attrs);
        init();
    }
    private void init(){
        paint=new Paint();
        TouchPoint=new Point();
        isChecking=false;
        onceDraw=true;
        PussWordButton=new ImageView[9];
        try {
            PussWordView = new Circle[9];
            for(int i=0;i<9;i++){
                PussWordView[i]=new Circle();
            }
            PassWord = new String();
            Pressed= BitmapFactory.decodeResource(this.getResources(),R.drawable.pressed);
            UnPressed=BitmapFactory.decodeResource(this.getResources(),R.drawable.un_pressed);
        }catch (Exception e){
            Log.e("print",e.toString());
        }
        Log.e("print","init_end");
    }
    private void reSetPassWord(){
        if(!PassWord.equals("")) {
            PassWord = "";
            onceDraw = true;
            for(int i=0;i<9;i++){
                PussWordView[i].isPressed=false;
            }
        }
    }
    private float Distance(Point a,Point b){
        return (float)Math.sqrt((a.X-b.X)*(a.X-b.X)+(a.Y-b.Y)*(a.Y-b.Y));
    }
    private boolean isPussCircle(Circle circle,Point point){
        if(Distance(circle.Center,point)<circle.Radiuse){
            return true;
        }
        else
            return false;
    }
    @Override
    protected void onLayout(boolean changed,int l,int t,int r,int b){
        super.onLayout(changed, l, t, r, b);
    }
    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(onceDraw){
            float Height=this.getMeasuredHeight();
            float Width=this.getWidth();
            Log.e("print","width"+Float.toString(Width)+"  height:"+Float.toString(Width));
            PussWordView[0].Radiuse=Width/(4.024f*2);       //计算每个圆的半径
            WidthSpace=PussWordView[0].Radiuse*2*0.512f;
            HeightSpace=PussWordView[0].Radiuse*2*0.625f;
            float sh=PussWordView[0].Radiuse*2/Pressed.getWidth();
            Matrix matrix=new Matrix();
            matrix.postScale(sh,sh);
            Pressed=Bitmap.createBitmap(Pressed,0,0,Pressed.getWidth(),Pressed.getHeight(),matrix,true);
            UnPressed=Bitmap.createBitmap(UnPressed,0,0,UnPressed.getWidth(),UnPressed.getHeight(),matrix,true);
            for(int i=0;i<9;i++){
                PussWordView[i].Center.X=i%3*WidthSpace+i%3*PussWordView[0].Radiuse*2+PussWordView[0].Radiuse;
                PussWordView[i].Center.Y=i/3*HeightSpace+i/3*PussWordView[0].Radiuse*2+PussWordView[0].Radiuse;
                PussWordView[i].Radiuse=PussWordView[0].Radiuse;
            }
            for(int i=0;i<9;i++){
                canvas.drawBitmap(UnPressed,PussWordView[i].Center.X-PussWordView[i].Radiuse,PussWordView[i].Center.Y-PussWordView[i].Radiuse,paint);
            }
            onceDraw=false;
        }
        else{
            for(int i=0;i<9;i++){
                canvas.drawBitmap(UnPressed,PussWordView[i].Center.X-PussWordView[i].Radiuse,PussWordView[i].Center.Y-PussWordView[i].Radiuse,paint);
            }
            for(int i=0;i<PassWord.length();i++){
                //Log.e("print","PL:"+Integer.toString(PassWord.length())+" i:"+Integer.toString(i)+"  :"+Character.toString(PassWord.charAt(i)));//绘制已有图形
                if(i==0){
                    canvas.drawBitmap(Pressed,PussWordView[Integer.parseInt(Character.toString(PassWord.charAt(i)))-1].Center.X-PussWordView[i].Radiuse,PussWordView[Integer.parseInt(Character.toString(PassWord.charAt(i)))-1].Center.Y-PussWordView[i].Radiuse,paint);
                }
                else{
                    drawLine(canvas,PussWordView[Integer.parseInt(Character.toString(PassWord.charAt(i-1)))-1],PussWordView[Integer.parseInt(Character.toString(PassWord.charAt(i)))-1].Center);//存在越界
                    canvas.drawBitmap(Pressed,PussWordView[Integer.parseInt(Character.toString(PassWord.charAt(i)))-1].Center.X-PussWordView[i].Radiuse,PussWordView[Integer.parseInt(Character.toString(PassWord.charAt(i)))-1].Center.Y-PussWordView[i].Radiuse,paint);
                }
            }
            if(!PassWord.equals("")){                                   //绘制动态连线
                drawLine(canvas,PussWordView[Integer.parseInt(Character.toString(PassWord.charAt(PassWord.length()-1)))-1],TouchPoint);
            }
        }
    }
    private void drawLine(Canvas canvas,Circle circle,Point end){
        paint.setColor(getResources().getColor(R.color.colorPressedBule));
        paint.setStrokeWidth(PussWordView[0].Radiuse*0.088f);
        canvas.drawLine(circle.Center.X,circle.Center.Y,end.X,end.Y,paint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isChecking) {
            //Log.e("print","x:"+Float.toString(event.getX())+"   y:"+Float.toString(event.getY()));
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    TouchPoint.X = event.getX();
                    TouchPoint.Y = event.getY();
                    for (int i = 0; i < 9; i++) {
                        if (!PussWordView[i].isPressed && isPussCircle(PussWordView[i], TouchPoint)) {
                            PussWordView[i].isPressed = true;
                            PassWord = PassWord + Integer.toString(i + 1);
                            break;
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    TouchPoint.X = event.getX();
                    TouchPoint.Y = event.getY();
                    for (int i = 0; i < 9; i++) {
                        if (!PussWordView[i].isPressed && isPussCircle(PussWordView[i], TouchPoint)) {
                            PussWordView[i].isPressed = true;
                            PassWord = PassWord + Integer.toString(i + 1);
                            break;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.e("print",PassWord);
                    reSetPassWord();
                    break;
            }
        }

        this.postInvalidate();
        return true;
    }
}
