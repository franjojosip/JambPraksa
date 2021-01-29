package ht.ferit.fjjukic.jamb.enums

import android.content.Context
import ht.ferit.fjjukic.jamb.R

enum class SheetColumns(private val resourceId: Int) {
    MAIN(R.string.app_name),
    DOWN(R.string.down),
    UP(R.string.up),
    BOTH(R.string.both);

    fun getString(context: Context): String{
        return context.resources.getString(resourceId)
    }
}