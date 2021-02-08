package com.strixtechnology.foody.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.strixtechnology.foody.R
import com.strixtechnology.foody.models.Result
import com.strixtechnology.foody.util.Constants
import kotlinx.android.synthetic.main.fragment_instructions.view.*

class instructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_instructions, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        view.instructions_webView.webViewClient = object: WebViewClient(){}
        val websiteUrl: String = myBundle!!.sourceUrl
        view.instructions_webView.loadUrl(websiteUrl)
        return view
    }
}