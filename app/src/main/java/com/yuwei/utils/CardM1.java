package com.yuwei.utils;

/**
 * Created by Administrator on 2016/11/28.
 */

public class CardM1 extends CardCommon {
    private static boolean isBoolean(int code) {
        if (code == SUCCESS) {
            return true;
        } else {
            return false;
        }
    }

    public static final int SUCCESS = 0;
    public static final byte TEST_SECTOR = 4;
    public static final byte[] KEY = Hex.toByteArray("FFFFFFFFFFFF");

    public static boolean rf_request(){
        byte[] bytes = new byte[3];
        int code =ModuleControl.rf_request(id, (byte) 0, bytes);
        log("rf_request",code,bytes);
        return isBoolean(code);
    }
    static long[] t = new long[20];
    public static boolean rf_anticoll(){

        int code =ModuleControl.rf_anticoll(id, (byte) 0, t);
        log("rf_anticoll",code);
        return isBoolean(code);
    }

    public static boolean rf_select(){
        int code =ModuleControl.rf_select(id, t[0], new byte[20]);
        log("rf_select",code);
        return isBoolean(code);
    }
    public static boolean rf_load_key(byte _SecNr,byte[] _Nkey){

        int code =ModuleControl.rf_load_key(id, (byte) 0, _SecNr,_Nkey);
        log("rf_load_key",code);
        return isBoolean(code);
    }
    public static boolean rf_authentication(byte _SecNr){
        int code =ModuleControl.rf_authentication(id, (byte) 0,_SecNr);
        log("rf_authentication",code);
        return isBoolean(code);
    }
    public static void end(){
        ModuleControl.rf_rfinf_reset(CardM1.id, (byte) 0);
    }
    public static boolean start(byte _SecNr,byte[] _Nkey){
        ModuleControl.rf_rfinf_reset(CardM1.id, (byte) 1);
        rf_request();
        rf_anticoll();
        rf_select();
        rf_load_key(_SecNr,_Nkey);
        return CardM1.rf_authentication(_SecNr);
    }
    /**
     *
     * @param _Adr	每个扇区的第一块，值：1到15
     * @param _Data	写入数据，长度不得超过16个字节
     * @return	true 执行成功，false失败
     */
    public static boolean rf_write(byte _Adr,byte[] _Data){
//        if (_Data.length > 16) {
//            throw new ArrayIndexOutOfBoundsException("_Data 不得超过16个字节");
//        }
        if (_Adr < 1) {
            _Adr = 1;
        } else if (_Adr >15){
            _Adr = 15;
        }
        int code =ModuleControl.rf_write(id, (byte) (TEST_SECTOR* _Adr), _Data);
        log("rf_write",code);
        return isBoolean(code);
    }

    /**
     *
     * @param _Adr	每个扇区的第一块，值：1到15
     * @param _Data	读取数据，长度不得超过16个字节
     * @return	true 执行成功，false失败
     */
    public static boolean rf_read(byte _Adr,byte[] _Data){
//        if (_Data.length > 16) {
//            throw new ArrayIndexOutOfBoundsException("_Data 不得超过16个字节");
//        }
        if (_Adr < 1) {
            _Adr = 1;
        } else if (_Adr >15){
            _Adr = 15;
        }
        int code =ModuleControl.rf_read(id,  (byte) (TEST_SECTOR* _Adr), _Data);
        log("rf_read",code,_Data);
        return isBoolean(code);
    }

    /**
     *写数据
     * @param _Adr	每个扇区的第一块，值：1到15
     * @param _Data	读取数据，长度不得超过16个字节
     * @param _Nkey 密码，密码长度为6个字节
     * @return	true 执行成功，false失败
     */
    public static boolean rf_write(byte _Adr,byte[] _Data,byte[] _Nkey){
        log("rf_write");
        int code = -1;
        boolean b=false;
        if (start(_Adr,_Nkey)) {
            b= rf_write(_Adr, _Data);
        }
        end();
        return b;
    }

    /**
     *读数据
     * @param _Adr	每个扇区的第一块，值：1到15
     * @param _Data	读取数据，长度不得超过16个字节
     * @param _Nkey 密码，密码长度为6个字节
     * @return	true 执行成功，false失败
     */
    public static boolean rf_read(byte _Adr,byte[] _Data,byte[] _Nkey){
        log("rf_read");
        int code = -1;
        boolean b=false;
        if (start(_Adr,_Nkey)) {
            b= rf_read(_Adr, _Data);
        }
        end();
        return b;
    }

}
