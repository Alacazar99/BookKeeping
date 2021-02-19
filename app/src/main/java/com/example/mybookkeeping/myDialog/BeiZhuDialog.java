package com.example.mybookkeeping.myDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mybookkeeping.R;
import com.example.mybookkeeping.utils.KeyBoardUtils;

import java.util.List;

public class BeiZhuDialog extends Dialog implements View.OnClickListener {

    EditText et;
    Button cancelBtn,sureBtn;
    OnEnsureListener onEnsureListener;
    private Context context;


    public KeyBoardUtils boardUtils;


    // 设置回调接口的方法；
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public BeiZhuDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_beizhu);
        et = findViewById(R.id.dialog_beizhu_et);
        cancelBtn = findViewById(R.id.dialog_beizhu_btn_cancel);
        sureBtn = findViewById(R.id.dialog_beizhu_btn_sure);
        cancelBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
    }

    public interface OnEnsureListener{
        public void onEnsure();
        public void onCancel();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_beizhu_btn_cancel:
                if (onEnsureListener!=null) {
                    onEnsureListener.onCancel();
                }
                break;
            case R.id.dialog_beizhu_btn_sure:
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure();
                }
                break;
        }
    }

    // 获取输入数据的方法；
    public String getEditText(){
        return et.getText().toString().trim();
    }

    // 设置Dialog的尺寸和屏幕尺寸一致；
    public void setDialogSize(){
        // 获取当前窗口对象；
        Window window  = getWindow();
        // 获取窗口对象参数；
        WindowManager.LayoutParams wlp = window.getAttributes();
        // 获取屏幕宽度；
        Display dWidth = window.getWindowManager().getDefaultDisplay();
        wlp.width = dWidth.getWidth();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
        handler.sendEmptyMessageDelayed(1,50);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
