package ht.ferit.fjjukic.jamb.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.jamb.ItemListener
import kotlinx.android.synthetic.main.edit_text_layout.view.*

class EditTextViewHolder(
    private val view: View,
    private val listener: ItemListener
) : RecyclerView.ViewHolder(view) {

    fun bindData(position: Int) {
        view.et_sheet.hint = "-"
        view.et_sheet.isFocusable = false

        view.et_sheet.setOnClickListener{
            listener.onClick(position)
        }
    }
}