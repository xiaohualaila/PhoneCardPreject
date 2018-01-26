package ug.phonecardpreject.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/5.
 */

/**
 * 票卡类型
 */
public class CardType implements Serializable {
    /**
     * 卡id
     */
    private byte id ;

    public boolean isPicture() {
        return isPicture;
    }

    public void setPicture(boolean picture) {
        isPicture = picture;
    }

    /**
     * 检票是否有图片
     */
    private boolean isPicture ;

    public boolean isWorke() {
        return isWorke;
    }

    public void setWorke(boolean worke) {
        isWorke = worke;
    }

    /**
     * 工作人员
     */
    private boolean isWorke ;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    /**
     * 进场次数
     */
    private int  count ;

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }
    /**
     *票卡类型名称
     */
    private String name ="";

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
    /**
     *票卡类型是否允许进场
     */
    private boolean isCheck = true;

    public short getMoney() {
        return money;
    }

    public CardType() {
    }

    public CardType(byte id, String name, short money, int count, boolean isPicture, boolean isWorke) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.count = count;
        this.isPicture = isPicture;
        this.isWorke = isWorke;
    }
/*    public CardType(byte id, String name, short money,int count) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.count = count;
    }*/

    public void setMoney(short money) {
        this.money = money;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private short money ;


}
