package com.yuwei.utils;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import hdx.HdxUtil;

import static com.yuwei.utils.ModuleControl.rf_ISO14443A_APDU;


/**
 * Created by Administrator on 2016/11/19.
 */

public class Card {
    public static final int SUCCESS = 0x9000;
    private static Handler h;
    private static ReadThread readThread = null;

    public static void onLog(Handler handler) {
        h = handler;
    }
    public static void offLog() {
        h = null;
    }

    private static int id;

    private static boolean isInit = false;
    private static final String LOG = "---Card";
    private static boolean isReadingCode = false;
    //private static String oneDimensionalCode = new String();

    public interface loopReadListener {
        /**
         * 读取卡文件内容
         * @param cardId    卡id
         */
        void getID(byte[] cardId);
    }

    /**
     * 蜂鸣器  大声
     * @param _Msec 响应时间：单位毫秒
     */
    public static void rf_beep(final int _Msec) {
//        log("蜂鸣器："+_Msec);
        new Thread(){
            @Override
            public void run() {
                ModuleControl.rf_beep(id,_Msec);
            }
        }.start();
    }

    static class ReadThread extends Thread {

        private volatile boolean isStop = true;
        private boolean repeat = false;
        private int sleepMS = 200;

        /**
         * 开启循环读取文件
         * @param repeat 是否重复回调 如果 true  则有卡时 每隔sleepMS 毫秒 回调一次，false 则表示在卡拿出前 只回调一次
         * @param sleepMS  寻卡时间间隔 毫秒数
         */
        public ReadThread(boolean repeat, int sleepMS) {
            this.repeat = repeat;
            this.sleepMS = sleepMS;
        }

        public ReadThread() {
            this.repeat = false;
            this.sleepMS = 200;
        }

        @Override
        public void run() {
            byte[] lastId = null;
            isStop = false;
            while (!isStop) {
                byte[] id = Card.getID();
                if (id != null && readListener != null) {
                    if(this.repeat){
                        readListener.getID(id);
                    }else{
                        if(!java.util.Arrays.equals(id, lastId)){
                            lastId = id;
                            readListener.getID(id);
                        }
                    }
                }else{
                    lastId = null;
                }
                try{
                    Thread.sleep(this.sleepMS);
                }catch (Exception e){

                }
            }
        }
    }
    private static loopReadListener readListener;

    /**
     * 开启循环读取文件
     * @param listener 读取回调
     * @param repeat 是否重复回调 如果 true  则有卡时 每隔sleepMS 毫秒 回调一次，false 则表示在卡拿出前 只回调一次
     * @param sleepMS  寻卡时间间隔 毫秒数
     */
    public static void startLoopRead(loopReadListener listener, boolean repeat, int sleepMS) {
        if (readListener == null) {
            readListener = listener;
            readThread = new ReadThread(repeat, sleepMS);
            readThread.start();
        } else {
//            throw new RuntimeException("请先调用com.yuwei.utils.Card.endLoopRead()方法!");
            log("_请先调用com.yuwei.utils.Card.endLoopRead()方法!");
        }
    }

    /**
     * 结束循环读取文件
     */
    public static void endLoopRead() {
        if (readThread != null) {
            readThread.isStop = true;
            try{
                readThread.join();
            }catch (Exception e){

            }
            readThread = null;
            readListener = null;
        }
    }

    /**
     * 获取卡id
     *
     * @return 返回卡id
     */
    public static byte[] getID() {
        int result = -1;
//        id = ModuleControl.rf_init(1, 2);
        //切换到读卡器
        ModuleControl.rf_rfinf_reset(id, (byte) 1);
        byte[] bs = new byte[20];
        byte[] len = new byte[1];
        result = ModuleControl.rf_ISO14443A_findcard(id, (byte) 0x26, len, bs);
        byte[] bytes = Hex.getBytes(len, bs);
        //log("卡id", result, bytes);
        en();
        if (bytes != null && bytes.length == 0) {
            bytes = null;
        }
        return bytes;
    }

    private static void log(String describe, int result, byte[] bs) {
        if (h != null) {
            Message obtain = Message.obtain();
            obtain.obj = describe + "返回码：" + result + "   返回值：" + Hex.toHexString(bs);
            h.sendMessage(obtain);
        }
    }

