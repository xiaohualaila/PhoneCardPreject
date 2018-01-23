package ug.phonecardpreject.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yuwei.utils.Hex;
import com.yuwei.utils.ModuleControl;
import com.yuwei.utils.Ultralight;

import java.util.Timer;
import java.util.TimerTask;

import ug.phonecardpreject.R;
import ug.phonecardpreject.activity.CardCPUActivity2;

public class XinActivity extends AppCompatActivity {
    boolean isStop = false;
    private TextView title;
    private TextView card_no;
    private Button back;

    private boolean isfirst = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        card_no = findViewById(R.id.card_no);
        back = ((Button) findViewById(R.id.back));
        title.setText("芯片验票");
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

    public void click(View view) {
        if(isfirst) {
            isfirst = false;
            finish();
        }
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
