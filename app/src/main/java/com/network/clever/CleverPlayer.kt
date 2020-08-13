package com.network.clever

import android.app.Activity
import android.app.Application
import android.content.*
import android.content.res.Configuration
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import com.bumptech.glide.Glide
import com.network.clever.data.preferences.LocalStorage
import com.network.clever.di.helper.AppInjector
import com.network.clever.service.MediaPlayerService
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject
import kotlin.system.exitProcess


class CleverPlayer : Application(), LifecycleObserver, HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var localStorage: LocalStorage

    internal var isInForeground = false

    companion object {
        fun exitApplication(activity: Activity) {
            ActivityCompat.finishAffinity(activity)
            exit()
        }

        private fun exit() {
            android.os.Process.killProcess(android.os.Process.myPid())
            System.runFinalizersOnExit(true)
            exitProcess(0)
        }
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.uprootAll()
            Timber.plant(Timber.DebugTree())
        }

        Logger.addLogAdapter(AndroidLogAdapter())

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        AppInjector.init(this)

        val intent = Intent(this, MediaPlayerService::class.java)
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)


//        val intent = Intent(applicationContext, MediaPlayerService::class.java)
//        intent.action = CommandActions.PLAY
//        startService(intent)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }

    override fun androidInjector() = dispatchingAndroidInjector

    class CleverNetworkComponentCallback(private val app: CleverPlayer) : ComponentCallbacks2 {
        override fun onConfigurationChanged(newConfig: Configuration?) {}

        override fun onTrimMemory(level: Int) {
            Glide.get(app).trimMemory(level)
            if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
                app.isInForeground = true
            }
        }

        override fun onLowMemory() {
            Timber.d("onLowMemory")

            Glide.get(app).clearMemory()
        }
    }

    var updateUI: () -> Unit = {}
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateUI()
        }
    }

    var audioService: MediaPlayerService? = null
    val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName,
            service: IBinder
        ) {
            audioService = (service as MediaPlayerService.MediaPlayerServiceBinder).service
        }

        override fun onServiceDisconnected(name: ComponentName) {
        }
    }
}