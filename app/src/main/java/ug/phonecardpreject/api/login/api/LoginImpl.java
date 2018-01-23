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

package ug.phonecardpreject.api.login.api;

import android.content.Context;
import android.support.annotation.NonNull;


import ug.phonecardpreject.api.base.bean.OAuth;
import ug.phonecardpreject.api.base.callback.TokenCallback;
import ug.phonecardpreject.api.base.impl.BaseImpl;
import ug.phonecardpreject.api.login.event.LoginEvent;
import ug.phonecardpreject.util.UUIDGenerator;

public class LoginImpl extends BaseImpl<LoginService> implements LoginAPI {

    public LoginImpl(@NonNull Context context) {
        super(context);
    }

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
        final String uuid = UUIDGenerator.getUUID();
        mService.getToken(user_name, password)
                .enqueue(new TokenCallback(mCacheUtil, new LoginEvent(uuid)));
        return uuid;
    }





}
