package com.georgeellickson.giphyviewer.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.georgeellickson.giphyviewer.R
import com.georgeellickson.giphyviewer.network.GiphyTrendingItem

class TrendingItemAdapter : ListAdapter<GiphyTrendingItem, TrendingItemAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_giphy, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(getItem(position))
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val gifImage: ImageView = view.findViewById(R.id.image_gif)

        fun bindViews(item: GiphyTrendingItem) {
            Glide.with(gifImage.context)
                .asGif()
                .load(item.images.gif)
                .into(gifImage)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<GiphyTrendingItem>() {
        override fun areItemsTheSame(old: GiphyTrendingItem, new: GiphyTrendingItem): Boolean {
            return old.id == new.id
        }

        override fun areContentsTheSame(old: GiphyTrendingItem, new: GiphyTrendingItem): Boolean {
            return old == new
        }
    }

}