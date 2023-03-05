package com.example.shoea.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shoea.R
import androidx.navigation.fragment.findNavController

class SplashScreen : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Delay navigation to the next fragment by 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
//            val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToNextFragment()
            findNavController().navigate(SplashScreenDirections.actionSplashScreenToProductListing())
        }, 2500)
    }

}