package sk.ferinaf.secrethitler.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.asColor

class WinnerDialog(private val liberalsWin: Boolean, var onConfirm: ()->Unit? = {}) : DialogFragment() {

    private var text: TextView? = null
    private var image: ImageView? = null
    private var button: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_winner, container)

        text = view.findViewById(R.id.winnerDialog_text)
        image = view.findViewById(R.id.winnerDialog_image)
        button = view.findViewById(R.id.winnerDialog_button)

        if (liberalsWin) {
            text?.setText(R.string.liberals_win)
            image?.setImageResource(R.drawable.ic_dove)
            image?.setColorFilter(R.color.blueDark.asColor())
        } else {
            text?.setText(R.string.fascists_win)
            image?.setImageResource(R.drawable.ic_skull)
            image?.setColorFilter(R.color.redDark.asColor())
        }

        button?.setOnClickListener {
            dialog?.cancel()
            onConfirm()
        }

        return view
    }

}