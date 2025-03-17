package org.hse.smartcalendar.data

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

class SettingsButton(val label: String, val icon: ImageVector, val action: ()->Unit){
    constructor(label: String, icon: ImageVector, screenName: String, navController: NavController) : this(label, icon, {navController.navigate(screenName)})
    @Composable
    fun DrawButton(){
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null, // decorative
            )
            Spacer(Modifier.width(16.dp))
            Text(
                fontWeight = FontWeight.Bold,
                text = label,
            )
        }
    }
}