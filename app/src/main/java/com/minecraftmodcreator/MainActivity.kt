package com.minecraftmodcreator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.minecraftmodcreator.ui.navigation.AppNavHost
import com.minecraftmodcreator.ui.navigation.Screen
import com.minecraftmodcreator.ui.theme.MinecraftModCreatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MinecraftModCreatorTheme {
                MainScreen()
            }
        }
    }
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    
    val bottomNavItems = listOf(
        BottomNavItem(
            route = Screen.Home.route,
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavItem(
            route = Screen.Create.route,
            title = "Create",
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Outlined.Add
        ),
        BottomNavItem(
            route = Screen.Mods.route,
            title = "My Mods",
            selectedIcon = Icons.Filled.Inventory2,
            unselectedIcon = Icons.Outlined.Inventory2
        ),
        BottomNavItem(
            route = Screen.Templates.route,
            title = "Templates",
            selectedIcon = Icons.Filled.Extension,
            unselectedIcon = Icons.Outlined.Extension
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // Only show bottom bar for main screens
    val showBottomBar = currentDestination?.route in listOf(
        Screen.Home.route,
        Screen.Create.route,
        Screen.Mods.route,
        Screen.Templates.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

private val Icons.Filled.Add: ImageVector
    get() = androidx.compose.material.icons.Icons.Filled.Add

private val Icons.Outlined.Add: ImageVector
    get() = androidx.compose.material.icons.Icons.Outlined.Add

private val Icons.Filled.Inventory2: ImageVector
    get() = androidx.compose.material.icons.Icons.Filled.Inventory2

private val Icons.Outlined.Inventory2: ImageVector
    get() = androidx.compose.material.icons.Icons.Outlined.Inventory2

private val Icons.Filled.Extension: ImageVector
    get() = androidx.compose.material.icons.Icons.Filled.Extension

private val Icons.Outlined.Extension: ImageVector
    get() = androidx.compose.material.icons.Icons.Outlined.Extension
