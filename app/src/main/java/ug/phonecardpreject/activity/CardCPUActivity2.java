package ug.phonecardpreject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yuwei.utils.Card;
import com.yuwei.utils.Hex;

import java.util.Timer;
import java.util.TimerTask;

import ug.phonecardpreject.R;

/**
 * 对CPU卡演示操作
 */
public class CardCPUActivity2 extends Activity {


    private TextView title;
    private TextView info;
    private Button back;
    private Button read_id;

    private boolean isfirst = true;

    boolean isStop = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = findViewById(R.id.title);
        info = ((TextView) findViewById(R.id.card_no));
        back = ((Button) findViewById(R.id.back));
        read_id = ((Button) findViewById(R.id.read_id));
        title.setText("二维码验票");
        read_id.setVisibility(View.VISIBLE);
        Card.onLog(handler);
        Card.init(115200);
        DisplayMetrics dm = new DisplayMetrics();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // info.setText(msg.obj.toString());
        }
    };

    public void click(View view) {
        switch (view.getId()){
            case R.id.back:
                if(isfirst){
                    isfirst = false;
                    finish();
                }
                break;
            case R.id.read_id:
                info.setText("");
                //获取卡id
                byte[] id = Card.getID();
                String id_code = Hex.toHexString(id);
                if(id_code != null){
                    info.setText(Hex.toHexString(id) + "00");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Card.offLog();
        Card.exit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Card.endLoopRead();
    }

    /**
     * 按键处理
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getAction()== KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_F11:
                    String bytes = Card.oneDimensionalCode();
                    if (bytes != null) {
                        Log.i("sss",bytes);
                        info.setText(bytes);
                        Card.rf_beep(20);
                    }
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }


}
