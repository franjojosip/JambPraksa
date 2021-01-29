package ht.ferit.fjjukic.jamb

import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.jamb.models.Dice

data class JambUI(
    val numberOfDices: Int,
    var dices: MutableLiveData<List<Dice>> = MutableLiveData(),
    var isStarted: Boolean = false,
    val numberOfThrows: MutableLiveData<Int> = MutableLiveData()
) {

    init{
        dices.postValue(List(numberOfDices) { Dice() })
        numberOfThrows.postValue(0)
    }

    fun roll() {
        isStarted = true
        dices.value!!.forEach {
            if (!it.isLocked) {
                it.number = (1..6).random()
            }
        }
        dices.postValue(dices.value)
        numberOfThrows.postValue(numberOfThrows.value!! + 1)
    }

    fun lock(index: Int): Boolean {
        this.dices.value!![index].isLocked = !this.dices.value!![index].isLocked
        return this.dices.value!![index].isLocked
    }

    fun checkDuplicates(quantity: Int): Boolean {
        return this.dices.value!!.groupingBy { it.number }.eachCount().filter { it.value >= quantity }.count() > 0
    }

    fun sumDuplicates(quantity: Int = 4, value: Int? = null): Int{
        return when{
            value != null ->{
                dices.value!!.filter {
                    it.number == value
                }.sumBy { it.number }
            }
            checkDuplicates(quantity) -> {
                var sum = 0
                this.dices.value!!.groupingBy { it.number }.eachCount().filter { it.value >= quantity }.forEach {
                    sum += (it.key * it.value)
                }
                sum + (quantity * 10)
            }

            else -> 0
        }
    }

    fun checkStraight(): Boolean {
        val sortedDices: List<Dice> = this.dices.value!!.sortedBy { it.number }
        for (i: Int in 1 until sortedDices.count()) {
            if (sortedDices[i].number != (sortedDices[i - 1].number + 1)) {
                return false
            }
        }
        return true
    }

    fun sumStraight(): Int {
        return when (checkStraight()) {
            true -> 40
            else -> 0
        }
    }

    fun sumFull(): Int {
        val groups = this.dices.value!!.groupingBy { it.number }.eachCount()
        return when {
            groups.filter { it.value > 1 }.count() > 1 && groups.filter { it.value > 2 }.count() > 0 -> {
                sumBiggerValues(groups.filter { it.value > 1 }) + 30
            }
            else -> 0
        }
    }

    private fun sumBiggerValues(groups: Map<Int, Int>): Int {
        return when {
            groups.keys.first() > groups.keys.last() -> {
                groups.keys.first() * 3 + groups.keys.last() * 2
            }
            else -> groups.keys.first() * 2 + groups.keys.last() * 3
        }
    }

    fun resetDiceThrows() {
        isStarted = false
        dices.value!!.forEach {
            it.isLocked = false
        }
        numberOfThrows.postValue(0)
    }
}