package gg.roundlayout.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import gg.roundlayout.views.throwable.RoundLayoutException;

/**
 * Created by ImGG on 2016/7/31.
 */
public class RoundLayout extends ViewGroup {
    private int childCount=0;//Layout中包含的子控件的个数
    private double eachChildAngle=0;//每个子控件在圆内所占的角度，有childCount计算2*Math.PI/childCount得到
    private int R=0;//父控件（RoundLayout）的半径(px)
    private int x=0;//父控件的中心点的x坐标
    private int y=0;//父控件的中心点的y坐标

    public RoundLayout(Context context) throws RoundLayoutException {
        this(context,null);
    }

    public RoundLayout(Context context, AttributeSet attrs) throws RoundLayoutException {
        this(context, attrs,0);
    }

    public RoundLayout(Context context, AttributeSet attrs, int defStyleAttr) throws RoundLayoutException {
        super(context, attrs, defStyleAttr);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) throws RoundLayoutException {
        this(context, attrs, defStyleAttr);
    }

    /**
     * wrap_content恐怕没有效果
     *
     * 测量控件大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        childCount=getChildCount();
        if (childCount<3)
            throw new RoundLayoutException("子控件个数应大于或等于3个");
        eachChildAngle = 2*Math.PI/childCount;
        for (int i=0;i<childCount;i++){
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
        //半径取长宽中较短的一边/2
        R=Math.min(getMeasuredWidth(),getMeasuredHeight())/2;
        //确定控件的中心点坐标
        x=getMeasuredWidth()/2;
        y=getMeasuredHeight()/2;
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }


    /**
     * 子控件相对父控件的位置
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(changed) {
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                //若希望当子控件不可见时自动补全为一个圆形，则将下面一行注释掉
                if (childView.getVisibility()==View.VISIBLE){
                    //childSize取childView长宽中较长边组成一个正方形
                    //childView的半径即childSize/2
                    int childSize = Math.max(childView.getMeasuredWidth(), childView.getMeasuredHeight());
                    int childX = x - (int) ((R - childSize / 2) * Math.cos(Math.PI/2-eachChildAngle * i));
                    int childY = y - (int) ((R - childSize / 2) * Math.sin(Math.PI/2-eachChildAngle * i));
                    Log.i("child" + i + "--->",x+"--"+y+"--"+ R + "---" + childX + "---" + childY);
                    childView.layout(childX - childSize / 2, childY - childSize / 2, childX + childSize / 2, childY + childSize / 2);
                }
            }
        }
    }

}
