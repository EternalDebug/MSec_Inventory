package com.example.inventory.data

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.inventory.appContext

class SettingsRep {
    private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "PreferencesFilename",
        masterKey,
        appContext!!,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    //прятать данные
    fun hideData(): Boolean {
        return (sharedPreferences.getString("hideData", "") ?: return false) == "true"
    }
    fun switchHideData() {
        sharedPreferences.edit().putString("hideData", (if(hideData()) "false" else "true")).apply()
    }

    //ограничить поделиться
    fun restrictedShare(): Boolean {
        return (sharedPreferences.getString("restrictedShare", "") ?: return false) == "true"
    }
    fun switchRestrictedShare() {
        sharedPreferences.edit().putString("restrictedShare", (if(restrictedShare()) "false" else "true")).apply()
    }


    fun setDefault(): Boolean {
        return (sharedPreferences.getString("setDefault", "") ?: return false) == "true"
    }
    fun default(): Int {
        val t = sharedPreferences.getString("default", "") ?: return 10
        if (t == "") return 10
        return t.toInt()
    }
    fun switchSetDefault() {
        sharedPreferences.edit().putString("setDefault", (if(setDefault()) "false" else "true")).apply()
    }
    fun newDefault(toInt: String) {
        sharedPreferences.edit().putString("default", toInt).apply()
    }
}