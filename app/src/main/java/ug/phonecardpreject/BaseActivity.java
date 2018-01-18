package ug.phonecardpreject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Administrator on 2016/11/28.
 */

public class BaseActivity extends AppCompatActivity {

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    System.out.println("---> " + msg.obj.toString());
                    info.append(msg.obj.toString());
                    info.append("\n");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sv.scrollTo(0, info.getHeight());
                        }
                    }, 300);
                    break;
                case 1:
                    Toast.makeText(BaseActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };
    protected TextView info;
    protected View sv;
    protected void handlerToast(String obj){
        Message obtain = Message.obtain();
        obtain.obj = obj;
        obtain.what = 1;
        handler.sendMessage(obtain);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = ((TextView) findViewById(R.id.info));
        sv = findViewById(R.id.sv);
        assignViews();
        listener();
    }

    protected void listener() {

    }

    protected void assignViews() {

    }
}
