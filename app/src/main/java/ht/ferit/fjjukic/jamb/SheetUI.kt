package ht.ferit.fjjukic.jamb

import android.content.Context
import ht.ferit.fjjukic.jamb.enums.SheetColumns
import ht.ferit.fjjukic.jamb.enums.SheetRows
import ht.ferit.fjjukic.jamb.enums.ViewHolderType
import ht.ferit.fjjukic.jamb.models.SheetField

class SheetUI(private val numOfColumns: Int, context: Context) {
    private val map: HashMap<Int, List<SheetField>> = hashMapOf()

    init {
        createMap(context)
    }

    fun getLayout(viewType: Int): Int {
        return when (viewType) {
            ViewHolderType.Image.ordinal -> R.layout.image_layout
            ViewHolderType.ReadText.ordinal -> R.layout.text_layout
            else -> R.layout.edit_text_layout
        }
    }

    fun getCount(): Int {
        var count = 0
        map.forEach {
            count += it.value.size
        }
        return count
    }

    fun getField(position: Int): SheetField {
        val row = position / numOfColumns
        val column = position % numOfColumns
        return map.getValue(row)[column]
    }

    fun getViewType(position: Int): Int {
        return getField(position).type.ordinal
    }

    private fun createMap(context: Context) {
        SheetRows.values().forEachIndexed { index, sheetRow ->
            map[index] = when (sheetRow) {
                SheetRows.GAME -> createFirstRow(context)
                else -> createDefaultRow(sheetRow, context)
            }
        }
    }

    private fun createFirstRow(context: Context): List<SheetField> {
        return List(numOfColumns) {
            SheetField(value = SheetColumns.values()[it].getString(context))
        }
    }

    private fun createDefaultRow(sheetRow: SheetRows, context: Context): List<SheetField> {
        return List(numOfColumns) {
            when {
                it == SheetColumns.MAIN.ordinal -> {
                    SheetField(
                        value = sheetRow.getString(context),
                        type = getViewHolderType(sheetRow)
                    )
                }
                sheetRow == SheetRows.SUM || sheetRow == SheetRows.DIFFERENCE || sheetRow == SheetRows.TOTAL -> {
                    SheetField(value = "0")
                }
                it == SheetColumns.DOWN.ordinal && sheetRow == SheetRows.ONE || it == SheetColumns.UP.ordinal && sheetRow == SheetRows.JAMB || it == SheetColumns.BOTH.ordinal -> {
                    SheetField(type = ViewHolderType.EditText)
                }
                else -> {
                    SheetField()
                }
            }
        }
    }

    private fun getViewHolderType(sheetRow: SheetRows): ViewHolderType {
        return when (sheetRow) {
            SheetRows.ONE, SheetRows.TWO, SheetRows.THREE, SheetRows.FOUR, SheetRows.FIVE, SheetRows.SIX -> ViewHolderType.Image
            else -> ViewHolderType.ReadText
        }
    }

    fun insertValue(value: Int, position: Int) {
        val field = getField(position)
        field.value = value.toString()
        field.type = ViewHolderType.ReadText
        setNext(position)
        countColumns()
    }

    private fun setNext(position: Int) {
        val columnDownIndex = SheetColumns.DOWN.ordinal
        val row = position / numOfColumns
        val column = position % numOfColumns
        if (column == columnDownIndex || column == SheetColumns.UP.ordinal) {
            val nextSheetRow: SheetRows =
                SheetRows.getNext(SheetRows.values()[row], column == columnDownIndex)
            if (nextSheetRow != SheetRows.TOTAL) {
                getField(getPosition(nextSheetRow.ordinal, column)).type = ViewHolderType.EditText
            }
        }
    }

    private fun countColumns() {
        SheetColumns.values().forEachIndexed { index, it ->
            if (SheetColumns.MAIN != it) {
                getField(getPosition(SheetRows.SUM.ordinal, index)).value =
                    getSum(SheetRows.ONE, SheetRows.SIX, index)
                getField(getPosition(SheetRows.DIFFERENCE.ordinal, index)).value =
                    getDifference(index).toString()
                getField(getPosition(SheetRows.TOTAL.ordinal, index)).value =
                    getSum(SheetRows.STRAIGHT, SheetRows.JAMB, index)
            }
        }
    }

    private fun getPosition(row: Int, column: Int): Int {
        return row * numOfColumns + column
    }

    private fun getSum(first: SheetRows, last: SheetRows, column: Int): String {
        var sum = 0
        var currentRow = first
        do {
            val sheetField = getField(getPosition(currentRow.ordinal, column))
            if (sheetField.value.isNotEmpty()) {
                sum += sheetField.value.toInt()
            }
            currentRow = SheetRows.getNext(currentRow)
        } while (currentRow != last)
        if (sum >= 60) sum += 30
        return sum.toString()
    }

    private fun getDifference(index: Int): Int {
        val numOfOnes = getField(getPosition(SheetRows.ONE.ordinal, index)).value
        val maxValue = getField(getPosition(SheetRows.MAX.ordinal, index)).value
        val minValue = getField(getPosition(SheetRows.MIN.ordinal, index)).value
        return when {
            maxValue.isNotEmpty() && minValue.isNotEmpty() && numOfOnes.isNotEmpty() && (maxValue.toInt() - minValue.toInt() > 0) -> {
                maxValue.toInt() - minValue.toInt() * numOfOnes.toInt()
            }
            else -> 0
        }
    }

    fun getValue(context: Context, position: Int, jambUI: JambUI): Int {
        val values = jambUI.dices.value!!.map { it.number }
        return when (val sheetRow = SheetRows.values()[position / numOfColumns]) {
            SheetRows.ONE, SheetRows.TWO, SheetRows.THREE, SheetRows.FOUR, SheetRows.FIVE, SheetRows.SIX -> jambUI.sumDuplicates(value = sheetRow.getString(context).toInt())
            SheetRows.MAX -> getSortedValue(values, true)
            SheetRows.MIN -> getSortedValue(values)
            SheetRows.STRAIGHT -> jambUI.sumStraight()
            SheetRows.FULL -> jambUI.sumFull()
            SheetRows.POKER -> jambUI.sumDuplicates(4)
            SheetRows.JAMB -> jambUI.sumDuplicates(5)
            else -> 0
        }
    }

    private fun getSortedValue(values: List<Int>, isMax: Boolean = false): Int {
        return when (isMax) {
            true -> values.sorted().reversed().dropLast(1).sum()
            else -> values.sorted().dropLast(1).sum()
        }
    }
}