    /**
     * 打印日志
     *
     * @param describe 日志内容
     */
    public static void log(String describe) {
        if (h != null) {
            Message obtain = Message.obtain();
            if (describe.startsWith("_")) {
                obtain.obj = describe;
            } else {
                obtain.obj = "\n------------- " + describe + " ---------------";
            }
            h.sendMessage(obtain);
        }
    }

    private static void end() {
        ModuleControl.rf_ISO14443A_Deselect(id);
        ModuleControl.rf_rfinf_reset(id, (byte) 0);
//        ModuleControl.rf_exit(id);
    }

    private static void en() {
        ModuleControl.rf_rfinf_reset(id, (byte) 0);
//        ModuleControl.rf_exit(id);
    }
    /**
     * 打开串口，应用启动之后调用
     * @param baud 读卡器波特率   旧版本：9600，新版本：115200
     */
    public static void init(long baud) {
        if(!isInit){
            id = ModuleControl.rf_init(0, baud);
            log("_初始化返回id: "+id);
        }
    }
    /**
     * 打开串口，应用启动之后调用
     *
     */
    public static void init() {
        init(115200);
    }
    /**
     * 修改波特率，默认 9600
     * @param baud 读卡器波特率
     */
    public static int changbps(long baud) {
             int code = ModuleControl.rf_changbps(id, baud);
        log("_修改频率,返回码："+code);
        return code;
    }



    /**
     * 关闭串口，整个应用退出之前调用
     */
    public static void exit() {
        ModuleControl.rf_exit(id);
        isInit = false;
    }



    /**
     * 开始操作
     *
     * @return
     */
    private static boolean start() {
        int result = -1;
//        id = ModuleControl.rf_init(1, 2);
        ModuleControl.rf_rfinf_reset(id, (byte) 1);
        byte[] bs = new byte[20];
        byte[] len = new byte[1];
        result = ModuleControl.rf_ISO14443A_findcard(id, (byte) 0x26, len, bs);
        byte[] bytes = Hex.getBytes(len, bs);
        log("卡id", result, bytes);
        if (result != 0) {
            en();
            return false;
        }
        byte[] bs2 = new byte[100];
        byte[] len2 = new byte[1];
        result = ModuleControl.rf_ISO14443A_ATS(id, len2, bs2);
        byte[] bytes2 = Hex.getBytes(len2, bs2);
        log("锁定卡", result, bytes2);
        if (result == 0) {
            return true;
        } else {
            end();
            return false;
        }
    }

