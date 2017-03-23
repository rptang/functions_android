package com.project.rptang.android.zxing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.project.rptang.android.R;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.zhy.autolayout.AutoLayoutActivity;

public class ZxingActivity extends AutoLayoutActivity {

    private TextView tv_result;
    private EditText et_content;
    private ImageView iv_qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing);

        tv_result = (TextView) findViewById(R.id.ttv_result);
        et_content = (EditText) findViewById(R.id.et_content);
        iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
    }

    public void scan(View view) {
        startActivityForResult(new Intent(ZxingActivity.this, CaptureActivity.class), 0);
        customScan();
    }

    public void customScan(){
//        new IntentIntegrator(this)
//                .setOrientationLocked(false)
//                .setCaptureActivity(CustomScanActivity.class) // 设置自定义的activity是CustomActivity
//                .initiateScan(); // 初始化扫描
    }

    public void make(View view){
        String input = et_content.getText().toString();
        if(input.equals("")){
            Toast.makeText(ZxingActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
        }else {
//            Bitmap bitmap = EncodingUtils.createQRCode(input,500,500,null);
            Bitmap bitmap = EncodingUtils.createQRCode(input,500,500,
                    BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
            iv_qrcode.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            tv_result.setText(result);
        }
    }
}
