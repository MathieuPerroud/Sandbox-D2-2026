package org.mathieu.sandbox_d2_2026

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.mathieu.data.di.dataModule


class SandboxApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SandboxApplication)
            modules(
                dataModule
            )
        }
    }
}
