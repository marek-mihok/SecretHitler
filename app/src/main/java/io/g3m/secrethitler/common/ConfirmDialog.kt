package io.g3m.secrethitler.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import io.g3m.secrethitler.R


class ConfirmDialog: AppCompatDialogFragment() {

    private var callback: ConfirmDialogListener? = null

    interface ConfirmDialogListener {
        fun confirmDialogResult(back: Boolean)
    }

    private fun touchInside(view: View, event: MotionEvent): Boolean {
        val viewLocation = IntArray(2)
        view.getLocationOnScreen(viewLocation)

        val viewMaxX = viewLocation[0] + view.width - 1
        val viewMaxY = viewLocation[1] + view.height - 1

        return (event.rawX <= viewMaxX && event.rawX >= viewLocation[0]
                && event.rawY <= viewMaxY && event.rawY >= viewLocation[1])
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val density = resources.displayMetrics.density
        val view = inflater.inflate(R.layout.confirm_dialog, container)
        callback = context as ConfirmDialogListener?

        val jaCard = view.findViewById<CardView>(R.id.jaCard)
        val neinCard = view.findViewById<CardView>(R.id.neinCard)

        val jaButton = view.findViewById<Button>(R.id.jaButton)
        val neinButton = view.findViewById<Button>(R.id.neinButton)

        neinButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    neinCard.cardElevation = Math.round(2 * density).toFloat()
                }
                MotionEvent.ACTION_UP -> {
                    neinCard.cardElevation = Math.round(4 * density).toFloat()
                    if(touchInside(v, event)) { this.dialog.cancel() }
                }
            }
            true }

        jaButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    jaCard.cardElevation = Math.round(2 * density).toFloat()
                }
                MotionEvent.ACTION_UP -> {
                    jaCard.cardElevation = Math.round(4 * density).toFloat()
                    if(touchInside(v, event)) {
                        this.dialog.cancel()
                        callback?.confirmDialogResult(true)
                    }
                }
            }
            true }

        return view
    }

}