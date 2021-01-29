package ht.ferit.fjjukic.jamb.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.jamb.ItemListener
import ht.ferit.fjjukic.jamb.SheetUI
import ht.ferit.fjjukic.jamb.enums.ViewHolderType
import ht.ferit.fjjukic.jamb.viewholders.EditTextViewHolder
import ht.ferit.fjjukic.jamb.viewholders.ImageViewHolder
import ht.ferit.fjjukic.jamb.viewholders.TextViewHolder


class RecyclerViewAdapter(var sheetUI: SheetUI, private val listener: ItemListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(sheetUI.getLayout(viewType), parent, false)
        return when (viewType) {
            ViewHolderType.Image.ordinal -> ImageViewHolder(view)
            ViewHolderType.ReadText.ordinal -> TextViewHolder(view)
            else -> EditTextViewHolder(view, listener)
        }
    }

    override fun getItemCount(): Int {
        return sheetUI.getCount()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ViewHolderType.Image.ordinal -> (holder as ImageViewHolder).bindData(sheetUI.getField(position))
            ViewHolderType.ReadText.ordinal -> (holder as TextViewHolder).bindData(sheetUI.getField(position))
            else -> {
                (holder as EditTextViewHolder).bindData(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return sheetUI.getViewType(position)
    }
}