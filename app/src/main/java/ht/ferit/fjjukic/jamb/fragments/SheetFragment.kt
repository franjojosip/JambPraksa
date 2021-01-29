package ht.ferit.fjjukic.jamb.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import ht.ferit.fjjukic.jamb.ItemListener
import ht.ferit.fjjukic.jamb.JambListener
import ht.ferit.fjjukic.jamb.R
import ht.ferit.fjjukic.jamb.recycler.RecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_sheet.*

class SheetFragment : Fragment() {
    companion object {
        fun newInstance(): SheetFragment =
            SheetFragment()
    }

    private lateinit var adapter: RecyclerViewAdapter
    private var jambListener: JambListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setRecyclerView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is JambListener) {
            jambListener = context
        } else {
            throw RuntimeException(getString(R.string.required_listener, "JambListener"))
        }
    }

    override fun onDetach() {
        super.onDetach()
        jambListener = null
    }

    private fun setAdapter() {
        jambListener?.let {
            val jamb = it.getJamb()
            val sheet = it.getSheet()
            val itemListener = object : ItemListener {
                override fun onClick(position: Int) {
                    if (jamb.isStarted) {
                        createDialog(position)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.throw_dices_before_insert),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            adapter = RecyclerViewAdapter(sheet, itemListener)
        }
    }

    private fun setRecyclerView() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 4)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.HORIZONTAL
            )
        )
    }

    private fun createDialog(position: Int) {
        jambListener?.let {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            val jamb = it.getJamb()
            val sheet = it.getSheet()
            val value = sheet.getValue(requireContext(), position, jamb)

            builder.setTitle(getString(R.string.enter_value_question))
            builder.setMessage(value.toString())

            builder.setPositiveButton(
                getString(R.string.yes)
            ) { dialog, _ ->
                sheet.insertValue(value, position)
                adapter.notifyDataSetChanged()
                jamb.resetDiceThrows()
                dialog.dismiss()
            }

            builder.setNegativeButton(
                R.string.no
            ) { dialog, _ ->
                dialog.dismiss()
            }

            val alert: AlertDialog = builder.create()
            alert.show()
        }
    }
}
