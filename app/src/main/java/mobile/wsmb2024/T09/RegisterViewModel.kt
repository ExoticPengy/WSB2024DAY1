package mobile.wsmb2024.T09

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel: ViewModel() {
    data class RegisterUiState(
        val userDetails: UserDetails = UserDetails(),
        val vehicleDetails: VehicleDetails = VehicleDetails()
    )

    data class UserDetails(
        var uid: String = "",
        var name: String = "",
        var email: String = "",
        var ic: String = "",
        var gender: String = "",
        var phone: String = "",
        var address: String = "",
        var photoUrl: String = ""
    )

    data class VehicleDetails(
        var model: String = "",
        var capacity: Int = 0,
        var features: String = ""
    )

    data class DriverDetails(
        var uid: String = "",
        var name: String = "",
        var email: String = "",
        var ic: String = "",
        var gender: String = "",
        var phone: String = "",
        var address: String = "",
        var photoUrl: String = "",
        var model: String = "",
        var capacity: Int = 0,
        var features: String = ""
    )

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    var registerUiState: StateFlow<RegisterUiState> = _registerUiState.asStateFlow()

    fun updateUiState(userDetails: UserDetails, vehicleDetails: VehicleDetails) {
        _registerUiState.value = RegisterUiState(
            userDetails, vehicleDetails
        )
    }

    var userIc by mutableStateOf("")

    val db = Firebase.firestore
    val driversRef = db.collection("drivers")
    val storageRef = Firebase.storage.reference
    val folderRef = storageRef.child("userProfiles")

    var step by mutableIntStateOf(1)
    var loading by mutableStateOf(false)
    var doneSaving by mutableStateOf(false)
    var notSaved by mutableStateOf(true)
    var selectedImageUri by mutableStateOf<Uri?>(null)

    var ic by mutableStateOf("")
    var password by mutableStateOf("")
    var email by mutableStateOf("")
    var name by mutableStateOf("")
    var gender by mutableStateOf("")
    var phone by mutableStateOf("")
    var address by mutableStateOf("")
    var model by mutableStateOf("")
    var capacity by mutableStateOf("")
    var features by mutableStateOf("")

//    fun uploadImage(context: Context) {
//        val contentResolver = context.contentResolver
//
//        if (selectedImageUri != null)
//        contentResolver.openInputStream(selectedImageUri!!) {
//
//        }
//
//        val imageBytes = null
//
//        imageRef.putBytes(imageBytes)
//    }

    fun uploadDriverDetails(uid: String) {
        if (notSaved) {
            notSaved = false
            val userDetails = _registerUiState.value.userDetails
            val vehicleDetails = _registerUiState.value.vehicleDetails

            val driverDetails = DriverDetails(
                ic = userDetails.ic,
                email = userDetails.email,
                name = userDetails.name,
                gender = userDetails.gender,
                phone = userDetails.phone,
                address = userDetails.address,
                photoUrl = selectedImageUri.toString(),
                uid = uid,
                model = vehicleDetails.model,
                capacity = vehicleDetails.capacity.toInt(),
                features = vehicleDetails.features
            )

            driversRef.add(driverDetails)
                .addOnSuccessListener {
                    doneSaving = true
                }
        }
    }

}