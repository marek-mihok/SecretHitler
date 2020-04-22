package sk.ferinaf.secrethitler.common

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import org.json.JSONArray
import org.json.JSONStringer
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
            @Suppress("DEPRECATION")
            vb.vibrate(vibrationDuration)
        }
    }
}


fun IntRange.random() =
        Random().nextInt((endInclusive + 1) - start) +  start

fun Int.asColor() = ContextCompat.getColor(App.context, this)

fun Int.asString(): String = App.context.resources.getString(this)

fun View.touchInside(event: MotionEvent): Boolean {
    val rawLocation = getRawXY()

    val viewMaxX = rawLocation.x + width - 1
    val viewMaxY = rawLocation.y + height - 1

    return (event.rawX <= viewMaxX && event.rawX >= rawLocation.x
            && event.rawY <= viewMaxY && event.rawY >= rawLocation.y)
}

fun View.getRawXY(): Point {
    val viewLocation = IntArray(2)
    getLocationOnScreen(viewLocation)
    return Point(viewLocation[0], viewLocation[1])
}

fun View.getRawCorrection(): Point {
    val point = getRawXY()
    point.x = point.x - x.toInt()
    point.y = point.y - y.toInt()
    return point
}

fun View.centerToView(v: View?) {
    if (v != null) {
        val rawCorrectionThis = getRawCorrection()
        val rawCorrectionView = v.getRawCorrection()
        x = v.x + rawCorrectionView.x - rawCorrectionThis.x + v.width / 2 - width / 2
        y = v.y + rawCorrectionView.y - rawCorrectionThis.y + v.height / 2 - height / 2
    }
}

fun View.showKeyboard() {
    requestFocus()
    postDelayed({
        val imm = (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }, 100)
}

fun View.hideKeyboard() {
    val imm = (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
    imm?.hideSoftInputFromWindow(windowToken,0)
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

fun <T> ArrayList<T>.toJSONString(): String {
    val stringer = JSONStringer().array()
    for (item in this) {
        stringer.value(item)
    }
    stringer.endArray()
    return stringer.toString()
}

fun View.setForceEnable(enabled: Boolean) {
    isEnabled = enabled
    val mOpacity = if (enabled) 1f else 0.38f
    alpha = mOpacity
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