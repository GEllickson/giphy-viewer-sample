package com.georgeellickson.giphyviewer.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.georgeellickson.giphyviewer.CoroutineTestRule
import com.georgeellickson.giphyviewer.storage.GiphyRepository
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var repo: GiphyRepository
    private lateinit var loadStateObserver: Observer<Boolean>
    private lateinit var navHomeObserver: Observer<Unit>
    private lateinit var toastMesObserver: Observer<String>
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        repo = mock()
        loadStateObserver = mock()
        navHomeObserver = mock()
        toastMesObserver = mock()
        viewModel = SettingsViewModel(repo)
    }

    @Test
    fun viewModelFactory_success() {
        val result = SettingsViewModel.Factory(repo).create(SettingsViewModel::class.java)
        assertThat(result, IsInstanceOf(SettingsViewModel::class.java))
    }

    @Test
    fun tryApiKey_success() = runBlockingTest {
        whenever(repo.tryApiKey(any())).doReturn(GiphyRepository.ApiKeyResponse.Success)
        viewModel.run {
            loadingState.observeForever(loadStateObserver)
            navigateToHome.observeForever(navHomeObserver)
            toastMessage.observeForever(toastMesObserver)
        }

        viewModel.tryApiKey("key")

        verify(loadStateObserver).onChanged(true)
        verify(navHomeObserver).onChanged(null)
        verify(toastMesObserver, times(0)).onChanged(any())
        verify(loadStateObserver).onChanged(false)
    }

    @Test
    fun tryApiKey_error() = runBlockingTest {
        whenever(repo.tryApiKey(any())).doReturn(GiphyRepository.ApiKeyResponse.Failure("error"))
        viewModel.run {
            loadingState.observeForever(loadStateObserver)
            navigateToHome.observeForever(navHomeObserver)
            toastMessage.observeForever(toastMesObserver)
        }

        viewModel.tryApiKey("key")

        verify(loadStateObserver).onChanged(true)
        verify(navHomeObserver, times(0)).onChanged(Unit)
        verify(toastMesObserver).onChanged(any())
        verify(loadStateObserver).onChanged(false)
    }

}