package ug.phonecardpreject.activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import ug.phonecardpreject.R;
import ug.phonecardpreject.base.BaseActivity;
import ug.phonecardpreject.base.ViewHolder;
import ug.phonecardpreject.greendaodemo.GreenDaoManager;
import ug.phonecardpreject.greendaodemo.greendao.gen.WhiteListDao;
import ug.phonecardpreject.util.FileUtil;
import ug.phonecardpreject.util.GetDataUtil;

/**
 * Created by Administrator on 2018/1/23.
 */

public class ShowActivity  extends BaseActivity {
    private boolean isBackKeyPressed = false;
    private String path;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        String filePath = FileUtil.getPath() + File.separator + "photo";
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }
        holder.setOnClickListener(this, R.id.ll_scan,R.id.ll_xin,R.id.ll_faxing,R.id.ll_clean_data,
                R.id.ll_data_down,R.id.ll_data_up,R.id.ll_setup,R.id.ll_quit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_scan:
                openActivity(ScanActivity.class);
                break;
            case R.id.ll_xin:
                openActivity(XinActivity.class);
                break;
            case R.id.ll_faxing:
                toastShort("等待添加功能");
                break;
            case R.id.ll_clean_data:
//                toastShort("等待添加功能");
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("是否要清除所有数据？")//dialog_msg
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                WhiteListDao whiteListDao = GreenDaoManager.getInstance().getSession().getWhiteListDao();
                                whiteListDao.deleteAll();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();

                break;
            case R.id.ll_data_down:
                getExcel();
                break;
            case R.id.ll_data_up:
                toastShort("等待添加功能");
//                openActivity(UWriteActivity.class);
                break;
            case R.id.ll_setup:
                toastShort("等待添加功能");
             //   openActivity(SetUpActivity.class);
                break;
            case R.id.ll_quit:
                quitPreject();
             //   openActivity(IDActivity.class);

                break;


        }
    }

    //判断Excel文件是否存在
    private void getExcel() {
        path = FileUtil.getPath()+ File.separator +"a.xls";
        File file = new File(path);
        if(!file.exists()){
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.dialog_msg)//dialog_msg
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.show();
            return;
        }else {//存在
            toastLong("正在从Excel导入数据！");
            new ExcelDataLoader().execute(path);
        }
    }

    //在异步方法中 调用
    private class ExcelDataLoader extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {

            return GetDataUtil.getXlsData(params[0], 0);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {

            if(isSuccess){
                WhiteListDao whiteListDao = GreenDaoManager.getInstance().getSession().getWhiteListDao();
                //加载成功
                toastLong("加载成功！共"+whiteListDao.loadAll().size() + "条记录");
            }else {
                //加载失败
                toastLong(getResources().getString(R.string.load_fail));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            return quitPreject();

        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean quitPreject() {
        if(isBackKeyPressed) {
            finish();
        }
        else {
            isBackKeyPressed = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                public void run() {
                    isBackKeyPressed = false;
                }
            };
            timer.schedule(timerTask, 2000);//600毫秒后无再次点击，则复位
        }
        return false;
    }

}
