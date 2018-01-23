package ug.phonecardpreject.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import ug.phonecardpreject.R;
import ug.phonecardpreject.base.BaseActivity;
import ug.phonecardpreject.base.ViewHolder;

/**
 * Created by Administrator on 2018/1/23.
 */

public class ShowActivity  extends BaseActivity {
    LinearLayout ll_scan;
    LinearLayout ll_xin;
    LinearLayout ll_faxing;
    LinearLayout ll_clean_data;
    LinearLayout ll_data_down;
    LinearLayout ll_data_up;
    LinearLayout ll_setup;
    LinearLayout ll_quit;
    private boolean isBackKeyPressed = false;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_show;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        ll_scan = holder.get(R.id.ll_scan);
        ll_xin = holder.get(R.id.ll_xin);
        ll_faxing = holder.get(R.id.ll_faxing);
        ll_clean_data = holder.get(R.id.ll_clean_data);
        ll_data_down = holder.get(R.id.ll_data_down);
        ll_data_up = holder.get(R.id.ll_data_up);
        ll_setup = holder.get(R.id.ll_setup);
        ll_quit = holder.get(R.id.ll_quit);

        holder.setOnClickListener(this, R.id.ll_scan,R.id.ll_xin,R.id.ll_faxing,R.id.ll_clean_data,
                R.id.ll_data_down,R.id.ll_data_up,R.id.ll_setup,R.id.ll_quit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_scan:
                openActivity(CardCPUActivity2.class);
                break;
            case R.id.ll_xin:
                openActivity(XinActivity.class);
                break;
            case R.id.ll_faxing:
                toastShort("等待添加功能");
                break;
            case R.id.ll_clean_data:
                toastShort("等待添加功能");
                break;
            case R.id.ll_data_down:
                toastShort("等待添加功能");
                break;
            case R.id.ll_setup:
                toastShort("等待添加功能");
                break;
            case R.id.ll_quit:
               finish();
                break;


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(isBackKeyPressed) {
                finish();
            }
            else {
                isBackKeyPressed = true;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    public void run() {
                        isBackKeyPressed = false;
                    }
                };
                timer.schedule(timerTask, 2000);//600毫秒后无再次点击，则复位
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
