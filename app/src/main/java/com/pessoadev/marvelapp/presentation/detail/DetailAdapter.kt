package com.pessoadev.marvelapp.presentation.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pessoadev.marvelapp.R
import com.pessoadev.marvelapp.data.model.ComicSerie
import com.pessoadev.marvelapp.databinding.ItemSeriesComicsBinding
import com.pessoadev.marvelapp.util.AppUtil

class DetailAdapter :
    RecyclerView.Adapter<DetailAdapter.SeriesComicsViewHolder>() {
    private val comicSerieList: MutableList<ComicSerie> = mutableListOf()
    lateinit var context: Context

    fun insertData(comicSerieList: MutableList<ComicSerie>) {
        this.comicSerieList.addAll(comicSerieList)
        notifyDataSetChanged()
    }

    inner class SeriesComicsViewHolder(private val itemBinding: ItemSeriesComicsBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(comicSerie: ComicSerie) {
            itemBinding.textViewName.text = comicSerie.name
            AppUtil.loadImageWithHeader(
                context,
                comicSerie.imageUrl,
                itemBinding.imageViewHero
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesComicsViewHolder {
        context = parent.context
        val binding = ItemSeriesComicsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SeriesComicsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeriesComicsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return comicSerieList.size
    }

    private fun getItem(position: Int): ComicSerie {
        return comicSerieList[position]
    }
}