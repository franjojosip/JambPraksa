package ht.ferit.fjjukic.jamb.models

import ht.ferit.fjjukic.jamb.enums.ViewHolderType
import ht.ferit.fjjukic.jamb.getImageResource

data class SheetField(
    var value: String = "",
    var type: ViewHolderType = ViewHolderType.ReadText
) {
    fun getImage(): Int = getImageResource(value.toInt())
}