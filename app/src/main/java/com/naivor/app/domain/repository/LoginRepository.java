/*
 * Copyright (c) 2016. Naivor.All rights reserved.
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
 */

package com.naivor.app.domain.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.naivor.app.data.remote.ApiResponce.LoginResponce;
import com.naivor.app.data.remote.ApiService.LoginApiService;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * LoginRepository 登录的数据仓库类
 * <p/>
 * Created by tianlai on 16-3-16.
 */
public class LoginRepository extends BaseRepository<LoginApiService> {

    @Inject
    public LoginRepository(Context context, Retrofit retrofit) {
        super(context, retrofit);
    }

    @Override
    protected Class<LoginApiService> getServiceClass() {
        return LoginApiService.class;
    }

    /**
     * 登录请求
     *
     * @param mobile
     * @param psw
     * @return
     */
    public rx.Observable<LoginResponce> login(@NonNull String mobile, @NonNull String psw) {

        startRequest();

        return service.login(mobile,psw);
    }
}
