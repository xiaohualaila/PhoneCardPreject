package com.yuwei.utils;

/**
 * 超高频
 */
public class uhf_hal_src {

    /*********************************************************************
     *函数名称：UHF_RFID_Open();
     *函数功能：打开RFID通讯端口
     *参    数： char portnum--串口号
     *		   int Baud--波特率
     *返 回 值：int 0--打开成功，其他--打开失败
     **********************************************************************/
    public static native int UHF_RFID_Open(int portnum,int Baud);


    /*********************************************************************
     *函数名称：UHF_RFID_Close();
     *函数功能：关闭RFID通讯端口
     *参    数： char portnum--串口号
     *返 回 值：int 0--关闭成功，其他--关闭失败
     **********************************************************************/
    public static native int UHF_RFID_Close(int portnum);



//回调接口

    //将数据放入缓冲区
    public static native int UHF_RFID_Send_putbuffer_callbak(int[]mfun, byte pbuf);


    //发送数据
    public static native int UHF_RFID_Send_data_callbak(int[] mfun);

    //接收数据回调功能
    public static native int UHF_RFID_Receive_getbuffer_callbak(int[]mfun,byte[]pbuf,  int timer_out);



    //检测是否有数据
    public static native int UHF_RFID_Receive_datacheck_callbak(int[]mfun,  int timer_out);



    public static native int UHF_RFID_get_dll_Version( byte[]version);


    /* 获取硬件版本号 */
    public static native int UHF_RFID_get_Hardware_version( byte[]sver);



    //获取固件版本号
    public static native int UHF_RFID_get_Firmware_version( byte[]sver);


    //获取模块ID
    public static native int UHF_RFID_get_module_ID( byte[]UID);

    //设置发射功率
    public static native int UHF_RFID_set_module_txpower(byte setflag, byte upower);


    //获取发射功率设置
    public static native int UHF_RFID_get_module_txpower(byte[]upower);

    //设置跳频表
    public static native int UHF_RFID_set_freq_table(byte freq_no, long[]freq_num);


    //获取跳频表设置
    public static native int UHF_RFID_get_freq_table(byte[]freq_no, long[]freq_num);




    //设置Gen2参数
    public static native int UHF_RFID_set_gen2_para(byte[]setpara);

    //获取Gen2参数
    public static native int UHF_RFID_get_gen2_para(byte[]setpara);


    //设置CW状态
    public static native int UHF_RFID_set_cw_status(byte v);


    //获取CW状态
    public static native int UHF_RFID_get_cw_status(byte[]v);



    //设置天线状态
    public static native int UHF_RFID_set_ant_port(byte setflag, byte[]enable_ant);

    //获取天线状态
    public static native int UHF_RFID_get_ant_prot(byte[]enable_ant);


    //设置Region
    public static native int UHF_RFID_set_Regions(byte setflag, byte uRegions);



    //获取Regions
    public static native int UHF_RFID_get_Regions(byte[]uRegions);


    //获取回波损耗
    public static native int UHF_RFID_get_port_loss( int[]ploss);



    //获取温度
    public static native int UHF_RFID_get_temperature( byte[]tmp);



    //功能：温度保护设置
//输入：
//输出：
//说明：温度保护，指当模块检测到的温度值超过 60 摄氏度时，会自动降低
//		寻卡速率，当温度值超过 85 摄氏度时，自动停止
    public static native int UHF_RFID_set_tempprotect( byte en);


    //功能：获取温度保护设置
//输入：
//输出：
//说明
    public static native int UHF_RFID_get_tempprotect( byte[]en);


//
//功能：设置单端口模块连续寻卡等待时间
//输入：
//输出：
//说明:

    public static native int UHF_RFID_set_mulwaittime( byte setflag,int worktimes,int waittimes);



    //功能：获取单端口模块连续寻卡等待时间
//输入：hCom,通讯端口句柄
//输出：*worktimes			最大 65535ms
//		*waittimes
//说明: 单位MS
//      获取当前模块连续寻卡等待时间设的设置参数
    public static native int UHF_RFID_get_mulwaittime( int[]worktimes,int[]waittimes);





//设置天线工作时间
//功能：
//输入：
//输出：
//说明:

    public static native int UHF_RFID_set_ant_work_time(byte setflag, byte ant_no, int worktime);



    //获取天线工作时间
    public static native int UHF_RFID_get_ant_work_time( byte ant,int[]worktime);


    //设置多天线间隔时间
    public static native int UHF_RFID_set_interval_time( byte setflag,int interval_time);


    //获取天线间隔时间
    public static native int UHF_RFID_get_interval_time(int[]worktime);


