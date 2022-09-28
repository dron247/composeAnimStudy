package org.dementiev.composeanimstudy.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun DraggableLesson() {
    val initialCard1OffsetX = 0f
    val initialCard1OffsetY = 0f
    val density = LocalDensity.current
    var card1OffsetX by remember { mutableStateOf(initialCard1OffsetX) }
    var card1OffsetY by remember { mutableStateOf(initialCard1OffsetY) }

    var cardAnimationState by remember { mutableStateOf(CardState.Idle) }
    val card2OffsetTransition = updateTransition(targetState = cardAnimationState, label = "2")
    val card2RotationTransition by card2OffsetTransition.animateFloat(label = "2_r") { state ->
        when (state) {
            CardState.Idle -> -10f
            CardState.Moving -> 0f
        }
    }
    val card2OffsetAnimation by card2OffsetTransition.animateIntOffset(
        label = "2_t",
        transitionSpec = {
            spring(
                stiffness = 800f,
                visibilityThreshold = IntOffset(1, 1)
            )
        }
    ) { state ->
        when (state) {
            CardState.Idle -> IntOffset(
                0,
                -with(density) { 20.dp.toPx() }.roundToInt()
            )
            CardState.Moving -> IntOffset(
                (card1OffsetX + with(density) { 5.dp.toPx() }).roundToInt(),
                (card1OffsetY - with(density) { 5.dp.toPx() }).roundToInt()
            )
        }
    }

    val card3OffsetTransition = updateTransition(targetState = cardAnimationState, label = "3")
    val card3RotationTransition by card2OffsetTransition.animateFloat(label = "3_r") { state ->
        when (state) {
            CardState.Idle -> -20f
            CardState.Moving -> 0f
        }
    }
    val card3OffsetAnimation by card3OffsetTransition.animateIntOffset(
        label = "3_t",
        transitionSpec = {
            spring(
                stiffness = Spring.StiffnessLow,
                visibilityThreshold = IntOffset(1, 1)
            )
        }
    ) { state ->
        when (state) {
            CardState.Idle -> IntOffset(
                0,
                -with(density) { 30.dp.toPx() }.roundToInt()
            )
            CardState.Moving -> IntOffset(
                (card1OffsetX + with(density) { 10.dp.toPx() }).roundToInt(),
                (card1OffsetY - with(density) { 10.dp.toPx() }).roundToInt()
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .offset { card3OffsetAnimation }
                .rotate(card3RotationTransition)
                .align(Alignment.Center)
                .width(200.dp)
                .height(140.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Blue)
                .offset(y = 20.dp)
        )
        Box(
            modifier = Modifier
                .offset { card2OffsetAnimation }
                .rotate(card2RotationTransition)
                .align(Alignment.Center)
                .width(200.dp)
                .height(140.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Red)
        )
        Box(
            modifier = Modifier
                .offset { IntOffset(card1OffsetX.roundToInt(), card1OffsetY.roundToInt()) }
                .align(Alignment.Center)
                .width(200.dp)
                .height(140.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            cardAnimationState = CardState.Moving
                        },
                        onDragEnd = {
                            card1OffsetX = initialCard1OffsetX
                            card1OffsetY = initialCard1OffsetY
                            cardAnimationState = CardState.Idle
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        card1OffsetX += dragAmount.x
                        card1OffsetY += dragAmount.y
                    }
                }
        ) {
            Text(
                text = "I'm not finished, but anyway, drag me",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(20.dp)
            )
        }
    }
}

enum class CardState {
    Idle,
    Moving
}
