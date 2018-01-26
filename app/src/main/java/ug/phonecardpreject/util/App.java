package ug.phonecardpreject.util;

import ug.phonecardpreject.base.BaseApplication;

/**
 * Created by Administrator on 2017/2/7.
 */

public class App extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();

//        registerHomeKeyReceiver(this);
    }
    ///////////////////////////////////////////////////////////////////////////
    // HOME 键广播
    ///////////////////////////////////////////////////////////////////////////
//    private static HomeWatcherReceiver mHomeKeyReceiver = null;
/*
    private static void registerHomeKeyReceiver(Context context) {
//        Log.i(LOG_TAG, "registerHomeKeyReceiver");
//        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        homeFilter.setPriority(Integer.MAX_VALUE);
    context.registerReceiver(mHomeKeyReceiver, homeFilter);
}
    public static void unregisterHomeKeyReceiver(Context context) {
//        Log.i(LOG_TAG, "unregisterHomeKeyReceiver");
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }*/

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.out.println("App.onLowMemory");
        System.gc();
    }
}
