package mobile.wsmb2024.T09

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel: ViewModel() {

    data class ProfileUiState(
        val driver: RegisterViewModel.DriverDetails = RegisterViewModel.DriverDetails()
    )

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    var profileUiState: StateFlow<ProfileUiState> = _profileUiState.asStateFlow()

    var loading by mutableStateOf(false)

    fun updateUiState(driver: RegisterViewModel.DriverDetails) {
        _profileUiState.value = ProfileUiState(
            driver
        )
    }

    val db = Firebase.firestore
    val driversRef = db.collection("drivers")
    val ridesRef = db.collection("rides")

    fun getDriver(uid: String) {
        loading = true
        driversRef.whereEqualTo("uid", uid).get()
            .addOnSuccessListener {
                var driverList = mutableListOf(RegisterViewModel.DriverDetails())
                for (doc in it) {
                    driverList.add(doc.toObject())
                }
                updateUiState(driver = driverList[1])
                loading = false
            }
    }
}