package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.BrassBright
import com.example.ui.theme.BrassGold
import com.example.ui.theme.MahoganyDark

@Composable
fun MrBookLogoBadge(
    size: Dp = 44.dp,
    isCircular: Boolean = false,
    tag: String = "mr_book_logo_badge",
    modifier: Modifier = Modifier
) {
    val shape = if (isCircular) CircleShape else RoundedCornerShape(size * 0.26f)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .shadow(4.dp, shape)
            .clip(shape)
            .background(MahoganyDark)
            .border(1.5.dp, BrassBright, shape)
            .padding(1.dp)
            .testTag(tag)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_mr_book_logo),
            contentDescription = "MR BOOK official logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .border(0.75.dp, BrassGold.copy(alpha = 0.5f), shape)
        )
    }
}
