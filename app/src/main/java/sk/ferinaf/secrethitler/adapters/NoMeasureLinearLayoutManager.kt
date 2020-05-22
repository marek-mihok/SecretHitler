package sk.ferinaf.secrethitler.adapters

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class NoMeasureLinearLayoutManager(context: Context?): LinearLayoutManager(context) {
    override fun isAutoMeasureEnabled(): Boolean {
        return false
    }
}
