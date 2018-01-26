package ug.phonecardpreject.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import ug.phonecardpreject.R;
import ug.phonecardpreject.bean.CardType;
import ug.phonecardpreject.util.App;
import ug.phonecardpreject.util.C;
import ug.phonecardpreject.util.Utils;

import static ug.phonecardpreject.util.C.ctMap;
import static ug.phonecardpreject.util.C.placeMap;
import static ug.phonecardpreject.util.C.quMap;


/**
 * Created by Administrator on 2016/12/27.
 */

public class Sp {

    public static final String NAME = "view";

    private static SharedPreferences getSp() {
        return App.getContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEdit() {
        return getSp().edit();
    }
    public static void clear() {
         getEdit().clear().commit();
    }

    /**
     * 选择票证检验
     * @return
     */
//    public static int getSelecCheckout() {
//        return getSp().getInt("SelecCheckout", R.id.checkout_yhid);//默认为选中芯票ID检验模式
//    }

    public static void setSelecCheckout(int value) {
        getEdit().putInt("SelecCheckout", value).commit();
    }

    /**
     * 发行编号
     * @return
     */
    public static String getEt_serial_number() {
        return getSp().getString("et_serial_number", "0000000");
    }

    public static void setEt_serial_number(String value) {
        getEdit().putString("et_serial_number", value).commit();
    }
    /**
     * 发行座号
     * @return
     */
    public static String getEt_hao() {
        return getSp().getString("et_hao", "1");
    }

    public static void setEt_hao(String value) {
        getEdit().putString("et_hao", value).commit();
    }
    /**
     * 发行排号
     * @return
     */
    public static String getEt_pai() {
        return getSp().getString("et_pai", "1");
    }

    public static void setEt_pai(String value) {
        getEdit().putString("et_pai", value).commit();
    }
    /**
     * 机器号
     * @return
     */
    public static String getDeviceId() {
        return getSp().getString("DeviceId", "未设");
    }

    public static void setDeviceId(String value) {
        getEdit().putString("DeviceId", value).commit();
    }
    /**
     * ip地址
     * @return
     */
    public static String getIP() {
        return getSp().getString("IP", "192.168.0.23");
    }

    public static void setIP(String value) {
        getEdit().putString("IP", value).commit();
    }
    /**
     * ip地址
     * @return
     */
    public static String getWeb() {
        return getSp().getString("web", "/YWMemberSysWebServiceTest/MemSysWebService.asmx");
    }
    public static void setWeb(String v) {
         getEdit().putString("web", v).commit();
    }

/*    public static void setWeb(String ip) {
        getEdit().putString("web", "/YWMemberSysWebServiceTest/MemSysWebService.asmx").commit();
    }*/
/*    *//**
     * 出厂发卡总数
     * @return
     *//*
    public static int getCFa() {
        return getSp().getInt("fa", 0);
    }

    public static void setCFa(int value) {
        getEdit().putInt("fa", value).commit();
    }
    *//**
     * 用户发卡总数
     * @return
     *//*
    public static int getUFa() {
        return getSp().getInt("ufa", 0);
    }

    public static void setUFa(int value) {
        getEdit().putInt("ufa", value).commit();
    }*/
/******************************************* 设置 **************************************************/

    /**
     * 出厂密码
     *
     * @return
     */
    public static String getcPwd() {
        //默认md5原文六个8
        return getSp().getString("cpwd", "9e19c177630a8d184124ee534dab5c1e");//默认为6个8加上
    }

    public static void setcPwd(String value) {
        getEdit().putString("cpwd", value).commit();
    }
    /**
     * 出厂秘钥
     * 【0】明文    【1】密文
     * @return
     */
    public static String[] getcSecret() {
        String s = getSp().getString("cSecret", "0bc2871d4129f6a3f573cf3a1da05b67|1f2393af27be57aa53c6be45be6ccfff|7979");
//        System.out.println("------s = " + s);
        if (!"".equals(s)) {
            return s.split("\\|");//qie
        }
        return null;
    }

    public static void setcSecret(String value) {
        getEdit().putString("cSecret", value).commit();
    }
    /**
     * 用户秘钥
     * 【0】明文    【1】密文
     * @return
     */
    public static String[] getuSecret() {
        String s = getSp().getString("uSecret", "0bc2871d4129f6a3f573cf3a1da05b67|1f2393af27be57aa53c6be45be6ccfff|7979");
        if (!"".equals(s)) {
            return s.split("\\|");
        }
        return null;
    }
    public static void setuSecret(String value) {
        getEdit().putString("uSecret", value).commit();

    }
    /**
     * [0]区[1]排[2]号
     *
     * @return
     */
    public static String[] getQPH() {
        return getSp().getString("qbh", "127;127;127").split(";");
    }

    public static void setQPH(String value) {
        getEdit().putString("qbh", value).commit();
    }
    /**
     * 用户密码
     *
     * @return
     */
    public static String getuPwd() {
        return getSp().getString("uPwd", "9e19c177630a8d184124ee534dab5c1e");
    }

    public static void setuPwd(String value) {
        getEdit().putString("uPwd", value).commit();
    }
    /**
     * 卡类型、金额
     *
     * @return
     */
    public static <T extends Serializable>  T getCardType() {
        try {
            return deSerialization(getSp().getString("Serialization", ""));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void setCardType(HashMap<String, CardType> value) {
        try {
            getEdit().putString("Serialization", serialize(value)).commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 区
     * @return
     */
    public static <T extends Serializable>  T getQu() {
        try {
            return deSerialization(getSp().getString("getQu", ""));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void setQu(HashMap<String, String> value) {
        try {
            getEdit().putString("getQu", serialize(value)).commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 区
     *
     * @return
     */
    public static <T extends Serializable>  T getPlace() {
        try {
            return deSerialization(getSp().getString("Place", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void setPlace(HashMap<String, String> value) {
        try {
            getEdit().putString("Place", serialize(value)).commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 序列化对象
     *
     * @param person
     * @return
     * @throws IOException
     */
    private static String serialize(Serializable person) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String serStr = null;
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(person);
        serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.d("serial", "serialize str =" + serStr);
        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static <T> T deSerialization(String str) throws IOException, ClassNotFoundException {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        T person = (T) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return person;
    }
    public static JSONObject sp2json() throws Exception {
        JSONObject jo = new JSONObject();
        jo.put("ctMap", serialize(ctMap));
        jo.put("placeMap", serialize(C.placeMap));
        jo.put("quMap", serialize(C.quMap));
        String[] qph = getQPH();
        jo.put("PHmax",qph[1]+";"+qph[2] );
        jo.put("pos", getDeviceId());
        jo.put("ip", getIP());
        jo.put("web", getWeb());
        jo.put("cPwd", getcPwd());
        jo.put("uPwd", getuPwd());
        String[] c = getcSecret();
        if (c != null) {
            jo.put("cSecret",c[0]+"|"+c[1]+"|"+c[2] );
        }
        c = getuSecret();
        if (c != null) {
            jo.put("uSecret",c[0]+"|"+c[1]+"|"+c[2] );
        }
        System.out.println("jo.toString() = " + jo.toString());
        return jo;
    }
    public static void out(File sp) throws Exception {
        JSONObject jo = sp2json();
        String s = jo.toString();
        System.out.println("out = " + s);
        FileOutputStream fos = new FileOutputStream(sp);
        fos.write(s.getBytes("GBK"));
        fos.flush();
        fos.close();
    }
    public static void in(byte[] bytes) throws Exception {
        String s = new String(bytes,"GBK");
        System.out.println("in = " + s);
        JSONObject jo = new JSONObject(s);

        setuSecret(jo.optString("uSecret"));
        setcSecret(jo.optString("cSecret"));
        Utils.secret();
        setuPwd(jo.getString("uPwd"));
        setcPwd(jo.getString("cPwd"));
        setWeb(jo.getString("web"));
        setIP(jo.getString("ip"));
        setDeviceId(jo.getString("pos"));
        C.imei = jo.getString("pos");
        setQPH("127;"+jo.getString("PHmax"));
        try {
            HashMap<String, CardType> value  = deSerialization(jo.getString("ctMap"));
            ctMap = value;
            setCardType(value);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            HashMap<String, String> value = deSerialization(jo.getString("placeMap"));
            placeMap = value;
            setPlace(value);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            HashMap<String, String> value = deSerialization(jo.getString("quMap"));
            quMap = value;
            setQu(value);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void in(File usbsp) throws Exception {
        FileInputStream fis = new FileInputStream(usbsp);
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes);
        fis.close();
        in(bytes);
    }
}
