package com.pessoadev.marvelapp

import com.pessoadev.marvelapp.data.model.Character
import com.pessoadev.marvelapp.data.model.Data
import com.pessoadev.marvelapp.data.repository.MarvelRepositoryImp
import com.pessoadev.marvelapp.domain.repository.MarvelRepository
import io.mockk.mockk

open class BaseUnitTest {
    lateinit var repoMock: MarvelRepository

    fun createCharacter(): Data {
        repoMock = mockk<MarvelRepositoryImp>()
        val character1 = Character(id = "1", name = "Hulk", favorite = true)
        val character2 = Character(id = "2", name = "IronMan", favorite = false)
        val listCharacter = arrayListOf(character1, character2)
        return Data(listCharacter)
    }
}