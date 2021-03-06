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

package com.network.clever.di.module

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.room.Room
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.meuus.base.network.NetworkError
import com.network.clever.BuildConfig
import com.network.clever.constant.AppConfig
import com.network.clever.data.datasource.model.Cache
import com.network.clever.data.datasource.network.FirebaseAPI
import com.network.clever.data.datasource.network.LiveDataCallAdapterFactory
import com.network.clever.data.datasource.network.YoutubeAPI
import com.network.clever.data.preferences.LocalStorage
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * A module for Android-specific dependencies which require a [Context] or
 * [android.app.Application] to create.
 */
@Module
class AppModule {
    companion object {
        const val timeout_read = 30L
        const val timeout_connect = 30L
        const val timeout_write = 30L
    }

    @Provides
    @Singleton
    fun appContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideLocalStorage(context: Context, cache: Cache): LocalStorage {
        return LocalStorage(context, cache)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor, context: Context): OkHttpClient {
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))

        val ok = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .connectTimeout(timeout_connect, TimeUnit.SECONDS)
            .readTimeout(timeout_read, TimeUnit.SECONDS)
            .writeTimeout(timeout_write, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            if ("robolectric" != Build.FINGERPRINT && BuildConfig.DEBUG)
                ok.addNetworkInterceptor(StethoInterceptor())

            val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    if (isJSONValid(message))
                        Logger.json(message)
                    else
                        Log.d("pretty", message)
                }

                fun isJSONValid(jsonInString: String): Boolean {
                    try {
                        JSONObject(jsonInString)
                    } catch (ex: JSONException) {
                        try {
                            JSONArray(jsonInString)
                        } catch (ex1: JSONException) {
                            return false
                        }

                    }

                    return true
                }

            })
            logging.level = HttpLoggingInterceptor.Level.BODY
            ok.addInterceptor(logging)

        }

        ok.addInterceptor(interceptor)
        return ok.build()
    }

    @Provides
    @Singleton
    fun provideGSon(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    //@Singleton
    fun getRequestInterceptor(context: Context, localStorage: LocalStorage): Interceptor {
        return Interceptor {
            Timber.tag("PRETTY_LOGGER")

            val original = it.request()

            val requested = with(original) {
                val builder = newBuilder()

                builder.header("Accept", "application/json")
//                localStorage.getAuthToken()?.also { token ->
//                    builder.header("Authorization", "Bearer $token")
//                }

                builder.build()
            }

            val response = it.proceed(requested)
            val body = response.body
            val bodyStr = body?.string()
            Timber.e("**http-num: ${response.code}")
            Timber.e("**http-body: $body")

            val cookies = HashSet<String>()
            for (header in response.headers("Set-Cookie")) {
                cookies.add(header)
            }

            if (!response.isSuccessful) {
                val networkError = Gson().fromJson(bodyStr, NetworkError::class.java)

                networkError.error.code?.let {
                    //todo : do something on error
                }
            }

            val builder = response.newBuilder()

            builder.body(
                ResponseBody.create(
                    body?.contentType()
                    , bodyStr?.toByteArray()!!
                )
            ).build()

        }
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(AppConfig.clientId)
            .requestEmail()
            .build()
    }

    @Singleton
    @Provides
    fun provideFirebaseAPI(gson: Gson, okHttpClient: OkHttpClient): FirebaseAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.firebaseServer)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(okHttpClient)
            .build()

        return retrofit.create(FirebaseAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideYoutubeAPI(gson: Gson, okHttpClient: OkHttpClient): YoutubeAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.youtubeServer)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(okHttpClient)
            .build()

        return retrofit.create(YoutubeAPI::class.java)
    }

    @Singleton
    @Provides
    internal fun provideCache(app: Application) =
        Room.databaseBuilder(app, Cache::class.java, "clever.db").build()

    @Singleton
    @Provides
    internal fun providePlaylistDao(cache: Cache) = cache.playlistDao()

    @Singleton
    @Provides
    internal fun provideMusicDao(cache: Cache) = cache.musicDao()
}