package ug.phonecardpreject.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.yuwei.utils.Cryptography;
import com.yuwei.utils.Hex;
import com.yuwei.utils.ModuleControl;
import com.yuwei.utils.Ultralight;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;

import ug.phonecardpreject.storage.Sp;

import static ug.phonecardpreject.util.C.csecret;
import static ug.phonecardpreject.util.C.usecret;


/**
 * Created by Administrator on 2016/12/27.
 */
public class Utils {
    public static final String DENG = "denghs";
  //  public static final String DENG = "ywkj-www.handpos.cn";//修改后的内容;
    public static final String ULTRALIGHT = "ultralight";
    //    000430+denghs;
    public static final String SUPER = "b5d4ec7f65136909a961780fc64472d2";
    public boolean islog=true;
//    代码实现方法:

    /**
     * 清除应用缓存的用户数据，同时停止所有服务和Alarm定时task
     * String cmd = "pm clear " + packageName;
     * String cmd = "pm clear " + packageName  + " HERE";
     * Runtime.getRuntime().exec(cmd)
     *
     * @param c
     * @return
     */
    public static Process clearAppUserData(Activity c) {
        Process p = execRuntimeProcess("pm clear " + c.getPackageName());
        if (p == null) {
//            LogTag.log("Clear app data packageName:" + packageName
//                    + ", FAILED !");
            Toast.makeText(c, "清除失败", Toast.LENGTH_SHORT).show();
        } else {
//            LogTag.log("Clear app data packageName:" + packageName
//                    + ", SUCCESS !");
            Toast.makeText(c, "清除成功", Toast.LENGTH_SHORT).show();
            c.finish();
        }
        return p;
    }
    public static void secret() {
        csecret = usecret = null;
        try {{
                //出厂秘钥
                String[] cs = Sp.getcSecret();
                byte[] decrypted = Cryptography.decrypted(cs[1], Cryptography.UPWD);
                String s = Hex.toHexString(decrypted);
                if (s.equals(cs[0])) {
                    csecret = Hex.toByteArray(Cryptography.string2MD5(Utils.ULTRALIGHT+s));
                    System.out.println(" csecret--------->> "+ Hex.toHexString(csecret));
                }
            }
            {
                //用户秘钥
                String[] cs = Sp.getuSecret();
                byte[] decrypted = Cryptography.decrypted(cs[1], Cryptography.UPWD);
                if (Hex.toHexString(decrypted).equals(cs[0])) {
                    usecret = Hex.toByteArray(Cryptography.string2MD5(Utils.ULTRALIGHT+cs[0]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Process execRuntimeProcess(String commond) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(commond);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Utils.execRuntimeProcess");
        }

        return p;
    }

    public static boolean isSecret() {  //是否设置秘钥;
        if (csecret == null) {           //出厂秘钥
            Toast.makeText(App.getContext(), "请设置出厂秘钥", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (usecret == null) {          //用户秘钥
            Toast.makeText(App.getContext(), "请设置用户秘钥", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean isCSecret() { //出厂秘钥
        if (csecret == null) {
            Toast.makeText(App.getContext(), "请设置出厂秘钥", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static void beep(final boolean b1) {
        if (b1) {
//            Ultralight.bell(100);
            ModuleControl.rf_beep(Ultralight.id, 10);
        } else {
//            Ultralight.bell(500);
            ModuleControl.rf_beep(Ultralight.id, 50);
        }
    }
    public static String decimalFormat(double v) {
        DecimalFormat df = new DecimalFormat("0000000");
        return df.format(v);
    }

    /**
     * 深度拷贝
     *
     * @param obj
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(T obj) {
        T clonedObj = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            clonedObj = (T) ois.readObject();
            ois.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return clonedObj;
    }
    public static void hideSoftInputFromWindow(Activity c) {
        InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(c.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void handler(Handler h, int what, Object obj, long delayMillis) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        h.sendMessageDelayed(msg,delayMillis);
    }
    public static void handler(Handler h, int what, Object obj){
        handler(h, what, obj, 0);
    }
    //打印log方法类;
    public  static void log(String msg){
        if (isSecret()){
            Log.d("yuwei--------->",msg);
        }
    }
}
