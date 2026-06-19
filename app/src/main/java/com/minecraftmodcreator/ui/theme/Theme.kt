package com.minecraftmodcreator.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = MinecraftGreen,
    onPrimary = White,
    primaryContainer = MinecraftGreenDark,
    onPrimaryContainer = White,
    secondary = MinecraftBrown,
    onSecondary = White,
    secondaryContainer = MinecraftBrownDark,
    onSecondaryContainer = White,
    tertiary = MinecraftBlue,
    onTertiary = White,
    tertiaryContainer = MinecraftBlueDark,
    onTertiaryContainer = White,
    error = MinecraftLava,
    onError = White,
    background = MinecraftDark,
    onBackground = White,
    surface = MinecraftDarkSurface,
    onSurface = White,
    surfaceVariant = GrayDark,
    onSurfaceVariant = GrayLight
)

private val LightColorScheme = lightColorScheme(
    primary = MinecraftGreen,
    onPrimary = White,
    primaryContainer = MinecraftGreenLight,
    onPrimaryContainer = Black,
    secondary = MinecraftBrown,
    onSecondary = White,
    secondaryContainer = MinecraftBrownLight,
    onSecondaryContainer = Black,
    tertiary = MinecraftBlue,
    onTertiary = White,
    tertiaryContainer = MinecraftBlueLight,
    onTertiaryContainer = Black,
    error = MinecraftLava,
    onError = White,
    background = MinecraftSand,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    surfaceVariant = GrayLight,
    onSurfaceVariant = GrayDark
)

@Composable
fun MinecraftModCreatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
