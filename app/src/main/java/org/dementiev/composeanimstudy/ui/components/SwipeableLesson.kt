package org.dementiev.composeanimstudy.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun SwipeableLesson() {
    val scope = rememberCoroutineScope()
    val sliderSize = 56.dp
    val trackHeight = sliderSize + 10.dp
    val swipeableSate = rememberSwipeableState(SwipeProgress.Initial)
    var trackSize by remember { mutableStateOf(Size.Zero) }
    val density = LocalDensity.current
    val swipeWidth = remember(trackSize) {
        if (trackSize.width == 0f) {
            1f
        } else {
            trackSize.width - (with(density) { sliderSize.toPx() })
        }
    }
    if (swipeableSate.isAnimationRunning) {
        DisposableEffect(Unit) {
            onDispose {
                scope.launch {
                    swipeableSate.animateTo(SwipeProgress.Initial)
                }
            }
        }
    }
    val textAlpha = animateFloatAsState(
        targetValue = 1f - swipeableSate.offset.value / swipeWidth
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(trackHeight))
            .background(Color.Green)
            .fillMaxWidth(.8f)
            .height(trackHeight)
            .padding(start = sliderSize / 4, end = sliderSize / 4)
            .onSizeChanged { trackSize = Size(it.width.toFloat(), it.height.toFloat()) }
            .swipeable(
                state = swipeableSate,
                anchors = mapOf(
                    0f to SwipeProgress.Initial,
                    swipeWidth * .8f to SwipeProgress.TransformPoint,
                    swipeWidth to SwipeProgress.Complete
                ),
                orientation = Orientation.Horizontal,
                thresholds = { _, _ -> FractionalThreshold(.3f) }
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = textAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Example example", fontSize = 22.sp)
            Text(text = "Example example", fontSize = 12.sp)
        }
        Box(
            modifier = Modifier
                .offset { IntOffset(swipeableSate.offset.value.roundToInt(), 0) }
                .clip(CircleShape)
                .background(Color.LightGray)
                .size(sliderSize)
                .align(Alignment.CenterStart)
        ) {
            Crossfade(
                targetState = swipeableSate,
                modifier = Modifier
                    .align(Alignment.Center)
            ) { swipeState ->
                if (swipeState.offset.value < swipeWidth * .8f) {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .width(40.dp)
                            .height(40.dp)
                    )
                } else {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .width(40.dp)
                            .height(40.dp)
                    )
                }
            }
        }
    }
}

enum class SwipeProgress(val raw: Int) {
    Initial(0),
    TransformPoint(1),
    Complete(2)
}
