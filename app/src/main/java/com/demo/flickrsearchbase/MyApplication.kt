package com.demo.flickrsearchbase

import androidx.fragment.app.FragmentManager
import com.demo.flickrsearchbase.utils.NavigatorImpl
import com.subwilven.basemodel.project_base.BaseApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MyApplication :BaseApplication(){

    val applicationModule = module() {
        factory { (fragmentManager: FragmentManager) ->  NavigatorImpl(fragmentManager) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(applicationModule)
        }
    }
}