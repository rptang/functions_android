package com.project.rptang.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.project.rptang.android.R;

import java.util.List;
import java.util.UUID;

import static com.project.rptang.android.bluetooth.le.BluetoothLeService.ACTION_DATA_AVAILABLE;

public class BlueTeethActivity extends AppCompatActivity {

    private static final String TAG = "BlueTeethActivity";

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothGatt mBluetoothGatt;
    private boolean isScanning = false;
    private BluetoothReceiver bluetoothReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_teeth);

        initBluetooth();
        //设定广播接收的filter
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //创建蓝牙广播信息的receiver
        bluetoothReceiver = new BluetoothReceiver();
        //注册广播接收器
        registerReceiver(bluetoothReceiver, intentFilter);

        mBluetoothAdapter.startDiscovery();

//        startLeScan();

    }

    private void initBluetooth() {
        BluetoothManager mBluetoothManager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);
        if (mBluetoothManager != null) {
            mBluetoothAdapter = mBluetoothManager.getAdapter();
            if (mBluetoothAdapter != null) {
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();  //打开蓝牙
                }
            }
        }
    }

    public void startLeScan() {
        if (mBluetoothAdapter == null) {
            return;
        }
        if (isScanning) {
            return;
        }
        isScanning = true;
        mBluetoothAdapter.startLeScan(mLeScanCallback);   //此mLeScanCallback为回调函数
    }

    public void stopLeScan() {
        if (mBluetoothAdapter == null) {
            return;
        }
        if (isScanning) {
            return;
        }
        isScanning = true;
        mBluetoothAdapter.cancelDiscovery();
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int arg1, byte[] arg2) {
            Log.i(TAG, "onLeScan() DeviceName------>" + device.getName());   //在这里可通过device这个对象来获取到搜索到的ble设备名称和一些相关信息
            if (device.getName() == null) {
                return;
            }
            if (device.getName().contains("LAPTOP-K1EOD4OP")) {      //判断是否搜索到你需要的ble设备
                Log.i(TAG, "onLeScan() DeviceAddress------>" + device.getAddress());
                mBluetoothDevice = device;    //获取到周边设备
                stopLeScan();    //1、当找到对应的设备后，立即停止扫描；2、不要循环搜索设备，为每次搜索设置适合的时间限制。避免设备不在可用范围的时候持续不停扫描，消耗电量。
                connect();   //连接
            }
        }
    };

    public boolean connect() {
        if (mBluetoothDevice == null) {
            Log.i(TAG, "BluetoothDevice is null.");
            return false;
        }
//两个设备通过BLE通信，首先需要建立GATT连接。这里我们讲的是Android设备作为client端，连接GATT Server
        mBluetoothGatt = mBluetoothDevice.connectGatt(this, false, mGattCallback); //mGattCallback为回调接口
        if (mBluetoothGatt != null) {
            if (mBluetoothGatt.connect()) {
                Log.d(TAG, "Connect succeed.");
                return true;
            } else {
                Log.d(TAG, "Connect fail.");
                return false;
            }
        } else {
            Log.d(TAG, "BluetoothGatt null.");
            return false;
        }
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices(); //执行到这里其实蓝牙已经连接成功了
                Log.i(TAG, "Connected to GATT server.");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                if (mBluetoothDevice != null) {
                    Log.i(TAG, "重新连接");
                    connect();
                } else {
                    Log.i(TAG, "Disconnected from GATT server.");
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {

            System.out.println("1111111111111111status = "+status);

            //读取到值，在这里读数据
            if (status == BluetoothGatt.GATT_SUCCESS) {

                final byte[] data = characteristic.getValue();
                for (int i = 0; i < data.length; i++) {
                    System.out.println("data......" + data[i]);
                }
                if (data != null && data.length > 0) {
                    final StringBuilder stringBuilder = new StringBuilder(data.length);
                    for (byte byteChar : data)
                        //以十六进制的形式输出
                        System.out.println(stringBuilder.append(String.format("%02X ", byteChar)));
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] arrayOfByte = characteristic.getValue();
            System.out.println("=========="+arrayOfByte);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            for (BluetoothGattService gattService : gatt.getServices()){
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                for(BluetoothGattCharacteristic gattCharacteristic:gattCharacteristics){
                    int prop = gattCharacteristic.getProperties();
                    if((prop & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0){
                        System.out.println(gattService.getUuid()+"======"+gattCharacteristic.getUuid()+"可写属性");
                    }
                    if((prop & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0){
                        System.out.println(gattService.getUuid()+"======"+gattCharacteristic.getUuid()+"通知属性");
                    }
                    if((prop & BluetoothGattCharacteristic.PROPERTY_READ) > 0){
                        System.out.println(gattService.getUuid()+"======"+gattCharacteristic.getUuid()+"可读属性");
                        gatt.readCharacteristic(gattCharacteristic);
                    }
                }
            }

            for (int i = 0; i < gatt.getServices().size(); i++) {
                System.out.println(gatt.getServices().get(i).toString());
            }
        }
    };

    private class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获得扫描到的远程蓝牙设备
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.i(TAG, "onLeScan() DeviceName------>" + device.getName());   //在这里可通过device这个对象来获取到搜索到的ble设备名称和一些相关信息
            if (device.getName() == null) {
                return;
            }
            if (device.getName().contains("PAB1000160810455")) {      //判断是否搜索到你需要的ble设备
                Log.i(TAG, "onLeScan() DeviceAddress------>" + device.getAddress());
                mBluetoothDevice = device;    //获取到周边设备
                stopLeScan();    //1、当找到对应的设备后，立即停止扫描；2、不要循环搜索设备，为每次搜索设置适合的时间限制。避免设备不在可用范围的时候持续不停扫描，消耗电量。
                connect();   //连接
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

}
