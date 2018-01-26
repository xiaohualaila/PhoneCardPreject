package ug.phonecardpreject.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ug.phonecardpreject.bean.Card;
import ug.phonecardpreject.util.App;
import ug.phonecardpreject.util.Utils;

/**
 * Created by Administrator on 2016/12/26.
 */

public class Sql extends SQLiteOpenHelper {
    private final SQLiteDatabase w;
    private final SQLiteDatabase r;
    /**
     * 用户发行
     */
    public final String FAU = "fau";
    /**
     * 出厂发行
     */
    public final String FAC = "fac";
    /**
     * 检验表
     */
    public final String YAN = "YAN";

    public static final String DB = "ultralight";

    public Sql(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        w = getWritableDatabase();
        r = getReadableDatabase();
    }

    private static Sql sql;

    public synchronized static Sql getInstance() {
        if (sql == null) {      //多数据并发,获取实例;
            return sql = new Sql(App.getContext(), DB, null, 1);
        }
        return sql;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {   //检验表
        db.execSQL("create table if not exists " + YAN + "(" +       //用户检验的数据库表格;
                "id varchar(16) primary key," +//票卡id
                "time varchar(20) not null," +//票卡检验时间
                "isUpdata varchar(1) not null default '0'," +//是否上传服务器
                "type varchar(1)," +//票卡类型
                "name varchar(100)," +//票卡名称
                "money int ," +//票卡金额
                "quType int," +//票卡区号类型
                "quName varchar(100)," +//票卡区号名称
                "pai int,hao int," +//票卡排号
                "sn varchar(7)," +//票卡编号
                "placeType int," +//票卡赛事类型
                "placeName varchar(100)," +//票卡赛事名称
                "isWork varchar(1) not null default '0',\n" +
                "pos  varchar(100)," +//机器号
                "res1  varchar(50)," +//预留
                "res2  varchar(50)," +//预留
                "res3  varchar(50)," +//预留
                "res4  varchar(50)," +//预留
                "res5  varchar(50)" +//预留
                ");");

        db.execSQL("create table if not exists " + FAU + "(\n" +         //用户发行表格
                "id varchar(16) primary key,\n" +
                "type varchar(1) not null,\n" +
                "name varchar(100),\n" +
                "money int ,\n" +
                "quType int,\n" +
                "quName varchar(100),\n" +
                "sn varchar(7),\n" +
                "placeType int,\n" +
                "placeName varchar(100),\n" +
                "pai int,hao int,\n" +
                "time varchar(20) not null,\n" +
                "isUpdata varchar(1) not null default '0',\n" +
                "isWork varchar(1) not null default '0',\n" +
                "pos  varchar(100)," +//机器号
                "res1  varchar(50)," +//预留
                "res2  varchar(50)," +//预留
                "res3  varchar(50)," +//预留
                "res4  varchar(50)," +//预留
                "res5  varchar(50)" +//预留
                ");");
        db.execSQL("create table if not exists " + FAC + "(\n" +//出厂发行的表格
                "id varchar(16) primary key,\n" +
                "isUpdata varchar(1) not null default '0',\n" +
                "time varchar(20) not null,\n" +
                "pos  varchar(100)," +//机器号
                "res1  varchar(50)," +//预留
                "res2  varchar(50)," +//预留
                "res3  varchar(50)," +//预留
                "res4  varchar(50)," +//预留
                "res5  varchar(50)" +//预留
                ")\n");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int count(String table) {
        return count(table,null);       //发行的数据库;
    }
    //传入表名
    public int count(String table, String where) {  //查询数据库总的数量;
        Cursor cursor = r.rawQuery("select * from " + table+(where == null?"":(" where "+where )), new String[0]);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public boolean isID(String table, String id) {
        Cursor c = w.rawQuery("select * from " + table + " where id = ?", new String[]{id});
        boolean b = c.getCount() == 1 ? true : false;
        c.close();
        return b;
    }
    public boolean isIDWork(String table, String id) {
        Cursor c = w.rawQuery("select * from " + table + " where id = ?", new String[]{id});
        boolean b =false;
        if (c.moveToFirst()) {
            b = (c.getInt(c.getColumnIndex("isWork")) == 1);
            b = !b;
        }
        c.close();
        return b;
    }
    public void deleteById(String table, String id) {
        w.execSQL("delete from " + table + " where id = ?", new Object[]{id});
    }

    public void deleteAll(String table) {
        w.execSQL("delete from " + table);
    }

    public void insertFA(String table, Card y) {
        ContentValues v = new ContentValues();
        switch (table) {
            case FAU: {
                v.put("id", y.getId());
                v.put("pos", y.getPos());
                v.put("time", y.getTime());
                v.put("sn", Utils.decimalFormat(y.getSerial_number()));//序列号;
                v.put("type", y.getType());
                v.put("name", y.getName());
                v.put("money", y.getMoney());
                v.put("quType", y.getQuType());
                v.put("quName", y.getQuName());
                v.put("placeType", y.getPlaceType());
                v.put("placeName", y.getPlaceName());
                v.put("pai", y.getPai());
                v.put("hao", y.getHao());
                v.put("isUpdata", y.isUpdata());
                v.put("isWork", y.isWork());
                w.insert(table, null, v);
                break;
            }
            case FAC:   // 插入一条数据;
                v.put("id", y.getId());
                v.put("pos", y.getPos());
                v.put("time", y.getTime());
                v.put("isUpdata", y.isUpdata());
                w.insert(table, null, v);
                break;
        }
    }
    public void insertYAN(Card y) {
        ContentValues v = new ContentValues();
        v.put("id", y.getId());
        v.put("pos", y.getPos());
        v.put("time", y.getTime());
        v.put("sn", Utils.decimalFormat(y.getSerial_number()));
        v.put("type", y.getType());
        v.put("name", y.getName());
        v.put("money", y.getMoney());
        v.put("quType", y.getQuType());
        v.put("quName", y.getQuName());
        v.put("placeType", y.getPlaceType());
        v.put("placeName", y.getPlaceName());
        v.put("pai", y.getPai());
        v.put("hao", y.getHao());
        v.put("isUpdata", y.isUpdata());
        v.put("isWork", y.isWork());
        w.insert(YAN, null, v);
    }

    public JSONArray uploading(String table, int limit) {  //以集合的方式添加数据库中的表;
        JSONArray ja = null;    //JSON数组;
        Cursor cursor = null;
        try {
            cursor = r.rawQuery("select * from " + table + " where isUpdata='0' "+(" limit "+limit), new String[]{});
            ja = new JSONArray();
            while (cursor.moveToNext()) {
                JSONObject jo = new JSONObject();
                switch (table) {
                    case YAN:
                    case FAU: {
                        jo.put("id", cursor.getString(cursor.getColumnIndex("id")));
                        jo.put("pos", cursor.getString(cursor.getColumnIndex("pos")));
                        jo.put("time", cursor.getString(cursor.getColumnIndex("time")));
                        jo.put("isUpdata", cursor.getString(cursor.getColumnIndex("isUpdata")));
                        jo.put("sn", cursor.getString(cursor.getColumnIndex("sn")));
                        jo.put("type", cursor.getString(cursor.getColumnIndex("type")));
                        jo.put("name", cursor.getString(cursor.getColumnIndex("name")));
                        jo.put("money", cursor.getString(cursor.getColumnIndex("money")));
                        jo.put("quType", cursor.getString(cursor.getColumnIndex("quType")));
                        jo.put("quName", cursor.getString(cursor.getColumnIndex("quName")));
                        jo.put("placeType", cursor.getString(cursor.getColumnIndex("placeType")));
                        jo.put("placeName", cursor.getString(cursor.getColumnIndex("placeName")));
                        jo.put("pai", cursor.getString(cursor.getColumnIndex("pai")));
                        jo.put("hao", cursor.getString(cursor.getColumnIndex("hao")));
                        jo.put("isWork", cursor.getString(cursor.getColumnIndex("isWork")));
                        break;
                    }
                    case FAC:
                        jo.put("id", cursor.getString(cursor.getColumnIndex("id")));
                        jo.put("pos", cursor.getString(cursor.getColumnIndex("pos")));
                        jo.put("time", cursor.getString(cursor.getColumnIndex("time")));
                        jo.put("isUpdata", cursor.getString(cursor.getColumnIndex("isUpdata")));
                        break;
                }
                ja.put(jo);//放入JSONarray中去;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return ja;
    }

    /**
     * 标记为已上传
     * @param table
     * @param id
     * @return
     */
    public boolean isUpdata1(String table, String id) {
        ContentValues cv = new ContentValues();
        cv.put("isUpdata",true);
        w.update(table,cv,"id = ?",new String[]{id});
        return true;
    }

/*    public Card uploading(JSONObject sp2json) {
        Card c = new Card();

        return c;
    }*/

    /*
            switch (table) {
            case YAN: {

                break;
            }
            case FAU: {

                break;
            }
            case FAC: {

                break;
            }
     */
/*    public Card getCard(String table) {
        Card card = new Card();
        switch (table) {
            case YAN: {

                break;
            }
            case FAU: {

                break;
            }
            case FAC: {

                break;
            }
        }
        return
    }*/
}
