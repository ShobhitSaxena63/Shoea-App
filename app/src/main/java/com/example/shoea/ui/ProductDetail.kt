package com.example.shoea.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoea.R
import com.example.shoea.data.Product
import com.example.shoea.databinding.FragmentProductDetailBinding
import com.google.android.material.snackbar.Snackbar
import nl.dionsegijn.steppertouch.OnStepCallback
import timber.log.Timber
import java.util.*


class ProductDetail : Fragment() {
    private lateinit var _binding:FragmentProductDetailBinding
    private val binding get() = _binding
    private lateinit var viewModel: ProductDetailViewModel
    private var timer: Timer? = null
    private var currentPage = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("FragmentLifeCycle onCreate()")
        val id = ProductDetailArgs.fromBundle(requireArguments()).id
        val viewModelFactory = ProductDetailViewModelFactory(id,1,requireActivity().application)
        viewModel = ViewModelProvider(this,viewModelFactory)[ProductDetailViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProductDetailBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productImagesVp.adapter = ProductImagesViewPager()

        binding.indicator.attachTo(binding.productImagesVp)

        viewModel.products.observe(viewLifecycleOwner){ product->
            product?.let {
                setData(product)
            }
            quantity(product.price)
        }



    }

    private fun setData(product: Product) {
        Timber.d("Product Details: $product")
        (binding.productImagesVp.adapter as ProductImagesViewPager).submitList(product.images)
        //Set product info to their respective views
        binding.title.text = product.title.replaceFirstChar(Char::titlecase)
        binding.descriptionText.text = product.description
        binding.price.text = getString(R.string.price,product.price.toString())
        val rating = String.format("%.1f", product.rating)
        binding.rating.text = rating
//        startTimer(product.images.size)
//        startAutoScroll(product.images.size)
        when(rating.toDouble()){
            in 0.0..4.5 -> binding.ratingStar.background = ContextCompat.getDrawable(requireActivity(),R.drawable.half_star)
            in 4.5..5.0 -> binding.ratingStar.background = ContextCompat.getDrawable(requireActivity(),R.drawable.full_star_icon)
        }



    }

//    private fun autoScroll() {
//        val handler = Handler(Looper.getMainLooper())
//
//        val runnable = object : Runnable {
//            override fun run() {
//                // Do your work here
//                Timber.d("Adapter Position for product Images ${binding.productImagesVp.currentItem}")
//                // Schedule the next execution of the runnable
//                handler.postDelayed(this, 3000)
//            }
//        }
//    }


    private fun quantity(price: Int) {
        val stepperTouch = binding.stepperTouch
        // Set min and max value for quantity button
        stepperTouch.minValue = 1
        stepperTouch.maxValue = 10
        // Set quantity button text to last save value
        stepperTouch.count = viewModel.getQty()
        // Set price textview and handle config changes
        binding.price.text = getString(R.string.price,(viewModel.getQty() * price).toString() )
        Timber.d("Updating Price Value Get = ${viewModel.getQty()}")
        stepperTouch.sideTapEnabled = true
        stepperTouch.addStepCallback(object : OnStepCallback {
            override fun onStep(value: Int, positive: Boolean) {
                Timber.d("Counter quantity $value | Count = ${ binding.stepperTouch.count}")
                if(value == stepperTouch.maxValue){
                    Snackbar.make(binding.root,"Max Quantity Reached",Snackbar.LENGTH_SHORT).show()
                }
                //First set value in viewModel
                viewModel.setQty(value)
                binding.price.text = getString(R.string.price,(value * price).toString() )
                Timber.d("Quantity value = $value | Set = $value")
            }
        })

    }

    private fun startAutoScroll(size:Int) {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                if (isAdded) {
                    requireActivity().runOnUiThread {
                        //update UI elements here
                        val viewPager  = binding.productImagesVp
                        currentPage = viewPager.currentItem + 1
                        if (currentPage == size) { // totalItems is the total number of items in the ViewPager2
                            currentPage = 0
                        }
                        Timber.d("Current Adapter currentPage = $currentPage")
                        viewPager.currentItem = currentPage
                    }
                }
            }
        }, 3000, 3000) // 3000ms is the delay between each auto-scroll
    }

    private fun stopAutoScroll() {
        timer?.cancel()
        timer = null
    }

    override fun onResume() {
        super.onResume()
        Timber.d("Fragment Lifecycle Details onResume()")
        viewModel.products.observe(viewLifecycleOwner){product->
            product?.let {
                binding.productImagesVp.currentItem = viewModel.getCurrentItem()
                startAutoScroll(product.images.size) }
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.d("Fragment Lifecycle Details OnPause()")
        stopAutoScroll()
        viewModel.setCurrentItem(binding.productImagesVp.currentItem)

    }
    override fun onDestroy() {
        super.onDestroy()
        Timber.d("Fragment Lifecycle Details onDestroy()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("Fragment Lifecycle Details onDestroyView()")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("Fragment Lifecycle Details onDetach()")

    }

}