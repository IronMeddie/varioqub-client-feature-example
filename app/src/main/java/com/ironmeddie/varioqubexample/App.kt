package com.ironmeddie.varioqubexample

import android.app.Application
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import com.yandex.varioqub.appmetricaadapter.AppMetricaAdapter
import com.yandex.varioqub.config.Varioqub
import com.yandex.varioqub.config.VarioqubSettings

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        appmetricaInit()
        virioqubInit()
    }

    fun appmetricaInit() {
        val config = YandexMetricaConfig.newConfigBuilder(Constance.API_KEY)
            .build()
        YandexMetrica.activate(this,config)
        YandexMetrica.enableActivityAutoTracking(this)
    }

    private fun virioqubInit() {
        val settings = VarioqubSettings.Builder("appmetrica.${Constance.APP_ID}")
            .withThrottleInterval(1000) // для теста, чтоб таймаут не мешал нам получать свежий конфиг
            .withLogs()
            .build()
        Varioqub.init(settings, AppMetricaAdapter(this), this)
    }
}