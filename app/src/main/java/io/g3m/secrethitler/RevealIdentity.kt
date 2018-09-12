package io.g3m.secrethitler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_reveal_identity.*

class RevealIdentity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reveal_identity)
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textView2.text = progress.toString()
                imageView2.rotationY = progress.toFloat()
            }

        })

        /*
        The average working distance for text messages was 36 centimeters (14.2 inches).
        ....... when viewing a web page on a smart phone:  32 centimeters (12.6 inches).
        Home test ... when "mega relaxed" 46cm = 18.1 inches
        dp -> 160 dp = 1 inch
         */
        val scale = resources.displayMetrics.density
        val distance = (160 * 18.1).toInt()
        imageView2.cameraDistance = distance * scale
    }
}
