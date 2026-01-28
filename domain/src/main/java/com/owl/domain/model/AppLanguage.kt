package com.owl.domain.model

import java.util.Locale

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val locale: Locale
) {
    ENGLISH("en", "English (US)", Locale.US),
    RUSSIAN("ru", "Russian", Locale("ru", "RU"));

    companion object {
        fun fromCode(code: String?): AppLanguage =
            entries.find { it.code == code } ?: ENGLISH

        fun getSystemDefault(): AppLanguage {
            val systemLang = Locale.getDefault().language
            return if (systemLang == "ru") RUSSIAN else ENGLISH
        }
    }

}