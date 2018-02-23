package ug.phonecardpreject.test;

import android.view.View;
import com.yuwei.utils.Cryptography;
import com.yuwei.utils.Hex;
import ug.phonecardpreject.R;
import ug.phonecardpreject.base.*;
import ug.phonecardpreject.storage.Sp;
import ug.phonecardpreject.util.ClearEditText;
import ug.phonecardpreject.util.Utils;

import static ug.phonecardpreject.util.C.csecret;
import static ug.phonecardpreject.util.C.sql;
import static ug.phonecardpreject.util.C.usecret;

/**
 * Created by Administrator on 2018/1/25.
 */

public class SetUpActivity extends ug.phonecardpreject.base.BaseActivity {
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

        holder.setOnClickListener(this, R.id.bt_sure,R.id.bt_clear_sql);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sure:
                String a = pwd_0.getText().toString().trim();
                String p = pwd_1.getText().toString().trim();
                if ("".equals(a) || "".equals(p)) {
                    toastLong("输入框不能为空");
                    return;
                }
                if (a.equals(p)) {
                    //设置出厂秘钥
                    String s = Cryptography.string2MD5(a + Utils.DENG);//经理要求MD5之后,也写到加密函数里面去;
                    byte[] encrypted = Cryptography.encrypted(Hex.hexStringToByteArray(s), Cryptography.UPWD);
                    String md52 = Cryptography.string2MD5(Utils.ULTRALIGHT + s);
                    csecret = Hex.toByteArray(md52);
                    Sp.setcSecret(s + "|" + Hex.toHexString(encrypted) + "|" + a);
                    //设置用户秘钥
                    String s1 = Cryptography.string2MD5(a + Utils.DENG);
                    byte[] encrypted1 = Cryptography.encrypted(Hex.hexStringToByteArray(s1), Cryptography.UPWD);
                    usecret = Hex.toByteArray(Cryptography.string2MD5(Utils.ULTRALIGHT + s1));
                    Sp.setuSecret(s + "|" + Hex.toHexString(encrypted1) + "|" + a);
                    toastLong("设置成功");
                    finish();
                } else {
                    toastLong("两次输入不匹配，请重新输入");
                }
                break;
            case R.id.bt_clear_sql:
                sql.deleteAll(sql.YAN);
                sql.deleteAll(sql.FAU);
                break;
        }
    }


}
