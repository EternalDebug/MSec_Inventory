package com.example.inventory.ui.settings

import androidx.lifecycle.ViewModel
import com.example.inventory.data.SettingsRep

class SettingsViewModel: ViewModel() {
    private val repka: SettingsRep = SettingsRep()

    fun hideData(): Boolean { return repka.hideData()}
    fun switchHideData() { return repka.switchHideData()}

    fun restrictedShare(): Boolean {return  repka.restrictedShare()}
    fun switchRestrictedShare() {return repka.switchRestrictedShare()}

    fun setDefault(): Boolean {return repka.setDefault()}
    fun default(): Int {return  repka.default()}
    fun switchSetDefault() {return repka.switchSetDefault()}

    fun newDefault(toInt: String) {
        val res = toInt.toIntOrNull()
        if (res != null && res > 0)
            repka.newDefault(res.toString())
    }
}