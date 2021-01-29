package ht.ferit.fjjukic.jamb.models

import ht.ferit.fjjukic.jamb.getImageResource

data class Dice(
    var number: Int = 1,
    var isLocked: Boolean = false
) {
    fun getImage(): Int = getImageResource(number)
}