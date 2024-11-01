package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.userProfileScreen.profileComponent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.BlackCustom
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.CoolGray
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.CyanWay
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.DarkPurple
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.IndigoDye
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.Purple80
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.PurpleGrey80
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.SilverB

@Composable
fun UpdateData(
    onDismiss: () -> Unit,
    onUpdateDataAdded: (String, String, String) -> Unit,
) {
    var nickname by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var infoUser by rememberSaveable { mutableStateOf("") }
    val validValue =
        remember(name, nickname, infoUser) {
            name.trim().isNotEmpty() && nickname.trim().isNotEmpty() && infoUser.trim().isNotEmpty()
        }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            colors =
                CardColors(
                    containerColor = IndigoDye,
                    contentColor = DarkPurple,
                    disabledContentColor = BlackCustom,
                    disabledContainerColor = CyanWay,
                ),
            border = BorderStroke(width = 4.dp, color = SilverB),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Update your information",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                    textAlign = TextAlign.Center,
                )
                TextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    placeholder = { Text(text = "New Nickname") },
                )
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(text = "New Name") },
                )
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = infoUser,
                    onValueChange = { infoUser = it },
                    placeholder = { Text(text = "New User Information") },
                )
                Spacer(modifier = Modifier.height(4.dp))
                UpdateButton(
                    validInput = validValue,
                    onUserClick = { onUpdateDataAdded(name, nickname, infoUser) },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun UpdateButton(
    validInput: Boolean,
    onUserClick: () -> Unit,
) {
    Button(
        onClick = onUserClick,
        border = BorderStroke(width = 1.dp, color = Purple80),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(24.dp),
        colors =
            ButtonColors(
                containerColor = PurpleGrey80,
                contentColor = IndigoDye,
                disabledContentColor = CoolGray,
                disabledContainerColor = CoolGray,
            ),
        enabled = validInput,
    ) {
        Text(
            text = "Update Now!",
            color = BlackCustom,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}
