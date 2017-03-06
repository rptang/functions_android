package com.project.rptang.android.scroll_message_listview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import com.project.rptang.android.R;

public class ScrollMessageActivity extends Activity implements View.OnClickListener {

    private ListView listView;// List数据列表
    private Button toTopBtn;// 返回顶部的按钮
    private MyAdapter adapter;
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_message);

        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        listView = (ListView) findViewById(R.id.my_listView);
        toTopBtn = (Button) findViewById(R.id.top_btn);

        adapter = new MyAdapter(this, getTitleDatas());
        listView.setAdapter(adapter);

        toTopBtn.setOnClickListener(this);
        listView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    // 当不滚动时
                    case OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        if (listView.getLastVisiblePosition() == (listView
                                .getCount() - 1)) {
                            toTopBtn.setVisibility(View.VISIBLE);
                        }
                        // 判断滚动到顶部
                        if (listView.getFirstVisiblePosition() == 0) {
                            toTopBtn.setVisibility(View.GONE);
                        }

                        break;
                    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                        scrollFlag = true;
                        break;
                    case OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                        scrollFlag = false;
                        break;
                }
            }

            /**
             * firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
             * visibleItemCount：当前能看见的列表项个数（小半个也算） totalItemCount：列表项共数
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
                if (scrollFlag
                        && ScreenUtil.getScreenViewBottomHeight(listView) >= ScreenUtil
                        .getScreenHeight(ScrollMessageActivity.this)) {
                    if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
                        toTopBtn.setVisibility(View.VISIBLE);
                    } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
                        toTopBtn.setVisibility(View.GONE);
                    } else {
                        return;
                    }
                    lastVisibleItemPosition = firstVisibleItem;
                }
            }
        });
    }

    /**
     * 获取标题数据列表
     *
     * @return
     */
    private List<String> getTitleDatas() {
        List<String> titleArray = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            titleArray.add("这是第" + i + "个item");
        }
        return titleArray;
    }

    /**
     * 滚动ListView到指定位置
     *
     * @param pos
     */
    private void setListViewPos(int pos) {
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            listView.smoothScrollToPosition(pos);
        } else {
            listView.setSelection(pos);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.top_btn:// 点击按钮返回到ListView的第一项
                setListViewPos(0);
                break;
        }
    }
}
