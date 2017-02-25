package com.project.rptang.android.upload_profile.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.project.rptang.android.R;
import com.project.rptang.android.upload_profile.utils.ImageUtils;
import com.project.rptang.android.upload_profile.view.CircleImageView;

import java.io.File;

public class UploadProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView mCircleImageView;
    protected Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile);

        mCircleImageView = (CircleImageView)findViewById(R.id.circle_image);
        mCircleImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ImageUtils.takeOrChoosePhoto(this, ImageUtils.TAKE_OR_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ImageUtils.TAKE_OR_CHOOSE_PHOTO:
                //获取到了原始图片
                File f = ImageUtils.getPhotoFromResult(this, data);
                //裁剪方法
                ImageUtils.doCropPhoto(this, f);
                break;
            case ImageUtils.PHOTO_PICKED_WITH_DATA:
                //获取到剪裁后的图片
                bitmap = ImageUtils.getCroppedImage();
                mCircleImageView.setImageBitmap(bitmap);
                if (bitmap!=null){
                    //TODO 这里可以执行上传头像方法
                }
                break;
        }
    }
}
