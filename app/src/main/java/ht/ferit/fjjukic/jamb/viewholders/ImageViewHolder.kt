package ht.ferit.fjjukic.jamb.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.jamb.models.SheetField
import kotlinx.android.synthetic.main.image_layout.view.*

class ImageViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {

    fun bindData(sheetField: SheetField) {
        view.iv_sheet.setImageResource(sheetField.getImage())
    }
}