package sk.ferinaf.secrethitler.adapters

import android.animation.ObjectAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import sk.ferinaf.secrethitler.App
import sk.ferinaf.secrethitler.common.vibrate

class AddPlayersTouchHelper(val playersAdapter: AddPlayersAdapter) : ItemTouchHelper(object: ItemTouchHelper.Callback() {
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (viewHolder is AddPlayersAdapter.AddPlayerListItem) {
            return makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.DOWN)
        }
        App.context.vibrate()
        (viewHolder as? AddPlayersAdapter.PlayerVH)?.let { item ->
            ObjectAnimator.ofFloat(item.cardView, "cardElevation", 6f * App.context.resources.displayMetrics.density).start()
        }
        return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                ItemTouchHelper.DOWN or ItemTouchHelper.UP)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (target is AddPlayersAdapter.PlayerVH) {
            playersAdapter.swipe(viewHolder.adapterPosition, target.adapterPosition)
        }
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        (viewHolder as? AddPlayersAdapter.PlayerVH)?.let { item ->
            ObjectAnimator.ofFloat(item.cardView, "cardElevation", 0f).start()
        }
        playersAdapter.saveCurrentList()
        playersAdapter.notifyDataSetChanged()
    }

})