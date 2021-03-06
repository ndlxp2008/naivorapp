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

package com.naivor.app.domain.presenter;

import com.naivor.app.domain.repository.RegisterRepository;
import com.naivor.app.presentation.view.RegisterView;

import javax.inject.Inject;

/**
 * Created by naivor on 16-4-2.
 */
public class RegisterPresenter extends BasePresenter<RegisterView,RegisterRepository> {

    @Inject
    public RegisterPresenter(RegisterRepository mRepository) {
        super(mRepository);
    }

    @Override
    protected void onResponce(Object o) {

    }
}
