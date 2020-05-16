package sk.ferinaf.secrethitler.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.widgets.CardButton


class ConfirmDialog: DialogFragment() {

    var onConfirm: (value: Boolean) -> Unit = {}
    var afterCreated: () -> Unit = {}

    var yesButton: CardButton? = null
    var noButton: CardButton? = null
    var title: TextView? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_confirm, container)

        yesButton = view.findViewById(R.id.dialog_button_yes)
        noButton = view.findViewById(R.id.dialog_button_no)
        title = view.findViewById(R.id.confirmDialog_title_text)

        noButton?.setOnClick {
            this.dialog?.cancel()
            onConfirm(false)
        }

        yesButton?.setOnClick {
            this.dialog?.cancel()
            onConfirm(true)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterCreated()
    }

}