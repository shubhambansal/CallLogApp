package test.app.calllog.screens.log.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.calllog_row_item.view.*
import test.app.calllog.R

class CallLogItemViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView),
    LayoutContainer {

    override val containerView: View
        get() = itemView

    fun bind(item: CallLogUiModel) {

        itemView.nameTextView.text = item.name
        itemView.numberTextView.text = item.number
        itemView.durationTextView.text = item.duration

    }


    companion object {

        fun createView(parent: ViewGroup) =
            CallLogItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.calllog_row_item, parent, false),
            )

    }
}