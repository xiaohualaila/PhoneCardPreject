package ug.phonecardpreject.test;

import android.view.View;
import com.yuwei.utils.Cryptography;
import com.yuwei.utils.Hex;
import ug.phonecardpreject.R;
import ug.phonecardpreject.base.*;
import ug.phonecardpreject.storage.Sp;
import ug.phonecardpreject.util.ClearEditText;
import ug.phonecardpreject.util.Utils;

/**
 * Created by Administrator on 2018/1/25.
 */

public class WritheCardActivity extends ug.phonecardpreject.base.BaseActivity {
    ClearEditText pwd_0;
    ClearEditText pwd_1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setup_secret;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        setTitle("设置秘钥");
        pwd_0 = holder.get(R.id.pwd_0);
        pwd_1 = holder.get(R.id.pwd_1);

        holder.setOnClickListener(this, R.id.tv_sure);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure:
                String a = pwd_0.getText().toString().trim();
                String p = pwd_1.getText().toString().trim();
                if ("".equals(a) || "".equals(p)) {
                    toastLong("输入框不能为空");
                    return;
                }
                if (a.equals(p)) {
                    String s = Cryptography.string2MD5(a + Utils.DENG);
                    byte[] encrypted = Cryptography.encrypted(Hex.hexStringToByteArray(s), Cryptography.UPWD);
                    Hex.toByteArray(Cryptography.string2MD5(Utils.ULTRALIGHT + s));
                    Sp.setuSecret(s + "|" + Hex.toHexString(encrypted) + "|" + a);
                    toastLong("设置成功");
                } else {
                    toastLong("两次输入不匹配，请重新输入");
                }
                break;
        }
    }


}
