package mobile.wsmb2024.T09

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DriverViewModel: ViewModel() {
    data class DriverUiState(
        var rides: List<RideDetails> = listOf(RideDetails())
    )

    data class RideDetails(
        var date: String = "",
        var time: String = "",
        var origin: String = "",
        var destination: String = "",
        var fare: Double = 0.0,
        var uid: String = "",
        var rideId: Int = 0
    )

    private val _driverUiState = MutableStateFlow(DriverUiState())
    var driverUiState: StateFlow<DriverUiState> = _driverUiState.asStateFlow()

    fun updateUiState(rideDetails: List<RideDetails>) {
        _driverUiState.value = DriverUiState(
            rideDetails
        )
    }

    var driver = RegisterViewModel.DriverDetails()

    var currentEdit = RideDetails()


    var date by mutableStateOf("")
    var time by mutableStateOf("")
    var origin by mutableStateOf("")
    var destination by mutableStateOf("")
    var fare by mutableStateOf("")
    var newRideId by mutableIntStateOf(0)
    var loading by mutableStateOf(false)
    var showRideDialog by mutableStateOf(false)
    var isEdit by mutableStateOf(false)

    val db = Firebase.firestore
    val driversRef = db.collection("drivers")
    val ridesRef = db.collection("rides")

    fun getRides(uid: String) {
        loading = true
        ridesRef.whereEqualTo("uid", uid).get()
            .addOnSuccessListener {
                var rideList = mutableListOf(RideDetails())
                for (doc in it) {
                    rideList.add(doc.toObject())
                    if (newRideId <= doc.toObject<RideDetails>().rideId) {
                        newRideId = doc.toObject<RideDetails>().rideId + 1
                    }
                }
                rideList.removeFirst()
                updateUiState(rideList)
                loading = false
            }
    }

    fun getDriver(uid: String) {
        loading = true
        driversRef.whereEqualTo("uid", uid).get()
            .addOnSuccessListener {
                var driverList = mutableListOf(RegisterViewModel.DriverDetails())
                for (doc in it) {
                    driverList.add(doc.toObject())
                }
                driver = driverList[1]
                loading = false
            }
    }

    fun updateRide() {
        val newRide = RideDetails(
            date = date,
            time = time,
            origin = origin,
            destination = destination,
            fare = fare.toDouble(),
            uid = currentEdit.uid,
            rideId = currentEdit.rideId
        )

        loading = true
        ridesRef
            .whereEqualTo("rideId", currentEdit.rideId)
            .whereEqualTo("uid", currentEdit.uid)
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    ridesRef.document(doc.id).set(newRide)
                        .addOnSuccessListener {
                            getRides(currentEdit.uid)
                        }
                }
            }
    }

    fun createRide() {
        loading = true
        ridesRef.add(RideDetails(
            date = date,
            time = time,
            origin = origin,
            destination = destination,
            fare = fare.toDouble(),
            uid = driver.uid,
            rideId = newRideId
        ))
            .addOnSuccessListener {
                getRides(driver.uid)
            }
    }

    fun reset() {
        date = ""
        time = ""
        origin = ""
        destination = ""
        fare = ""
    }

    fun edit(ride: RideDetails) {
        date = ride.date
        time = ride.time
        origin = ride.origin
        destination = ride.destination
        fare = ride.fare.toString()
    }
}