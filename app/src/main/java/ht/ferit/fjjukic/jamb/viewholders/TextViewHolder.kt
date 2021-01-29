package ht.ferit.fjjukic.jamb.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.jamb.models.SheetField
import kotlinx.android.synthetic.main.text_layout.view.*

class TextViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {

    fun bindData(sheetField: SheetField) {
        view.tv_sheet.text = sheetField.value
    }
}