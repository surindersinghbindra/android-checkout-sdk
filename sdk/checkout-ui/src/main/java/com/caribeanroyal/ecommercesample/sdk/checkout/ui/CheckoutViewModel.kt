package com.caribeanroyal.ecommercesample.sdk.checkout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.caribeanroyal.ecommercesample.sdk.checkout.core.CheckoutSdk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.caribeanroyal.ecommercesample.sdk.checkout.core.AlreadyProcessedException
import java.util.UUID
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Log

class CheckoutViewModel(
    private val sdkEngine: CheckoutSdk
) : ViewModel() {

    private val sessionKey = UUID.randomUUID().toString()


    private val _state = MutableStateFlow(CheckoutState())
    val state: StateFlow<CheckoutState> = _state.asStateFlow()

    init {
        handleIntent(CheckoutIntent.LoadProcessors)
    }

    fun handleIntent(intent: CheckoutIntent) {
        when (intent) {
            is CheckoutIntent.LoadProcessors -> loadProcessors()
            is CheckoutIntent.SubmitPayment -> processPayment(intent)
            is CheckoutIntent.NextStep -> {
                _state.update { it.copy(currentStep = minOf(it.currentStep + 1, intent.maxStep)) }
            }
            is CheckoutIntent.PreviousStep -> {
                _state.update { it.copy(currentStep = maxOf(it.currentStep - 1, 0)) }
            }
            is CheckoutIntent.UpdatePartySize -> {
                _state.update { it.copy(partySize = intent.size) }
            }
            is CheckoutIntent.SelectRoom -> {
                _state.update { it.copy(roomType = intent.type) }
            }
            is CheckoutIntent.ToggleDrinkPackage -> {
                _state.update { it.copy(includeDrinkPackage = intent.included) }
            }
            is CheckoutIntent.ToggleWiFi -> {
                _state.update { it.copy(includeWiFi = intent.included) }
            }
            is CheckoutIntent.UpdateGuestInfo -> {
                _state.update { it.copy(guestFirstName = intent.firstName, guestLastName = intent.lastName) }
            }
        }
    }
    
    fun calculateDynamicTotal(basePrice: Double): Double {
        val st = _state.value
        var total = basePrice
        total += (st.roomPrices[st.roomType] ?: 0.0)
        
        // Add-ons (arbitrary price for the sake of demo)
        if (st.includeDrinkPackage) total += 120.0 * st.partySize
        if (st.includeWiFi) total += 50.0 * st.partySize
        
        return total
    }

    private fun loadProcessors() {
        val processors = sdkEngine.enabledProcessors
        _state.update { it.copy(availableProcessors = processors) }
    }

    private fun processPayment(intent: CheckoutIntent.SubmitPayment) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            sdkEngine.logEvent("CheckoutViewModel", "Starting payment process for ${intent.amount} ${intent.currency}")
            
            try {
                val success = sdkEngine.executeCheckout(
                    amount = intent.amount,
                    currency = intent.currency,
                    processorType = intent.processor.type,
                    idempotencyKey = sessionKey
                )
                
                if (success) {
                    sdkEngine.logEvent("CheckoutViewModel", "Payment succeeded")
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                } else {
                    sdkEngine.logError("CheckoutViewModel", "Payment failed")
                    _state.update { it.copy(isLoading = false, error = "Payment failed") }
                }
            } catch (e: AlreadyProcessedException) {
                sdkEngine.logEvent("CheckoutViewModel", "Payment already processed (Idempotency key matched)")
                _state.update { it.copy(isLoading = false, error = "This order has already been placed.") }
            } catch (e: Exception) {
                sdkEngine.logError("CheckoutViewModel", "Payment exception: ${e.message}", e)
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}

class CheckoutViewModelFactory(
    private val sdkEngine: CheckoutSdk
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CheckoutViewModel(sdkEngine) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
