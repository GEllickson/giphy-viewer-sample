package com.georgeellickson.giphyviewer.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.georgeellickson.giphyviewer.databinding.FragmentViewImageBinding

class ViewImageFragment : Fragment() {

    private var isLeaving = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentViewImageBinding.inflate(inflater, container, false)
        val view = binding.root

        view.setOnClickListener {
            if (!isLeaving) {
                isLeaving = true
                requireActivity().onBackPressed()
            }
        }

        val transitionName = arguments?.getString(EXTRA_TRANSITION_NAME) ?: ""
        binding.imageGif.transitionName = transitionName
        postponeEnterTransition()
        val gifUrl = arguments?.getString(EXTRA_URL) ?: ""
        Glide.with(view)
            .asGif()
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            })
            .load(gifUrl)
            .into(binding.imageGif)
        return view
    }

    companion object {
        private const val EXTRA_URL = "image_url"
        private const val EXTRA_TRANSITION_NAME = "transitionName"
        fun newInstance(url: String, transitionName: String): Fragment {
            return ViewImageFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_URL, url)
                    putString(EXTRA_TRANSITION_NAME, transitionName)
                }
            }
        }
    }
}