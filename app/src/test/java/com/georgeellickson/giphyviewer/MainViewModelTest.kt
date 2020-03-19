package com.georgeellickson.giphyviewer

import com.georgeellickson.giphyviewer.storage.GiphyKeyPref
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var keyPref: GiphyKeyPref
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        keyPref = mock()
        viewModel = MainViewModel(keyPref)
    }

    @Test
    fun getStartState_hasKey() {
        whenever(keyPref.hasApiKey()).doReturn(true)
        val result = viewModel.getStartState()
        assertEquals(LaunchStartState.HOME, result)
    }

    @Test
    fun getStartState_noKey() {
        whenever(keyPref.hasApiKey()).doReturn(false)
        val result = viewModel.getStartState()
        assertEquals(LaunchStartState.SETTINGS, result)
    }
}