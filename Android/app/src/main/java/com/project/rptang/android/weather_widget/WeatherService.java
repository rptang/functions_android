package com.project.rptang.android.weather_widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.project.rptang.android.R;

public class WeatherService extends Service implements WeatherSearch.OnWeatherSearchListener {

    private static final String TAG = "WeatherService";

    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    public WeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {

        initLocation();
    }

    private void initLocation() {

        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        Log.d(TAG, "onLocationChanged: "+aMapLocation.getDistrict().toString());
                        initWeather(aMapLocation.getDistrict().toString());
                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }

            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    private void initWeather(String cityName){
        mquery = new WeatherSearchQuery(cityName, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        mweathersearch = new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }

    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        Log.d(TAG, "onWeatherLiveSearched: " + rCode);
        if (rCode == 1000) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                LocalWeatherLive weatherlive = weatherLiveResult.getLiveResult();
                updateViews(weatherlive);
//                reporttime1.setText(weatherlive.getReportTime()+"发布");
//                weather.setText(weatherlive.getWeather());
//                Temperature.setText(weatherlive.getTemperature()+"°");
//                wind.setText(weatherlive.getWindDirection()+"风     "+weatherlive.getWindPower()+"级");
//                humidity.setText("湿度         "+weatherlive.getHumidity()+"%");
            } else {
//                ToastUtil.show(WeatherSearchActivity.this, R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(WeatherSearchActivity.this, rCode);
        }
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
    }

    /**
     * 执行刷新界面操作
     */
    private void updateViews(LocalWeatherLive weatherlive) {
        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.weather);
        rv.setTextViewText(R.id.tv_weather, weatherlive.getWeather());
        AppWidgetManager awm = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName cn = new ComponentName(getApplicationContext(), WeatherProvider.class);
        awm.updateAppWidget(cn, rv);
    }
}
