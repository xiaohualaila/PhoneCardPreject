package ug.phonecardpreject;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;
import com.yuwei.utils.Card;
import com.yuwei.utils.CardM1;
import com.yuwei.utils.ModuleControl;
import java.util.Date;
import static com.yuwei.utils.CardCommon.log;
import static com.yuwei.utils.CardM1.rf_read;

/**
 * 对M1卡演示类
 */
public class M1Activity extends BaseActivity {
    /**
     * 扇区号
     */
    public static final byte _SECNR = 8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_m1);
        super.onCreate(savedInstanceState);
        CardM1.init(this,115200);
        CardM1.onLog(handler);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_F11:
                    String bytes = Card.oneDimensionalCode();
                    if (bytes != null) {
                        Toast.makeText(this, bytes, Toast.LENGTH_LONG).show();
                        Card.bell(100);
                    }
                    break;
                case KeyEvent.KEYCODE_F1: {
                    log("按键功能描述");
                    log("8、写入内容");
                    log("9、读取内容");
                    CardM1.rf_beep(200);
                    break;
                }
                case KeyEvent.KEYCODE_1: {
                     CardM1.rf_request();
                    break;
                }
                case KeyEvent.KEYCODE_2: {
                    CardM1.rf_anticoll();
                    break;
                }
                case KeyEvent.KEYCODE_3: {
                    CardM1.rf_select();
                    break;
                }
                case KeyEvent.KEYCODE_4: {
                    CardM1.rf_load_key(_SECNR, CardM1.KEY);
                    break;
                }
                case KeyEvent.KEYCODE_5: {
                    CardM1.rf_authentication(_SECNR);
                    break;
                }
                case KeyEvent.KEYCODE_6: {
                    CardM1.rf_write(_SECNR, new Date().toLocaleString().getBytes());
                    break;
                }
                case KeyEvent.KEYCODE_7: {
                    byte[] bytes1 = new byte[16];
                    boolean b = rf_read(_SECNR, bytes1);
                    if (b) {
                        Toast.makeText(this,new String(bytes1), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case KeyEvent.KEYCODE_8: { //写完整操作
                    CardM1.rf_write(_SECNR, new Date().toGMTString().getBytes(), CardM1.KEY);
                    break;
                }
                case KeyEvent.KEYCODE_9: {  //读完整操作
                    byte[] bytes1 = new byte[16];
                    boolean b = CardM1.rf_read(_SECNR, bytes1, CardM1.KEY);
                    if (b) {
                        Toast.makeText(this,new String(bytes1), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("M1Activity.onDestroy");
        ModuleControl.rf_rfinf_reset(CardM1.id, (byte) 0);
        CardM1.offLog();
        CardM1.exit();
    }
}
