package ug.phonecardpreject.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yuwei.utils.Card;
import com.yuwei.utils.Cryptography;
import com.yuwei.utils.Hex;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ug.phonecardpreject.R;

/**
 * 对CPU卡演示操作
 */
public class CardCPUActivity extends Activity  {

    public final static int FILE_1_LEN = 246;
    public final static int FILE_2_LEN = 48;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            info.append(msg.obj.toString());
            info.append("\n");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sv.scrollTo(0, info.getHeight());
                }
            }, 300);
        }
    };
    private TextView info;
    private View sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);
        info = ((TextView) findViewById(R.id.info));
        sv = findViewById(R.id.sv);
        Card.onLog(handler);
        Card.init(115200);
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

    int id = -1;

    private static final String LOG = "---XinActivity";

    public void click(View v) {
        boolean result = false;

        switch (v.getId()) {
            case R.id.cardid: {
                //获取卡id
                byte[] id = Card.getID();
                //Toast.makeText(this, Hex.toHexString(id), Toast.LENGTH_LONG).show();
                info.append("卡ID:"+ Hex.toHexString(id)+"\n");

                break;
            }

            case R.id.clear: {
                //清除
                result = Card.clearStruct(Cryptography.WPWD);
                result(result);
                break;
            }

            case R.id.init: {
                // 初始化
                result = Card.initStruct(Cryptography.WPWD);
                Toast.makeText(this, ""+result, Toast.LENGTH_SHORT).show();
                result(result);
                break;
            }

            case R.id.create01: {
                //添加文件1
                result = Card.addFile((short) 1, (short) FILE_1_LEN, false, Cryptography.WPWD);
                result(result);
                break;
            }


            case R.id.write01: {
                //写文件1
                String c = "";
                /*for(int i=0;i<10;i++) {
                    c += "abcdefghijklmno";
                }*/

                /*for(int i=0;i<12;i++) {
                    c += "0123456789";
                }*/
                //List<Byte> content = Hex.bytesToList(c.getBytes());
                List<Byte> content = new ArrayList<Byte>();
                for(int i=0;i<FILE_1_LEN;i++) {
                    content.add((byte)0xff);
                }


                //将内容写入文件ox02中
                result= Card.writeFile((short)1, (short)FILE_1_LEN, content, Cryptography.WPWD);
                result(result);
                break;
            }

            case R.id.read01: {
                // 读取文件1
                ArrayList<Byte> content = new ArrayList<Byte>();
                boolean b = Card.readFile((short) 1, (short) FILE_1_LEN, content, null);
                if (b) {
                    byte[] bs = Hex.listTobytes(content);
                    String s = Hex.byteListToString(content);
                    info.append("文件1内容:"+s+"\n");
                    //Toast.makeText(this, Hex.byteArrayToString(bs), Toast.LENGTH_SHORT).show();
                }
                break;
            }


            case R.id.create02: {
                //添加文件2
                result = Card.addFile((short) 2, (short) FILE_2_LEN, true, Cryptography.WPWD);
                result(result);

                if(Card.beginSeries(Cryptography.WPWD)){
                    result = Card.addFileSeries((short) 3, (short) 30, false);
                    result = Card.addFileSeries((short) 4, (short) 30, false);
                    result = Card.addFileSeries((short) 5, (short) 30, false);
                    result = Card.addFileSeries((short) 6, (short) 30, false);
                    result = Card.addFileSeries((short) 7, (short) 30, false);
                    result = Card.addFileSeries((short) 8, (short) 30, false);
                    result = Card.addFileSeries((short) 9, (short) 30, false);
                }
                Card.endSeries();

                break;
            }

            case R.id.write02: {
                //写文件2
                SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String c = "来，笑一个！"+ df.format(new Date());
                List<Byte> content = Hex.bytesToList(c.getBytes());

                //将内容写入文件ox02中
                result= Card.writeFile((short)2, (short)FILE_2_LEN, content, Cryptography.WPWD);
                result(result);


                //连续写文件
                if(Card.beginSeries(Cryptography.WPWD)){
                    content = Hex.bytesToList("文件3的内容".getBytes());
                    result = Card.writeFileSeries((short) 3, (short) 30, content);

                    content = Hex.bytesToList("文件4的内容".getBytes());
                    result = Card.writeFileSeries((short) 4, (short) 30, content);

                    content = Hex.bytesToList("文件5的内容".getBytes());
                    result = Card.writeFileSeries((short) 5, (short) 30, content);

                    content = Hex.bytesToList("文件6的内容".getBytes());
                    result = Card.writeFileSeries((short) 6, (short) 30, content);

                    content = Hex.bytesToList("文件7的内容".getBytes());
                    result = Card.writeFileSeries((short) 7, (short) 30, content);

                    content = Hex.bytesToList("dfsdfsdfe".getBytes());
                    result = Card.writeFileSeries((short) 8, (short) 30, content);

                    content = Hex.bytesToList("文件9的内容".getBytes());
                    result = Card.writeFileSeries((short) 9, (short) 30, content);
                }
                Card.endSeries();
                break;
            }

            case R.id.read02: {
                // 读取文件2
                ArrayList<Byte> content = new ArrayList<Byte>();
                boolean b = Card.readFile((short) 2, (short) FILE_2_LEN, content, Cryptography.WPWD);
                if (b) {
                    byte[] bs = Hex.listTobytes(content);
                    Toast.makeText(this, Hex.byteArrayToString(bs), Toast.LENGTH_SHORT).show();
                }

                //连续读文件
                String ss = "";
                /*for(int i=3;i<=9;i++){
                    result = Card.readFile((short) i, (short) 30, content, Cryptography.WPWD);
                    ss += "文件"+i+"："+Hex.byteListToString(content)+"\n";
                }*/
                if(Card.beginSeries(null)){
                    result = Card.readFileSeries((short) 3, (short) 30, content);
                    ss += "\n文件3："+ Hex.byteListToString(content)+"\n";

                    result = Card.readFileSeries((short) 4, (short) 30, content);
                    ss += "文件4："+ Hex.byteListToString(content)+"\n";

                    result = Card.readFileSeries((short) 5, (short) 30, content);
                    ss += "文件5："+ Hex.byteListToString(content)+"\n";

                    result = Card.readFileSeries((short) 6, (short) 30, content);
                    ss += "文件6："+ Hex.byteListToString(content)+"\n";

                    result = Card.readFileSeries((short) 7, (short) 30, content);
                    ss += "文件7："+ Hex.byteListToString(content)+"\n";

                    result = Card.readFileSeries((short) 8, (short) 30, content);
                    ss += "文件8："+ Hex.byteListToString(content)+"\n";

                    result = Card.readFileSeries((short) 9, (short) 30, content);
                    ss += "文件9："+ Hex.byteListToString(content)+"\n";
                }
                Card.endSeries();

                Toast.makeText(this, ss, Toast.LENGTH_SHORT).show();

                break;
            }
            case  R.id.clc: {
                info.setText("");
                break;
            }
            case  R.id.button1: { // 修改波特率
                Card.changbps(115200);
                break;
            }
        }
//        Card.bell(100);
        Card.rf_beep(50);
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
                        Toast.makeText(this, bytes, Toast.LENGTH_LONG).show();
                        Log.i("sss",bytes);
//                        Card.bell(100);
                        Card.rf_beep(50);
                    }
                    break;
                case KeyEvent.KEYCODE_1:
                    Card.rf_beep(50);
                    info.append("开始循环读卡!\n");

                    /**
                     * 开启循环读取文件
                     * @param listener 读取回调
                     * @param repeat 是否重复回调 如果 true  则有卡时 每隔sleepMS 毫秒 回调一次，false 则表示在卡拿出前 只回调一次
                     * @param sleepMS  寻卡时间间隔 毫秒数
                     */
                    Card.startLoopRead(new Card.loopReadListener(){
                        @Override
                        public void getID(byte[] cardId) {
                            //寻卡成功时回调
                            final String s = "寻卡成功:"+ Hex.toHexString(cardId);

/*                            //连续读文件
                            final StringBuilder ss = new StringBuilder();
                            boolean result = false;
                            List<Byte> content = new ArrayList<Byte>();
                            //如果只是读取不需要加密的文件， key 为null
                            if(Card.beginSeries(null)){
                                result = Card.readFileSeries((short) 3, (short) 30, content);
                                ss .append( "\n文件3："+Hex.byteListToString(content)+"\n");

                                result = Card.readFileSeries((short) 4, (short) 30, content);
                                ss .append("文件4："+Hex.byteListToString(content)+"\n");

                                result = Card.readFileSeries((short) 5, (short) 30, content);
                                ss .append("文件5："+Hex.byteListToString(content)+"\n");

                                result = Card.readFileSeries((short) 6, (short) 30, content);
                                ss .append( "文件6："+Hex.byteListToString(content)+"\n");

                                result = Card.readFileSeries((short) 7, (short) 30, content);
                                ss .append( "文件7："+Hex.byteListToString(content)+"\n");

                                result = Card.readFileSeries((short) 8, (short) 30, content);
                                ss .append(  "文件8："+Hex.byteListToString(content)+"\n");

                                result = Card.readFileSeries((short) 9, (short) 30, content);
                                ss .append( "文件9："+Hex.byteListToString(content)+"\n");
                            }
                            Card.endSeries();*/

                            runOnUiThread(new Thread(){
                                @Override
                                public void run() {
                                    info.append(s + "\n");
//                                    info.append(ss.toString() + "\n");
                                }
                            });
                            Card.rf_beep(50);
                        }
                    }, false, 400);
                    break;
                case KeyEvent.KEYCODE_2:
                    Card.rf_beep(50);
                    Card.endLoopRead();
                    info.append("结束循环读卡!\n");
                    break;

                case KeyEvent.KEYCODE_3:
                    Card.rf_beep(50);
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private String ids;
    private byte[] content;

    private void result(boolean result) {
        if (result) {
            info.append("********** 执行成功 ***********\n");
        } else {
            info.append("********** 执行失败 ***********\n");
        }
    }
}
