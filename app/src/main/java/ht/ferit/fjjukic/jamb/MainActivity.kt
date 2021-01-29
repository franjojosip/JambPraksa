package ht.ferit.fjjukic.jamb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ht.ferit.fjjukic.jamb.fragments.RollDiceFragment
import ht.ferit.fjjukic.jamb.fragments.SheetFragment

class MainActivity : AppCompatActivity(), RouteListener, JambListener{
    private lateinit var jambUI: JambUI
    private lateinit var sheetUI: SheetUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jambUI = JambUI(6)
        sheetUI = SheetUI(4, applicationContext)

        supportFragmentManager.beginTransaction()
            .add(R.id.main_fragment, RollDiceFragment.newInstance(), "roll_dice")
            .commit()
    }

    override fun goToSheet() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment, SheetFragment.newInstance(), "sheet")
            .commit()
    }

    override fun goToRollDice() {
        val currentFragment = supportFragmentManager.fragments.last()
        if (currentFragment.tag != "roll_dice") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, RollDiceFragment.newInstance())
                .commit()
        }
    }

    override fun onBackPressed() {
        goToRollDice()
    }

    override fun getJamb(): JambUI {
        return this.jambUI
    }

    override fun getSheet(): SheetUI {
        return this.sheetUI
    }
}
