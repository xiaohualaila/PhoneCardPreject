package ug.phonecardpreject.util;

import android.widget.ImageView;

/**
 * Created by Administrator on 2017/2/15.
 */

public class SBmp extends Bmp {
    public static void yanError(ImageView iv, String result) {
        if (result.contains("页")) {
            yan_error_dq(iv);
        } else if (result.contains("用户")){
            yan_error_yh(iv);
        } else if (result.contains("本入口")){
            yan_error_select(iv);
        }else if (result.contains("入场次数已满")){
            yan_error_count(iv);
        } else {
            yan_error(iv);
        }
    }
    public static void yanNomal(ImageView iv, String result) {
        if (result.contains("用户")) {    //这个用于混合模式
            yan_error_yh(iv);
        } else {
            yan_nomal(iv);
        }
    }
    public static void faError(ImageView iv, String result) {
        if (result.contains("出厂")) {
            fa_error_cc(iv);
        } else if (result.contains("页")){
            fa_error_xr(iv);
        }  else {
            fa_error(iv);
        }
    }
}
