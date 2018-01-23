/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-03-08 01:01:18
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package ug.phonecardpreject.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.File;
import java.io.IOException;

import ug.phonecardpreject.api.base.bean.OAuth;
import ug.phonecardpreject.api.login.api.LoginAPI;
import ug.phonecardpreject.api.login.api.LoginImpl;
import ug.phonecardpreject.log.Config;
import ug.phonecardpreject.log.Logger;
import ug.phonecardpreject.util.DebugUtil;

/**
 * diycode 实现类，没有回调接口，使用 EventBus 来接收数据
 */
public class Diycode implements LoginAPI {

    private static LoginImpl sLoginImpl;

    //--- 单例 -----------------------------------------------------------------------------------

    private volatile static Diycode mDiycode;

    private Diycode() {
    }


    public static Diycode getSingleInstance() {
        if (null == mDiycode) {
            synchronized (Diycode.class) {
                if (null == mDiycode) {
                    mDiycode = new Diycode();
                }
            }
        }
        return mDiycode;
    }

    //--- 初始化 ---------------------------------------------------------------------------------

    public static Diycode init(@NonNull Context context, @NonNull final String client_id,
                               @NonNull final String client_secret) {
        initLogger(context);
        Logger.i("初始化 diycode");

        OAuth.client_id = client_id;
        OAuth.client_secret = client_secret;

        initImplement(context);

        return getSingleInstance();
    }

    private static void initLogger(@NonNull Context context) {
        // 在 debug 模式输出日志， release 模式自动移除
        if (DebugUtil.isInDebug(context)) {
            Logger.init("Diycode").setLevel(Config.LEVEL_FULL);
        } else {
            Logger.init("Diycode").setLevel(Config.LEVEL_NONE);
        }
    }

    private static void initImplement(Context context) {
        Logger.i("初始化 implement");
        try {
            sLoginImpl = new LoginImpl(context);




        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.i("初始化 implement 结束");
    }


    //--- login ------------------------------------------------------------------------------------

    /**
     * 登录时调用
     * 返回一个 token，用于获取各类私有信息使用，该 token 用 LoginEvent 接收。
     *
     * @param user_name 用户名
     * @param password  密码
     * @see LoginEvent
     */
    @Override
    public String login(@NonNull String user_name, @NonNull String password) {
        return sLoginImpl.login(user_name, password);
    }



}
