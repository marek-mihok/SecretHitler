package sk.ferinaf.secrethitler.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity
import sk.ferinaf.secrethitler.widgets.CardButton


class ConfirmDialog: DialogFragment() {

    var onConfirm: (value: Boolean) -> Unit = {}
    var afterCreated: () -> Unit = {}

    var yesButton: CardButton? = null
    var noButton: CardButton? = null
    var title: TextView? = null
    var detailText: TextView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val fullScreen = (activity as? BaseActivity)?.fullScreen ?: true
        if (fullScreen) {
            dialog.window?.decorView?.apply {
                systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
        }
        return dialog
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_confirm, container)

        yesButton = view.findViewById(R.id.dialog_button_yes)
        noButton = view.findViewById(R.id.dialog_button_no)
        title = view.findViewById(R.id.confirmDialog_title_text)
        detailText = view.findViewById(R.id.confirm_dialog_detail_text)

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