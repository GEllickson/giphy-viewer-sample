package com.georgeellickson.giphyviewer.home

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.georgeellickson.giphyviewer.R
import com.georgeellickson.giphyviewer.appComponent
import com.georgeellickson.giphyviewer.navController
import com.georgeellickson.giphyviewer.settings.SettingsFragment
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    @Inject
    lateinit var viewModelFactory: HomeViewModel.Factory
    private val viewModel: HomeViewModel by viewModels { viewModelFactory }
    private lateinit var toolbar: Toolbar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().application.appComponent.inject(this@HomeFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.title_home)
        setHasOptionsMenu(true)

        toolbar = view.findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        val trendingItemAdapter = TrendingItemAdapter()
        // TODO consider alternative approach to item sizing and number of columns
        val isLandscape = requireContext().resources.configuration
            .orientation == Configuration.ORIENTATION_LANDSCAPE
        val columns = if (isLandscape) 3 else 2
        view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            adapter = trendingItemAdapter
            layoutManager = GridLayoutManager(view.context, columns)
        }
        val loadingView = view.findViewById<View>(R.id.loading_view)
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).apply {
            setOnRefreshListener { viewModel.refreshLatestGifs() }
            setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        }

        viewModel.trendingGifs.observe(viewLifecycleOwner, Observer { items ->
            trendingItemAdapter.submitList(items)
            swipeRefresh.isRefreshing = false
        })
        viewModel.navigateToSettings.observe(viewLifecycleOwner, Observer {
            requireActivity().navController.navigateTo(SettingsFragment())
        })
        viewModel.loadingSpinnerVisible.observe(viewLifecycleOwner, Observer {
            loadingView.visibility = if (it) View.VISIBLE else View.GONE
        })
        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
        })
    }

    override fun onDestroyView() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(null)
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear_api_key -> {
                viewModel.clearApiKey()
                true
            }
            R.id.refresh_gifs -> {
                viewModel.refreshLatestGifs()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
