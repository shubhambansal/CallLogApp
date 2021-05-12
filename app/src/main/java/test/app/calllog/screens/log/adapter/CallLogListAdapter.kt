package test.app.calllog.screens.log.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CallLogListAdapter : RecyclerView.Adapter<CallLogItemViewHolder>() {

    private val dataList = mutableListOf<CallLogUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogItemViewHolder {
        return CallLogItemViewHolder.createView(parent)
    }

    override fun onBindViewHolder(holder: CallLogItemViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setList(list: List<CallLogUiModel>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }
}