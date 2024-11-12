package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.createAccountScreen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.evolution.data.database.UserData
import ar.edu.unlam.mobile.scaffolding.evolution.data.database.firestore_collection_userFutureFight
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.Routes.HomeScreenRoute
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.IndigoDye
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.SilverB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CreateAccountScreenBeta(
    navController: NavController,
    auth: FirebaseAuth,
) {
    val context = LocalContext.current
    val showLoginForm =
        rememberSaveable {
            mutableStateOf(true)
        }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(SilverB, IndigoDye),
                            startY = 0f,
                            endY = 600f,
                        ),
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.size(5.dp))
            Image(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.iv_logo),
                contentDescription = "MarvelFF Logo",
            )

            if (showLoginForm.value) {
                Text(
                    text = "Log In",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                UserFormLogin(
                    isCreateAccount = false,
                ) { email, password ->
                    Log.d("FutureFightPrueba", "Creando cuenta con $email y $password")
                    loginUser(auth, email, password, navController, context)
                }
            } else {
                Text(
                    text = "Create Account",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                UserFormCreateAccount(isCreateAccount = true) { email, password, name, nickname ->
                    Log.d("FutureFightPrueba", "Logueado con $email, $password, $name y $nickname")
                    createUser(auth, email, password, name, nickname, navController, context)
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val textQuestion = if (showLoginForm.value) "Don't have an account yet?" else "Already have an account?"
                val textAnswer = if (showLoginForm.value) "Create an Account" else "Log In"
                Text(text = textQuestion)
                Text(
                    text = textAnswer,
                    modifier =
                        Modifier
                            .clickable {
                                showLoginForm.value = !showLoginForm.value
                            }.padding(start = 5.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun UserFormLogin(
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit,
) {
    val email =
        rememberSaveable {
            mutableStateOf("")
        }
    val password =
        rememberSaveable {
            mutableStateOf("")
        }
    val passwordVisible =
        rememberSaveable {
            mutableStateOf(false)
        }
    val validValue =
        remember(email.value, password.value) {
            email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
        }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        EmailInput(emailState = email)
        PasswordInput(
            passwordState = password,
            labelId = "Password",
            passwordVisible = passwordVisible,
        )
        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login",
            validInput = validValue,
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun UserFormCreateAccount(
    isCreateAccount: Boolean = false,
    onDone: (String, String, String, String) -> Unit,
) {
    val email =
        rememberSaveable {
            mutableStateOf("")
        }
    val password =
        rememberSaveable {
            mutableStateOf("")
        }
    val name =
        rememberSaveable {
            mutableStateOf("")
        }
    val nickname =
        rememberSaveable {
            mutableStateOf("")
        }
    val passwordVisible =
        rememberSaveable {
            mutableStateOf(false)
        }
    val validValue =
        remember(email.value, password.value, name.value, nickname.value) {
            email.value.trim().isNotEmpty() &&
                password.value.trim().isNotEmpty() &&
                name.value.trim().isNotEmpty() &&
                nickname.value.trim().isNotEmpty()
        }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        EmailInput(emailState = email)
        PasswordInput(
            passwordState = password,
            labelId = "Password",
            passwordVisible = passwordVisible,
        )
        NameInput(nameState = name)
        NicknameInput(nicknameState = nickname)
        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login",
            validInput = validValue,
        ) {
            onDone(
                email.value.trim(),
                password.value.trim(),
                name.value.trim(),
                nickname.value.trim(),
            )
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    validInput: Boolean,
    onUserClick: () -> Unit,
) {
    Button(
        onClick = onUserClick,
        modifier =
            Modifier
                .padding(3.dp)
                .fillMaxWidth(),
        shape = CircleShape,
        enabled = validInput,
    ) {
        Text(
            text = textId,
            modifier = Modifier.padding(5.dp),
        )
    }
}

@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>,
) {
    val visualTransformation =
        if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelId) },
        singleLine = true,
        modifier =
            Modifier
                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()) {
                PasswordVisibleIcon(passwordVisible)
            }
        },
    )
}

@Composable
fun PasswordVisibleIcon(passwordVisible: MutableState<Boolean>) {
    val image = if (passwordVisible.value) Icons.Default.VisibilityOff else Icons.Default.Visibility
    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
        Icon(imageVector = image, contentDescription = "ocultar/desocultar")
    }
}

@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = "Email",
) {
    InputFieldCom(
        valueState = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email,
    )
}

@Composable
fun NameInput(
    nameState: MutableState<String>,
    labelId: String = "Name",
) {
    InputFieldCom(
        valueState = nameState,
        labelId = labelId,
        keyboardType = KeyboardType.Email,
    )
}

@Composable
fun NicknameInput(
    nicknameState: MutableState<String>,
    labelId: String = "Nickname",
) {
    InputFieldCom(
        valueState = nicknameState,
        labelId = labelId,
        keyboardType = KeyboardType.Email,
    )
}

@Composable
fun InputFieldCom(
    valueState: MutableState<String>,
    labelId: String,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType,
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        modifier =
            Modifier
                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                .fillMaxWidth(),
        keyboardOptions =
            KeyboardOptions(
                keyboardType = keyboardType,
            ),
    )
}

fun loginUser(
    auth: FirebaseAuth,
    email: String,
    password: String,
    navController: NavController,
    context: Context,
) {
    if (email.isNotBlank() && password.isNotBlank()) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast
                    .makeText(context, "Login Successful", Toast.LENGTH_SHORT)
                    .show()
                navController.navigate(HomeScreenRoute) {
                    popUpTo<HomeScreenRoute> { inclusive = true }
                }
            } else {
                Toast
                    .makeText(context, "User or Password incorrect", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    } else {
        Toast.makeText(context, "User or Password incorrect", Toast.LENGTH_SHORT).show()
    }
}

fun createUser(
    auth: FirebaseAuth,
    email: String,
    password: String,
    name: String,
    nickname: String,
    navController: NavController,
    context: Context,
) {
    auth
        .createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                val userData =
                    UserData(
                        userID = userId,
                        name = name,
                        nickname = nickname,
                        email = email,
                        infoUser = "Info Default",
                    )
                saveUserDataToFirestore(userData, context)
                // TODO navega hacia la pantalla principal si la cuenta fue creada
                navController.navigate(HomeScreenRoute) {
                    popUpTo<HomeScreenRoute> { inclusive = true }
                }
            } else {
                Log.e("FutureFightPrueba", "Creación de cuenta fallida: ${task.exception}")
                Toast
                    .makeText(context, "Account Already Exists", Toast.LENGTH_SHORT)
                    .show()
            }
        }
}

fun saveUserDataToFirestore(
    userData: UserData,
    context: Context,
) {
    val db = FirebaseFirestore.getInstance()
    db
        .collection(firestore_collection_userFutureFight)
        .document(userData.userID!!)
        .set(userData)
        .addOnSuccessListener {
            Toast
                .makeText(context, "Account Created Successful", Toast.LENGTH_SHORT)
                .show()
            Log.d("FutureFightPrueba", "Datos de usuario guardados en Firestore")
        }.addOnFailureListener { e ->
            Log.e("FutureFightPrueba", "Error al guardar datos de usuario: $e")
            Toast
                .makeText(context, "Error Creating Account", Toast.LENGTH_SHORT)
                .show()
        }
}
