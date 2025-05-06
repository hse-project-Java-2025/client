package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.ui.theme.*

@Composable
fun MultipleLinearProgressIndicator(
    modifier: Modifier = Modifier,
    firstProgress: Float,
    secondProgress: Float,
    thirdProgress: Float,
    firstColor: Color,
    secondColor: Color,
    thirdColor:Color,
    backgroundColor: Color,
    clipShape: RoundedCornerShape = RoundedCornerShape(32.dp)
) {
    Box(
        modifier = Modifier
            .clip(clipShape)
            .background(backgroundColor)
            .height(16.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(thirdColor)
                .fillMaxHeight()
                .fillMaxWidth(thirdProgress+secondProgress+firstProgress)
        )
        Box(
            modifier = Modifier
                .background(secondColor)
                .fillMaxHeight()
                .fillMaxWidth(secondProgress+firstProgress)
        )
        Box(
            modifier = Modifier
                .background(firstColor)
                .fillMaxHeight()
                .fillMaxWidth(firstProgress)
        )
    }
}
@Preview
@Composable
fun MultipleLinearProgressIndicatorPreview(){
    SmartCalendarTheme {
        MultipleLinearProgressIndicator(
            firstProgress = 0.2f,
            secondProgress = 0.3f,
            thirdProgress = 0.4f,
            firstColor = DailyTaskType.WORK.color,
            secondColor = DailyTaskType.FITNESS.color,
            thirdColor = DailyTaskType.STUDIES.color,
            backgroundColor = DailyTaskType.COMMON.color,
        )
    }
}
