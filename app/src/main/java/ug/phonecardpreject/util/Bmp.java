package ug.phonecardpreject.util;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/2/15.
 */

public class Bmp {
    //获取系统文件夹上pictures的图片；
    private static final File PIC = App.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    /************************************************** 检验 ***********************************************************/
    /*
     * 对话框
     *
     * @param iv
     */
    public static void dialog(View iv) {
        iv.setBackground(new BitmapDrawable(App.getContext().getResources(), getPrivateLoacalBitmap("dialog.png")));
    }
    /**
     * 登录背景
     *
     * @param iv
     */
    public static void login_bg(View iv) {
        iv.setBackground(new BitmapDrawable(App.getContext().getResources(), getPrivateLoacalBitmap("login_bg.png")));
    }
    /**
     * 本入口无效
     *
     * @param iv
     */
    public static void yan_error_yh(ImageView iv) {
        iv.setImageBitmap(getPrivateLoacalBitmap("yan_error_yh.png"));
    }
    /**
     * 本入口无效
     *
     * @param iv
     */
    public static void yan_error_select(ImageView iv) {
        iv.setImageBitmap(getPrivateLoacalBitmap("yan_error_select.png"));
    }
    /**
     * 入场次数已满
     *
     * @param iv
     */
    public static void yan_error_count(ImageView iv) {
        iv.setImageBitmap(getPrivateLoacalBitmap("yan_error_count.png"));
    }
    /**
     * 读取失败
     *
     * @param iv
     */
    public static void yan_error_dq(ImageView iv) {
        iv.setImageBitmap(getPrivateLoacalBitmap("yan_error_dq.png"));
    }
    /**
     * 无效票证
     *
     * @param iv
     */
    public static void yan_error(ImageView iv) {
        iv.setImageBitmap(getPrivateLoacalBitmap("yan_error.png"));
    }
    /**
     * 此票已验
     * @param iv
     */
    public static void yan_warn(ImageView iv) {
        iv.setImageBitmap(getPrivateLoacalBitmap("yan_warn.png"));
    }
    /**
     * 检验通过
     * @param iv
     */
    public static void yan_nomal(ImageView iv) {
        iv.setImageBitmap(getPrivateLoacalBitmap("yan_nomal.png"));
    }
    /************************************************** 发行 ***********************************************************/
    /**
     * 发行 出厂校验失败
     *
     * @param iv
     */
    public static void fa_error_cc(ImageView iv) {
        iv.setImageBitmap(getPrivateLoacalBitmap("fa_error_cc.png"));
    }
    /**
     * 发行写入失败
     *
     * @param iv
     */
    public static void fa_error_xr(ImageView iv) {
        iv.setImageBitmap(getPrivateLoacalBitmap("fa_error_xr.png"));
    }
    /**
     * 发行失败
     *
     * @param iv
     */
    public static void fa_error(ImageView iv) {
        iv.setImageBitmap(getPrivateLoacalBitmap("fa_error.png"));
    }
    /**
     * 此票已发行
     * @param iv
     */
    public static void fa_warn(ImageView iv) {
        iv.setImageBitmap(getPrivateLoacalBitmap("fa_warn.png"));
    }
    /**
     * 发行成功
     * @param iv
     */
    public static void fa_nomal(ImageView iv) {
        iv.setImageBitmap(getPrivateLoacalBitmap("fa_nomal.png"));
    }

    /**
     * 请放票卡
     *
     * @return
     */
    public static void please(ImageView iv) {
        iv.setImageBitmap(getPrivateLoacalBitmap("please.png"));
    }

    /**
    * 主页背景
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void main(View iv) {
        iv.setBackground(new BitmapDrawable(App.getContext().getResources(), getPrivateLoacalBitmap("main.jpg")));
    }
    /**
    * 主页背景
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void bg(View iv) {
        iv.setBackground(new BitmapDrawable(App.getContext().getResources(), getPrivateLoacalBitmap("bg.jpg")));
    }
    /**
     * 加载本地（图片），先从pictures目录下寻找；没有的话再从asserts目录下找，前提名字要一样；
     *
     * @param name 文件名
     * @return
     */
    private static Bitmap getPrivateLoacalBitmap(String name) {
        Bitmap bitmap = null;
        File file = null;
        try {
            file = new File(PIC, name);
            FileInputStream fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis);///把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
//          e.printStackTrace();
            AssetManager assets = App.getContext().getAssets();
            InputStream open = assets.open("Bitmap/" + name);
            FileOutputStream os = new FileOutputStream(file);
            byte[] bytes = new byte[open.available()];
            open.read(bytes);
            os.write(bytes);
            open.close();
            os.close();
            bitmap = getPrivateLoacalBitmap(name);
        } finally {
            return bitmap;
        }
    }
    /**
     * 加载本地图片
     * @param file 文件
     * @return
     */
    public static Bitmap getBitmap(File file) {
        Bitmap bitmap = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis);///把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            return bitmap;
        }
    }
}
