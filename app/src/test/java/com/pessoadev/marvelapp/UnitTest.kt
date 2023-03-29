package com.pessoadev.marvelapp

import com.pessoadev.marvelapp.data.model.CharactersResponse
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class UnitTest : BaseUnitTest() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private lateinit var testScope: TestScope

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        testScope = TestScope()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testCharacterList() {
        testScope.runTest {
            val responseMock = CharactersResponse(createCharacter())
            coEvery { repoMock.getCharacters(20, 1) } returns (responseMock)
            assertEquals(
                responseMock.data.results.size,
                repoMock.getCharacters(20, 1).data.results.size
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testCharacterListNotNull() {
        testScope.runTest {
            val responseMock = CharactersResponse(createCharacter())
            coEvery { repoMock.getCharacters(20, 1) } returns (responseMock)

            responseMock.data.results.forEach {
                assertNotNull(it)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testCharacterFavorite() {
        testScope.runTest{
            val responseMock = CharactersResponse(createCharacter())
            coEvery { repoMock.getCharacters(20, 1) } returns (responseMock)
            assertEquals(
                responseMock.data.results[0].favorite,
                repoMock.getCharacters(20, 1).data.results[0].favorite
            )
        }
    }
}
