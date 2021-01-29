package ht.ferit.fjjukic.jamb.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ht.ferit.fjjukic.jamb.*
import ht.ferit.fjjukic.jamb.enums.RollDiceAction
import ht.ferit.fjjukic.jamb.models.Dice
import kotlinx.android.synthetic.main.fragment_roll_dice.*

class RollDiceFragment : Fragment(), View.OnClickListener {
    companion object {
        fun newInstance(): RollDiceFragment =
            RollDiceFragment()
    }

    private var jambListener: JambListener? = null
    private lateinit var model: RollDiceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = ViewModelProviders.of(this).get(RollDiceViewModel::class.java)
        return inflater.inflate(R.layout.fragment_roll_dice, container, false);
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setButtonListener()
        setImageViewListener()

    }

    private fun setObservers() {
        model.getNumberOfThrows().observe(this, Observer {
            tvNumOfThrows.text = getString(R.string.number_of_throws, it)
        })
        model.getDices().observe(this, Observer {
            setImages(it)
        })
        model.messageHelper.observe(this, Observer {
            if (it != null) {
                Toast.makeText(
                    activity?.applicationContext,
                    it.getMessage(requireContext()),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        model.navigate.observe(this, Observer {
            if (it) {
                (requireActivity() as MainActivity).goToSheet()
            }
        })
    }

    private fun setButtonListener() {
        btnRollADice.setOnClickListener(this)
        btnIsJamb.setOnClickListener(this)
        btnIsPoker.setOnClickListener(this)
        btnIsStraight.setOnClickListener(this)
        btnGoToSheet.setOnClickListener(this)
    }

    private fun setImageViewListener() {
        ivDice1.setOnClickListener(this)
        ivDice2.setOnClickListener(this)
        ivDice3.setOnClickListener(this)
        ivDice4.setOnClickListener(this)
        ivDice5.setOnClickListener(this)
        ivDice6.setOnClickListener(this)
    }

    private fun setImages(dices: List<Dice>) {
        ivDice1.setImageResource(dices[0].getImage())
        ivDice2.setImageResource(dices[1].getImage())
        ivDice3.setImageResource(dices[2].getImage())
        ivDice4.setImageResource(dices[3].getImage())
        ivDice5.setImageResource(dices[4].getImage())
        ivDice6.setImageResource(dices[5].getImage())
    }

    override fun onClick(v: View) {
        val action: RollDiceAction
        val messageHelper: MessageHelper

        when (v.id) {
            btnGoToSheet.id -> {
                action = RollDiceAction.NAVIGATE
                messageHelper = MessageHelper(warningResourceId = R.string.roll_a_dice)
            }
            btnRollADice.id -> {
                action = RollDiceAction.ROLL
                messageHelper = MessageHelper(R.string.max_num_of_throws, null, R.string.roll_a_dice)
            }
            btnIsJamb.id, btnIsPoker.id, btnIsStraight.id -> {
                val value: String = when (v.id) {
                    btnIsPoker.id -> getString(R.string.poker)
                    btnIsJamb.id -> getString(R.string.app_name)
                    else -> getString(R.string.straight)
                }
                messageHelper = MessageHelper(R.string.condition_true, R.string.condition_false, R.string.roll_a_dice, value)
                action = RollDiceAction.CHECK
            }
            else -> {
                messageHelper = MessageHelper(R.string.unlock_message, R.string.lock_message, R.string.roll_a_dice, Integer.parseInt(v.tag.toString()).toString())
                action = RollDiceAction.LOCK
            }
        }
        model.makeAction(action, messageHelper)
    }
}