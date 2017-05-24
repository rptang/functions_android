package com.project.rptang.android.crosswalk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebResourceResponse;
import com.project.rptang.android.R;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

public class CrossWalkActivity extends Activity {

    private XWalkView mXWalkView;
    private XWalkSettings mXWalkSettings;
    private String obtainUploadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cross_walk);

        initView();
        initData();
        addEventListener();
    }

    private void initView(){
        mXWalkView = (XWalkView) findViewById(R.id.x_walkview);

        //添加对javascript支持
        XWalkPreferences.setValue("enable-javascript", true);
//开启调式,支持谷歌浏览器调式
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
//置是否允许通过file url加载的Javascript可以访问其他的源,包括其他的文件和http,https等其他的源
        XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);
//JAVASCRIPT_CAN_OPEN_WINDOW
        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);
// enable multiple windows.
        XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);

        mXWalkView.setHorizontalScrollBarEnabled(false);
        mXWalkView.setVerticalScrollBarEnabled(false);
        mXWalkView.setScrollBarStyle(XWalkView.SCROLLBARS_OUTSIDE_INSET);
        mXWalkView.setScrollbarFadingEnabled(true);

        //获取setting
        mXWalkSettings = mXWalkView.getSettings();
//支持空间导航
        mXWalkSettings.setSupportSpatialNavigation(true);
        mXWalkSettings.setBuiltInZoomControls(true);
        mXWalkSettings.setSupportZoom(true);

    }

    private void initData(){
        mXWalkView.load("https://www.baidu.com/", null);
        mXWalkView.setDrawingCacheEnabled(false);//不使用缓存
        mXWalkView.getNavigationHistory().clear();//清除历史记录
        mXWalkView.clearCache(true);//清楚包括磁盘缓存
    }

    private void addEventListener(){

        mXWalkView.setUIClient(new XWalkUIClient(mXWalkView) {

            @Override
            public void onReceivedTitle(XWalkView view, String title) {
                Log.d("onReceivedTitle", "onReceivedTitle: "+title);
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onPageLoadStarted(XWalkView view, String url) {

                super.onPageLoadStarted(view, url);
            }

            @Override
            public void onFullscreenToggled(XWalkView view, boolean enterFullscreen) {
                Log.d("onFullscreenToggled", "onFullscreenToggled: "+enterFullscreen);
                super.onFullscreenToggled(view, enterFullscreen);
            }

            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {

                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onScaleChanged(XWalkView view, float oldScale, float newScale) {
                if (view != null) {
                    view.invalidate();
                }
                super.onScaleChanged(view, oldScale, newScale);
            }

            @Override
            public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
                super.onPageLoadStopped(view, url, status);
            }
        });

        mXWalkView.setResourceClient(new XWalkResourceClient(mXWalkView) {
            @Override
            public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {

                super.onReceivedLoadError(view, errorCode, description, failingUrl);
            }

            @Override
            public WebResourceResponse shouldInterceptLoadRequest(XWalkView view, String url) {
//                LogUtils.d("http", "shouldOverrideUrlLoading-url=" + url);
                return super.shouldInterceptLoadRequest(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
                if(!url.contains("&platform=android-app")){
                    url=  url + "&platform=android-app";
                }
                Log.d("OverrideUrlLoading", "shouldOverrideUrlLoading: "+url);
                obtainUploadUrl = url;
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onProgressChanged(XWalkView view, int progressInPercent) {
                Log.d("onProgressChanged", "onProgressChanged: "+progressInPercent);
                super.onProgressChanged(view, progressInPercent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mXWalkView != null) {
            mXWalkView.onDestroy();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mXWalkView != null) {
            mXWalkView.pauseTimers();
            mXWalkView.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mXWalkView != null) {
            mXWalkView.resumeTimers();
            mXWalkView.onShow();
        }
    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mXWalkView.getNavigationHistory().canGoBack()) {

                mXWalkView.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);//返回上一页面
            } else {
            /*finish();*/
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
