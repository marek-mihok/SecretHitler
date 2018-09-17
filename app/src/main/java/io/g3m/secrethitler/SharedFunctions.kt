package io.g3m.secrethitler

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity


fun AppCompatActivity.vibrate() {
    val vb = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val sp = getSharedPreferences("Settings", 0)
    val shouldVibrate = sp.getBoolean("vibrations", false)
    val vibrationDuration = resources.getInteger(R.integer.vibration_duration).toLong()

    if (shouldVibrate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vb.vibrate(VibrationEffect.createOneShot(vibrationDuration,VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vb.vibrate(vibrationDuration)
        }
    }
}





// KOMENTAY KTORE JE NAM LUTO VYMAZAT :D ...


/*seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val rotation = (progress - 180)
        var newRotation = rotation
        textView2.text = progress.toString()


        if (rotation > -90 || rotation < 90) {
            imageView2.setImageResource(R.drawable.img_liberal_role)
        }

        if (rotation < -90) {
            newRotation = rotation + 180
            imageView2.setImageResource(R.drawable.img_fascist_role)
        }

        if (rotation > 90) {
            newRotation = rotation + 180
            imageView2.setImageResource(R.drawable.img_hitla_role)
        }

        imageView2.rotationY = newRotation.toFloat()

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
imageView2.cameraDistance = distance * scale */