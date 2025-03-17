package org.hse.smartcalendar.ui.elements

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Medal: ImageVector
    get() {
        if (_Medal != null) {
            return _Medal!!
        }
        _Medal = ImageVector.Builder(
            name = "Medal",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(7.21f, 15f)
                lineTo(2.66f, 7.14f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.13f, -2.2f)
                lineTo(4.4f, 2.8f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6f, 2f)
                horizontalLineToRelative(12f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.6f, 0.8f)
                lineToRelative(1.6f, 2.14f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.14f, 2.2f)
                lineTo(16.79f, 15f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(11f, 12f)
                lineTo(5.12f, 2.2f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(13f, 12f)
                lineToRelative(5.88f, -9.8f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(8f, 7f)
                horizontalLineToRelative(8f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(17f, 17f)
                arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 22f)
                arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7f, 17f)
                arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 17f, 17f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 18f)
                verticalLineToRelative(-2f)
                horizontalLineToRelative(-0.5f)
            }
        }.build()
        return _Medal!!
    }

private var _Medal: ImageVector? = null

public val Follow_the_signs: ImageVector
    get() {
        if (_Follow_the_signs != null) {
            return _Follow_the_signs!!
        }
        _Follow_the_signs = ImageVector.Builder(
            name = "Follow_the_signs",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(380f, 220f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(300f, 140f)
                reflectiveQuadToRelative(23.5f, -56.5f)
                reflectiveQuadTo(380f, 60f)
                reflectiveQuadToRelative(56.5f, 23.5f)
                reflectiveQuadTo(460f, 140f)
                reflectiveQuadToRelative(-23.5f, 56.5f)
                reflectiveQuadTo(380f, 220f)
                moveTo(120f, 920f)
                lineToRelative(110f, -564f)
                lineToRelative(-70f, 30f)
                verticalLineToRelative(134f)
                horizontalLineTo(80f)
                verticalLineToRelative(-188f)
                lineToRelative(210f, -86f)
                quadToRelative(27f, -11f, 55f, -2.5f)
                reflectiveQuadToRelative(43f, 34.5f)
                lineToRelative(38f, 64f)
                quadToRelative(27f, 44f, 72.5f, 71f)
                reflectiveQuadTo(600f, 440f)
                verticalLineToRelative(80f)
                quadToRelative(-66f, 0f, -122.5f, -28f)
                reflectiveQuadTo(382f, 416f)
                lineToRelative(-24f, 120f)
                lineToRelative(82f, 82f)
                verticalLineToRelative(302f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(-240f)
                lineToRelative(-86f, -80f)
                lineToRelative(-70f, 320f)
                close()
                moveToRelative(550f, 0f)
                verticalLineToRelative(-560f)
                horizontalLineTo(520f)
                verticalLineToRelative(-280f)
                horizontalLineToRelative(360f)
                verticalLineToRelative(280f)
                horizontalLineTo(730f)
                verticalLineToRelative(560f)
                close()
                moveToRelative(51f, -601f)
                lineToRelative(99f, -99f)
                lineToRelative(-99f, -99f)
                lineToRelative(-43f, 43f)
                lineToRelative(26f, 26f)
                horizontalLineTo(580f)
                verticalLineToRelative(60f)
                horizontalLineToRelative(124f)
                lineToRelative(-26f, 26f)
                close()
            }
        }.build()
        return _Follow_the_signs!!
    }

private var _Follow_the_signs: ImageVector? = null


public val Calendar_add_on: ImageVector
    get() {
        if (_Calendar_add_on != null) {
            return _Calendar_add_on!!
        }
        _Calendar_add_on = ImageVector.Builder(
            name = "Calendar_add_on",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(680f, 880f)
                verticalLineToRelative(-120f)
                horizontalLineTo(560f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(120f)
                verticalLineToRelative(-120f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(120f)
                horizontalLineToRelative(120f)
                verticalLineToRelative(80f)
                horizontalLineTo(760f)
                verticalLineToRelative(120f)
                close()
                moveToRelative(-480f, -80f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(120f, 720f)
                verticalLineToRelative(-480f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(200f, 160f)
                horizontalLineToRelative(40f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(80f)
                horizontalLineToRelative(240f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(80f)
                horizontalLineToRelative(40f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(760f, 240f)
                verticalLineToRelative(244f)
                quadToRelative(-20f, -3f, -40f, -3f)
                reflectiveQuadToRelative(-40f, 3f)
                verticalLineToRelative(-84f)
                horizontalLineTo(200f)
                verticalLineToRelative(320f)
                horizontalLineToRelative(280f)
                quadToRelative(0f, 20f, 3f, 40f)
                reflectiveQuadToRelative(11f, 40f)
                close()
                moveToRelative(0f, -480f)
                horizontalLineToRelative(480f)
                verticalLineToRelative(-80f)
                horizontalLineTo(200f)
                close()
                moveToRelative(0f, 0f)
                verticalLineToRelative(-80f)
                close()
            }
        }.build()
        return _Calendar_add_on!!
    }

private var _Calendar_add_on: ImageVector? = null


public val BrainCircuit: ImageVector
    get() {
        if (_BrainCircuit != null) {
            return _BrainCircuit!!
        }
        _BrainCircuit = ImageVector.Builder(
            name = "BrainCircuit",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 5f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = false, -5.997f, 0.125f)
                arcToRelative(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.526f, 5.77f)
                arcToRelative(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.556f, 6.588f)
                arcTo(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = false, 12f, 18f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9f, 13f)
                arcToRelative(4.5f, 4.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3f, -4f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(6.003f, 5.125f)
                arcTo(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6.401f, 6.5f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(3.477f, 10.896f)
                arcToRelative(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.585f, -0.396f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(6f, 18f)
                arcToRelative(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.967f, -0.516f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 13f)
                horizontalLineToRelative(4f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 18f)
                horizontalLineToRelative(6f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 2f)
                verticalLineToRelative(1f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 8f)
                horizontalLineToRelative(8f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(16f, 8f)
                verticalLineTo(5f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, -2f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(16.5f, 13f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 16f, 13.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 15.5f, 13f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 16.5f, 13f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(18.5f, 3f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 18f, 3.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 17.5f, 3f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 18.5f, 3f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20.5f, 21f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20f, 21.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 19.5f, 21f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20.5f, 21f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20.5f, 8f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20f, 8.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 19.5f, 8f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20.5f, 8f)
                close()
            }
        }.build()
        return _BrainCircuit!!
    }

private var _BrainCircuit: ImageVector? = null


public val Finance: ImageVector
    get() {
        if (_Finance != null) {
            return _Finance!!
        }
        _Finance = ImageVector.Builder(
            name = "Finance",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(200f, 840f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(120f, 760f)
                verticalLineToRelative(-640f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(640f)
                horizontalLineToRelative(640f)
                verticalLineToRelative(80f)
                close()
                moveToRelative(40f, -120f)
                verticalLineToRelative(-360f)
                horizontalLineToRelative(160f)
                verticalLineToRelative(360f)
                close()
                moveToRelative(200f, 0f)
                verticalLineToRelative(-560f)
                horizontalLineToRelative(160f)
                verticalLineToRelative(560f)
                close()
                moveToRelative(200f, 0f)
                verticalLineToRelative(-200f)
                horizontalLineToRelative(160f)
                verticalLineToRelative(200f)
                close()
            }
        }.build()
        return _Finance!!
    }

private var _Finance: ImageVector? = null
