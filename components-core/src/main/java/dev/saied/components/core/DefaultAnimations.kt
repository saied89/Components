@file:Suppress("ConstPropertyName")

package dev.saied.components.core

import androidx.compose.animation.core.spring

/**
 * Animation Definitions from Material3
 */
internal object DefaultAnimationSpecs {
    fun <T> defaultSpatialSpec() =
        spring<T>(
            dampingRatio = StandardMotionTokens.SpringDefaultSpatialDamping,
            stiffness = StandardMotionTokens.SpringDefaultSpatialStiffness
        )

    fun <T>  fastSpatialSpec() =
        spring<T>(
            dampingRatio = StandardMotionTokens.SpringFastSpatialDamping,
            stiffness = StandardMotionTokens.SpringFastSpatialStiffness
        )

    fun <T>  slowSpatialSpec() =
        spring<T>(
            dampingRatio = StandardMotionTokens.SpringSlowSpatialDamping,
            stiffness = StandardMotionTokens.SpringSlowSpatialStiffness
        )

    fun <T>  defaultEffectsSpec() =
        spring<T>(
            dampingRatio = StandardMotionTokens.SpringDefaultEffectsDamping,
            stiffness = StandardMotionTokens.SpringDefaultEffectsStiffness
        )

    fun <T>  fastEffectsSpec() =
        spring<T>(
            dampingRatio = StandardMotionTokens.SpringFastEffectsDamping,
            stiffness = StandardMotionTokens.SpringFastEffectsStiffness
        )

    fun <T>  slowEffectsSpec() =
        spring<T>(
            dampingRatio = StandardMotionTokens.SpringSlowEffectsDamping,
            stiffness = StandardMotionTokens.SpringSlowEffectsStiffness
        )
}


private object StandardMotionTokens {
    const val SpringDefaultSpatialDamping = 0.9f
    const val SpringDefaultSpatialStiffness = 700.0f
    const val SpringDefaultEffectsDamping = 1.0f
    const val SpringDefaultEffectsStiffness = 1600.0f
    const val SpringFastSpatialDamping = 0.9f
    const val SpringFastSpatialStiffness = 1400.0f
    const val SpringFastEffectsDamping = 1.0f
    const val SpringFastEffectsStiffness = 3800.0f
    const val SpringSlowSpatialDamping = 0.9f
    const val SpringSlowSpatialStiffness = 300.0f
    const val SpringSlowEffectsDamping = 1.0f
    const val SpringSlowEffectsStiffness = 800.0f
}