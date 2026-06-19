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

    // New Creator Screens
    data object ModTypeSelection : Screen(
        route = "mod_type_selection",
        title = "Choose Type"
    )

    data object ModMetadata : Screen(
        route = "mod_metadata/{modId}",
        title = "Mod Info"
    ) {
        fun createRoute(modId: String) = "mod_metadata/$modId"
    }

    data object TextureEditor : Screen(
        route = "texture_editor/{modId}/{textureId}",
        title = "Texture Editor"
    ) {
        fun createRoute(modId: String, textureId: String = "new") = "texture_editor/$modId/$textureId"
    }

    data object XRayEditor : Screen(
        route = "xray_editor/{modId}",
        title = "X-Ray Editor"
    ) {
        fun createRoute(modId: String) = "xray_editor/$modId"
    }

    data object GlowEditor : Screen(
        route = "glow_editor/{modId}",
        title = "Glow Editor"
    ) {
        fun createRoute(modId: String) = "glow_editor/$modId"
    }

    data object InterfaceEditor : Screen(
        route = "interface_editor/{modId}",
        title = "Interface Editor"
    ) {
        fun createRoute(modId: String) = "interface_editor/$modId"
    }

    companion object {
        val bottomNavItems = listOf(Home, Create, Mods, Templates)
    }
}
