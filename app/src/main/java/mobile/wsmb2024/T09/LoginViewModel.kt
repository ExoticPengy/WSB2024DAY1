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

class LoginViewModel: ViewModel() {

    val db = Firebase.firestore
    val driversRef = db.collection("drivers")

    var ic by mutableStateOf("")
    var password by mutableStateOf("")
    var loading by mutableStateOf(false)

    fun getDriver(authViewModel: AuthViewModel) {
        loading = true

        driversRef.whereEqualTo("ic", ic).get()
            .addOnSuccessListener {
                var driverList = mutableListOf(RegisterViewModel.DriverDetails())
                for (doc in it) {
                    driverList.add(doc.toObject())
                }
                authViewModel.signIn(driverList[1].email, password)
            }
    }
}