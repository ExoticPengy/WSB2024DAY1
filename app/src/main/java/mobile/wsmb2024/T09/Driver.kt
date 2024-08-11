package mobile.wsmb2024.T09

import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Driver(
    driverViewModel: DriverViewModel = viewModel(),
    authViewModel: AuthViewModel,
    navBack: () -> Unit,
    navProfile: () -> Unit
) {
    val authUiState by authViewModel.authUiState.collectAsState()
    val authState = authUiState.authState
    val driverUiState by driverViewModel.driverUiState.collectAsState()
    val rides = driverUiState.rides
    val message = authUiState.message

    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            "Authenticated" -> {
                driverViewModel.getDriver(authViewModel.getUid())
                driverViewModel.getRides(authViewModel.getUid())
            }
            "Unauthenticated" -> {
                navBack()
            }
        }
    }

    Surface(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (driverViewModel.loading) {
                LoadingDialog()
            }
            Spacer(Modifier.height(20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth().height(80.dp)
            ) {
                IconButton(
                    onClick = {
                        authViewModel.signOut()
                    },
                    modifier = Modifier.background(Color.Red, RoundedCornerShape(50))
                ) {
                    Icon(imageVector = Icons.Default.ExitToApp, null, tint = Color.White)
                }
                Text("Driver Ride")
                IconButton(
                    onClick = {
                        navProfile()
                    },
                    modifier = Modifier.background(Color.Black, RoundedCornerShape(50))
                ) {
                    Icon(imageVector = Icons.Default.Person, null, tint = Color.White)
                }
            }
            Divider(thickness = 2.dp)
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
            ) {
                items(rides) { ride ->
                    Spacer(Modifier.height(30.dp))
                    Card(
                    ) {
                        OutlinedTextField(
                            value = ride.date,
                            onValueChange = { },
                            label = { Text("Date") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                )
                            },
                            isError = false,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            singleLine = true,
                            modifier = Modifier.width(280.dp)
                        )
                        OutlinedTextField(
                            value = ride.time,
                            onValueChange = { },
                            label = { Text("Time") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                )
                            },
                            isError = false,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            singleLine = true,
                            modifier = Modifier.width(280.dp)
                        )
                        OutlinedTextField(
                            value = ride.origin,
                            onValueChange = { },
                            label = { Text("Origin") },
                            readOnly = true,
                            isError = false,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            singleLine = true,
                            modifier = Modifier.width(280.dp)
                        )
                        OutlinedTextField(
                            value = ride.destination,
                            onValueChange = { },
                            label = { Text("Destination") },
                            readOnly = true,
                            isError = false,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            singleLine = true,
                            modifier = Modifier.width(280.dp)
                        )
                        OutlinedTextField(
                            value = ride.fare.toString(),
                            onValueChange = { },
                            label = { Text("Fare") },
                            readOnly = true,
                            isError = false,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            singleLine = true,
                            modifier = Modifier.width(280.dp)
                        )
                        IconButton(
                            onClick = {
                                driverViewModel.showRideDialog = true
                                driverViewModel.isEdit = true
                                driverViewModel.currentEdit = ride
                                driverViewModel.edit(ride)
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Edit, null)
                        }
                    }
                }
            }
            ExtendedFloatingActionButton(
                onClick = {
                    driverViewModel.showRideDialog = true
                    driverViewModel.reset()
                          },
                modifier = Modifier.padding(bottom = 30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }

            if (driverViewModel.showRideDialog) {
                RideDialog(
                    driverViewModel = driverViewModel,
                    onDismiss = {
                        driverViewModel.showRideDialog = false
                        driverViewModel.reset()
                    }
                )
            }
        }
    }
}

@Composable
fun RideDialog(
    driverViewModel: DriverViewModel,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card {
            OutlinedTextField(
                value = driverViewModel.date,
                onValueChange = { driverViewModel.date = it },
                label = { Text("Date") },
                readOnly = true,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null,
                    ) },
                isError = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                modifier = Modifier.width(280.dp)
            )
            OutlinedTextField(
                value = driverViewModel.time,
                onValueChange = { driverViewModel.time = it },
                label = { Text("Time") },
                readOnly = true,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.AccessTime, contentDescription = null,
                    ) },
                isError = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                modifier = Modifier.width(280.dp)
            )
            OutlinedTextField(
                value = driverViewModel.origin,
                onValueChange = { driverViewModel.origin = it },
                label = { Text("Origin") },
                isError = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                modifier = Modifier.width(280.dp)
            )
            OutlinedTextField(
                value = driverViewModel.destination,
                onValueChange = { driverViewModel.destination = it },
                label = { Text("Destination") },
                isError = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                modifier = Modifier.width(280.dp)
            )
            OutlinedTextField(
                value = driverViewModel.fare,
                onValueChange = { driverViewModel.fare = it },
                label = { Text("Fare") },
                isError = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                modifier = Modifier.width(280.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.width(280.dp)
            ) {
                IconButton(onClick = { onDismiss() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
                IconButton(onClick = {
                    if (driverViewModel.isEdit) {
                        driverViewModel.updateRide()
                    }
                    else {
                        driverViewModel.createRide()
                    }
                    onDismiss()
                }) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null
                    )
                }
            }
        }
    }
}