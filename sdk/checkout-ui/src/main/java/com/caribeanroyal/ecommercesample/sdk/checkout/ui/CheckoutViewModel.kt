package com.caribeanroyal.ecommercesample.sdk.checkout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutSdk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val checkoutSdk: CheckoutSdk
) : ViewModel() {

    private val _state = MutableStateFlow(CheckoutState())
    val state: StateFlow<CheckoutState> = _state.asStateFlow()

    fun handleIntent(intent: CheckoutIntent) {
        when (intent) {
            is CheckoutIntent.SubmitPayment -> processPayment(intent.amount, intent.currency)
            is CheckoutIntent.RetryPayment -> resetState()
        }
    }

    private fun processPayment(amount: Double, currency: String) {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val result = checkoutSdk.executeCheckout(amount, currency)
                if (result) {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                } else {
                    _state.update { it.copy(isLoading = false, errorMessage = "Payment declined") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = e.message ?: "Unknown error") }
            }
        }
    }

    private fun resetState() {
        _state.value = CheckoutState()
    }
}

class CheckoutViewModelFactory(
    private val checkoutSdk: CheckoutSdk
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CheckoutViewModel(checkoutSdk) as T
    }
}