    //设置RF链路组合
    public static native int UHF_RFID_set_RF_link( byte setflag,byte rfmode);


    //获取RF链路组合
    public static native int UHF_RFID_get_RF_link( byte[]rfmode);



    //设置FastID
    public static native int UHF_RFID_set_fastID( byte enable);




    //获取FastID
    public static native int UHF_RFID_get_fastid( byte[]enable);


    //设置Tagfocus
    public static native int UHF_RFID_set_tagfocus( byte enable);




    //获取Tagfocus
    public static native int UHF_RFID_get_tagfocus( byte[]enable);


    //获取RSSI功能
    public static native int UHF_RFID_get_rssivalue( byte[]s);



    //设置模块的通讯波特率
//
    public static native int UHF_RFID_set_commbaud( byte Baud);



    //软件复位
    public static native int UHF_RFID_set_softreset();



    //设置Dual和Singel模式
    public static native int UHF_RFID_set_dualorsingel(byte setflag, byte setmode);


    //获取Dual和Singel模式
    public static native int UHF_RFID_get_dualorsingel(byte[]setmode);

    public static native int UHF_RFID_set_EPCandTIDset(byte setflag, byte setmode);



    public static native int UHF_RFID_get_EPCandTIDset(byte[]setmode);



    //寻标签过滤规则
    public static native int UHF_RFID_set_selectmaskrule(byte setflag, byte mask_Bank, int mask_bitaddr, int mask_bitlen, byte[]mask_dat);

    //--------------------------------------------------------------------------------------------------------
//标签操作
//--------------------------------------------------------------------------------------------------------
//单次寻卡
    public static native int UHF_RFID_inventory_tag(byte[]cpc, byte[]cepc, byte[]epclen, byte[]cTID, byte[]TID_len,  int[]rssi, byte[]ANT_no, int time_out);





    //连续寻卡
    public static native int UHF_RFID_inventory_mult(int times);


    //停止连续寻卡
    public static native int UHF_RFID_stop_inventory();

    public static native int UHF_RFID_inventory_multreadtag(byte[]cpc, byte[]cepc, byte[]epclen, byte[]cTID, byte[]TID_len,   int[]rssi, byte[]ANT_no, int time_out);

    //读数据
    public static native int UHF_RFID_read_data( byte[]uAccessPwd,
                                                 byte mask_Bank,int mask_bitaddr,int mask_bitlen,byte[]mask_dat,
                                                 byte read_bank,int read_addr,int read_len,byte[]read_rbuf,byte[]err_code);



    //写数据
    public static native int UHF_RFID_write_data( byte[]uAccessPwd,
                                                  byte mask_Bank,int mask_bitaddr,int mask_bitlen,byte[]mask_dat,
                                                  byte write_bank,int write_addr,int write_len,byte[]write_wbuf,byte[]err_code);



    //锁定和解锁
    public static native int UHF_RFID_lock_unlock( byte[]uAccessPwd,
                                                   byte mask_Bank,int mask_bitaddr,int mask_bitlen,byte[]mask_dat,
                                                   byte[]lock_buf,byte[]err_code);


    //Kill
    public static native int UHF_RFID_kill_tag( byte[]uAccessPwd,
                                                byte mask_Bank,int mask_bitaddr,int mask_bitlen,byte[]mask_dat,
                                                byte[]err_code);

    //--------------------------------------------------------------------------------------------------------
//EPC 写卡
//--------------------------------------------------------------------------------------------------------
    public static native int UHF_RFID_EPCread_data( byte[]uAccessPwd,byte[]epc,byte epclen,
                                                    char read_bank,int read_addr,int read_len,byte[]read_rbuf,byte[]err_code);



    //写数据
    public static native int UHF_RFID_EPCwrite_data( byte[]uAccessPwd,byte[]epc,byte epclen,
                                                     byte write_bank,int write_addr,int write_len,byte[]write_wbuf,byte[]err_code);

    //锁定和解锁
    public static native int UHF_RFID_EPClock_unlock( byte[]uAccessPwd,byte[]epc,byte epclen,
                                                      byte[]lock_buf,byte[]err_code);


    //Kill
    public static native int UHF_RFID_EPCkill_tag( byte[]ukillPwd, byte[]epc,byte epclen,
                                                   byte[]err_code);





    //时间段寻卡
    public static native int UHF_RFID_period_inventory( int timeout,long[]rdno);



    //获取时间段寻卡结果
    public static native int UHF_RFID_get_period_inventory_result(int hCom);



    //BlockWrite
    public static native int UHF_RFID_blockwrite_data(int hCom);


    //BlockErase
    public static native int UHF_RFID_blockerase_memory(int hCom);
}