    /**
     * 写外部认证，读外部认证已废弃，
     *
     * @param inkey 写秘钥
     * @return
     */
    private static boolean exterAuth(byte[] inkey) {
        if (inkey != null) {
            byte fileid = (byte) 0x00;
            byte[] byteRandom = {0x00, (byte) 0x84, 0x00, 0x00, 0x08};
            byte[] randowLen = new byte[1];
            byte[] randowBuf = new byte[100];
            int intRandom = rf_ISO14443A_APDU(id, (byte) byteRandom.length, byteRandom, randowLen, randowBuf);
            randowBuf = Hex.getBytes(randowLen[0] - 2, randowBuf);
            log("获取随机数", intRandom, randowBuf);
            if (intRandom == SUCCESS) {
                byte[] encrypted = Cryptography.encrypted(randowBuf, inkey);
                byteRandom = new byte[]{0x00, (byte) 0x82, 0x00, fileid, 0x08};
                byte[] bytes = Hex.bytePlusbByte(byteRandom, encrypted);
                byte[] bs = new byte[200];
                byte[] len = new byte[1];
                intRandom = rf_ISO14443A_APDU(id, (byte) bytes.length, bytes, len, bs);
                randowBuf = Hex.getBytes(len, bs);
                log("认证", intRandom, randowBuf);
                if (intRandom == SUCCESS) {
                    return true;
                } else {
                    log("_认证失败");
                    return false;
                }

            } else {
                log("_获取随机数失败");
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 说明：清除CPU卡，使CPU恢复为空结构
     *
     * @param key 写密码
     * @return false 失败， true 成功
     */
    public static boolean clearStruct(byte[] key) {
        log("清除CPU卡");
        if (start()) {
            int code = -1;
            exterAuth(key);
            byte[] bytes = Hex.hexStringToByteArray("800E000000");
            byte[] bs = new byte[200];
            byte[] len = new byte[1];
            code = rf_ISO14443A_APDU(id, (byte) bytes.length, bytes, len
                    , bs);
            byte[] bytes1 = Hex.getBytes(len, bs);
            log("清除CPU卡", code, bytes1);
            end();
            if (code == SUCCESS) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 创建文件
     *
     * @param fileId  文件id
     * @param fileLen 文件长度 不得超过246个字节
     * @param auth    读些文件时是否认证 true 需要， false 不需要
     * @param key     写秘钥
     * @return true 成功 false 失败
     */
    public static boolean addFile(short fileId, short fileLen, boolean auth, byte[] key) {
        log(fileId + "文件添加");
        if (start()) {
            if (exterAuth(key)) {
                int code = file(fileId, fileLen, auth);
                end();
                if (code == 0x9000) {
                    return true;
                } else {
                    return false;
                }
            } else {
                end();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 创建文件
     * @param fileId
     * @param fileLen
     * @param auth
     * @return
     */
    private static int file(short fileId, short fileLen, boolean auth) {
        int code = -1;

        byte[] fileIdBytes = new byte[]{(byte) ((fileId & 0xFF00) >> 8), (byte) (fileId & 0xFF)};
        byte[] fileLenBytes = new byte[]{(byte) ((fileLen & 0xFF00) >> 8), (byte) (fileLen & 0xFF)};

        if (fileId == 0) {
            fileIdBytes = null;
        }
        if (fileLen == 0) {
            fileLenBytes = null;
        }

        byte k = auth ? (byte) 0xF1 : (byte) 0xF0;
        List<Byte> back = new ArrayList<Byte>();
        if (fileIdBytes != null && fileIdBytes.length == 2 && fileLenBytes != null && fileLenBytes.length == 2) {
            byte[] sends = Hex.mergeBytes(
                    new byte[]{(byte) 0x80, (byte) 0xE0},
                    fileIdBytes,
                    new byte[]{0x07, 0x28},
                    fileLenBytes,
                    new byte[]{k, 0x04, (byte) 0xFF, (byte) 0xFE});
            log("_创建文件"+fileId+"发送指令：" + Hex.toHexString(sends));
            byte[] bs = new byte[20];
            byte[] len = new byte[1];
            code = rf_ISO14443A_APDU(id, (byte) sends.length, sends, len, bs);
            byte[] bytes = Hex.getBytes(len, bs);
            log("创建文件", code, bytes);
        }
        return code;
    }
/*

    public boolean mode(byte[] wKey) {
        if (start()) {
            if (exterAuth(wKey)) {
                int code = -1;


                end();
                if (code == 0x9000) {
                    return true;
                } else {
                    return false;
                }
            } else {
                end();
                return false;
            }
        } else {
            end();
            return false;
        }
    }
*/

    /**
     * 写文件
     *
     * @param fileId  文件id
     * @param fileLen 文件长度  注意：如果fileLen的长度大于所创建该文件的长度，则写入失败
     * @param content 写入文件内容
     * @param wkey    写秘钥
     * @return int false失败， true 成功
     */
    public static boolean writeFile(short fileId, short fileLen, List<Byte> content, byte[] wkey) {
        log(fileId + "文件写入");
        if (start()) {
            if (exterAuth(wkey)) {
                int code = -1;
                byte[] fileIdBytes = new byte[]{(byte) ((fileId & 0xFF00) >> 8), (byte) (fileId & 0xFF)};
                byte[] fileLenBytes = new byte[]{(byte) ((fileLen & 0xFF00) >> 8), (byte) (fileLen & 0xFF)};

                //填充0
                if (content.size() < fileLen) {
                    int xx = fileLen - content.size();
                    for (int i = 0; i < xx; i++) {
                        content.add((byte) 0);
                    }
                } else if (content.size() > fileLen) {
                    end();
                    return false;
                }


                byte[] wstr = Hex.listTobytes(content);

                byte f = 0;
                if (fileIdBytes.length != 2) {
                    end();
                    return false;
                }
                f = (byte) (0x80 + fileIdBytes[1]);
                byte[] bs = Hex.bytePlusbByte(new byte[]{0x00, (byte) 0xD6, f, 0x00, (byte) fileLen}, wstr);
                byte[] len = new byte[1];
                byte[] bytes = new byte[200];
                code = rf_ISO14443A_APDU(id, (byte) bs.length, bs, len, bytes);
                byte[] bytes1 = Hex.getBytes(len, bytes);
                log("写入", code, bytes1);
                end();
                if (code == 0x9000) {
                    return true;
                } else {
                    return false;
                }
            } else {
                end();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 说明：读取文件
     *
     * @param fileId  文件id
     * @param fileLen 文件长度  注意：如果fileLen的长度大于所创建该文件的长度，则读取失败
     * @param content 返回内容
     * @param wKey    写秘钥 传 null 读不需要认证，否则需要
     * @return int false失败， true成功
     */
    public static boolean readFile(short fileId, short fileLen, List<Byte> content, byte[] wKey) {
        log(fileId + "文件读取");
        if (start()) {
            exterAuth(wKey);
            boolean ok =  read(fileId, fileLen, content);
            end();
            return ok;
        } else {
            return false;
        }
    }

    private static boolean read(short fileId, short fileLen, List<Byte> content) {
        int code = -1;
        content.clear();


        byte[] fileIdBytes = new byte[]{(byte) ((fileId & 0xFF00) >> 8), (byte) (fileId & 0xFF)};
        byte ff = 0;
        if (fileIdBytes.length != 2) {
            return false;
        }
        ff = (byte) (0x80 + fileIdBytes[1]);
        byte[] bs = {0x00, (byte) 0xb0, ff, 0x00, (byte) fileLen};
        byte[] len = new byte[1];
        byte[] bytes = new byte[500];
        code = rf_ISO14443A_APDU(id, (byte) bs.length, bs, len, bytes);
        int ilen = -1;
        byte[] bytes1 = null;
        if (len[0] < 0b0) {
            ilen = 128 + 128 + len[0];
             bytes1 = Hex.getBytes(ilen, bytes);
        } else {
            bytes1 = Hex.getBytes(len, bytes);
        }

        log("读取文件", code, bytes1);

        if (code == 0x9000) {
            for (int i = 0; i < fileLen; i++) {
                content.add(bytes1[i]);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 扫描一维码
     *
     * @return null代表没有值，否则获取成功
     */
    public static String oneDimensionalCode() {
        if(isReadingCode){
            return null;
        }
        isReadingCode = true;

        int result = -1;
        byte[] bs0 = new byte[200];
        ModuleControl.barcode_trig(id, (byte) 0);
        result = ModuleControl.barcode_read(id, 5, bs0);
        //ModuleControl.rf_exit(id);
        if (result > 0) {
            byte[] bs = Hex.getBytes(result, bs0);
            String s = new String(bs);
            //System.out.println("一维码：[" + Hex.toHexString(bs) + "]");
            s = s .replaceAll("\n", "");
            String[] ss = s.split("\r");
            if(ss.length>0){
                s = ss[ss.length-1];
            }

            log("一维码：[" + s + "]");

            isReadingCode = false;
            return s;
        }

        isReadingCode = false;
        return null;
    }


    /**
     * 初始化卡
     *
     * @param wKey 写秘钥
     * @return true 成功  false   失败
     */
    public static boolean initStruct(byte[] wKey) {
        log("初始化卡");
        if (start()) {
            int code = -1;
            //                        创建秘钥文件
            {
                byte[] bytes = {(byte) 0x80, (byte) 0xE0, 0x00, 0x00, 0x07, 0x3F, 0x00, 0x50, 0x01, (byte) 0xF6, (byte) 0xFF, (byte) 0xFF};
                byte[] rbuf = new byte[200];
                byte[] rlen = new byte[1];
                code = rf_ISO14443A_APDU(id, (byte) bytes.length, bytes, rlen, rbuf);
                byte[] bytes1 = Hex.getBytes(rlen, rbuf);
                log("创建秘钥文件", code, bytes1);
                if (code != SUCCESS) {
                    end();
                    return false;
                }
            }
            //                        写入写秘钥      第四个字节代表秘钥文件id “00”
            {
                byte[] bytes = Hex.bytePlusbByte(new byte[]{(byte) 0x80, (byte) 0xD4, 0x01, 0x00, 0x15, 0x39, (byte) 0xF0, (byte) 0xF6, (byte) 0xAA, (byte) 0xFF}, wKey);
                log("_写入写秘钥发送值：" + Hex.toHexString(bytes));
                byte[] rbuf = new byte[200];
                byte[] rlen = new byte[1];
                code = rf_ISO14443A_APDU(id, (byte) bytes.length, bytes, rlen, rbuf);
                byte[] bytes1 = Hex.getBytes(rlen, rbuf);
                log("写入写秘钥", code, bytes1);
            }

            /*for (short i = 1; i <= fileCount; i++) {
                code =file(i, (short) 30, false);
                if (code != SUCCESS) {
                    return false;
                }
            }*/

            end();
            if (code != SUCCESS) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
/*
//                        写入写秘钥      第四个字节代表秘钥文件id “00”
        code = rf_ISO14443A_APDU(Hex.bytePlusbByte(new byte[]{(byte) 0x80, (byte) 0xD4, 0x01, 0x00, 0x15, 0x39, (byte) 0xF0, (byte) 0xF6, (byte) 0xAA, (byte) 0xFF},
                wrpwd), new ArrayList<Byte>());
        if (back.size() > 0) {
            return -1;
        }
//                        写入读秘钥   第四个字节代表秘钥文件id “01”

        code = rf_ISO14443A_APDU(Hex.bytePlusbByte(new byte[]{(byte) 0x80, (byte) 0xD4, 0x01, 0x01, 0x15, 0x39, (byte) 0xF0, (byte) 0xF6, 0x03, (byte) 0x88},
                rdpwd), new ArrayList<Byte>());
        if (back.size() > 0) {
            return -1;
        }*/

    }

    /**
     * 调用蜂鸣器
     *
     * @param ms 响应时间，单位毫秒
     */
    public static void bell(final int ms) {
        HdxUtil.EnableBuzze(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(ms);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    HdxUtil.EnableBuzze(0);
                }
            }
        }).start();
    }




    /**
     * 开始系列操作
     * @param key     写秘钥  如果只是读取不需要加密的文件， key 为null
     * @return true 成功  false   失败
     */
    public static boolean beginSeries(byte[] key)
    {
        if(start())
        {
            return exterAuth(key);
        }
        return false;
    }


    /**
     * 结束系列操作
     *
     * @return true 成功  false   失败
     */
    public static boolean endSeries()
    {
        end();
        return true;
    }


    /**
     * 系列操作 -- 添加文件
     * @param fileId  文件id
     * @param fileLen 文件长度 最优设置 48个字节以下，48十六进制：0x30
     * @param auth    读些文件时是否认证 true 需要， false 不需要
     * @return true 成功  false   失败
     */
    public static boolean addFileSeries(short fileId, short fileLen, boolean auth)
    {
        log(fileId + "系列操作 -- 文件添加");
        int code = file(fileId, fileLen, auth);
        if (code == 0x9000) {
            return true;
        }
        return false;
    }


    /**
     * 系列操作 -- 写文件
     * @param fileId  文件id
     * @param fileLen 文件长度  注意：如果fileLen的长度大于所创建该文件的长度，则写入失败
     * @param content 写入文件内容
     * @return int false失败， true 成功
     */
    public static boolean writeFileSeries(short fileId, short fileLen, List<Byte> content)
    {
        int code = -1;
        byte[] fileIdBytes = new byte[]{(byte) ((fileId & 0xFF00) >> 8), (byte) (fileId & 0xFF)};
        byte[] fileLenBytes = new byte[]{(byte) ((fileLen & 0xFF00) >> 8), (byte) (fileLen & 0xFF)};

        //填充0
        if (content.size() < fileLen) {
            int xx = fileLen - content.size();
            for (int i = 0; i < xx; i++) {
                content.add((byte) 0);
            }
        } else if (content.size() > fileLen) {
            end();
            return false;
        }


        byte[] wstr = Hex.listTobytes(content);

        byte f = 0;
        if (fileIdBytes.length != 2) {
            end();
            return false;
        }
        f = (byte) (0x80 + fileIdBytes[1]);
        byte[] bs = Hex.bytePlusbByte(new byte[]{0x00, (byte) 0xD6, f, 0x00, (byte) fileLen}, wstr);
        byte[] len = new byte[1];
        byte[] bytes = new byte[200];
        code = rf_ISO14443A_APDU(id, (byte) bs.length, bs, len, bytes);
        byte[] bytes1 = Hex.getBytes(len, bytes);
        log("写入", code, bytes1);


        if (code == 0x9000) {
            return true;
        }
        return false;
    }


    /**
     * 系列操作 -- 读取文件
     *
     * @param fileId  文件id
     * @param fileLen 文件长度  注意：如果fileLen的长度大于所创建该文件的长度，则读取失败
     * @param content 返回内容
     * @return int false失败， true成功
     */
    public static boolean readFileSeries(short fileId, short fileLen, List<Byte> content) {
        log(fileId + "系列操作 -- 文件读取");
        return read(fileId, fileLen, content);
    }
}
