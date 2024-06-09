package com.dicoding.storyapp.data.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.api.response.ListStoryItem
import com.dicoding.storyapp.view.detail.DetailActivity
import com.dicoding.storyapp.databinding.StoryCardBinding

class ItemAdapter : PagingDataAdapter<ListStoryItem, ItemAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = StoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            if (item != null) {
                intent.putExtra("itemId", item.id)
                intent.putExtra("itemUrl", item.photoUrl)
            }

            val photoPair = android.util.Pair.create(holder.binding.ivItemPhoto as View, "photo")
            val namePair = android.util.Pair.create(holder.binding.tvItemName as View, "name")
            val descriptionPair = android.util.Pair.create(holder.binding.tvItemDesc as View, "description")

            val options = ActivityOptions.makeSceneTransitionAnimation(
                holder.itemView.context as Activity,
                photoPair,
                namePair,
                descriptionPair
            ).toBundle()

            holder.itemView.context.startActivity(intent, options)
        }
    }

    class ItemViewHolder(val binding: StoryCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            Glide.with(binding.ivItemPhoto.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)
            binding.tvItemName.text = story.name
            binding.tvItemDesc.text = story.description
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}