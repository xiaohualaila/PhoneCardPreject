package ug.phonecardpreject.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.yuwei.utils.Card;
import java.io.File;
import ug.phonecardpreject.R;
import ug.phonecardpreject.base.BaseActivity;
import ug.phonecardpreject.base.ViewHolder;
import ug.phonecardpreject.bean.WhiteList;
import ug.phonecardpreject.greendaodemo.GreenDaoManager;
import ug.phonecardpreject.greendaodemo.greendao.gen.WhiteListDao;
import ug.phonecardpreject.util.FileUtil;

/**
 * 二维码验票
 */
public class ScanActivity extends BaseActivity {
    private TextView info,title_text,name;

    LinearLayout ll_img,wrong_tip,right_tip;
    RelativeLayout ll_content;
    ImageView card_img,people_img;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        setTitle("");
        title_text = holder.get(R.id.title_text);
        info = holder.get(R.id.card_no);
        ll_img = holder.get(R.id.ll_img);
        ll_content = holder.get(R.id.ll_content);
        card_img = holder.get(R.id.card_img);
        people_img = holder.get(R.id.people_img);
        name = holder.get(R.id.name);
        wrong_tip = holder.get(R.id.wrong_tip);
        right_tip = holder.get(R.id.right_tip);
        title_text.setText("二维码验票");
        card_img.setImageResource(R.drawable.er);
        Card.onLog(handler);
        Card.init(115200);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == -1) {
                ll_img.setVisibility(View.VISIBLE);
                ll_content.setVisibility(View.GONE);
                wrong_tip.setVisibility(View.GONE);
                right_tip.setVisibility(View.GONE);
            } else if (msg.what == 1) {
                //查询到人
                WhiteList whiteList = (WhiteList) msg.obj;
                ll_img.setVisibility(View.GONE);
                ll_content.setVisibility(View.VISIBLE);
                wrong_tip.setVisibility(View.GONE);
                right_tip.setVisibility(View.VISIBLE);
                name.setText(whiteList.getName());
                info.setText(whiteList.getCode_id());
                showImage(whiteList.getName());
            }else if(msg.what == 2){
                ll_img.setVisibility(View.GONE);
                ll_content.setVisibility(View.VISIBLE);
                wrong_tip.setVisibility(View.VISIBLE);
                right_tip.setVisibility(View.GONE);
                name.setText("");
                info.setText("");
                people_img.setImageResource(R.drawable.no_people);
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
                    Log.i("sss",">>>>" + bytes);
                    if (bytes != null) {
                        checkData(bytes);
                        Card.rf_beep(20);
                    }else {
                        Message msg = Message.obtain();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            msg.what = -1;
                            handler.sendMessage(msg);
                        }
                    },1000);
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void checkData(String ticket) {
        ticket = ticket.toUpperCase();
        WhiteList whiteList= GreenDaoManager.getInstance().getSession().getWhiteListDao()
                .queryBuilder().where(WhiteListDao.Properties.Code_id.eq(ticket)).build().unique();
        if(whiteList != null){
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = whiteList;
            handler.sendMessage(msg);
        }else {
            Message msg = Message.obtain();
            msg.what = 2;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.read_id:
//                info.setText("");
//                //获取卡id
//                byte[] id = Card.getID();
//                String id_code = Hex.toHexString(id);
//                if(id_code != null){
//                    info.setText(Hex.toHexString(id) + "00");
//                }
//                break;
        }
    }
}
