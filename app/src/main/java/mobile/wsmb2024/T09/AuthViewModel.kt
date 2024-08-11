package mobile.wsmb2024.T09

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel: ViewModel() {

    data class AuthUiState(
        val authState: String = "",
        val message: String = ""
    )

    private val _authUiState = MutableStateFlow(AuthUiState())
    var authUiState: StateFlow<AuthUiState> = _authUiState.asStateFlow()

    fun updateUiState(authState: String, message: String) {
        _authUiState.value = AuthUiState(
            authState = authState,
            message = message
        )
    }
}