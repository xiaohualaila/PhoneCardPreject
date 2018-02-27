package ug.phonecardpreject.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ug.phonecardpreject.R;
import ug.phonecardpreject.base.BaseActivity;
import ug.phonecardpreject.base.ViewHolder;
import ug.phonecardpreject.util.SharedPreferencesUtil;
import ug.phonecardpreject.view.ClearEditText;


public class SetupActivity extends BaseActivity {
    private TextView title_text,tv_current_num;
    private ClearEditText ce_num;
    private int current_num = 5;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setup;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        setTitle("");

        title_text = holder.get(R.id.title_text);
        tv_current_num = holder.get(R.id.tv_current_num);
        ce_num = holder.get(R.id.ce_num);
        holder.setOnClickListener(this, R.id.sure);

        title_text.setText("设置进场次数");
        String enter_num = SharedPreferencesUtil.getStringByKey("enter_num",this);
        if(!TextUtils.isEmpty(enter_num)){
            current_num = Integer.valueOf(enter_num);
        }
        tv_current_num.setText("当前入场次数：  " + current_num + "次");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure:
                String num = ce_num.getText().toString().trim();
                if(TextUtils.isEmpty(num)){
                    toastLong("入场次数不能为空！");
                    return;
                }
                SharedPreferencesUtil.save("enter_num",num,this);
                toastLong("设置成功！");
                finish();
                break;
        }
    }



}
