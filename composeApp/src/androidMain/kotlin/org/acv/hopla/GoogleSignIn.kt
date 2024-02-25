package org.acv.hopla

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@Composable
fun SignIn(googleSignInClient: GoogleSignInClient) {
  val startForResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
      if (result.resultCode == Activity.RESULT_OK) {
        val intent = result.data
        if (result.data != null) {
          val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
          Log.e("GoogleSignIn", "Token: ${task.result.idToken}")
        }
      }
    }

  Button(
    onClick = { startForResult.launch(googleSignInClient.signInIntent) },
    modifier = Modifier
      .width(300.dp)
      .height(45.dp),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        painter = painterResource(id = R.drawable.ic_google),
        contentDescription = "Google icon",
        tint = Color.Unspecified,
      )
      Text(
        text = stringResource(R.string.sign_in_button),
        color = Black,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        modifier = Modifier.padding(start = 10.dp)
      )
    }
  }
}