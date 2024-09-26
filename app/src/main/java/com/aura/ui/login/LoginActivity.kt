package com.aura.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aura.databinding.ActivityLoginBinding
import com.aura.ui.home.HomeActivity
import com.aura.viewmodels.LoginState
import com.aura.viewmodels.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * The login activity for the app.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLoginButton()
        observeLoginState()
        updateLoginButtonState()
    }

    private fun setupLoginButton() {
        binding.login.setOnClickListener {
            val username = binding.identifier.text.toString()
            val password = binding.password.text.toString()
            viewModel.login(username, password)
        }
    }

    private fun updateLoginButtonState() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val username = binding.identifier.text.toString()
                val password = binding.password.text.toString()
                binding.login.isEnabled = username.isNotEmpty() && password.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        binding.identifier.addTextChangedListener(textWatcher)
        binding.password.addTextChangedListener(textWatcher)
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            viewModel.loginState.collect { granted ->
                when (granted) {
                    is LoginState.Loading -> showLoading(true)
                    is LoginState.Success -> handleSuccess()
                    is LoginState.Error -> {
                        showLoading(false)
                        binding.login.isEnabled = false
                        showError(granted.message)
                    }
                    else -> showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun handleSuccess() {
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("id", binding.identifier.text.toString())
        startActivity(intent)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        binding.login.isEnabled = true
    }
}


