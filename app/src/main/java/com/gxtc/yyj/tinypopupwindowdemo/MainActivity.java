package com.gxtc.yyj.tinypopupwindowdemo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gxtc.yyj.tinypopupwindowdemo.tinypopupwindow.TinyPopupWindow;


public class MainActivity extends AppCompatActivity implements TinyPopupWindow.OnClickListener {

    TinyPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        popupWindow = TinyPopupWindow
                .builder()
                .withContext(this)
                .setContentView(R.layout.view_popup)
                .setBackgroundDrawable(new ColorDrawable(Color.GREEN))
                .setAnimationStyle(R.style.MyPopupAnim)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .setClippingEnabled(true)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnDismissListener(new TinyPopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Toast.makeText(MainActivity.this, "我消失了", Toast.LENGTH_SHORT).show();
                    }
                })
//                .setText(R.id.btn_left, "设置文字左边")
//                .setText(R.id.btn_right, "设置文字右边")
                .setText(new int[]{R.id.btn_left, R.id.btn_right}, new String[]{"左手边", "右手边"})
//                .setOnClickListener(R.id.btn_left,this)
//                .setOnClickListener(R.id.btn_right,this)
                .setOnClickListener(new int[]{R.id.btn_left, R.id.btn_right}, this)
                .build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                Toast.makeText(this, "我是左边文字", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
            case R.id.btn_right:
                Toast.makeText(this, "我是右边文字", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
        }
    }

    public void show(View view) {
        if (!popupWindow.isShowing())
            popupWindow.showAsDropDown(findViewById(R.id.btn_show));
    }
}
