package com.georgeellickson.giphyviewer.home

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.georgeellickson.giphyviewer.R
import com.georgeellickson.giphyviewer.appComponent
import com.georgeellickson.giphyviewer.navController
import com.georgeellickson.giphyviewer.settings.SettingsFragment
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    @Inject
    lateinit var viewModelFactory: HomeViewModel.Factory
    private val viewModel: HomeViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().application.appComponent.inject(this@HomeFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.title_home)
        setHasOptionsMenu(true)

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
        viewModel.navigateToSettings.observe(viewLifecycleOwner, Observer {
            requireActivity().navController.navigateTo(SettingsFragment())
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.clear_api_key) {
            viewModel.clearApiKey()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
