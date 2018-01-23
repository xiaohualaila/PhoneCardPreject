package ug.phonecardpreject.util;

import android.text.TextUtils;
import android.util.Log;



import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import ug.phonecardpreject.bean.WhiteList;
import ug.phonecardpreject.greendaodemo.GreenDaoManager;
import ug.phonecardpreject.greendaodemo.greendao.gen.WhiteListDao;


/**
 * Created by Administrator on 2017/11/20.
 */

public class GetDataUtil {

    /**
     * 获取 excel 表格中的数据,不能在主线程中调用
     *
     * @param xlsName excel 表格的名称
     * @param index   第几张表格中的数据
     */
    public static Boolean getXlsData(String xlsName, int index) {
        boolean saveSuccess = false;
        try {
            File file =new File(xlsName);
            InputStream in=new FileInputStream(file);
            Workbook workbook = Workbook.getWorkbook(in);
            Sheet sheet = workbook.getSheet(index);
            int sheetNum = workbook.getNumberOfSheets();
            int sheetRows = sheet.getRows();
            int sheetColumns = sheet.getColumns();

//            Log.d(TAG, "the num of sheets is " + sheetNum);
//            Log.d(TAG, "the name of sheet is  " + sheet.getName());
//            Log.d(TAG, "total rows is 行=" + sheetRows);
//            Log.d(TAG, "total cols is 列=" + sheetColumns);
            WhiteList whiteList =null;
             String name;//姓名
             String xin_id;//芯片id
             String code_id;//二维码id
             int num;//次数
             String idCardNo;//身份证号3
             String company;//公司4
             String work;//职位5
             String certificates;//证件6
            WhiteListDao whiteListDao = GreenDaoManager.getInstance().getSession().getWhiteListDao();
            whiteListDao.deleteAll();
            for (int i = 0; i < sheetRows; i++) {
                name = sheet.getCell(0, i).getContents();
                xin_id = sheet.getCell(1, i).getContents();
                code_id = sheet.getCell(2, i).getContents();
                num = Integer.parseInt(sheet.getCell(3, i).getContents());

                Log.i("xxx",name +" "+ xin_id +" "+code_id+" "+num+" ");
                if(TextUtils.isEmpty(name) && TextUtils.isEmpty(xin_id)&& TextUtils.isEmpty(code_id)){
                    break;
                }
                whiteList = new WhiteList(name,xin_id.toUpperCase(),code_id.toUpperCase().trim(),num);
                whiteListDao.insert(whiteList);
            }
            workbook.close();
            saveSuccess = true;
        } catch (Exception e) {
            saveSuccess =false;
        }
        return saveSuccess;
    }

    public  static WhiteList getCodeDataBooean (String code){
                WhiteListDao whiteListDao = GreenDaoManager.getInstance().getSession().getWhiteListDao();
                WhiteList whiteList =  whiteListDao.queryBuilder().where(WhiteListDao.Properties.Code_id.eq(code)).build().unique();
                if(whiteList != null){
                    return whiteList;
                }else {
                    return null;
                }
    }

    public  static WhiteList getXinDataBooean (String code){
        WhiteListDao whiteListDao = GreenDaoManager.getInstance().getSession().getWhiteListDao();
        WhiteList whiteList =  whiteListDao.queryBuilder().where(WhiteListDao.Properties.Xin_id.eq(code)).build().unique();
        if(whiteList != null){
            return whiteList;
        }else {
            return null;
        }
    }

}
