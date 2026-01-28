package com.owl.ainterview.core

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import com.owl.domain.model.AppLanguage
import java.util.Locale

object LocaleUtils {
    fun updateBaseContextLocale(context: Context, language: AppLanguage): ContextWrapper {
        val locale = language.locale
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        val localizedContext = context.createConfigurationContext(configuration)
        return ContextWrapper(localizedContext)
    }
}