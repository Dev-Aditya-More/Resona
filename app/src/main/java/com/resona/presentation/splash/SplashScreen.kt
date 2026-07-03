package com.resona.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val barTargets = listOf(0.4f, 0.7f, 1.0f, 0.7f, 0.4f)

@Composable
fun SplashScreen(onComplete: () -> Unit) {
    val density = LocalDensity.current
    val titleStartY = with(density) { 20.dp.toPx() }

    val barAnims = barTargets.indices.map { remember { Animatable(0f) } }
    val barsAlpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val titleY = remember { Animatable(titleStartY) }
    val taglineAlpha = remember { Animatable(0f) }
    val screenAlpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        launch { barsAlpha.animateTo(1f, tween(150)) }
        barAnims.forEachIndexed { i, anim ->
            launch {
                delay(i * 90L)
                anim.animateTo(
                    targetValue = barTargets[i],
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
            }
        }
        delay(700)

        launch { titleAlpha.animateTo(1f, tween(450)) }
        titleY.animateTo(0f, tween(450, easing = FastOutSlowInEasing))

        delay(180)

        taglineAlpha.animateTo(1f, tween(380))

        delay(750)

        screenAlpha.animateTo(0f, tween(400))

        onComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { alpha = screenAlpha.value }
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // Waveform bars
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .height(80.dp)
                    .graphicsLayer { alpha = barsAlpha.value }
            ) {
                barAnims.forEach { anim ->
                    Box(
                        modifier = Modifier
                            .width(11.dp)
                            .height(80.dp)
                            .graphicsLayer {
                                scaleY = anim.value
                                transformOrigin = TransformOrigin(0.5f, 1f)
                            }
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                            )
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "RESONA",
                style = MaterialTheme.typography.headlineLarge,
                letterSpacing = 8.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.graphicsLayer {
                    alpha = titleAlpha.value
                    translationY = titleY.value
                }
            )

            Spacer(Modifier.height(10.dp))

            // Tagline
            Text(
                text = "your music, offline",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.graphicsLayer { alpha = taglineAlpha.value }
            )
        }
    }
}
