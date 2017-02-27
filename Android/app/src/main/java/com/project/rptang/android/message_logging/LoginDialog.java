package com.project.rptang.android.message_logging;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.project.rptang.android.R;
import com.project.rptang.android.custom_upload_image.SelectDialog;

import static com.project.rptang.android.R.id.txt_cancel;

/**
 * Created by rptang on 2017/2/27.
 */

public class LoginDialog {

    private Context mContext;
    private TextView txt_system_info,txt_login_confirm;
    private ImageView iv_system_info;
    private Dialog dialog;

    public LoginDialog(Context context) {
        this.mContext = context;
    }

    @SuppressWarnings("deprecation")
    public LoginDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.login_dialog, null);

        // 获取自定义Dialog布局中的控件
        iv_system_info = (ImageView) view.findViewById(R.id.iv_system_info);
        txt_system_info = (TextView) view.findViewById(R.id.txt_system_info);
        txt_login_confirm = (TextView) view.findViewById(R.id.txt_login_confirm);
        txt_login_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 定义Dialog布局和参数
        dialog = new Dialog(mContext, R.style.DialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }

    public LoginDialog setTextSystemInfo(String info){
        txt_system_info.setText(info);
        return this;
    }

    public LoginDialog setImageSystemInfo(Drawable drawable){
        iv_system_info.setBackground(drawable);
        return this;
    }

    public LoginDialog setDismissOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show(){
        dialog.show();
    }



}
