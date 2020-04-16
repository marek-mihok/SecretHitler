package sk.ferinaf.secrethitler.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.cardview.widget.CardView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.widgets.NoButton
import sk.ferinaf.secrethitler.widgets.YesButton
import kotlin.math.roundToInt


class ConfirmDialog: DialogFragment() {

    private var callback: ConfirmDialogListener? = null

    interface ConfirmDialogListener {
        fun confirmDialogResult(back: Boolean)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_confirm, container)
        callback = context as ConfirmDialogListener?

        val yesButton = view.findViewById<YesButton>(R.id.dialog_button_yes)
        val noButton = view.findViewById<NoButton>(R.id.dialog_button_no)

        noButton.setOnClick {
            this.dialog?.cancel()
        }

        yesButton.setOnClick {
            this.dialog?.cancel()
            callback?.confirmDialogResult(true)
        }

        return view
    }

}