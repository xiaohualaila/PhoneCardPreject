package ug.phonecardpreject.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.yuwei.utils.Hex;
import com.yuwei.utils.ModuleControl;
import com.yuwei.utils.Ultralight;

import java.util.Timer;
import java.util.TimerTask;

import ug.phonecardpreject.R;
import ug.phonecardpreject.base.*;
import ug.phonecardpreject.bean.Card;
import ug.phonecardpreject.storage.Sp;
import ug.phonecardpreject.util.Bmp;
import ug.phonecardpreject.util.C;
import ug.phonecardpreject.util.SBmp;
import ug.phonecardpreject.util.UL;
import ug.phonecardpreject.util.Utils;

import static ug.phonecardpreject.util.C.sql;
import static ug.phonecardpreject.util.UL.verifyID_C;
import static ug.phonecardpreject.util.UL.verifyID_U;


/**
 * 这个界面为四个模块功能的公用界面:发行功能,芯片ID检验模式功能,芯片秘钥检验模式功能,3.用户秘钥检验模式功能;
 */
public class IDActivity extends ug.phonecardpreject.base.BaseActivity {
    // Content View Elements
    private TextView tv_total, tv, tv_id;
    private EditText et_fa;
    private CheckBox cb_fa;
    private String mId = "";
    private String mId2 = "";
    /**
     * 打包
     */
    private boolean isPack = false;
    private int intet;
    private ImageView iv_correct, iv_result;
    private Timer timer;
    private int type;
    // End Of Content View Elements
    private LinearLayout ll_pwd, item_pack;
    private EditText et_pwd;
    private CheckBox cb_pwd;
    /**
     * 明文秘钥
     */
    private String cs;
    private Button bt_updata;


    int mt;
    /**
     * 芯票ID检验模式
     */
    public static final int XPID = 2;
    /**
     * 出厂发行;
     */
    public static final int FAC = 1;
    /**
     * 芯票秘钥模式;
     */
    public static final int YANC = 3;
    /**
     * 用户秘钥模式;
     */
    public static final int YANUID = 4;
    //默认为芯票ID模式;
    public static final int MSG_ERROR = 600;
    /**
     * 新增芯片秘钥实时监测模式(联网检验);
     */
    public static final int YANMODE_TIME = 7;
    //新增芯片秘钥实时监测模式(联网检验)IP设置错误判断;
    private static final int IP_ERROR = 8;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_id;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        setTitle("验票");
        iv_correct = holder.get(R.id.iv_correct);
        tv_total =  holder.get(R.id.tv_total);
        tv =  holder.get(R.id.tv_result);
        tv_id = holder.get(R.id.tv_id);
        et_fa = holder.get(R.id.et_fa);
        cb_fa = holder.get(R.id.cb_fa);
        iv_result = holder.get(R.id.iv_result);
        ll_pwd = holder.get(R.id.ll_pwd);
        item_pack = holder.get(R.id.item_pack);
        et_pwd = holder.get(R.id.et_pwd);
        cb_pwd = holder.get(R.id.cb_pwd);
        bt_updata = holder.get(R.id.bt_updata);

