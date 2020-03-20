package com.georgeellickson.giphyviewer.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.georgeellickson.giphyviewer.CoroutineTestRule
import com.georgeellickson.giphyviewer.network.ApiResponse
import com.georgeellickson.giphyviewer.network.GiphyTrendingItem
import com.georgeellickson.giphyviewer.storage.GiphyRepository
import com.georgeellickson.giphyviewer.storage.GiphyRepositoryTest
import com.georgeellickson.giphyviewer.util.StringProvider
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException

class HomeViewModelTest {

    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var repo: GiphyRepository
    private lateinit var strProvider: StringProvider
    private lateinit var loadingObserver: Observer<Boolean>
    private lateinit var gifsObserver: Observer<List<GiphyTrendingItem>>
    private lateinit var navSettingObserver: Observer<Unit>
    private lateinit var toastObserver: Observer<String>

    @Before
    fun setUp() {
        repo = mock()
        strProvider = mock {
            on { getString(any()) } doReturn "error"
            on { getString(any(), any()) } doReturn "error"
        }
        loadingObserver = mock()
        gifsObserver = mock()
        navSettingObserver = mock()
        toastObserver = mock()
    }

    @Test
    fun viewModelFactory_success() = coroutineRule.testDispatcher.runBlockingTest {
        val factory = HomeViewModel.Factory(repo, strProvider)
        assertThat(
            factory.create(HomeViewModel::class.java),
            IsInstanceOf(HomeViewModel::class.java)
        )
    }

    @Test
    fun init_successRefresh() = coroutineRule.testDispatcher.runBlockingTest {
        whenever(repo.refreshTrendingGifs()).doReturn(ApiResponse.Success(GiphyRepositoryTest.RESULT.data))

        HomeViewModel(repo, strProvider).apply {
            loadingSpinnerVisible.observeForever(loadingObserver)
            trendingGifs.observeForever(gifsObserver)
        }

        verify(loadingObserver).onChanged(true)
        advanceUntilIdle()
        verify(gifsObserver).onChanged(GiphyRepositoryTest.RESULT.data)
        verify(loadingObserver).onChanged(false)
    }

    @Test
    fun init_fail403() = coroutineRule.testDispatcher.runBlockingTest {
        val error403 = mock<HttpException> { on { code() } doReturn 403 }
        whenever(repo.refreshTrendingGifs()).doReturn(ApiResponse.Error(error403))

        HomeViewModel(repo, strProvider).apply {
            loadingSpinnerVisible.observeForever(loadingObserver)
            navigateToSettings.observeForever(navSettingObserver)
            toastMessage.observeForever(toastObserver)
        }

        verify(loadingObserver).onChanged(true)
        advanceUntilIdle()
        verify(navSettingObserver).onChanged(null)
        verify(strProvider).getString(any())
        verify(toastObserver).onChanged(any())
        verify(loadingObserver).onChanged(false)
    }

    @Test
    fun init_failError() = coroutineRule.testDispatcher.runBlockingTest {
        val error = mock<HttpException> { on { code() } doReturn 400 }
        whenever(repo.refreshTrendingGifs()).doReturn(ApiResponse.Error(error))

        HomeViewModel(repo, strProvider).apply {
            loadingSpinnerVisible.observeForever(loadingObserver)
            navigateToSettings.observeForever(navSettingObserver)
            toastMessage.observeForever(toastObserver)
        }

        verify(loadingObserver).onChanged(true)
        advanceUntilIdle()
        verify(navSettingObserver, times(0)).onChanged(any())
        verify(strProvider).getString(any(), any())
        verify(toastObserver).onChanged(any())
        verify(loadingObserver).onChanged(false)
    }

    @Test
    fun clearApiKey() = coroutineRule.testDispatcher.runBlockingTest {
        val viewModel = HomeViewModel(repo, strProvider)
        viewModel.navigateToSettings.observeForever(navSettingObserver)

        viewModel.clearApiKey()

        verify(repo).clearApiKey()
        verify(navSettingObserver).onChanged(null)
    }

}