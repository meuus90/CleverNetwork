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

package com.network.clever.data.preferences

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.network.clever.data.datasource.model.Cache
import com.network.clever.data.datasource.model.setting.AppSetting
import com.network.clever.presentation.Caller
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalStorage
@Inject
constructor(val context: Context, val cache: Cache) {
    companion object {
        internal const val USER_FILE = "User"

        const val key_app_setting = "app_setting"
        const val key_auth_token = "auth_token"
    }

    private val pref: SharedPreferences =
        context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE)

    internal fun clearCache() {
        cleanPreference(clearRoom = true)
    }

    internal fun logOut() {
//        clearCache()
        Caller.logoutApp(context)
    }


    internal fun cleanPreference(clearRoom: Boolean = true) {
        val editor = pref.edit()
        editor.clear()
        editor.apply()
        editor.commit()

        if (clearRoom)
            clearRoom()
    }

    private fun clearRoom() = runBlocking {
        cache.playlistDao().clear()
        cache.musicDao().clear()
    }

    internal fun getAppSetting(): AppSetting {
        val gson = Gson()
        val json = pref.getString(key_app_setting, "")

        return if (json.isNullOrEmpty()) AppSetting(false, false)
        else gson.fromJson(json, AppSetting::class.java)
    }

    internal fun setAppSetting(appSetting: AppSetting) {
        val editor = pref.edit()
        val gson = Gson()
        val json = gson.toJson(appSetting)
        editor.putString(key_app_setting, json)
        editor.apply()
    }

    internal fun getAuthToken(): String? {
        val gson = Gson()
        val json = pref.getString(key_auth_token, "")

        return gson.fromJson(json, String::class.java)
    }

    internal fun setAuthToken(authToken: String?) {
        val editor = pref.edit()
        val gson = Gson()
        val json = gson.toJson(authToken)
        editor.putString(key_auth_token, json)
        editor.apply()
    }
}