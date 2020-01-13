package com.georgeellickson.giphyviewer.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.georgeellickson.giphyviewer.R
import com.georgeellickson.giphyviewer.appComponent
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
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(HomeViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val trendingItemAdapter = TrendingItemAdapter()
        // TODO consider alternative approach to item sizing and number of columns
        val isLandscape = requireContext().resources.configuration
            .orientation == Configuration.ORIENTATION_LANDSCAPE
        val columns = if (isLandscape) 3 else 2
        view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            adapter = trendingItemAdapter
            layoutManager = GridLayoutManager(view.context, columns)
        }

        viewModel.trendingGifs.observe(viewLifecycleOwner, Observer { items ->
            trendingItemAdapter.submitList(items)
        })

        return view
    }
}
