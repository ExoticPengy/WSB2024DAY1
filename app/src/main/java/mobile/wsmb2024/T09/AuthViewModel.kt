package mobile.wsmb2024.T09

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
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

    fun updateUiState(authState: String, message: String = "") {
        _authUiState.value = AuthUiState(
            authState = authState,
            message = message
        )
    }

    var uid by mutableStateOf("")
    var uic by mutableStateOf("")
    var uEmail by mutableStateOf("")

    val db = Firebase
    val auth = Firebase.auth

    fun signUp(email: String, password: String) {
        updateUiState("Loading")
        if (email.isBlank() || password.isBlank()) {
            updateUiState("Empty", "Email or Password is Empty!")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                uid = auth.currentUser!!.uid
                updateUiState("Success")
                signOut()
            }
            .addOnFailureListener {
                updateUiState("Failure", it.message?:"Failed to Sign Up")
            }
    }

    fun signIn(email: String, password: String) {
        updateUiState("Loading")
        if (email.isBlank() || password.isBlank()) {
            updateUiState("Empty", "Email or Password is Empty!")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                uid = auth.currentUser!!.uid
                updateUiState("Authenticated")
            }
            .addOnFailureListener {
                updateUiState("Wrong", it.message?:"Email or Password is Wrong!")
            }
    }

    fun signOut() {
        auth.signOut()
    }

    fun reset() {
        uid = ""
        uic = ""
        uEmail = ""
    }
}