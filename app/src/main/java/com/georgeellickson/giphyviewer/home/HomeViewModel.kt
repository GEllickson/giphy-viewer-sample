package com.georgeellickson.giphyviewer.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.georgeellickson.giphyviewer.network.GiphyTrendingItem
import com.georgeellickson.giphyviewer.storage.GiphyRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel(private val giphyRepo: GiphyRepository) : ViewModel() {

    val trendingGifs: LiveData<List<GiphyTrendingItem>> = giphyRepo.trendingGifs

    init {
        viewModelScope.launch {
            giphyRepo.refreshTrendingGifs()
        }
    }

    class Factory @Inject constructor(private val repo: GiphyRepository) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(repo) as T
        }
    }

}