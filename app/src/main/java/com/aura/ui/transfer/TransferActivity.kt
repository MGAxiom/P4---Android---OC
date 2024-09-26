package com.aura.ui.transfer

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aura.databinding.ActivityTransferBinding

/**
 * The transfer activity for the app.
 */
class TransferActivity : AppCompatActivity()
{

  /**
   * The binding for the transfer layout.
   */
  private lateinit var binding: ActivityTransferBinding

  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)

    binding = ActivityTransferBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val recipient = binding.recipient
    val amount = binding.amount
    val transfer = binding.transfer
    val loading = binding.loading

    updateTransferButtonState()

    transfer.setOnClickListener {
      loading.visibility = View.VISIBLE

      setResult(Activity.RESULT_OK)
      finish()
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
}
