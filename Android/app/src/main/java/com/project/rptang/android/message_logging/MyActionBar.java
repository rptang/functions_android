package com.project.rptang.android.message_logging;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.rptang.android.R;

/**
 * Created by Stiven on 2017/2/27.
 */

public class MyActionBar extends RelativeLayout {

    private static final String TAG = "MyActionBar";

    //声明我们需要的控件，这些控件是和attrs.xml中我们定义的属性对应的
    private TextView tvLeftTitle,tvRightTitle,tvTitle;//actionbar控件上显示的标题

    private int leftTextColor;
    private Drawable leftTextDrawable;
    private String leftText;
    private float leftTextSize;
    private int leftTextPaddingLeft,leftTextPaddingTop;

    private int rightTextColor;
    private Drawable rightTextDrawable;
    private String rightText;
    private float rightTextSize;
    private int rightTextPaddingRight,rightTextPaddingTop;

    private String title;
    private int titleTextColor;//显示文字的颜色
    private float titleTextSize;//显示出来的文字的大小

    private Drawable actionBarBackground;

    private LayoutParams leftParams, rightParams, titleParams;//设置控件的布局

    private MyActionbarClickListener listener;//定义接口对象

    //自定义接口
    public interface MyActionbarClickListener {

        void leftClick();
        void rightClick();
    }

    public void setOnMyActionbarClickListener(MyActionbarClickListener listener) {
//暴露给调用者的方法，这样调用者便可以将具体实现以匿名内部类的形式传递进来
        this.listener = listener;
    }


    public MyActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);

//使用TypedArray,获取在attrs.xml中我们自定义的一系列属性,并将属性赋给我们定义的控件,然后我们便可以从TypedArray中获取到这些属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyActionBar);
        leftTextColor = typedArray.getColor(R.styleable.MyActionBar_leftTextColor, 0);
        leftTextDrawable = typedArray.getDrawable(R.styleable.MyActionBar_leftTextDrawable);
        leftText = typedArray.getString(R.styleable.MyActionBar_leftText);
        leftTextSize = typedArray.getDimension(R.styleable.MyActionBar_leftTextSize, 0);
        leftTextPaddingLeft = (int) typedArray.getDimension(R.styleable.MyActionBar_leftTextPaddingLeft,0);
        leftTextPaddingTop = (int) typedArray.getDimension(R.styleable.MyActionBar_leftTextPaddingTop,0);

        rightTextColor = typedArray.getColor(R.styleable.MyActionBar_rightTextColor, 0);
        rightTextDrawable = typedArray.getDrawable(R.styleable.MyActionBar_rightTextDrawable);
        rightText = typedArray.getString(R.styleable.MyActionBar_rightText);
        rightTextSize = typedArray.getDimension(R.styleable.MyActionBar_rightTextSize, 0);
        rightTextPaddingRight = (int) typedArray.getDimension(R.styleable.MyActionBar_rightTextPaddingRight,0);
        rightTextPaddingTop = (int) typedArray.getDimension(R.styleable.MyActionBar_rightTextPaddingTop,0);

        title = typedArray.getString(R.styleable.MyActionBar_mtitle);
        titleTextColor = typedArray.getColor(R.styleable.MyActionBar_titleTextColor, 0);
        titleTextSize = typedArray.getDimension(R.styleable.MyActionBar_titleTextSize, 0);

        actionBarBackground = typedArray.getDrawable(R.styleable.MyActionBar_actionbarBackground);
        typedArray.recycle();//回收此变量，节约资源，避免由于缓存导致的其他问题

        // 实例化我们定义的控件
        tvLeftTitle = new TextView(context);
        tvRightTitle = new TextView(context);
        tvTitle = new TextView(context);
        // 将我们获取到的属性分别赋给相应的控件
        tvLeftTitle.setText(leftText);
        tvLeftTitle.setTextSize(leftTextSize);
        tvLeftTitle.setTextColor(leftTextColor);
        tvLeftTitle.setPadding(leftTextPaddingLeft,leftTextPaddingLeft,0,leftTextPaddingLeft);
        leftTextDrawable.setBounds(0,0,leftTextDrawable.getMinimumWidth(),leftTextDrawable.getMinimumHeight());
        tvLeftTitle.setCompoundDrawables(null,leftTextDrawable,null,null);

        tvRightTitle.setText(rightText);
        tvRightTitle.setTextSize(rightTextSize);
        tvRightTitle.setTextColor(rightTextColor);
        tvRightTitle.setPadding(0,rightTextPaddingRight,rightTextPaddingRight,rightTextPaddingRight);
        rightTextDrawable.setBounds(0,0,rightTextDrawable.getMinimumWidth(),rightTextDrawable.getMinimumHeight());
        tvRightTitle.setCompoundDrawables(null,rightTextDrawable,null,null);

        tvTitle.setText(title);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setGravity(Gravity.CENTER);//设置标题居中

        setBackground(actionBarBackground);//设置viewGroup(即两个button和一个tittle)的背景颜色

        //以上我们已经设置好了各个控件的属性，那接下来就需要把这些控件放置到viewGroup中了，即我们需要设置一下他们的布局属性了
        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);//设置左Button的width和height
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);//设置左边button的布局属性，即左对齐
//        leftParams.setMargins();
        addView(tvLeftTitle, leftParams);//将左边button加入到viewGroup中

        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        addView(tvRightTitle, rightParams);

        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(tvTitle, titleParams);

        tvLeftTitle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.leftClick();
            }
        });
        tvRightTitle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.rightClick();
            }
        });
    }

    public void setRightIsVisible(boolean flag)//设置右button是否显示
    {
        if (flag) {
            tvRightTitle.setVisibility(VISIBLE);
        }else {
            tvRightTitle.setVisibility(GONE);
        }
    }
}
