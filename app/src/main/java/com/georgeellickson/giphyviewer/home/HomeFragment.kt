package com.georgeellickson.giphyviewer.home

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.georgeellickson.giphyviewer.R
import com.georgeellickson.giphyviewer.appComponent
import com.georgeellickson.giphyviewer.databinding.FragmentHomeBinding
import com.georgeellickson.giphyviewer.navController
import com.georgeellickson.giphyviewer.settings.SettingsFragment
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: HomeViewModel.Factory
    private val viewModel: HomeViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().application.appComponent.inject(this@HomeFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = getString(R.string.title_home)
        setHasOptionsMenu(true)

        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.findViewById(R.id.toolbar))
        val trendingItemAdapter = TrendingItemAdapter { sharedElement, url ->
            navigateToImageFragment(url, sharedElement)
        }
        // clear previous transition as sharedElement in reverse doesn't work yet
        exitTransition = null
        // TODO consider alternative approach to item sizing and number of columns
        val isLandscape = requireContext().resources.configuration
            .orientation == Configuration.ORIENTATION_LANDSCAPE
        val columns = if (isLandscape) 3 else 2
        binding.recyclerView.apply {
            adapter = trendingItemAdapter
            layoutManager = GridLayoutManager(view.context, columns)
        }
        binding.swipeRefresh.apply {
            setOnRefreshListener { viewModel.refreshLatestGifs() }
            setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        }

        viewModel.trendingGifs.observe(viewLifecycleOwner, Observer { items ->
            trendingItemAdapter.submitList(items)
            binding.swipeRefresh.isRefreshing = false
        })
        viewModel.navigateToSettings.observe(viewLifecycleOwner, Observer {
            requireActivity().navController.navigateTo(SettingsFragment(), false)
        })
        viewModel.loadingSpinnerVisible.observe(viewLifecycleOwner, Observer {
            binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
        })
        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
        })
        return view
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

    private fun navigateToImageFragment(url: String, sharedElement: View) {
        val inflater = TransitionInflater.from(requireContext())
        val sharedEnterTrans = inflater.inflateTransition(R.transition.shared_gif_transition)
        val exitTrans = inflater.inflateTransition(R.transition.fade_out_transition)
        val frag = ViewImageFragment.newInstance(url, sharedElement.transitionName)
        frag.sharedElementEnterTransition = sharedEnterTrans
        exitTransition = exitTrans
        requireActivity().navController.navigateTo(frag, sharedElement)
    }
}
