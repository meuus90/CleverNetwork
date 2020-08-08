/*
 * Copyright (C)  2020 MeUuS90
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.network.clever.di.component

import android.app.Application
import com.network.clever.CleverNetwork
import com.network.clever.di.module.AppModule
import com.network.clever.di.module.activity.AuthActivityModule
import com.network.clever.di.module.activity.MainActivityModule
import com.network.clever.di.module.activity.PlaylistActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        AuthActivityModule::class,
        MainActivityModule::class,
        PlaylistActivityModule::class
    ]
)

interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }

    fun inject(app: CleverNetwork)
}