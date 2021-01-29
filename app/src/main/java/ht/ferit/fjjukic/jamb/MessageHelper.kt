package ht.ferit.fjjukic.jamb

import android.content.Context


data class MessageHelper(
    var successResourceId: Int? = null,
    var failResourceId: Int? = null,
    var warningResourceId: Int,
    var value: String? = null,
    var isSuccess: Boolean? = null
) {
    fun getMessage(context: Context): String {
        return when (isSuccess){
            true -> {
                context.getString(successResourceId!!, value)
            }
            false -> {
                context.getString(failResourceId!!, value)
            }
            else -> {
                context.getString(warningResourceId)
            }
        }
    }
}