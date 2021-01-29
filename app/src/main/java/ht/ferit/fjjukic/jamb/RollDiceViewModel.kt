package ht.ferit.fjjukic.jamb

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ht.ferit.fjjukic.jamb.enums.RollDiceAction
import ht.ferit.fjjukic.jamb.enums.SheetRows
import ht.ferit.fjjukic.jamb.models.Dice
import java.util.*

class RollDiceViewModel : ViewModel() {
    private val jambUI = JambUI(6)
    var messageHelper: MutableLiveData<MessageHelper> = MutableLiveData()
    var navigate: MutableLiveData<Boolean> = MutableLiveData()

    init {
        messageHelper.value = null
        navigate.value = false
    }

    fun makeAction(action: RollDiceAction, helper: MessageHelper) {
        when (action) {
            RollDiceAction.ROLL -> {
                rollDices(helper)
            }
            RollDiceAction.LOCK -> {
                lockDice(helper)
            }
            RollDiceAction.CHECK -> {
                checkCategory(helper)
            }
            RollDiceAction.NAVIGATE -> {
                navigate.value = true
            }
        }
    }

    fun getNumberOfThrows(): MutableLiveData<Int> {
        return jambUI.numberOfThrows
    }

    fun getDices(): MutableLiveData<List<Dice>> {
        return jambUI.dices
    }

    private fun lockDice(helper: MessageHelper) {
        if (jambUI.isStarted && jambUI.numberOfThrows.value!! > 0) {
            helper.isSuccess = jambUI.lock(helper.value!!.toInt() - 1)
        }
        messageHelper.value = helper
    }

    private fun checkCategory(helper: MessageHelper) {
        if (jambUI.isStarted && jambUI.numberOfThrows.value!! > 0) {
            helper.isSuccess = when (helper.value!!.toLowerCase(Locale.getDefault())) {
                SheetRows.JAMB.name.toLowerCase(Locale.getDefault()) -> {
                    jambUI.checkDuplicates(5)
                }
                SheetRows.POKER.name.toLowerCase(Locale.getDefault()) -> {
                    jambUI.checkDuplicates(4)
                }
                SheetRows.STRAIGHT.name.toLowerCase(Locale.getDefault()) -> {
                    jambUI.checkStraight()
                }
                else -> null
            }
        }
        messageHelper.value = helper

    }

    private fun rollDices(helper: MessageHelper) {
        if (jambUI.numberOfThrows.value!! < 3) {
            jambUI.roll()
        } else {
            messageHelper.value = helper
        }
    }
}