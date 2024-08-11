package mobile.wsmb2024.T09

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun Profile(
    profileViewModel: ProfileViewModel = viewModel(),
    authViewModel: AuthViewModel,
    navBack: () -> Unit
) {
    val authUiState by authViewModel.authUiState.collectAsState()
    val authState = authUiState.authState
    val profileUiState by profileViewModel.profileUiState.collectAsState()
    val driver = profileUiState.driver

    LaunchedEffect(authState) {
        when (authState) {
            "Authenticated" -> {
                profileViewModel.getDriver(authViewModel.getUid())
            }
        }
    }

    Surface(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {


            ElevatedCard(
                modifier = Modifier
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .width(300.dp)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Account Details",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                    AsyncImage(
                        model = driver.photoUrl,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(50))
                            .border(1.dp, Color.Black, RoundedCornerShape(50))
                            .align(Alignment.CenterHorizontally)
                    )
                    Text("IC No: ${driver.ic}")
                    Text("Email: ${driver.email}")

                    Divider(thickness = 1.dp, color =  Color.Black)
                    Text(
                        text = "Personal Details",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                    Text("Name: ${driver.name}")
                    Text("Gender: ${driver.gender}")
                    Text("Phone: ${driver.phone}")
                    Text("Address: ${driver.address}")

                    Divider(thickness = 1.dp, color =  Color.Black)
                    Text(
                        text = "Vehicle Details",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                    Text("Car Model: ${driver.model}")
                    Text("Sitting Capacity: ${driver.capacity}")
                    Text("Special Features: ${driver.features}")
                }
            }

            ElevatedButton(
                onClick = { navBack() },
                enabled = true,
                colors = ButtonDefaults.buttonColors(containerColor = toColor("#52aef1"))
            ) {
                Text("Back")
            }
        }
    }
}