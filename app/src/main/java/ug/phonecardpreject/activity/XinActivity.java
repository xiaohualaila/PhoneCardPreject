package ug.phonecardpreject.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.yuwei.utils.Card;
import com.yuwei.utils.Hex;
import com.yuwei.utils.ModuleControl;
import com.yuwei.utils.Ultralight;

import java.io.File;
import ug.phonecardpreject.R;
import ug.phonecardpreject.base.BaseActivity;
import ug.phonecardpreject.base.ViewHolder;
import ug.phonecardpreject.bean.WhiteList;
import ug.phonecardpreject.greendaodemo.GreenDaoManager;
import ug.phonecardpreject.greendaodemo.greendao.gen.WhiteListDao;
import ug.phonecardpreject.util.FileUtil;


public class XinActivity extends BaseActivity {
    boolean isStop = false;
    private TextView card_no,title_text,name;
    LinearLayout ll_img,ll_tip;
    RelativeLayout ll_content;
    ImageView people_img,left_img,right_img;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        setTitle("");
        card_no = holder.get(R.id.card_no);
        title_text = holder.get(R.id.title_text);
        ll_img = holder.get(R.id.ll_img);
        ll_content = holder.get(R.id.ll_content);
        ll_tip = holder.get(R.id.ll_tip);
        name = holder.get(R.id.name);
        people_img = holder.get(R.id.people_img);
        left_img = holder.get(R.id.left_img);
        right_img = holder.get(R.id.right_img);

        title_text.setText("芯片验票");
        Card.init(115200);
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

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (msg.what == -1) {
                //显示刷卡页面
                ll_img.setVisibility(View.VISIBLE);//刷卡图片
                ll_content.setVisibility(View.GONE);//内容
                ll_tip.setVisibility(View.GONE);
            } else if (msg.what == 1) {
                //显示正确信息
                WhiteList whiteList = (WhiteList) msg.obj;
                ll_img.setVisibility(View.GONE);
                ll_content.setVisibility(View.VISIBLE);
                ll_tip.setVisibility(View.VISIBLE);
                left_img.setImageResource(R.drawable.right_1);
                right_img.setImageResource(R.drawable.right);
                name.setText(whiteList.getName());
                card_no.setText(whiteList.getXin_id());
                showImage(whiteList.getName());
            }else if(msg.what == 2){
                //读取失败信息
                ll_img.setVisibility(View.GONE);
                ll_content.setVisibility(View.VISIBLE);
                ll_tip.setVisibility(View.VISIBLE);
                left_img.setImageResource(R.drawable.wrong_1);
                right_img.setImageResource(R.drawable.wrong);
                name.setText("");
                card_no.setText("");
                people_img.setImageResource(R.drawable.no_people);
            }else if (msg.what == 3) {
                //入场次数太多
                WhiteList whiteList = (WhiteList) msg.obj;
                ll_img.setVisibility(View.GONE);
                ll_content.setVisibility(View.VISIBLE);
                ll_tip.setVisibility(View.VISIBLE);
                left_img.setImageResource(R.drawable.too_num2);
                right_img.setImageResource(R.drawable.too_num1);
                name.setText(whiteList.getName());
                card_no.setText(whiteList.getXin_id());
                showImage(whiteList.getName());
            }
        }
    };

    public void showImage(String name){
        String filePath = FileUtil.getPath() + File.separator + "photo" + File.separator + name + ".png";
        if (!TextUtils.isEmpty(filePath)) {
            Glide.with(this).load(filePath).error(R.drawable.no_people).into(people_img);
        }
    }

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
                        handler.sendMessage(msg);
                    } else {
                        String s = Hex.toHexString(id);//获取到卡片ID值之后(16进制数组转化为字符串);
                        Card.rf_beep(20);
                   //     Log.i("sss", ">>>>>>>>" + s);
                        checkData(s);
                    }
                    Thread.sleep(1500);
                }
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
        }

        private void checkData(String ticket) {
            ticket = ticket.toUpperCase();
            WhiteList whiteList= GreenDaoManager.getInstance().getSession().getWhiteListDao()
                    .queryBuilder().where(WhiteListDao.Properties.Xin_id.eq(ticket)).build().unique();
            if(whiteList != null){
                String num_str = whiteList.getNum();
                if(!num_str.isEmpty()){
                    int num = Integer.parseInt(num_str);
                    if(num > 3){
                        //入场次数太多
                        Message msg = Message.obtain();
                        msg.what = 3;
                        msg.obj = whiteList;
                        handler.sendMessage(msg);
                    }else {
                        whiteList.setNum(String.valueOf(num + 1));
                        GreenDaoManager.getInstance().getSession().getWhiteListDao().update(whiteList);
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = whiteList;
                        handler.sendMessage(msg);
                    }
                }else {
                    whiteList.setNum("1");
                    GreenDaoManager.getInstance().getSession().getWhiteListDao().update(whiteList);
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = whiteList;
                    handler.sendMessage(msg);
                }
            }else {
                Message msg = Message.obtain();
                msg.what = 2;
                handler.sendMessage(msg);
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
        Card.exit();
    }


}
