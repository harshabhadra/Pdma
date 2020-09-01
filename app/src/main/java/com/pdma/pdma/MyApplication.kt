package com.pdma.pdma

import com.easypay.epmoney.epmoneylib.application.PaisaNikalApp
import timber.log.Timber

class MyApplication :PaisaNikalApp(){
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        PaisaNikalApp.init(this)
    }
}