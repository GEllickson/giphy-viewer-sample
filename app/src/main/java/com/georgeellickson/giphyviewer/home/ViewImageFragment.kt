package com.georgeellickson.giphyviewer.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.georgeellickson.giphyviewer.R

class ViewImageFragment : Fragment(R.layout.fragment_view_image) {

    private var isLeaving = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnClickListener {
            if (!isLeaving) {
                isLeaving = true
                requireActivity().onBackPressed()
            }
        }
        val gifView = view.findViewById<ImageView>(R.id.image_gif)
        val transitionName = arguments?.getString(EXTRA_TRANSITION_NAME) ?: ""
        gifView.transitionName = transitionName
        postponeEnterTransition()
        val gifUrl = arguments?.getString(EXTRA_URL) ?: ""
        Glide.with(view)
            .asGif()
            .listener(object: RequestListener<GifDrawable> {
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
            .into(gifView)
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