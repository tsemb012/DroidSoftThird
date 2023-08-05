package com.example.droidsoftthird.composable.entrance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.R
import com.example.droidsoftthird.utils.compose.supportWideScreen


sealed class WelcomeEvent {
    object SignIn : WelcomeEvent()
    object SignUp : WelcomeEvent()
}

@Composable
fun WelcomeScreen(onEvent: (WelcomeEvent) -> Unit) {
    var showBranding by remember { mutableStateOf(true) }

    Surface(modifier = Modifier.supportWideScreen()) {
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.app_welcome), // ここに画像リソースを指定
                contentDescription = null, // アクセシビリティのための説明文
                alignment = Alignment.TopCenter, // 画像を中央に配置
                modifier = Modifier.fillMaxSize(), // 画像が全体に広がるようにする
                contentScale = ContentScale.Fit// 必要に応じて画像をクロップまたは他の方法で調整
            )

            val fontName = GoogleFont("Noto Sans Vithkuqi")
            val fontProvider = GoogleFont.Provider(
                providerAuthority = "com.google.android.gms.fonts",
                providerPackage = "com.google.android.gms",
                certificates = R.array.com_google_android_gms_fonts_certs
            )

            val fontFamily = FontFamily(
                Font(googleFont = fontName, fontProvider = fontProvider)
            )

            val baseColor = colorResource(id = R.color.base_100)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                baseColor.copy(alpha = 0.3f), // 開始色
                                baseColor.copy(alpha = 0.2f),
                                baseColor.copy(alpha = 0.2f),
                                baseColor.copy(alpha = 0.5f),
                                baseColor,
                                baseColor,
                                baseColor // 終了色
                            ),
                            //stops = listOf(0.0f, 0.5f, 1.0f), // 各色の位置
                            startY = 0.0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )


            Column (
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BrandLogo()
                Spacer(modifier = Modifier.height(32.dp))
                SignInCreateAccount(
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

        }
    }
}


@Composable
private fun SignInCreateAccount(
    onEvent: (WelcomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.primary_dark)
            ),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp, disabledElevation = 0.dp),
            shape = RoundedCornerShape(8.dp),
            onClick = { onEvent(WelcomeEvent.SignUp) },
            modifier = modifier
                .padding(start = 36.dp, end = 36.dp, top = 8.dp, bottom = 4.dp)
        ) {
            Text(stringResource(id = R.string.sign_up), color = Color.White, style = MaterialTheme.typography.h6, fontWeight = FontWeight.SemiBold)
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.LightGray
            ),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp, disabledElevation = 0.dp),
            shape = RoundedCornerShape(8.dp),
            onClick = { onEvent(WelcomeEvent.SignIn) },
            modifier = modifier
                .padding(start = 36.dp, end = 36.dp, top = 4.dp, bottom = 8.dp)
        ) {
            Text(stringResource(id = R.string.login), color = Color.DarkGray, style = MaterialTheme.typography.h6, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(name = "Welcome light theme")
@Composable
fun WelcomeScreenPreview() {
    MaterialTheme {
        WelcomeScreen {}
    }
}