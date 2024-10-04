package com.aura.ui.transfer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aura.databinding.ActivityTransferBinding
import com.aura.viewmodels.TransferState
import com.aura.viewmodels.TransferViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * The transfer activity for the app.
 */
class TransferActivity : AppCompatActivity()
{

  /**
   * The binding for the transfer layout.
   */
  private lateinit var binding: ActivityTransferBinding
  private val viewModel: TransferViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)

    binding = ActivityTransferBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val recipient = binding.recipient.text
    val amount = binding.amount
    val sender = intent.getStringExtra("id")
    val transfer = binding.transfer
    val loading = binding.loading


    updateTransferButtonState()
    observeTransferState()

    transfer.setOnClickListener {
      loading.visibility = View.VISIBLE

      viewModel.transferFunds(
        sender.orEmpty(),
        recipient.toString(),
        amount.text.toString().toDouble()
      )
    }
  }

  private fun updateTransferButtonState() {
    val textWatcher = object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val recipient = binding.recipient.text.toString()
        val amount = binding.amount.text.toString()
        binding.transfer.isEnabled = recipient.isNotEmpty() && amount.isNotEmpty()
      }

      override fun afterTextChanged(s: Editable?) {}
    }
    binding.recipient.addTextChangedListener(textWatcher)
    binding.amount.addTextChangedListener(textWatcher)
  }

  private fun observeTransferState() {
    lifecycleScope.launch {
      viewModel.transferState.collect { state ->
        when (state) {
          is TransferState.Loading -> showLoading(true)
          is TransferState.Success -> {
            showLoading(false)
            getTransferResult(state.result)
            val intent = Intent().apply {
              putExtra("TRANSFER_AMOUNT", binding.amount.text.toString().toDoubleOrNull() ?: 0.0)
              putExtra("TRANSFER_STATUS", "SUCCESS")
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
          }
          is TransferState.Error -> {
            showLoading(false)
            showError(state.message)
          }
          else -> showLoading(false)
        }
      }
    }
  }

  private fun showLoading(isLoading: Boolean) {
    binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
  }

  private fun getTransferResult(result: Boolean) {
    Toast.makeText(this, "Transferred funds successfully", Toast.LENGTH_SHORT).show()
  }

  private fun showError(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
  }
}
