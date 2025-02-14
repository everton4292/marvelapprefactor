package com.pessoadev.marvelapp.presentation.characters.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pessoadev.marvelapp.R
import com.pessoadev.marvelapp.data.model.Character
import com.pessoadev.marvelapp.databinding.ItemCharacterBinding
import com.pessoadev.marvelapp.databinding.ItemSeriesComicsBinding
import com.pessoadev.marvelapp.util.GlideApp

class CharactersAdapter : RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>() {

    lateinit var context: Context
    lateinit var favoriteListener: OnFavoriteClickListener
    lateinit var unFavoriteListener: OnUnFavoriteClickListener
    lateinit var clickCharacterListener: OnClickCharacterListener

    private val charactersList: MutableList<Character> = mutableListOf()

    inner class CharactersViewHolder(private val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            binding.textViewHeroName.text = character.name

            GlideApp.with(context)
                .load("${character.thumbnail?.path}.${character.thumbnail?.extension}")
                .into(binding.imageViewHero)

            binding.checkBoxFavorite.isChecked = character.favorite

            binding.checkBoxFavorite.setOnClickListener {
                if (binding.checkBoxFavorite.isChecked) {
                    favoriteListener.onClick(character)
                    character.favorite = true
                } else {
                    unFavoriteListener.onClick(character)
                    character.favorite = false
                }
            }
            itemView.setOnClickListener {
                clickCharacterListener.onClick(character)
            }
        }
    }

    fun addCharacters(listCharacters: List<Character>) {
        charactersList.clear()
        charactersList.addAll(listCharacters)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        context = parent.context
        val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CharactersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return charactersList.size
    }

    private fun getItem(position: Int): Character {
        return charactersList[position]
    }


    inline fun setOnFavoriteClickListener(crossinline listener: (Character) -> Unit) {
        this.favoriteListener = object :
            OnFavoriteClickListener {
            override fun onClick(character: Character) = listener(character)
        }
    }

    inline fun setOnUnFavoriteClickListener(crossinline listener: (Character) -> Unit) {
        this.unFavoriteListener = object :
            OnUnFavoriteClickListener {
            override fun onClick(character: Character) = listener(character)
        }
    }

    inline fun setOnClickCharacterListener(crossinline listener: (Character) -> Unit) {
        this.clickCharacterListener = object :
            OnClickCharacterListener {
            override fun onClick(character: Character) = listener(character)
        }
    }


    interface OnFavoriteClickListener {
        fun onClick(character: Character) = Unit
    }

    interface OnUnFavoriteClickListener {
        fun onClick(character: Character) = Unit
    }

    interface OnClickCharacterListener {
        fun onClick(character: Character) = Unit
    }


}