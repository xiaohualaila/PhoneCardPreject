package ug.phonecardpreject.util;


import java.util.HashMap;

import ug.phonecardpreject.bean.CardType;
import ug.phonecardpreject.storage.Sp;
import ug.phonecardpreject.storage.Sql;

/**
 * Created by Administrator on 2017/1/11.
 */

/**
 * 常量
 */
public class C {
    /**
     * pos机号
     */
    public static String imei = Sp.getDeviceId();
    public static final int SLEEP = 100;
    public static final int SLEEP_UP = 3000;
    public final static Sql sql = Sql.getInstance();
    public static byte[] csecret;//设置界面发行时赋值;
    public static byte[] usecret;//用户登录界面赋值
    /**
     * 卡类型
     */
    public static HashMap<String, CardType> ctMap =Sp.getCardType();
    /**
     * 区
     */
    public static HashMap<String, String> quMap =Sp.getQu();
    /**
     * 活动
     */
    public static HashMap<String, String> placeMap =Sp.getPlace();
}