package com.example.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Background layout rendering ambient liquid glowing orb gradients behind translucent glass panels.
 */
@Composable
fun GlassBackground(
    isDark: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val bgGradient = if (isDark) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF070B19),
                Color(0xFF0F172A),
                Color(0xFF030712)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE0E7FF),
                Color(0xFFF1F5F9),
                Color(0xFFE2E8F0)
            )
        )
    }

    val orb1Color = if (isDark) Color(0xFF0EA5E9).copy(alpha = 0.22f) else Color(0xFF38BDF8).copy(alpha = 0.35f)
    val orb2Color = if (isDark) Color(0xFF8B5CF6).copy(alpha = 0.20f) else Color(0xFFC084FC).copy(alpha = 0.30f)
    val orb3Color = if (isDark) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFF34D399).copy(alpha = 0.25f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgGradient)
            .drawBehind {
                // Top-left glowing orb
                drawCircle(
                    color = orb1Color,
                    radius = size.width * 0.55f,
                    center = Offset(size.width * 0.15f, size.height * 0.12f)
                )
                // Middle-right orb
                drawCircle(
                    color = orb2Color,
                    radius = size.width * 0.60f,
                    center = Offset(size.width * 0.85f, size.height * 0.45f)
                )
                // Bottom-left orb
                drawCircle(
                    color = orb3Color,
                    radius = size.width * 0.50f,
                    center = Offset(size.width * 0.20f, size.height * 0.85f)
                )
            },
        content = content
    )
}

/**
 * Translucent frosted glass card component with shiny borders and subtle shadow elevation.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    isDark: Boolean = true,
    shape: Shape = RoundedCornerShape(24.dp),
    onClick: (() -> Unit)? = null,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 8.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.98f else 1.0f,
        animationSpec = springAnimation(),
        label = "glassCardScale"
    )

    val surfaceColor = if (isDark) {
        Color(0xFF1E293B).copy(alpha = 0.50f)
    } else {
        Color(0xFFFFFFFF).copy(alpha = 0.70f)
    }

    val borderBrush = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.35f),
                Color.White.copy(alpha = 0.08f),
                Color(0xFF38BDF8).copy(alpha = 0.25f)
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.95f),
                Color.White.copy(alpha = 0.40f),
                Color(0xFF0EA5E9).copy(alpha = 0.20f)
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        )
    }

    Surface(
        modifier = modifier
            .scale(scale)
            .shadow(elevation, shape, clip = false)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            ),
        shape = shape,
        color = surfaceColor,
        border = BorderStroke(borderWidth, borderBrush)
    ) {
        Column(
            modifier = Modifier
                .clip(shape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.08f else 0.25f),
                            Color.Transparent
                        )
                    )
                )
                .padding(16.dp)
        ) {
            content()
        }
    }
}

/**
 * Interactive Liquid Glass Button with spring click behavior and optional icon.
 */
@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDark: Boolean = true,
    accentColor: Color = GlassCyan,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1.0f,
        animationSpec = springAnimation(),
        label = "glassBtnScale"
    )

    val buttonBrush = Brush.horizontalGradient(
        colors = listOf(
            accentColor.copy(alpha = if (enabled) 0.85f else 0.40f),
            accentColor.copy(alpha = if (enabled) 0.65f else 0.30f)
        )
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .background(buttonBrush)
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(
                        listOf(Color.White.copy(alpha = 0.6f), Color.White.copy(alpha = 0.1f))
                    )
                ),
                RoundedCornerShape(20.dp)
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

/**
 * Translucent Chip for filters or category choices.
 */
@Composable
fun GlassChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDark: Boolean = true,
    icon: ImageVector? = null,
    accentColor: Color = GlassCyan
) {
    val chipBg = if (selected) {
        accentColor.copy(alpha = 0.85f)
    } else if (isDark) {
        Color(0xFF1E293B).copy(alpha = 0.50f)
    } else {
        Color.White.copy(alpha = 0.70f)
    }

    val textColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(chipBg)
            .border(
                BorderStroke(
                    1.dp,
                    if (selected) {
                        Brush.linearGradient(listOf(Color.White, accentColor))
                    } else {
                        Brush.linearGradient(
                            listOf(
                                Color.White.copy(alpha = if (isDark) 0.25f else 0.8f),
                                Color.Transparent
                            )
                        )
                    }
                ),
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                color = textColor,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

/**
 * Animated liquid progress bar with percentage readout.
 */
@Composable
fun GlassProgressBar(
    progress: Float, // 0.0 to 1.0
    modifier: Modifier = Modifier,
    isDark: Boolean = true,
    fillColor: Color = GlassCyan,
    height: Dp = 12.dp
) {
    val clampedProgress = progress.coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = clampedProgress,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "glassProgressAnim"
    )

    val trackBg = if (isDark) Color(0xFF334155).copy(alpha = 0.5f) else Color(0xFFCBD5E1).copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(trackBg)
            .border(
                BorderStroke(0.5.dp, Color.White.copy(alpha = if (isDark) 0.2f else 0.6f)),
                CircleShape
            )
    ) {
        if (animatedProgress > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(height)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(fillColor, fillColor.copy(alpha = 0.8f), Color.White.copy(alpha = 0.9f))
                        )
                    )
            )
        }
    }
}

private fun <T> springAnimation() = androidx.compose.animation.core.spring<T>(
    dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
    stiffness = androidx.compose.animation.core.Spring.StiffnessLow
)
