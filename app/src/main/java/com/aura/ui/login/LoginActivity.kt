package com.aura.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.aura.data.remote.ApiClient
import com.aura.databinding.ActivityLoginBinding
import com.aura.ui.home.HomeActivity
import com.aura.viewmodels.LoginState
import com.aura.viewmodels.LoginViewModel
import com.aura.viewmodels.LoginViewModelFactory
import kotlinx.coroutines.launch

/**
 * The login activity for the app.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = LoginViewModelFactory(ApiClient.loginApiService)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        if (binding.identifier.text.isNotEmpty() && binding.password.text.toString().isNotEmpty()) {
            binding.login.isEnabled = true
        }

        setupLoginButton()
        observeLoginState()
        updateLoginButtonstate()
    }

    private fun setupLoginButton() {
        binding.login.setOnClickListener {
            val username = binding.identifier.text.toString()
            val password = binding.password.text.toString()
            viewModel.login(username, password)
        }
    }

    private fun updateLoginButtonstate() {
        lifecycleScope.launch {
            binding.login.isEnabled = binding.password.text.isNotBlank() &&
                    binding.identifier.text.isNotBlank()
        }
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    is LoginState.Idle -> {
                        binding.loading.visibility = View.GONE
                    }
                    is LoginState.Loading -> {
                        binding.login.isEnabled = false
                    }
                    is LoginState.Success -> {
                        binding.loading.visibility = View.GONE
                        binding.login.isEnabled = true

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is LoginState.Error -> {
                        binding.loading.visibility = View.GONE
                        binding.login.isEnabled = true

                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
