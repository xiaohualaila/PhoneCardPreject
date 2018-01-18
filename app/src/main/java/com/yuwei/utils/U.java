package com.yuwei.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/11.
 */

public class U {
    public static String simpleDateFormat(Date date) {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return df.format(date);
    }
}
