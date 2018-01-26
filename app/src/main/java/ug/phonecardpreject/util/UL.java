package ug.phonecardpreject.util;


import com.yuwei.utils.Cryptography;
import com.yuwei.utils.Hex;
import com.yuwei.utils.Ultralight;
import java.util.Set;

import ug.phonecardpreject.bean.Card;
import ug.phonecardpreject.bean.CardType;
import ug.phonecardpreject.test.UWriteActivity;

import static com.yuwei.utils.Hex.b4;
import static com.yuwei.utils.Ultralight.end;
import static com.yuwei.utils.Ultralight.rf_UL_read_common;
import static com.yuwei.utils.Ultralight.rf_UL_write_common;
import static com.yuwei.utils.Ultralight.start;
import static ug.phonecardpreject.util.C.csecret;
import static ug.phonecardpreject.util.C.sql;
import static ug.phonecardpreject.util.C.usecret;

/**
 * Created by Administrator on 2017/1/20.
 */

public class UL {
    /**
     * 写出厂
     * @param sid
     * @return
     */

    public static boolean cwrite(String sid) { //将卡ID写入Ultralight卡片中去;
        byte[] id = Hex.hexStringToByteArray(sid);
        if (id == null) {
            return false;
        }
        byte[] encrypted = Cryptography.encrypted(id, csecret);  //加密的密码为(utraLight+((明文+denghs)md5再转化为字符串)的md5;)
        boolean code = Ultralight.rf_UL_write((byte) 4, 2, encrypted);
        return code;
    }

    public static boolean verifyID(String id) {
        byte[] data = rf_UL_read_common(4, 2);
        if (data == null) {
            return false;
        }
        byte[] decrypted = Cryptography.decrypted(data, csecret);
        String s = Hex.toHexString(decrypted);
        if (id == null) {
            byte[] bid = Ultralight.rf_UL_findcard();
            id = Hex.toHexString(bid);
        }
        if (!id.equals(s)) {
            return false;
        }
        return true;
    }
    /**
     * 校验出厂id
     *
     * @param id
     * @return
     */
    public static boolean verifyID_C(String id) {
        System.out.println(id);
        start();
        boolean b = verifyID(id);
        end();
        return b;
    }
    /**
     * 校验用户id
     *
     * @param id
     * @return
     */
    public static boolean verifyID_U(String id) {
        start();
        byte[] data = rf_UL_read_common(6, 2);
        if (data == null) {
            return false;
        }
        byte[] decrypted = Cryptography.decrypted(data, usecret);
        String s = Hex.toHexString(decrypted);
        if (id == null) {
            byte[] bid = Ultralight.rf_UL_findcard();
            id = Hex.toHexString(bid);
        }
        if (!id.equals(s)) {
            return false;
        }
        end();
        return true;
    }
    /**
     * 第4、5 页：出厂秘钥
     * 第6、7页：（字节）
          【0】前四位：门票类型/ 后四位：活动类型
          【1-3】区类型、排、号
          【4-6】编号
          【7】 异或校验校验
     * 第10页：
     * 【0】进场总数
     *
     * @param id
     * @param mt    门票类型
     * @param place
     * @param qu
     * @param pai
     * @param hao   @return
     * @param sn
     */
    public static String uwrite(String id, byte mt, byte place, byte qu, byte pai, byte hao, int sn) {
        start();
        try {
            if (!verifyID(id)) {
                return "出厂id校验失败";
            }
            byte[] body = new byte[8];
            byte b0 = Hex.b4(mt, place);
            body[0] = b0;
            body[1] = qu;
            body[2] = pai;
            body[3] = hao;
            byte[] sns = Hex.intToByte(sn);
            body[4] = sns[0];
            body[5] = sns[1];
            body[6] = sns[2];
            body[7] = Cryptography.xor(body);
            byte[]result = Cryptography.encrypted(body, usecret);
//            if (body == null) {
//                return "加密失败";
//            }
            if (!rf_UL_write_common(6, 2, result)) {
                return "写入6、7页失败";
            }
            if (!rf_UL_write_common(10, 1, new byte[4])) {
                return "写入10页失败";
            }
            Card c = new Card();
            c.setId(id);
            String read = read(c);
            if ("有效票证".equals(read)) {
                if ((body[7] != c.getXor()) || (b0 !=  Hex.b4(Byte.parseByte(c.getType()), (byte) c.getPlaceType()))) {
                    return "用户校验失败";
                }
            } else {
                return read;
            }
            return UWriteActivity.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            end();
        }
    }
    public static String uwriteId(String sid) {
        start();
        try {
//            if (!verifyID(sid)) {
//                return "出厂id校验失败";
//            }
            byte[] id = Hex.hexStringToByteArray(sid);
            byte[] encrypted = Cryptography.encrypted(id, usecret);
            boolean code = Ultralight.rf_UL_write((byte) 6, 2, encrypted);
            if (!code) {
                return "发行失败";
            }
            return UWriteActivity.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            end();
        }
    }

    /**
     * 用户读公共方法
     * @param c
     * @return
     * @throws Exception
     */
    private static String read(Card c) throws Exception {
        byte[] body = rf_UL_read_common(6, 2);
        if (body == null) {
            return "读取6、7页失败";
        }
        byte[] b10 = rf_UL_read_common(10, 1);
        if (b10 == null) {
            return "读取10页失败";
        }
        body = Cryptography.decrypted(body, usecret);
        if (!Cryptography.xorVerify(body)) {
            return "用户异或校验失败";
        }
        c.setIn(b10[0]);
        byte[] b0 = b4(body[0]);
        //门票类型；
        {
            String s = b0[0] + "";
            CardType cardType = C.ctMap.get(s);
            if (cardType != null) {
                c.setCardType(cardType);
            } else {
                c.setType(s);
                c.setName("该票卡类型没有添加");
                c.setMoney((short) -1);
                c.setCount(0);
                c.setPicture(false);
                c.setWork(false);
            }
        }
        //  产地名
        {
            String s = b0[1] + "";
            String s1 = C.placeMap.get(s);
            c.setPlaceType(b0[1]);
            if (s == null) {
                c.setPlaceName("未设置");
            } else {
                c.setPlaceName(s1);
            }
        }
        //  区排号
        {
            String qu = body[1] + "";
            String s = C.quMap.get(qu);
            if (s == null) {
                c.setQuName("未设置");
            } else {
                c.setQuName(s);
            }
            c.setQuType(body[1]);
            c.setPai(body[2]);
            c.setHao(body[3]);
        }
        //  编号
        {
            int sn = Hex.byteToInt(new byte[]{body[4], body[5], body[6], 0});
            c.setSerial_number( sn);
        }
        c.setXor(body[7]);
        return "有效票证";
    }


    private static boolean xr10(Card c) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (1+c.getIn());
        return rf_UL_write_common(10, 1,bytes );
    }
    /**
     * 票卡归零
     */
    public static boolean clear() {
        int code = -1;
        byte[] bytes = new byte[4];
        byte[] id = Ultralight.getID();
        if (id == null) {
            return false;
        }
        sql.deleteById(sql.FAU, Hex.toHexString(id));
        code = Ultralight.rf_UL_write(6, bytes);
        if (code != 0) {
            return false;
        }
        code = Ultralight.rf_UL_write(7, bytes);
        code = Ultralight.rf_UL_write(8, bytes);
        code = Ultralight.rf_UL_write(9, bytes);
        code = Ultralight.rf_UL_write(10, bytes);
        return true;
    }

}
