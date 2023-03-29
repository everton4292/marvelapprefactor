package com.pessoadev.marvelapp.presentation.characters.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.pessoadev.marvelapp.databinding.FavoriteFragmentBinding
import com.pessoadev.marvelapp.presentation.detail.DetailActivity
import org.koin.android.ext.android.inject

class FavoriteFragment : Fragment() {

    companion object {
        fun newInstance() =
            FavoriteFragment()
    }

    private val viewModel: CharactersViewModel by activityViewModels()
    private val charactersAdapter: CharactersAdapter by inject()
    private lateinit var layoutManagerGrid: GridLayoutManager
    private lateinit var binding: FavoriteFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFavoriteRecyclerView()
        viewModel.getFavorites()

        viewModel.listCharactersFavorites.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.apply {
                    imageViewNoFavorite.visibility = View.VISIBLE
                    textViewNoFavorite.visibility = View.VISIBLE
                }

            } else {
                binding.apply {
                    imageViewNoFavorite.visibility = View.INVISIBLE
                    textViewNoFavorite.visibility = View.INVISIBLE
                }
            }
            charactersAdapter.addCharacters(it)
        }


        charactersAdapter.apply {

            setOnUnFavoriteClickListener {
                viewModel.deleteCharacter(it)
            }

            setOnClickCharacterListener {
                startActivity(Intent(activity, DetailActivity::class.java).apply {
                    putExtra("character", it)
                })
            }
        }
    }

    private fun setupFavoriteRecyclerView() {
        layoutManagerGrid =
            if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                GridLayoutManager(activity, 2)
            } else {
                GridLayoutManager(activity, 3)
            }

        binding.favoriteRecyclerView.apply {
            adapter = charactersAdapter
            setHasFixedSize(true)
            layoutManager = layoutManagerGrid
        }
    }
}
