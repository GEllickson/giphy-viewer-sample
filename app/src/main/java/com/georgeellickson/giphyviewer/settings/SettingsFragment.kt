package com.georgeellickson.giphyviewer.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.georgeellickson.giphyviewer.MainViewModel
import com.georgeellickson.giphyviewer.R
import com.georgeellickson.giphyviewer.appComponent
import com.georgeellickson.giphyviewer.home.HomeFragment
import com.georgeellickson.giphyviewer.navController
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: SettingsViewModelFactory
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().apply {
            title = getString(R.string.title_settings)
            application.appComponent.inject(this@SettingsFragment)
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel::class.java)
        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            requireActivity().navController.navigateTo(HomeFragment())
        })

        val view = inflater.inflate(R.layout.fragment_settings, container, false)
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
        return view
    }

}
