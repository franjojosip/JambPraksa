package ht.ferit.fjjukic.jamb.enums

import android.content.Context
import ht.ferit.fjjukic.jamb.R

enum class SheetRows(private val resourceId: Int) {
    GAME(R.string.app_name),
    ONE(R.string.one),
    TWO(R.string.two),
    THREE(R.string.three),
    FOUR(R.string.four),
    FIVE(R.string.five),
    SIX(R.string.six),
    SUM(R.string.sum),
    MAX(R.string.max),
    MIN(R.string.min),
    DIFFERENCE(R.string.difference),
    STRAIGHT(R.string.straight),
    FULL(R.string.full),
    POKER(R.string.poker),
    JAMB(R.string.app_name),
    TOTAL(R.string.total);

    fun getString(context: Context): String{
        return context.resources.getString(resourceId)
    }

    companion object {
        fun getNext(row: SheetRows, isDownWay: Boolean = true): SheetRows {
            return when (isDownWay) {
                true -> {
                    when (row) {
                        ONE -> TWO
                        TWO -> THREE
                        THREE -> FOUR
                        FOUR -> FIVE
                        FIVE -> SIX
                        SIX -> MAX
                        MAX -> MIN
                        MIN -> STRAIGHT
                        STRAIGHT -> FULL
                        FULL -> POKER
                        POKER -> JAMB
                        else -> TOTAL
                    }
                }
                else -> {
                    when (row) {
                        JAMB -> POKER
                        POKER -> FULL
                        FULL -> STRAIGHT
                        STRAIGHT -> MIN
                        MIN -> MAX
                        MAX -> SIX
                        SIX -> FIVE
                        FIVE -> FOUR
                        FOUR -> THREE
                        THREE -> TWO
                        TWO -> ONE
                        else -> TOTAL
                    }
                }
            }
        }
    }
}