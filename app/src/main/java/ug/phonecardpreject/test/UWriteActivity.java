package ug.phonecardpreject.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yuwei.utils.Cryptography;
import com.yuwei.utils.Hex;
import com.yuwei.utils.ModuleControl;
import com.yuwei.utils.Ultralight;
import java.util.ArrayList;

import ug.phonecardpreject.R;
import ug.phonecardpreject.activity.XinActivity;
import ug.phonecardpreject.base.ViewHolder;
import ug.phonecardpreject.bean.Card;
import ug.phonecardpreject.bean.CardType;
import ug.phonecardpreject.storage.Sp;
import ug.phonecardpreject.storage.Sql;
import ug.phonecardpreject.util.C;
import ug.phonecardpreject.util.SBmp;
import ug.phonecardpreject.util.UL;
import ug.phonecardpreject.util.Utils;

import static java.lang.Integer.parseInt;
import static ug.phonecardpreject.util.C.ctMap;
import static ug.phonecardpreject.util.C.placeMap;
import static ug.phonecardpreject.util.C.quMap;
import static ug.phonecardpreject.util.C.sql;
import static ug.phonecardpreject.util.Utils.beep;
import static ug.phonecardpreject.util.C.csecret;
import static ug.phonecardpreject.util.C.usecret;
/**
 * 用户设置
 */
public class UWriteActivity extends  ug.phonecardpreject.base.BaseActivity  {

    public static ArrayList<String> list, listQu, listPlace;
    private TextView tv_result;
    TextView  tv_content;
    private ImageView iv_correct, iv_result;
    private int[] qph = new int[3];
    private CheckBox cb_pwd,cb_fa;
    private EditText et_serial_number,et_fa,et_pwd;
    RadioGroup rg;
    RadioButton rb1,rb2;
    Button bt_clear;

    private byte qu;
    private byte pai;
    private byte hao;
    private int sn;
    private byte place;
    private CardType cardType;
    private boolean onlyid = false;
    private final int OFF = 1111;
    String us = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_uwrite;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        setTitle("发行票卡");
        et_serial_number = holder.get(R.id.et_serial_number);
        cb_pwd = holder.get(R.id.cb_pwd);
        tv_content =holder.get(R.id.tv_content);
        iv_correct =holder.get(R.id.iv_correct);
        tv_result = holder.get(R.id.tv_result);
        iv_result = holder.get(R.id.iv_result);
        et_fa = holder.get(R.id.et_fa);
        cb_fa = holder.get(R.id.cb_fa);
        rg = holder.get(R.id.rg);
        rb1 = holder.get(R.id.rb1);
        rb2 = holder.get(R.id.rb2);
        bt_clear = holder.get(R.id.bt_clear);
        et_pwd = holder.get(R.id.et_pwd);
//////////////////////////////////////////////////////////////////////
        place = Byte.parseByte("1");//区
        int h = Integer.parseInt("1");//号
        int p = Integer.parseInt("1");//排
        int sni = Integer.parseInt("1");//票证编号
        qu = Byte.parseByte("1");//门票座位
        this.pai = (byte) p;
        hao = (byte) h;
        this.sn = sni;
     //   cardType = ctMap.get("1");//活动类型
      //  setData();
        setView();

