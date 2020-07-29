package com.demo.flickrsearchbase

import com.subwilven.basemodel.project_base.BaseApplication
import com.subwilven.basemodel.project_base.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication :BaseApplication(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(applicationModule)
        }
    }
}