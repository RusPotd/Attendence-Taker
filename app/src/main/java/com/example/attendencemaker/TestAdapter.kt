package com.example.attendencemaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TestAdapter(private val testList: List<TestExample>,
private val listener: OnItemClickListener, private val longListner: OnItemLongClickListener? = null
                  ) :
    RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.testexample, parent, false)

        return TestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val currentItem = testList[position]

        holder.textView.text = currentItem.text
    }

    override fun getItemCount() = testList.size

    inner class TestViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview),
    View.OnClickListener, View.OnLongClickListener {
        val textView: TextView = itemview.findViewById(R.id.test_Name)

        init {
            itemview.setOnClickListener(this)
            itemview.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position!=RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            val position: Int = adapterPosition
            if(position!=RecyclerView.NO_POSITION){
                longListner!!.onItemLongClick(position)
            }
            return true
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    interface OnItemLongClickListener{
        fun onItemLongClick(position: Int)
    }

}