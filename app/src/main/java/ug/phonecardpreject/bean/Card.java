package ug.phonecardpreject.bean;

/**
 * Created by Administrator on 2016/12/27.
 */

import com.yuwei.utils.U;

import java.util.Date;

import ug.phonecardpreject.storage.Sp;

/**
 *4、5：卡id
 * 6:   卡类型
 * 7:座号
 * 8 9：发卡时间
 * 10 11:验票时间
 */
public class Card {
    public Card() {

    }
    private byte in =0;
    public byte getIn() {
        return in;
    }
    public void setIn(byte in) {
        this.in = in;
    }
    /**
     * 进场总数
     */
    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
    /**
     *机器号
     */
    private String pos = Sp.getDeviceId();
    /**
     *卡id
     */
    private String id = "";

    /**
     *第一次进场时间
     */
    private String time = U.simpleDateFormat(new Date());

    public boolean isUpdata() {
        return isUpdata;
    }

    public void setUpdata(boolean updata) {
        isUpdata = updata;
    }
    /**
     * 是否跟新
     */
    private boolean isUpdata;

    public byte getXor() {
        return xor;
    }

    public void setXor(byte xor) {
        this.xor = xor;
    }

    /**
     * 校验位，票卡第七页的第最后一个字节
     */
    private byte xor;
    /**
     * 门票类型
     */
    private String type = "";
    /**
     * 门票名称
     */
    private String name = "";
    /**
     * 门票金额
     */
    private short money ;

    public int getQuType() {
        return quType;
    }

    public void setQuType(int quType) {
        this.quType = quType;
    }

    public String getQuName() {
        return quName;
    }

    public void setQuName(String quName) {
        this.quName = quName;
    }

    public int getPlaceType() {
        return placeType;
    }

    public void setPlaceType(int placeType) {
        this.placeType = placeType;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    /**
     *区类型
     */
    private int quType ;
    /**
     *去名称
     */
    private String quName="" ;
    /**
     *票卡编号
     */
    private int serial_number ;


    public int getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
    }
    /**
     *活动类型
     */
    private int placeType ;
    /**
     *活动名称
     */
    private String placeName="" ;
    /**
     *排号
     */
    private int pai ;
    /**
     *座号
     */
    private int hao ;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 入场次数
     */
    private int count ;

    public boolean isPicture() {
        return isPicture;
    }

    public void setPicture(boolean picture) {
        isPicture = picture;
    }
    /**
     *显示图片
     */
    private boolean isPicture ;
    /**
     *工作人员
     */
    private boolean isWork;

    public boolean isWork() {
        return isWork;
    }

    public void setWork(boolean work) {
        isWork = work;
    }

    public CardType getCardType() {
        CardType ct = new CardType(Byte.parseByte(type),name,money,count,isPicture, isWork);
        return ct;
    }

    public void setCardType(CardType cardType) {
            type =""+cardType.getId();
            name =cardType.getName();
            money =cardType.getMoney();
            count =cardType.getCount();
            isPicture =cardType.isPicture();
            isWork =cardType.isWorke();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(short money) {
        this.money = money;
    }

    public int getPai() {
        return pai;
    }

    public void setPai(int pai) {
        this.pai = pai;
    }

    public int getHao() {
        return hao;
    }

    public void setHao(int hao) {
        this.hao = hao;
    }
}
