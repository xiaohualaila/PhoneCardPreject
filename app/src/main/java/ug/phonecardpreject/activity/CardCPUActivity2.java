package ug.phonecardpreject.activity;

import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.yuwei.utils.Card;
import com.yuwei.utils.Hex;
import ug.phonecardpreject.R;
import ug.phonecardpreject.base.BaseActivity;
import ug.phonecardpreject.base.ViewHolder;

/**
 * 对CPU卡演示操作
 */
public class CardCPUActivity2 extends BaseActivity {

    private TextView info;
    private Button read_id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        setTitle("二维码验票");
        info = holder.get(R.id.card_no);
        read_id = holder.get(R.id.read_id);
        read_id.setVisibility(View.VISIBLE);
        Card.onLog(handler);
        Card.init(115200);

        holder.setOnClickListener(this,R.id.read_id);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // info.setText(msg.obj.toString());
        }
    };


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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
}
