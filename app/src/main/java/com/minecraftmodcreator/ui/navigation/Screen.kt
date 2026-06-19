package com.minecraftmodcreator.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null
) {
    data object Home : Screen(
        route = "home",
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object Create : Screen(
        route = "create",
        title = "Create",
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Outlined.Add
    )

    data object Mods : Screen(
        route = "mods",
        title = "My Mods",
        selectedIcon = Icons.Filled.Inventory,
        unselectedIcon = Icons.Outlined.Inventory
    )

    data object Templates : Screen(
        route = "templates",
        title = "Templates",
        selectedIcon = null,
        unselectedIcon = null
    )

    data object Editor : Screen(
        route = "editor/{modId}",
        title = "Editor"
    ) {
        fun createRoute(modId: String?) = "editor/${modId ?: "new"}"
    }

    data object ElementEditor : Screen(
        route = "element_editor/{modId}/{elementId}",
        title = "Element Editor"
    ) {
        fun createRoute(modId: String, elementId: String?) = "element_editor/$modId/${elementId ?: "new"}"
    }

    data object Export : Screen(
        route = "export/{modId}",
        title = "Export"
    ) {
        fun createRoute(modId: String) = "export/$modId"
    }

    companion object {
        val bottomNavItems = listOf(Home, Create, Mods, Templates)
    }
}
