package com.georgeellickson.giphyviewer.settings

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.georgeellickson.giphyviewer.R
import com.georgeellickson.giphyviewer.appComponent
import com.georgeellickson.giphyviewer.databinding.FragmentSettingsBinding
import com.georgeellickson.giphyviewer.home.HomeFragment
import com.georgeellickson.giphyviewer.navController
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: SettingsViewModel.Factory
    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().application.appComponent.inject(this@SettingsFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = getString(R.string.title_settings)

        val binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            requireActivity().navController.navigateTo(HomeFragment(), false)
        })

        val editText = binding.editApiKey.apply {
            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.tryApiKey(v.text.toString())
                }
                true
            }
        }
        val button = binding.buttonEnter.apply {
            setOnClickListener {
                viewModel.tryApiKey(editText.text.toString())
            }
        }
        binding.footer.movementMethod = LinkMovementMethod.getInstance()

        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            val enabled = !it
            editText.isEnabled = enabled
            button.isEnabled = enabled
            val buttonText = if (enabled) R.string.enter else R.string.loading
            button.setText(buttonText)
        })
        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
        })
        return view
    }

}
