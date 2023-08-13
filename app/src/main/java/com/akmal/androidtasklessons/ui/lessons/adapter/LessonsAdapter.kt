package com.akmal.androidtasklessons.ui.lessons.adapter

import android.annotation.SuppressLint
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.akmal.androidtasklessons.databinding.ItemLessonBinding
import com.akmal.androidtasklessons.ui.lessons.model.LessonViewData
import com.akmal.androidtasklessons.utils.Constants
import com.akmal.androidtasklessons.utils.extensions.loadImage

class LessonsAdapter : RecyclerView.Adapter<LessonsAdapter.Holder>() {
    private val items: MutableList<LessonViewData> = mutableListOf()
    private var itemClickListener: ((LessonViewData, Boolean, List<LessonViewData>) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLessonBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<LessonViewData>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    inner class Holder(private val binding: ItemLessonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LessonViewData, pos: Int) = with(binding) {
            image.loadImage(data.thumbnail)
            name.text = data.name
            desc.text = data.description

            val shouldApplyFilter = pos > 2 && !Constants.isPurchased

            if (shouldApplyFilter) {
                val matrix = ColorMatrix()
                matrix.setSaturation(0f)
                image.colorFilter = ColorMatrixColorFilter(matrix)
                view.isVisible = true
            } else {
                image.clearColorFilter()
                view.isVisible = false
            }

            root.setOnClickListener {
                itemClickListener?.invoke(data, shouldApplyFilter, items)
            }

        }
    }

    fun setOnItemClickListener(block: (LessonViewData, Boolean, List<LessonViewData>) -> Unit) {
        itemClickListener = block
    }
}
