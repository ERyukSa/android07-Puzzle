package com.juniori.puzzle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.juniori.puzzle.data.weather.WeatherItem
import com.juniori.puzzle.databinding.ItemWeatherDetailBinding

class WeatherRecyclerViewAdapter :
    ListAdapter<WeatherItem, WeatherRecyclerViewAdapter.WeatherViewHolder>(diffUtil) {
    inner class WeatherViewHolder(val binding: ItemWeatherDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(weatherItem: WeatherItem) {
            binding.item = weatherItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            ItemWeatherDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        return holder.bind(currentList[position])
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<WeatherItem>() {
            override fun areItemsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
                return oldItem == newItem
            }

        }
    }

}