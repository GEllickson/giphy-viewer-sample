package com.georgeellickson.giphyviewer.storage

import com.georgeellickson.giphyviewer.network.*
import com.georgeellickson.giphyviewer.util.StringProvider
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GiphyRepositoryTest {

    private lateinit var keyPref: GiphyKeyPref
    private lateinit var api: GiphyApiService
    private lateinit var stringProvider: StringProvider
    private lateinit var repo: GiphyRepository

    @Before
    fun setUp() {
        keyPref = mock()
        api = mock()
        stringProvider = mock()
        repo = GiphyRepository(keyPref, api, stringProvider)
    }

    @Test
    fun getCachedResult_startsNull() {
        assertNull(repo.cachedResult)
    }

    @Test
    fun refreshTrendingGifs_success() = runBlockingTest {
        whenever(keyPref.getApiKey()).doReturn(API_KEY)
        whenever(api.getTrending(API_KEY)).doReturn(RESULT)

        val result = repo.refreshTrendingGifs()

        assertThat(result, IsInstanceOf(ApiResponse.Success::class.java))
        val data = (result as ApiResponse.Success).data
        assertEquals(RESULT.data, data)
        assertEquals(data, repo.cachedResult)
    }

    @Test
    fun refreshTrendingGifs_error() = runBlockingTest {
        whenever(keyPref.getApiKey()).doReturn(API_KEY)
        whenever(api.getTrending(API_KEY)).doThrow(RuntimeException("error"))

        val result = repo.refreshTrendingGifs()

        assertThat(result, IsInstanceOf(ApiResponse.Error::class.java))
        assertNull(repo.cachedResult)
    }

    @Test
    fun tryApiKey_success() = runBlockingTest {
        whenever(keyPref.getApiKey()).doReturn(API_KEY)
        whenever(api.getTrending(API_KEY)).doReturn(RESULT)

        val result = repo.tryApiKey(API_KEY)

        verify(keyPref).getApiKey()
        assertEquals(result, GiphyRepository.ApiKeyResponse.Success)
    }

    @Test
    fun tryApiKey_error() = runBlockingTest {
        whenever(keyPref.getApiKey()).doReturn(API_KEY)
        whenever(api.getTrending(API_KEY)).doThrow(RuntimeException("error"))

        val result = repo.tryApiKey(API_KEY)

        assertThat(result, IsInstanceOf(GiphyRepository.ApiKeyResponse.Failure::class.java))
        verify(keyPref).clear()
    }

    @Test
    fun clearApiKey_callsDependency() {
        repo.clearApiKey()
        verify(keyPref).clear()
    }

    companion object {
        const val API_KEY = "my_key"
        val RESULT = GiphyTrendingHolder(
            listOf(
                GiphyTrendingItem(
                    "id1",
                    "main_url",
                    ItemImages(ImageMediumStill("url_medium"), ImageMediumGif("url_gif"))
                ),
                GiphyTrendingItem(
                    "id2",
                    "main_url2",
                    ItemImages(ImageMediumStill("url_medium2"), ImageMediumGif("url_gif2"))
                )
            )
        )
    }
}