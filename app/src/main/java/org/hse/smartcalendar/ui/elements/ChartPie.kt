package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme

data class ChartModel(
    val value: Float,
    val color: Color,
)
@Composable
fun ChartCirclePie(
    modifier: Modifier,
    charts: List<ChartModel>,
    size: Dp = 200.dp,
    strokeWidth: Dp = 16.dp
) {
    Canvas(modifier = modifier
        .size(size)
        .padding(12.dp),

        onDraw = {

            var startAngle = 0f
            var sweepAngle = 0f

            charts.forEach {

                sweepAngle = (it.value / 100) * 360

                drawArc(
                    color = it.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                startAngle += sweepAngle
            }

        })
}
@Preview
@Composable
fun ChartPiePreview(){
    val charts = listOf(
        ChartModel(value = 20f, color = Color.Black),
        ChartModel(value = 30f, color = Color.Gray),
        ChartModel(value = 40f, color = Color.Green),
        ChartModel(value = 10f, color = Color.Red),
    )
    SmartCalendarTheme {
        ChartCirclePie(Modifier, charts)
    }
}
@Composable
fun CircleColored(modifier: Modifier = Modifier, color: Color){
    Canvas(modifier = modifier, onDraw = {
        drawCircle(color = color)
    })
}

