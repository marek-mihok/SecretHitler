package sk.ferinaf.secrethitler.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.showKeyboard
import sk.ferinaf.secrethitler.widgets.CardButton

class AddPlayerDialog(name: String? = null, onConfirm: (name: String)->Unit): DialogFragment() {

    private var onConfirmAction: (name: String)->Unit = {}
    private var playerName: String? = null
    private var editText: EditText? = null

    init {
        onConfirmAction = onConfirm
        playerName = name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_add_player, container)

        val cancelButton = view.findViewById<CardButton>(R.id.addPlayerDialog_cancelButton)
        val confirmButton = view.findViewById<CardButton>(R.id.addPlayerDialog_confirmButton)

        editText = view.findViewById(R.id.addPlayerDialog_editText)

        playerName?.let {
            editText?.setText(it)
        }

        cancelButton.setOnClick {
            this.dialog?.cancel()
        }

        confirmButton.setOnClick {
            onConfirmAction(editText?.text.toString())
            this.dialog?.cancel()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        editText?.showKeyboard()

        dialog?.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                dismiss()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

}