        //设置出厂秘钥
        if (!Utils.isSecret()) {
            finish();
            return;
        }
        String[] strings = Sp.getuSecret();
        if (strings != null) {
            us = strings[2];
        } else {
            Toast.makeText(this, "请先设置用户秘钥", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        fa = sql.count(sql.FAU);
        tv_content.setText(fa + "张");

    }

    private void setData() {
        list = new ArrayList<>();
        listQu = new ArrayList<>();
        listPlace = new ArrayList<>();
       // list.addAll(ctMap.keySet());
      //  listQu.addAll(quMap.keySet());
      //  listPlace.addAll(placeMap.keySet());
        fa = sql.count(sql.FAU);
        tv_content.setText(fa + "张");
        String[] qph = Sp.getQPH();
        for (int i = 0; i < 3; i++) {
            this.qph[i] = parseInt(qph[i]);
        }
    }


    boolean isStop = true;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_F11:
                Utils.hideSoftInputFromWindow(UWriteActivity.this);
                fa();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void fa() {
        if (!isStop) {
            h.sendEmptyMessageDelayed(MSG_END, 300);
            isStop = true;
            toastShort("结束发行");
            etEnabled(true);
        } else {
            tv_result.setText("请放票卡");
            SBmp.please(iv_result);
            if (keycode_f12()) {
                etEnabled(false);
                toastShort("开始发行");
            } else {
                etEnabled(true);
                toastShort("启动发行失败，参数不准确，请重新设置");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void msg_add(Message msg) {
        String obj = msg.obj.toString();
        SBmp.fa_nomal(iv_result);
        tv_result.setText(obj);
        tv_result.setTextColor(Color.GREEN);
        iv_correct.setBackground(getResources().getDrawable(R.mipmap.correct));
        tv_content.setText((++fa) + "张");
        Card y = new Card();
        y.setId(cardId);
        sql.insertFA(sql.FAU, y);
        isPack();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void msg_error(Message msg) {
        String obj = msg.obj.toString();
        SBmp.faError(iv_result, obj);
        tv_result.setText(obj);
        tv_result.setTextColor(Color.RED);
        iv_correct.setBackground(getResources().getDrawable(R.mipmap.error));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void msg_warn(Message msg) {
        String obj = msg.obj.toString();
        if (cardId.equals(addId)) {
            SBmp.fa_nomal(iv_result);
            tv_result.setText(SUCCESS);
            tv_result.setTextColor(Color.GREEN);
            iv_correct.setBackground(getResources().getDrawable(R.mipmap.correct));
            return;
        }
        SBmp.fa_warn(iv_result);
        tv_result.setText(obj);
        tv_result.setTextColor(Color.YELLOW);
        iv_correct.setBackground(getResources().getDrawable(R.mipmap.warn));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void msg_none(Message msg) {
        SBmp.please(iv_result);
        String obj = msg.obj.toString();
        tv_result.setText(obj);
        tv_result.setTextColor(Color.WHITE);
        iv_correct.setBackground(getResources().getDrawable(R.mipmap.fanka));
    }

    String cardId = "";
    String addId;
    public static final String SUCCESS = "发行成功";
    private static final int MSG_ADD = 477;
    private static final int MSG_WARN = 577;
    private static final int MSG_ERROR = 677;
    private static final int MSG_NONE = 777;
    public static final int MSG_END = 1;
    Handler h = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ADD:
                    msg_add(msg);
                    break;
                case MSG_WARN:
                    msg_warn(msg);
                    break;
                case MSG_ERROR:
                    msg_error(msg);
                    break;
                case MSG_NONE:
                    msg_none(msg);
                    break;
                case MSG_END:
                    tv_result.setText("");
                    iv_result.setImageBitmap(null);
                    break;
                case OFF:
                    cardId = "";
                    isStop = true;
                    etEnabled(true);
                    iv_result.setImageBitmap(null);
                    break;
            }
        }
    };


    private boolean idbeep = true;

    /**
     * 发行卡
     * @return
     */
    private boolean keycode_f12() {

            isStop = false;
            etEnabled(false);
            new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isStop) {
                            String id = Hex.toHexString(Ultralight.getID());
                            if (id != null) {
                                if (idbeep) {
                                    idbeep = false;
                                    if (!id.equals(cardId)) {
                                        if (!sql.isID(sql.FAU, id)) {
                                            String b = null;
                                         //   if (onlyid) {
                                                b = UL.uwriteId(id);
                                           // } else {
                                              //  b = UL.uwrite(id, cardType.getId(), place, qu, pai, hao, sn);//门票类型，活动类型，区，排，号，票证编号
                                           // }

                                            if (SUCCESS.equals(b)) {
                                                cardId = id;
                                                sendMessage(MSG_ADD, b);
                                            } else {
                                                sendMessage(MSG_ERROR, b);
                                            }
                                        } else {
                                            addId = id;
                                            sendMessage(MSG_WARN, "此票已发行");
                                        }
                                    } else {
                                        addId = id;
                                        sendMessage(MSG_WARN, "此票已发行");
                                    }
                                }
                            } else {
                                idbeep = true;
                                Message msg = Message.obtain();
                                msg.what = MSG_NONE;
                                msg.obj = "请放票卡";
                                h.sendMessage(msg);
                            }
                            Thread.sleep(C.SLEEP);
                        }
                        h.sendEmptyMessage(OFF);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("sss",e.toString());
                        h.sendEmptyMessage(OFF);
                    }
                }
            }.start();
        return true;
    }

    private void sendMessage(int what, Object obj) throws InterruptedException {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        h.sendMessage(msg);
        if (what == MSG_ADD) {
            beep(true);
        } else {
            beep(false);
        }
    }

    int mt;

    private void isPack() {
        if (isPack) {
            if ((fa - mt) >= intet) {
                isStop = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("数量已够" + intet + "张是否继续？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mt = fa;
                        if (!keycode_f12()) {
                            Toast.makeText(UWriteActivity.this, "请重新设置参数在启动", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                builder.create().show();
            }
        }
    }



    private int fa;

    @Override
    protected void onPause() {
        super.onPause();
        h.sendEmptyMessage(OFF);
//        iv_correct.setBackground(getResources().getDrawable(R.mipmap.fanka));
    }


    private void etEnabled(final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                et_serial_number.setFocusable(enabled);
                et_serial_number.setFocusableInTouchMode(enabled);

            }
        });
    }

    boolean isPack;
    int intet;

    private void setView() {
        et_serial_number.setText(Sp.getEt_serial_number());
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UL.clear()) {
                    Toast.makeText(UWriteActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    beep(true);
                } else {
                    Toast.makeText(UWriteActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    beep(false);
                }
            }
        });
        et_serial_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        cb_fa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPack = isChecked;
                et_fa.setEnabled(!isChecked);
                if (isChecked) {
                    intet = Integer.parseInt(et_fa.getText().toString().trim());
                    mt = fa;
                } else {
                }
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb1) {
                    onlyid = true;
                } else {
                    if (ctMap == null || quMap == null || quMap.size() < 1 || ctMap.size() < 1 || placeMap == null || placeMap.size() < 1) {
                        Toast.makeText(UWriteActivity.this, "用户参数没有设置完整", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    onlyid = false;
                }
            }
        });
        cb_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et_pwd.setText(us);
                } else {
                    et_pwd.setText("");
                }
            }
        });
    }

    public void clickUpdata(View view) {
//        H.updatasAll(this, sql.FAU);
//        sql.deleteAll(sql.FAU);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Sp.setEt_hao("1");
        Sp.setEt_serial_number(et_serial_number.getText().toString().trim());
        Sp.setEt_pai("1");

        ModuleControl.rf_rfinf_reset(Ultralight.id, (byte) 0);
        Ultralight.offLog();
        Ultralight.exit();
    }

    @Override
    public void onClick(View v) {

    }

    public void start() {
        Ultralight.onLog(h);
        Ultralight.init(this, 115200);

    }
}
