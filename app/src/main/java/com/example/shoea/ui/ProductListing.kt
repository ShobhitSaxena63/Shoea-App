package com.example.shoea.ui

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.shoea.R
import com.example.shoea.data.network.ErrorCode
import com.example.shoea.data.network.Status
import com.example.shoea.databinding.FragmentProductListingBinding
import timber.log.Timber


class ProductListing : Fragment() {
//    private lateinit var _binding:FragmentProductListingBinding
//    private val binding get() = _binding
    private var _binding: FragmentProductListingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductListingViewModel by lazy {
        ViewModelProvider(this)[ProductListingViewModel::class.java]
    }
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProvider(this)[ProductListingViewModel::class.java]
//        viewModel.deleteData()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.d("Fragment Lifecycle List onAttach()")
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProductListingBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.fetchFromNetwork()
        Timber.d("Fragment Lifecycle List onViewCreated()")


        binding.productsRv.adapter = ProductListAdapter{
            findNavController().navigate(ProductListingDirections.actionProductListingToProductDetail(it))
        }
            if (isInternetOn()) {
                Timber.d("Internet connection is on")
                loadData()
            } else {
                Timber.d("Internet connection is off")
                showNoInternetDialog()
            }

        loadingStatus()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }
    }
    private fun createNetworkCallback(): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            private val handler = Handler(Looper.getMainLooper())
            override fun onAvailable(network: Network) {
                // Internet connectivity is available
                handler.post{
                    loadData()
                }
            }

            override fun onLost(network: Network) {
                // Internet connectivity is lost
                handler.post {
                    viewModel.refreshData()
                    val currentFragmentId = findNavController().currentDestination?.id
                    if (currentFragmentId == R.id.productDetail) {
                        findNavController().navigate(R.id.action_productDetail_to_productListing)
                    }
                    showNoInternetDialog()
                }
            }
        }
    }
    private fun showNoInternetDialog() {
        _binding?.statusError?.visibility = View.VISIBLE
        _binding?.statusError?.text = getString(R.string.network_error_msg)
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Please turn on your internet connection")
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ -> }
        val alert = dialogBuilder.create()
        alert.setTitle("No Internet Connection")
        alert.show()
    }

    private fun isInternetOn(): Boolean {
        val connectivityManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun loadData(){
        Timber.d("Getting Product onViewCreated ")
        view?.let{
            viewModel.products.observe(viewLifecycleOwner){ productList->
                if(productList.isEmpty()){
                    viewModel.fetchFromNetwork()
                }
                productList?.let {
                    (binding.productsRv.adapter as ProductListAdapter).submitList(productList)
                }
            }
        }
    }
    private fun loadingStatus(){
        viewModel.loadingStatus.observe(viewLifecycleOwner){ loadingStatus->
            when (loadingStatus?.status) {
                Status.LOADING -> {
                    binding.loadingStatus.visibility = View.VISIBLE
                    binding.statusError.visibility = View.INVISIBLE
                }
                Status.SUCCESS -> {
                    binding.loadingStatus.visibility = View.INVISIBLE
                    binding.statusError.visibility = View.INVISIBLE
                }
                Status.ERROR -> {
                    binding.loadingStatus.visibility = View.INVISIBLE
                    showErrorMessage(loadingStatus.errorCode,loadingStatus.message)
                    binding.statusError.visibility = View.VISIBLE
                }
                else -> { }
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }
    private fun showErrorMessage(errorCode: ErrorCode?, message: String?) {
        when(errorCode) {
            ErrorCode.NO_DATA -> { binding.statusError.text = getString(R.string.no_data_msg)}
            ErrorCode.NETWORK_ERROR -> { binding.statusError.text = getString(R.string.network_error_msg) }
            ErrorCode.UNKNOWN_ERROR -> { binding.statusError.text = getString(R.string.unknown_error_msg,message) }
            else -> {Timber.d("error code $errorCode")}
        }
    }



    override fun onDetach() {
        super.onDetach()
        connectivityManager.unregisterNetworkCallback(networkCallback)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Timber.d("Fragment Lifecycle List OnPause()")
    }

}