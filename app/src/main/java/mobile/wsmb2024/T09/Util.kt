package mobile.wsmb2024.T09

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

fun toColor(color: String): Color {
    return Color(color.toColorInt())
}