     //   type = getIntent().getIntExtra("type", XPID);
        type = getIntent().getIntExtra("type", YANC);
        switch (type) {
            case YANUID:
                if (!Utils.isCSecret()) {
                    finish();
                }
                total = sql.count(sql.YAN);
                ll_pwd.setVisibility(View.GONE);
                setTitle(getString(R.string.yan_yhmy));     //用户秘钥检验模式;
                break;
            case XPID:      //芯票ID模式;
                total = sql.count(sql.YAN);
                ll_pwd.setVisibility(View.GONE);
                setTitle(getString(R.string.yan_xpid));   //芯票ID检验模式;
                break;
            case FAC:   //出厂设置的Activity中进入;
                ((TextView) findViewById(R.id.tv_c)).setText("本机发行总数：");
                item_pack.setVisibility(View.VISIBLE);
                String[] strings = Sp.getcSecret();
                if (strings != null) {
                    cs = strings[2];
                } else {
                    Toast.makeText(this, "请设置出厂秘钥", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                bt_updata.setVisibility(View.VISIBLE);
                total = sql.count(sql.FAC);                 //查询数据库出厂发行的总数;
                ll_pwd.setVisibility(View.VISIBLE);
                setTitle(getString(R.string.setting_fa));   //开始发行
                break;
            case YANC:  //芯票秘钥模式;
                if (!Utils.isCSecret()) {
                    finish();
                }
                total = sql.count(sql.YAN);
                setTitle(getString(R.string.yan_xpmy));    //芯票秘钥检验模式
                break;
            case YANMODE_TIME:
                findViewById(R.id.ll_sum).setVisibility(View.GONE);//芯票秘钥实时监测不需要统计数量;
                if (!Utils.isCSecret()) {
                    finish();
                }
                setTitle(getString(R.string.yan_time));    //芯票秘钥实时监测模式;
                break;
        }
        tv_total.setText((total) + "张");
        setListener();
    }

    private void setListener() {
        cb_fa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("IDActivity.onCheckedChanged");
                isPack = isChecked;
                et_fa.setEnabled(!isChecked);
                if (isChecked) { //连续检验;
                    intet = Integer.parseInt(et_fa.getText().toString().trim());
                    mt = total;//
                }
            }
        });

        cb_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //显示票卡秘钥;
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et_pwd.setText(cs);
                } else {
                    et_pwd.setText("");
                }
            }
        });
        bt_updata.setOnClickListener(new View.OnClickListener() {   //上传数据至服务器;
            @Override
            public void onClick(View v) {
              //  H.updatasAll(IDActivity.this, sql.FAC);
            }
        });
    }

    boolean isStop = false;
    boolean isBeep = true;
    boolean isAdd = true;
    /**
     * 总数
     */
    int total;

    private void isPack() {
        if (isPack) {
            if ((total - mt) >= intet) {    //intent为输入框设定的值;
                isStop = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("数量已够" + intet + "张是否继续？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mt = total;
                        start();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.create().show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();//获取焦点的时候每隔三秒开始检票;
    }

    private void start() {
        Ultralight.onLog(handler);
        Ultralight.init(this, 115200);
        isStop = false;
        MyTread myTread = new MyTread();
        myTread.setPriority(Thread.MAX_PRIORITY);
        myTread.start();//读卡;
//      检票时，每隔3秒上传一次数据到服务器;
//        if (type != FAC) {      //判断不是出厂发行模式就上传检票数据至服务器;
//            timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                 //   H.updatasSingle(IDActivity.this, sql.YAN); //将数据库上传至服务器;
//                    System.gc();
//                }
//            }, SLEEP_UP, SLEEP_UP);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStop = true;
//        if (timer != null) {
//            timer.cancel();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ModuleControl.rf_rfinf_reset(Ultralight.id, (byte) 0);
        Ultralight.offLog();
        Ultralight.exit();
    }

    private static final int MSG_ADD = 477;
    private static final int MSG_WARN = 2;
    Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (msg.what == -1) {
                isBeep = true;
                isAdd = false;
                tv.setTextColor(Color.WHITE);
                tv.setText(msg.obj.toString());
                tv_id.setText("");
                iv_correct.setBackground(getResources().getDrawable(R.mipmap.fanka));
                SBmp.please(iv_result);
            } else if (msg.what == MSG_ADD) {
                isAdd = true;
                tv_id.setText("芯片ID号：" + mId);
                tv_id.setTextColor(Color.GREEN);
                tv_total.setText((total) + "张");
                isPack();
                tv.setText(msg.obj.toString());
                tv.setTextColor(Color.GREEN);
                iv_correct.setBackground(getResources().getDrawable(R.mipmap.correct));
                if (type == FAC) {
                    SBmp.fa_nomal(iv_result);
                } else {
                    SBmp.yan_nomal(iv_result);
                }
            } else if (msg.what == MSG_WARN && isBeep && !isAdd) { // 警告
                isBeep = false;
/*                        if (mId.equals(mId2)) {
                            tv.setText("有效票卡");
                            tv.setTextColor(Color.GREEN);
                            tv_id.setText("票卡ID：" + mId);
                            iv_correct.setVisibility(View.VISIBLE);
                            iv_correct.setBackground(getResources().getDrawable(R.mipmap.correct));
                            return;
                        }*/
                iv_correct.setVisibility(View.VISIBLE);
                tv.setTextColor(Color.YELLOW);
                iv_correct.setBackground(getResources().getDrawable(R.mipmap.warn));
                tv.setText(msg.obj.toString());
                tv_id.setText("芯片ID号：" + mId);
                if (type == FAC) {
                    SBmp.fa_warn(iv_result);
                } else {
                    SBmp.yan_warn(iv_result);
                }
            } else if (msg.what == MSG_ERROR) {
                tv.setTextColor(Color.RED);
                tv.setText(msg.obj.toString());
                tv_id.setText("");
                iv_correct.setBackground(getResources().getDrawable(R.mipmap.error));
                if (type == FAC) {
                    SBmp.faError(iv_result, msg.obj.toString());
                } else {
                    SBmp.yanError(iv_result, msg.obj.toString());
                }
                if (type == YANMODE_TIME) {
                    tv_id.setText(msg.obj.toString());
                    tv_id.setTextColor(Color.RED);
                }
            }
        }
    };
    boolean idbeep = true;//判断符号;

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
                        idbeep = true;
                        Message msg = Message.obtain();
                        msg.what = -1;
                        msg.obj = "请放票卡";
                        handler.sendMessage(msg);
                    } else {
                        if (idbeep) {
                            idbeep = false;
                            String s = Hex.toHexString(id);//获取到卡片ID值之后(16进制数组转化为字符串);
                            System.out.println(s);
                            switch (type) {
                                case XPID: {
                                    xpid(s);              //芯片ID检验模式功能;
                                }
                                break;
                                case FAC: {             //出厂发行芯票的功能;
                                    fac(s);
                                }
                                break;
                                case YANC: {           //芯片秘钥检验模式;
                                    yanc(s);
                                }
                                break;
                                case YANUID: {          //用户秘钥检验模式;
                                    yanc(s);
                                }
                                break;
                                case YANMODE_TIME:   //用户芯片秘钥实时监测模式;
                                  //  realcheck(s);
                                    break;
                            }
                        }
                    }
                    Thread.sleep(C.SLEEP);
                }
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
        }

        private void xpid(String id) throws InterruptedException {  //同一张卡被连续检票两次也视为校验通过;
            if (!id.equals(mId)) { //如ID=100,mId=0;
                mId = id;
                if (!sql.isID(sql.YAN, id)) {
                    total++;
                    mId2 = id;
                    Card yan = new Card();
                    yan.setId(id);
                    sql.insertYAN(yan);
                    sendMessage(MSG_ADD, "有效票卡");
                } else {
                    sendMessage(2, "此票已验");
                }
            } else {
                if (mId == mId2) {
                    sendMessage(MSG_ADD, "有效票卡");
                } else {
                    sendMessage(2, "此票已验");
                }
            }
        }

        private void sendMessage(int what, Object obj) throws InterruptedException {
            Message msg = Message.obtain();
            msg.what = what;
            msg.obj = obj;
            handler.sendMessage(msg);
            if (what == MSG_ADD) {
                Utils.beep(true);   //两次的响声不一样;
            } else {
                Utils.beep(false);
            }
        }

        private void fac(String id) throws InterruptedException {
            if (!id.equals(mId)) {
                mId = id;
                if (!sql.isID(sql.FAC, id)) {       //查询数据库,如果没有此ID;
                    boolean write = UL.cwrite(id);  //写入ID;
                    if (write) {
                        if (verifyID_C(id)) {       //校验写入的ID;
                            total++;
                            Card mcard = new Card();
                            mcard.setId(id);
                            sql.insertFA(sql.FAC, mcard);  //插入写入卡的ID;
                            sendMessage(MSG_ADD, "发行成功");
                        } else {
                            mId = "-1";
                            sendMessage(MSG_ERROR, "发行失败");  //验证失败;
                        }
                    } else {
                        mId = "-1";
                        sendMessage(MSG_ERROR, "写入失败");  //写入失败;
                    }
                } else {
                    sendMessage(2, "此票已发行");    //数据库已经记录的卡;
                }
            } else {
                sendMessage(2, "此票已发行");    //相同ID的卡;
            }
        }

        /**
         * 出厂检票，或用户id检票
         *
         * @param id
         * @throws InterruptedException
         */
        private void yanc(String id) throws InterruptedException {
            if (!id.equals(mId)) {
                mId = id;
                if (!sql.isID(sql.YAN, id)) {
                 //   if (true) {
                    if (verifyID_C(id)) {
                        if (YANUID == type) {          //用户秘钥模式;
                            if (!verifyID_U(id)) {      //用户如果没有加密芯片ID的话校验失败;
                                mId = "-1";
                                sendMessage(MSG_ERROR, "用户校验失败");
                                return;
                            }
                        }
                        mId2 = id;
                        total++;
                        Card y = new Card();
                        y.setId(id);
                        sql.insertYAN(y);
                        sendMessage(MSG_ADD, "有效票卡");
                    } else {
                        mId = "-1";
                        sendMessage(MSG_ERROR, "无效票卡");  //卡ID校验失败;
                    }
                } else {
                    sendMessage(2, "此票已验");              //数据库已保存;
                }
            } else {
                if (mId == mId2) {
                    sendMessage(MSG_ADD, "有效票卡");      //检验的是同一张卡的ID;
                } else {
                    sendMessage(2, "此票已验");
                }
            }
        }

        /**
         * 芯票秘钥实时监测模式;
         *
         * @param CardID
         * @throws Exception
         */
//        public void realcheck(String CardID) throws Exception {
//            if (!Sp.getIP().equals(H.SERVERID)) {
//                System.out.println(Sp.getIP());
//                sendMessage(MSG_ERROR, "服务器地址错误,请重新确认");
//                return;
//            }
//            boolean[] serverResult = H.getServerResult(CardID);
//            if (serverResult[0]) {
//                if (verifyID_C(CardID)) {
//                    mId = CardID;
//                    sendMessage(MSG_ADD, "有效票卡");
//                } else {
//                    sendMessage(MSG_ERROR, "无效票证");
//                }
//            } else {
//                sendMessage(MSG_ERROR, "访问网络");
//            }
//        }
    }
}
