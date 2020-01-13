package com.georgeellickson.giphyviewer.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.georgeellickson.giphyviewer.R
import com.georgeellickson.giphyviewer.appComponent
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: HomeViewModel.Factory
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().apply {
            title = getString(R.string.title_home)
            application.appComponent.inject(this@HomeFragment)
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        viewModel.trendingGifs.observe(viewLifecycleOwner, Observer { items ->
            items.forEach { giphy ->
                // TODO replace with RecyclerView impl
                Timber.d(giphy.url)
            }
        })

        return view
    }
}
