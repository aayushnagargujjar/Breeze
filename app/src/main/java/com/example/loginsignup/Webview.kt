package com.example.loginsignup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.loginsignup.databinding.FragmentWebviewBinding

class Webview : Fragment(R.layout.fragment_webview) {
    private var _binding: FragmentWebviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var url = arguments?.getString("url")
                if (url != null) {
                    binding.webview.loadUrl(url)
                }

        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
