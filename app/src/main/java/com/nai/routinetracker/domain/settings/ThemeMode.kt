package com.nai.routinetracker.domain.settings

enum class ThemeMode(val storageKey: String) {
    System("system"),
    Light("light"),
    Dark("dark");

    companion object {
        fun fromStorageKey(storageKey: String?): ThemeMode {
            return entries.firstOrNull { it.storageKey == storageKey } ?: System
        }
    }
}
