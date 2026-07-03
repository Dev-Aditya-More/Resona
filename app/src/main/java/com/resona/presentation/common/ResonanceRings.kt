package com.resona.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

private const val RING_DURATION_MS = 2200

/**
 * Sound-wave ripples that expand outward and fade, evoking resonance.
 * Only animates (and only spends CPU) while [isPlaying] is true.
 */
@Composable
fun ResonanceRings(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    ringCount: Int = 3,
    color: Color = MaterialTheme.colorScheme.primary
) {
    AnimatedVisibility(
        visible = isPlaying,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300)),
        modifier = modifier
    ) {
        val transition = rememberInfiniteTransition(label = "resonance")
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            repeat(ringCount) { i ->
                val progress by transition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(RING_DURATION_MS, easing = LinearOutSlowInEasing),
                        repeatMode = RepeatMode.Restart,
                        initialStartOffset = StartOffset(i * (RING_DURATION_MS / ringCount))
                    ),
                    label = "ring$i"
                )
                Box(
                    Modifier
                        .matchParentSize()
                        .graphicsLayer {
                            val scale = 1f + progress * 0.55f
                            scaleX = scale
                            scaleY = scale
                            alpha = (1f - progress) * 0.55f
                        }
                        .border(2.dp, color, CircleShape)
                )
            }
        }
    }
}
