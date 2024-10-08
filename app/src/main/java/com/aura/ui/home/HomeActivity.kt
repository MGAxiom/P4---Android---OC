package com.aura.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aura.R
import com.aura.databinding.ActivityHomeBinding
import com.aura.ui.login.LoginActivity
import com.aura.ui.transfer.TransferActivity
import com.aura.viewmodels.BalanceState
import com.aura.viewmodels.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * The home activity for the app.
 */
class HomeActivity : AppCompatActivity() {

    /**
     * The binding for the home layout.
     */
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModel()

    /**
     * A callback for the result of starting the TransferActivity.
     */
    private val startTransferActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val data: Intent? = result.data
                    val transferAmount = data?.getDoubleExtra("TRANSFER_AMOUNT", 0.0) ?: 0.0
                    val transferStatus = data?.getStringExtra("TRANSFER_STATUS")
                    handleSuccessfulTransfer(transferAmount, transferStatus)
                }
                Activity.RESULT_CANCELED -> {
                    val errorMessage = result.data?.getStringExtra("ERROR_MESSAGE")
                    if (errorMessage != null) {
                        handleFailedTransfer(errorMessage)
                    } else {
                        handleCancelledTransfer()
                    }
                }
                else -> handleFailedTransfer("Unexpected result")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transfer = binding.transfer
        val id = intent.getStringExtra("id")

        getAccountId(id.orEmpty())
        observeBalanceState()
        setupRetryButton(id.orEmpty())

        transfer.setOnClickListener {
            startTransferActivityForResult.launch(
                Intent(
                    this@HomeActivity,
                    TransferActivity::class.java
                ).putExtra("id", id)
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.disconnect -> {
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleSuccessfulTransfer(amount: Double, status: String?) {
        val id = intent.getStringExtra("id")
        id?.let { getAccountId(it) }
        Toast.makeText(this, "Transfer successful. Balance updated.", Toast.LENGTH_SHORT).show()
    }

    private fun handleCancelledTransfer() {
        Toast.makeText(this, "Transfer was cancelled.", Toast.LENGTH_SHORT).show()
    }

    private fun handleFailedTransfer(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getAccountId(id: String) {
        viewModel.getBalance(id)
    }

    private fun setupRetryButton(id: String) {
        binding.retry.setOnClickListener {
            getAccountId(id)
        }
    }


    private fun observeBalanceState() {
        lifecycleScope.launch {
            viewModel.balanceState.collect { isFound ->
                when (isFound) {
                    is BalanceState.Loading -> showLoading(true)
                    is BalanceState.Success -> {
                        updateBalance(isFound.balance)
                        showLoading(false)
                    }

                    is BalanceState.Error -> {
                        showLoading(false)
                        showError(isFound.message)
                    }

                    else -> showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun updateBalance(balance: Double) {
        Toast.makeText(this, "Balance retrieved", Toast.LENGTH_SHORT).show()
        binding.balance.text = "$balance €"
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        binding.retry.visibility = View.VISIBLE
    }
}




