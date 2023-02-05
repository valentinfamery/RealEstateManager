package com.openclassrooms.realestatemanager.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel
import com.openclassrooms.realestatemanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginEmailAndPassword(
    isExpanded: Boolean,
    userViewModel: UserViewModel,
    navigateToRegisterScreen: () -> Unit,

    ) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    var passwordVisible by rememberSaveable { mutableStateOf(false) }



    ConstraintLayout(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        val (entryEmail, entryPassword, buttonLogin, buttonRegister, textButtonRegister, buttonResetPassword) = createRefs()
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.loginFieldEmail)) },
            singleLine = true,
            modifier = Modifier.constrainAs(entryEmail) {
                top.linkTo(parent.top, margin = 250.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
                width = if(!isExpanded) Dimension.percent(0.8f) else Dimension.percent(0.3f)
                height = Dimension.wrapContent
            }
        )

        val icon = if(passwordVisible)
            painterResource(id = R.drawable.ic_baseline_visibility_24 )
        else
            painterResource(id = R.drawable.ic_baseline_visibility_off_24)


        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.loginFieldPassword)) },
            singleLine = true,
            modifier = Modifier.constrainAs(entryPassword) {
                top.linkTo(entryEmail.bottom, margin = 25.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
                width = if(!isExpanded) Dimension.percent(0.8f) else Dimension.percent(0.3f)
                height = Dimension.wrapContent
            },
            visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = { IconButton(onClick = {
                passwordVisible = !passwordVisible
            }) {
                Icon(painter = icon, contentDescription ="" )
            }}

        )

        Text(
            text = stringResource(R.string.notHaveAccount),
            modifier = Modifier.constrainAs(textButtonRegister) {
                top.linkTo(buttonLogin.bottom, margin = 15.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            })

        TextButton(
            onClick = { navigateToRegisterScreen()/* Do something! */ },
            modifier = Modifier.constrainAs(buttonRegister) {
                top.linkTo(textButtonRegister.bottom, margin = 0.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            },
        ) {
            Text(stringResource(R.string.loginButtonRegister))
        }

        TextButton(
            onClick = {
                userViewModel.sendPasswordResetEmail(email)
            },
            modifier = Modifier.constrainAs(buttonResetPassword) {
                top.linkTo(entryPassword.bottom, margin = 0.dp)
                end.linkTo(entryPassword.end, margin = 0.dp)
            },
        ) {
            Text(stringResource(R.string.forgotPassword))
        }

        Button(
            onClick = {
                userViewModel.loginUser(email, password)
            },
            modifier = Modifier
                .size(width = 275.dp, height = 50.dp)
                .constrainAs(buttonLogin) {
                    top.linkTo(entryPassword.bottom, margin = 50.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                },
        ) {
            Text(stringResource(R.string.buttonLogin))
        }

    }

    when(val resetPasswordResponse = userViewModel.sendPasswordResetEmailResponse){
        is Response.Failure ->{
            Toast.makeText(context, resetPasswordResponse.e.toString(), Toast.LENGTH_SHORT).show()
        }
        is Response.Success -> {
            Toast.makeText(context, stringResource(R.string.successMessageEditPassword), Toast.LENGTH_SHORT).show()
        }
        Response.Empty -> {}
        Response.Loading -> {}
    }
}