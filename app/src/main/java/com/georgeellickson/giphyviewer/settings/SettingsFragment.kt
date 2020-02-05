package com.georgeellickson.giphyviewer.settings

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.georgeellickson.giphyviewer.R
import com.georgeellickson.giphyviewer.appComponent
import com.georgeellickson.giphyviewer.home.HomeFragment
import com.georgeellickson.giphyviewer.navController
import javax.inject.Inject

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var viewModelFactory: SettingsViewModelFactory
    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().apply {
            title = getString(R.string.title_settings)
            application.appComponent.inject(this@SettingsFragment)
        }

        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            requireActivity().navController.navigateTo(HomeFragment())
        })

        val editText = view.findViewById<EditText>(R.id.edit_api_key).apply {
            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.setKey(v.text.toString())
                }
                true
            }
        }
        view.findViewById<Button>(R.id.button_enter).apply {
            setOnClickListener {
                viewModel.setKey(editText.text.toString())
            }
        }
    }

}
