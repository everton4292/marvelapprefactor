package com.pessoadev.marvelapp.presentation.characters.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pessoadev.marvelapp.data.model.Character
import com.pessoadev.marvelapp.databinding.CharactersFragmentBinding
import com.pessoadev.marvelapp.presentation.detail.DetailActivity
import com.pessoadev.marvelapp.util.EndlessRecyclerViewScrollListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CharactersFragment : Fragment() {

    companion object {
        fun newInstance() =
            CharactersFragment()
    }

    private val viewModel: CharactersViewModel by activityViewModel()
    private val charactersAdapter: CharactersAdapter by inject()
    lateinit var layoutManagerGrid: GridLayoutManager
    private lateinit var binding: CharactersFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = CharactersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCharacterRecyclerView()

        viewModel.apply {
            listCharacters.observe(viewLifecycleOwner) {
                if (it != null) updateCharacterList(it)
            }

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    binding.progress.visibility = View.VISIBLE
                } else {
                    binding.progress.visibility = View.INVISIBLE
                }
            }
            errorMessage.observe(viewLifecycleOwner) {
                binding.textViewErrorMessage.text = it
            }
        }

        charactersAdapter.apply {
            setOnFavoriteClickListener {
                viewModel.saveCharacter(it)
            }

            setOnUnFavoriteClickListener {
                viewModel.deleteCharacter(it)
            }

            setOnClickCharacterListener {
                startActivity(Intent(activity, DetailActivity::class.java).apply {
                    putExtra("character", it)
                })
            }
        }

        binding.textViewTryAgain.setOnClickListener {
            viewModel.getCharacters()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getCharacters()
    }

    private fun setupCharacterRecyclerView() {
        layoutManagerGrid =
            if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                GridLayoutManager(activity, 2)
            } else {
                GridLayoutManager(activity, 3)
            }

        binding.charactersRecyclerView.apply {
            adapter = charactersAdapter
            setHasFixedSize(true)
            this.layoutManager = layoutManagerGrid

            addOnScrollListener(
                object : EndlessRecyclerViewScrollListener(layoutManagerGrid) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        viewModel.getCharacters()
                    }
                }
            )
        }
    }

    private fun updateCharacterList(charactersList: List<Character>) {
        charactersAdapter.addCharacters(charactersList)
    }

    override fun onResume() {
        viewModel.verifyLocalFavorites()
        super.onResume()
    }
}
