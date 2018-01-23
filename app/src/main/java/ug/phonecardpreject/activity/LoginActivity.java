package ug.phonecardpreject.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ug.phonecardpreject.R;
import ug.phonecardpreject.api.login.event.LoginEvent;
import ug.phonecardpreject.base.BaseActivity;
import ug.phonecardpreject.base.ViewHolder;
import ug.phonecardpreject.log.Logger;

/**
 * Created by Administrator on 2018/1/23.
 */

public class LoginActivity extends BaseActivity {
    EditText mUsername;
    EditText mPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        setTitle("");
        mUsername = holder.get(R.id.username);
        mPassword = holder.get(R.id.password);

        holder.setOnClickListener(this, R.id.login);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
//                String name = mUsername.getText().toString();
//                String pswd = mPassword.getText().toString();
//                if (name.isEmpty() || pswd.isEmpty()) {
//                    toastShort("账号或密码不能为空");
//                    return;
//                }
//                if(!name.equals("yirenyipiao")){
//                    toastShort("账号有误");
//                    return;
//                }
//                if(!pswd.equals("123456")){
//                    toastShort("密码有误");
//                    return;
//                }
                openActivity(ShowActivity.class);
                finish();
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        registerKeyboardListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterKeyboardListener();
    }

    private void registerKeyboardListener() {
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Logger.e("onGlobalLayout");
                if (isKeyboardShown(rootView)) {
                    Logger.e("软键盘弹起");
                    getViewHolder().get(R.id.span1).setVisibility(View.GONE);
                    getViewHolder().get(R.id.span2).setVisibility(View.GONE);
                } else {
                    Logger.e("软键盘未弹起");
                    getViewHolder().get(R.id.span1).setVisibility(View.INVISIBLE);
                    getViewHolder().get(R.id.span2).setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void unRegisterKeyboardListener() {
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(null);
    }

    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }
}
