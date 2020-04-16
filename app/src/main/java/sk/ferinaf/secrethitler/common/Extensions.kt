package sk.ferinaf.secrethitler.common

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.json.JSONArray
import sk.ferinaf.secrethitler.App
import sk.ferinaf.secrethitler.R
import java.util.*
import kotlin.collections.ArrayList


fun Context.vibrate() {
    val vb = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val sp = getSharedPreferences("Settings", 0)
    val shouldVibrate = sp.getBoolean("vibrations", true)
    val vibrationDuration = resources.getInteger(R.integer.vibration_duration).toLong()

    if (shouldVibrate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vb.vibrate(VibrationEffect.createOneShot(vibrationDuration,VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vb.vibrate(vibrationDuration)
        }
    }
}


fun IntRange.random() =
        Random().nextInt((endInclusive + 1) - start) +  start

fun Int.asColor() = ContextCompat.getColor(App.context, this)

fun View.touchInside(view: View, event: MotionEvent): Boolean {
    val viewLocation = IntArray(2)
    view.getLocationOnScreen(viewLocation)

    val viewMaxX = viewLocation[0] + view.width - 1
    val viewMaxY = viewLocation[1] + view.height - 1

    return (event.rawX <= viewMaxX && event.rawX >= viewLocation[0]
            && event.rawY <= viewMaxY && event.rawY >= viewLocation[1])
}

@Suppress("UNCHECKED_CAST")
fun <T> JSONArray.toArrayList(): ArrayList<T> {
    val arrayList = arrayListOf<T>()
    for(i in 0 until this.length()) {
        (this[i] as? T)?.let {
            arrayList.add(it)
        }
    }
    return arrayList
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