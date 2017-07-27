package com.project.rptang.android.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.rptang.android.R;

import java.util.Set;

public class BlueToothActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "BlueToothActivity";

    private Button btn_close_bluetooth, btn_open_bluetooth, btn_scan_bluetooth, btn_stop_scan_bluetooth;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothReceiver bluetoothReceiver;
    private BluetoothService mBluetoothService; //自定义蓝牙服务类
    //hanlder消息标识 message.what
    public static final int MESSAGE_STATE_CHANGE = 1; // 状态改变
    public static final int MESSAGE_DEVICE_NAME = 4;  // 设备名字
    public static final int MESSAGE_TOAST = 5;         // Toast
    public static final int MESSAGE_READ = 2;          // 读取数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);

        initView();
        addEventListener();

        //设定广播接收的filter
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //创建蓝牙广播信息的receiver
        bluetoothReceiver = new BluetoothReceiver();
        //注册广播接收器
        registerReceiver(bluetoothReceiver, intentFilter);

        //创建自定义蓝牙服务对象
        if (mBluetoothService == null) {
            mBluetoothService = new BluetoothService(BlueToothActivity.this, mHandler);
        }

    }

    private void initView() {
        btn_close_bluetooth = (Button) findViewById(R.id.btn_close_bluetooth);
        btn_open_bluetooth = (Button) findViewById(R.id.btn_open_bluetooth);
        btn_scan_bluetooth = (Button) findViewById(R.id.btn_scan_bluetooth);
        btn_stop_scan_bluetooth = (Button) findViewById(R.id.btn_stop_scan_bluetooth);
    }

    private void addEventListener() {
        btn_close_bluetooth.setOnClickListener(this);
        btn_open_bluetooth.setOnClickListener(this);
        btn_scan_bluetooth.setOnClickListener(this);
        btn_stop_scan_bluetooth.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close_bluetooth:
                closeBlueTooth();
                break;
            case R.id.btn_open_bluetooth:
                openBlueTooth();
                break;
            case R.id.btn_scan_bluetooth:
                scanBlueTooth();
                break;
            case R.id.btn_stop_scan_bluetooth:
                stopScanBlueTooth();
                break;
        }
    }

    /**
     * 关闭蓝牙
     */
    private void closeBlueTooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.disable();
    }

    /**
     * 打开蓝牙
     */
    private void openBlueTooth() {
        //打开蓝牙方式一(提示对话框)
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }

    /**
     * 扫描蓝牙设备
     */
    private void scanBlueTooth() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();//开始扫描其他蓝牙设备

        System.out.println("本机蓝牙名称:" + bluetoothAdapter.getName());
        System.out.println("本机蓝牙地址:" + bluetoothAdapter.getAddress());

        Set<BluetoothDevice> set = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bd : set) {
            System.out.println("已配对设备名称:" + bd.getName());
            System.out.println("已配对设备地址:" + bd.getAddress());
        }
    }

    private void stopScanBlueTooth() {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
    }


    private class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获得扫描到的远程蓝牙设备  
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device.getName() != null && device.getName().equalsIgnoreCase("LAPTOP-K1EOD4OP")){
                if (mBluetoothService != null) {
                    //根据MAC地址远程获取一个蓝牙设备，这里固定了，实际开发中，需要动态设置参数（MAC地址）
                    BluetoothDevice sensor_down = bluetoothAdapter.getRemoteDevice(device.getAddress());
                    if (sensor_down != null) {
                        //成功获取到远程蓝牙设备（传感器），这里默认只连接MAGIKARE_SENSOR_DOWN = 1这个设备
                        mBluetoothService.connect(sensor_down, 1);
                    }
                }

            }
            System.out.println("name:" + device.getName());
            System.out.println("address:" + device.getAddress());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
    }

    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_STATE_CHANGE:
                    //                    连接状态
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Log.d(TAG, "STATE_CONNECTED");
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.d(TAG, "STATE_CONNECTING");
                            break;
                        case BluetoothService.STATE_LISTEN:
                            Log.d(TAG, "STATE_LISTEN");
                            break;
                        case BluetoothService.STATE_NONE:
                            Log.d(TAG, "STATE_NONE");
                            break;
                    }
                    break;

                case MESSAGE_DEVICE_NAME:
                    String mConnectedDeviceName = msg.getData().getString("device_name");
                    Log.i("bluetooth","成功连接到:"+mConnectedDeviceName);
                    Toast.makeText(getApplicationContext(),"成功连接到设备" + mConnectedDeviceName,Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),msg.getData().getString("toast"), Toast.LENGTH_SHORT).show();
                    break;

            }
            return false;
        }
    });


}