package com.minecraftmodcreator.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.minecraftmodcreator.ui.screens.editor.EditorScreen
import com.minecraftmodcreator.ui.screens.editor.ElementEditorScreen
import com.minecraftmodcreator.ui.screens.home.HomeScreen
import com.minecraftmodcreator.ui.screens.mods.ModsScreen
import com.minecraftmodcreator.ui.screens.templates.TemplatesScreen
import com.minecraftmodcreator.ui.screens.export.ExportScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCreate = {
                    navController.navigate(Screen.Editor.createRoute(null))
                },
                onNavigateToMods = {
                    navController.navigate(Screen.Mods.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                }
            )
        }

        composable(Screen.Create.route) {
            EditorScreen(
                modId = null,
                onNavigateBack = { navController.popBackStack() },
                onElementClick = { modId, elementId ->
                    navController.navigate(Screen.ElementEditor.createRoute(modId, elementId))
                },
                onExportClick = { modId ->
                    navController.navigate(Screen.Export.createRoute(modId))
                }
            )
        }

        composable(Screen.Mods.route) {
            ModsScreen(
                onModClick = { modId ->
                    navController.navigate(Screen.Editor.createRoute(modId))
                },
                onCreateNew = {
                    navController.navigate(Screen.Editor.createRoute(null))
                }
            )
        }

        composable(Screen.Templates.route) {
            TemplatesScreen(
                onTemplateClick = { templateId ->
                    // TODO: Create mod from template
                }
            )
        }

        composable(
            route = Screen.Editor.route,
            arguments = listOf(navArgument("modId") { type = NavType.StringType })
        ) { backStackEntry ->
            val modId = backStackEntry.arguments?.getString("modId")
            EditorScreen(
                modId = if (modId == "new") null else modId,
                onNavigateBack = { navController.popBackStack() },
                onElementClick = { mId, elementId ->
                    navController.navigate(Screen.ElementEditor.createRoute(mId, elementId))
                },
                onExportClick = { mId ->
                    navController.navigate(Screen.Export.createRoute(mId))
                }
            )
        }

        composable(
            route = Screen.ElementEditor.route,
            arguments = listOf(
                navArgument("modId") { type = NavType.StringType },
                navArgument("elementId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val modId = backStackEntry.arguments?.getString("modId") ?: return@composable
            val elementId = backStackEntry.arguments?.getString("elementId")
            ElementEditorScreen(
                modId = modId,
                elementId = if (elementId == "new") null else elementId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Export.route,
            arguments = listOf(navArgument("modId") { type = NavType.StringType })
        ) { backStackEntry ->
            val modId = backStackEntry.arguments?.getString("modId") ?: return@composable
            ExportScreen(
                modId = modId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
