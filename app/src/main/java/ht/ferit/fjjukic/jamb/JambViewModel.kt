package ht.ferit.fjjukic.jamb

import androidx.lifecycle.ViewModel

class JambViewModel: ViewModel() {
    private lateinit var jambUI: JambUI
    private lateinit var sheetUI: SheetUI

    fun getJamb() = jambUI

    fun getSheet() = sheetUI

    fun setJamb(jambUI: JambUI){
        this.jambUI = jambUI
    }

    fun setSheet(sheetUI: SheetUI){
        this.sheetUI = sheetUI
    }

    fun resetDiceThrows(){
        this.jambUI.resetDiceThrows()
    }
}