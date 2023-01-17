package com.example.test.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.test.databinding.ActivityMainBinding
import com.example.test.uitls.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels()
    //private val viewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        binding.pbLoading.visibility = View.INVISIBLE

        binding.btnSubmit.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val pin = binding.etPin.text.toString()

            if (username.isNotEmpty() && pin.isNotEmpty()) {
                if (checkInternet(this)) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.getUserNum(username, pin.toInt())
                    }
                } else {
                    showToast("no internet")
                }
        } else {
            showToast("Fields mustn't be empty ")
        }

    }

    viewModel.userLiveData.observe(this, Observer
    {
        resource ->
        when (resource) {
            is Resource.Loading -> {
                showProgress()
            }
            is Resource.Success -> {
                hideProgress()
                resource.data?.let {
                    showNum(it.mem_account)
                }

            }
            is Resource.Error -> {
                hideProgress()
                hideNum()
                showToast(resource.message!!)
            }

        }
    })


}

private fun showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

private fun hideNum() {
    binding.tvMsgTitle.visibility = View.INVISIBLE
    binding.tvResponse.visibility = View.INVISIBLE
}

private fun hideProgress() {
    binding.pbLoading.visibility = View.INVISIBLE
}

private fun showNum(memAccount: String) {
    binding.tvMsgTitle.visibility = View.VISIBLE
    binding.tvResponse.apply {
        visibility = View.VISIBLE
        text = memAccount
    }
}

private fun showProgress() {
    binding.pbLoading.visibility = View.VISIBLE
}


fun checkInternet(context: Context): Boolean {
    val connec = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    val mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
    return wifi!!.isConnected || mobile!!.isConnected
}
}