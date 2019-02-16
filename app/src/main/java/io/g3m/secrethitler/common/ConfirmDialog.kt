package io.g3m.secrethitler.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import io.g3m.secrethitler.R
import kotlinx.android.synthetic.main.activity_reveal_identity.*

class ConfirmDialog: AppCompatDialogFragment() {

    private var callback: ConfirmDialogListener? = null

    interface ConfirmDialogListener {
        fun confirmDialogResult(back: Boolean)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val density = resources.displayMetrics.density
        val view = inflater.inflate(R.layout.confirm_dialog, container)
        callback = context as ConfirmDialogListener?

        val jaCard = view.findViewById<CardView>(R.id.jaCard)
        val neinCard = view.findViewById<CardView>(R.id.neinCard)

        /*
        jaCard.setOnClickListener {
            // This line is importatnt to sent value to get back :)
            this.dialog.cancel()
            callback?.confirmDialogResult(true)
        }


        neinCard.setOnClickListener {
            this.dialog.cancel()
        }*/

        neinCard.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    neinCard.cardElevation = Math.round(2 * density).toFloat()}
                MotionEvent.ACTION_UP -> {
                    neinCard.cardElevation = Math.round(4 * density).toFloat()
                    this.dialog.cancel()
                }
            }
            true }

        jaCard.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    jaCard.cardElevation = Math.round(2 * density).toFloat()
                }
                MotionEvent.ACTION_UP -> {
                    jaCard.cardElevation = Math.round(4 * density).toFloat()
                    this.dialog.cancel()
                    callback?.confirmDialogResult(true)
                }
            }
            true }

        return view
    }

}