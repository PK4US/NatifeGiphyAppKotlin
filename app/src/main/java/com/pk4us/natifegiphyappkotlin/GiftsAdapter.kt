package com.pk4us.natifegiphyappkotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GifsAdapter(val context: Context, val gifts: List<DataObject>) :
    RecyclerView.Adapter<GifsAdapter.ViewHolder>() {

    lateinit var mListner: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListner = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false),
            mListner
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = gifts[position]

        Glide.with(context).load(data.images.ogImage.url)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return gifts.size
    }

    class ViewHolder(itemView: View, listner: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.ivGif)

        init {
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }
    }
}
