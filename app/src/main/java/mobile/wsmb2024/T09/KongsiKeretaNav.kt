package mobile.wsmb2024.T09

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class KongsiKereta {
    login,
    register,
    driver,
    profile
}

@Composable
fun KongsiKeretaNav(
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = KongsiKereta.login.name
    ) {
        composable(route = KongsiKereta.login.name) {
            Login(
                authViewModel = authViewModel,
                navRegister = { navController.navigate(KongsiKereta.register.name) },
                navDriver = { navController.navigate(KongsiKereta.driver.name) }

            )
        }
        composable(route = KongsiKereta.register.name) {
            Register(
                authViewModel = authViewModel,
                navBack = { navController.popBackStack() })
        }
        composable(route = KongsiKereta.driver.name) {
            Driver(
                authViewModel = authViewModel,
                navBack = { navController.popBackStack() },
                navProfile = { navController.navigate(KongsiKereta.profile.name) }
            )
        }
        composable(route = KongsiKereta.profile.name) {
            Profile(
                authViewModel = authViewModel,
                navBack = { navController.popBackStack() },
            )
        }

    }
}