package com.pessoadev.marvelapp.presentation.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pessoadev.marvelapp.R
import com.pessoadev.marvelapp.data.model.Character
import com.pessoadev.marvelapp.databinding.ActivityDetailBinding
import com.pessoadev.marvelapp.util.GlideApp
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private val viewModel: DetailViewModel by viewModel()
    private var detailComicsAdapter = DetailAdapter()
    private val detailSeriesAdapter = DetailAdapter()
    lateinit var character: Character
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        character = intent?.extras?.getParcelable(CHARACTER)!!
        supportActionBar?.title = character.name

        loadCharacter(character)
        viewModel.loadCharacter(character)
        loadRecyclersViews()

        viewModel.getComicsSeriesByCharacterId(character.id)

        viewModel.comicsSeriesList.observe(this) {
            detailComicsAdapter.insertData(it.first)
            detailSeriesAdapter.insertData(it.second)
        }
    }

    private fun loadCharacter(character: Character) {
        binding.textViewDescription.text = character.description
        val imageViewHero = binding.imageViewHero

        GlideApp.with(this)
            .load("${character.thumbnail?.path}.${character.thumbnail?.extension}")
            .into(imageViewHero)

    }

    private fun loadRecyclersViews() {
        val layoutManagerSerie = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )
        val layoutManagerComic = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )

        binding.recyclerViewSeries.apply {
            adapter = detailComicsAdapter
            setHasFixedSize(true)
            this.layoutManager = layoutManagerSerie
        }

        binding.recyclerViewComics.apply {
            adapter = detailSeriesAdapter
            setHasFixedSize(true)
            this.layoutManager = layoutManagerComic
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        viewModel.character.observe(this) {
            if (it.favorite) {
                menu.getItem(0).icon =
                    AppCompatResources.getDrawable(this, R.drawable.ic_heath_filled_24dp)
            } else {
                menu.getItem(0).icon =
                    AppCompatResources.getDrawable(this, R.drawable.ic_heart_outline_24dp)
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_favorite -> {
                viewModel.favoriteCharacter()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val CHARACTER = "character"
    }
}
