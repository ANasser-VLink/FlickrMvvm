package com.subwilven.basemodel.project_base.utils

import android.content.Context
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import com.subwilven.basemodel.project_base.utils.PrefManager.LANGUAGE
import java.util.*

public object LocalManager {

    fun setLocale(c: Context) {
        setNewLocale(c, getLanguage(c))
    }

    fun setNewLocale(c: Context, language: String?) {
        persistLanguage(c, language)
        updateResources(c, language)
    }

    fun getLanguage(c: Context): String? {
        return PreferenceManager.getDefaultSharedPreferences(c)!![LANGUAGE, "en"]
    }

    private fun persistLanguage(c: Context, language: String?) {
        language?.let { PreferenceManager.getDefaultSharedPreferences(c)!![LANGUAGE] = language }
    }

    fun updateResources(context: Context, language: String?): Context {
        var context = context
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)

        config.setLocale(locale)
        context = context.createConfigurationContext(config)

        return context
    }
}
