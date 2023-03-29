package com.pessoadev.marvelapp.presentation.characters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pessoadev.marvelapp.R
import com.pessoadev.marvelapp.databinding.ActivityCharacterBinding
import com.pessoadev.marvelapp.presentation.characters.fragments.CharactersFragment
import com.pessoadev.marvelapp.presentation.characters.fragments.FavoriteFragment

class CharactersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.toolbar.title = getString(R.string.app_name)
        setupViewPager()
    }

    private fun setupViewPager() {

        val charactersListFragment = CharactersFragment.newInstance()
        val favoriteListFragment = FavoriteFragment.newInstance()

        CharactersPageAdapter(supportFragmentManager).apply {
            addFragment(charactersListFragment, getString(R.string.characters))
            addFragment(favoriteListFragment, getString(R.string.favorites))
        }.also { characterPageAdapter ->
            binding.viewpager.adapter = characterPageAdapter
        }

        binding.tabs.setupWithViewPager(binding.viewpager)
    }
}
