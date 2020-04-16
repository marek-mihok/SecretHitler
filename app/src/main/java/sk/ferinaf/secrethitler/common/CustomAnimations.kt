package sk.ferinaf.secrethitler.common

import android.view.animation.*

object CustomAnimations {

    val textFadeIn = AlphaAnimation(0F, 0.66F)
        get() {
            field.interpolator = LinearInterpolator()
            field.duration = 1000
            field.startOffset = 1000
            field.isFillEnabled = true
            field.fillAfter = true
            return field
        }


    val textFadeOut = AlphaAnimation(0.66F, 0F)
        get() {
            field.duration = 250
            return field
        }
}