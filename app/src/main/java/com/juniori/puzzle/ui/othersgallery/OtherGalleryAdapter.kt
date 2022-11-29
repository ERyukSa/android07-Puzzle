package com.juniori.puzzle.ui.othersgallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.juniori.puzzle.databinding.ItemGalleryRecyclerBinding
import com.juniori.puzzle.domain.entity.VideoInfoEntity
import com.juniori.puzzle.util.GalleryDiffCallBack

class OtherGalleryAdapter(
    private val onClick: (VideoInfoEntity) -> Unit
) : ListAdapter<VideoInfoEntity, OtherGalleryAdapter.ViewHolder>(
    GalleryDiffCallBack()
) {

    class ViewHolder(
        val binding: ItemGalleryRecyclerBinding,
        val height: Int,
        val onClick: (VideoInfoEntity) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoInfoEntity) {
            binding.root.layoutParams.height = height / VISIBLE_ITEM_COUNT
            binding.root.setOnClickListener {
                onClick(item)
            }
            binding.data = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemGalleryRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, parent.height, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        const val VISIBLE_ITEM_COUNT = 3
    }
}