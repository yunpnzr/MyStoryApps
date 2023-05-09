package com.example.mystoryapps.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapps.databinding.ItemsStoryBinding
import com.example.mystoryapps.detail.DetailActivity
import com.example.mystoryapps.response.ListStoryItem

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemsStoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = getItem(position)

        holder.itemView.setOnClickListener {
            val intent = createIntent(holder.itemView.context, story)
            holder.itemView.context.startActivity(intent)
        }

        story?.let { holder.bind(it) }
    }

    inner class ListViewHolder(private val binding: ItemsStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            binding.username.text = story.name

            Glide.with(itemView.context)
                .load(story.photoUrl)
                .skipMemoryCache(true)
                .into(binding.storyImage)

            story.description
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private fun createIntent(context: Context, story: ListStoryItem?): Intent {
        return Intent(context, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_NAME, story?.name)
            putExtra(DetailActivity.EXTRA_PHOTO_URL, story?.photoUrl)
            putExtra(DetailActivity.EXTRA_DESCRIPTION, story?.description)
        }
    }
}


//class StoryAdapter : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {

//    var listStory = ArrayList<ListStoryItem>()

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val binding = ItemsStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ListViewHolder(binding)
//    }

//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        val story = listStory[position]

//        holder.itemView.setOnClickListener {
//            val intent = createIntent(holder.itemView.context, story)
//            holder.itemView.context.startActivity(intent)
//        }

//        holder.bind(story)

//    }

//    override fun getItemCount(): Int = listStory.size

//    class ListViewHolder(private val _binding: ItemsStoryBinding) :
//        RecyclerView.ViewHolder(_binding.root) {
//        fun bind(story: ListStoryItem) {

//            _binding.username.text = story.name

//            Glide.with(itemView.context)
//                .load(story.photoUrl)
//                .skipMemoryCache(true)
//                .into(_binding.storyImage)

//            story.description

//        }
//    }
//    fun createIntent(context: Context, story: ListStoryItem): Intent {
//        return Intent(context, DetailActivity::class.java).apply {
//            putExtra(DetailActivity.EXTRA_NAME, story.name)
//            putExtra(DetailActivity.EXTRA_PHOTO_URL, story.photoUrl)
//            putExtra(DetailActivity.EXTRA_DESCRIPTION, story.description)
//        }
//    }

//}