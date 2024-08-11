package mobile.wsmb2024.T09

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class KongsiKereta {
    login,
    register,
    driver
}

@Composable
fun KongsiKeretaNav(
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = KongsiKereta.register.name
    ) {
        composable(route = KongsiKereta.register.name) {
            Register(authViewModel = authViewModel)
        }
    }
}