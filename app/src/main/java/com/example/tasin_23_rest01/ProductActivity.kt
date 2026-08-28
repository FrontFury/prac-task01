package com.example.tasin_23_rest01

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasin_23_rest01.adapter.ProductAdapter
import com.example.tasin_23_rest01.databinding.ActivityProductBinding
import com.example.tasin_23_rest01.model.Product
import com.example.tasin_23_rest01.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Retry button is ALWAYS visible
        binding.btnRetry.setOnClickListener {
            loadProducts()
        }

        loadProducts()
    }

    private fun setupRecyclerView() {

        productAdapter = ProductAdapter(emptyList())

        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(this@ProductActivity)
            adapter = productAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadProducts() {

        if (!isInternetAvailable()) {
            showError("No internet connection.\nPlease check your internet.")
            return
        }

        showLoading()

        ApiClient.apiService.getProducts()
            .enqueue(object : Callback<List<Product>> {

                override fun onResponse(
                    call: Call<List<Product>>,
                    response: Response<List<Product>>
                ) {

                    binding.progressBar.visibility = View.GONE

                    if (response.isSuccessful) {

                        val products = response.body()

                        if (!products.isNullOrEmpty()) {

                            productAdapter.updateData(products)

                            binding.rvProducts.visibility = View.VISIBLE
                            binding.tvError.visibility = View.GONE

                        } else {

                            showError("No products found.")
                        }

                    } else {

                        showError(
                            "Server error: ${response.code()}"
                        )
                    }
                }

                override fun onFailure(
                    call: Call<List<Product>>,
                    t: Throwable
                ) {

                    binding.progressBar.visibility = View.GONE

                    showError(
                        "Failed to load products.\nPlease try again."
                    )
                }
            })
    }

    private fun showLoading() {

        binding.progressBar.visibility = View.VISIBLE
        binding.rvProducts.visibility = View.GONE
        binding.tvError.visibility = View.GONE

        // Retry remains visible
        binding.btnRetry.visibility = View.VISIBLE
    }

    private fun showError(message: String) {

        binding.progressBar.visibility = View.GONE
        binding.rvProducts.visibility = View.GONE

        binding.tvError.visibility = View.VISIBLE
        binding.tvError.text = message

        // Retry remains visible
        binding.btnRetry.visibility = View.VISIBLE
    }

    private fun isInternetAvailable(): Boolean {

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

        val network =
            connectivityManager.activeNetwork
                ?: return false

        val capabilities =
            connectivityManager.getNetworkCapabilities(network)
                ?: return false

        return capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        )
    }
}