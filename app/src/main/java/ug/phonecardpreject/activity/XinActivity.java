package ug.phonecardpreject.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.yuwei.utils.Hex;
import com.yuwei.utils.ModuleControl;
import com.yuwei.utils.Ultralight;
import ug.phonecardpreject.R;
import ug.phonecardpreject.base.BaseActivity;
import ug.phonecardpreject.base.ViewHolder;


public class XinActivity extends BaseActivity {
    boolean isStop = false;
    private TextView card_no;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        setTitle("芯片验票");
        card_no = holder.get(R.id.card_no);

    }

    @Override
    protected void onResume() {
        super.onResume();
        start();//获取焦点的时候每隔三秒开始检票;
    }

    private void start() {
        isStop = false;
        Ultralight.onLog(handler);
        Ultralight.init(this, 115200);
        MyTread myTread = new MyTread();
        myTread.setPriority(Thread.MAX_PRIORITY);
        myTread.start();//读卡;

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (msg.what == -1) {
                card_no.setText(msg.obj.toString());

            } else if (msg.what == 1) {
                card_no.setText(msg.obj.toString());
            }
        }
    };

    @Override
    public void onClick(View v) {
    }

    /*
   * 读写卡处理线程;
   */
    class MyTread extends Thread {
        @Override
        public void run() {    //每0.1秒读卡一次;
            try {
                while (!isStop) {
                    byte[] id = Ultralight.getID();
                    if (id == null) {
                        Message msg = Message.obtain();
                        msg.what = -1;
                        msg.obj = "请放票卡";
                        handler.sendMessage(msg);
                    } else {
                        String s = Hex.toHexString(id);//获取到卡片ID值之后(16进制数组转化为字符串);
                        Log.i("sss", ">>>>>>>>" + s);
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = s;
                        handler.sendMessage(msg);
                    }
                    Thread.sleep(300);
                }
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStop = true;
        System.out.println("M1Activity.onDestroy");
        ModuleControl.rf_rfinf_reset(Ultralight.id, (byte) 0);
        Ultralight.offLog();
        Ultralight.exit();
    }


}
