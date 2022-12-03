package com.openclassrooms.realestatemanager.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.openclassrooms.realestatemanager.domain.models.PhotoWithTextFirebase
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAddPhotoWithText(openDialogAddPhotoWithText: Boolean,addPhotoWithText : (photoWithText : PhotoWithTextFirebase)-> Unit,closeDialogAddPhoto : () -> Unit) {
    var photoSelect by rememberSaveable { mutableStateOf<Uri>(Uri.EMPTY) }
    var titlePhoto by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    val someActivityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                val uriImageSelected: Uri? = data?.data
                if (uriImageSelected != null) {


                    photoSelect = uriImageSelected
                    Log.e("photoSelectLauncher",photoSelect.toString())
                }

            }
        }
    )

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if (it) {
                val i = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                someActivityResultLauncher.launch(i)
            } else {
                Toast.makeText(
                    context,
                    "Impossible d'ajouter un biens , veuillez autorisé l'accees au stockage interne",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
    )

    if (openDialogAddPhotoWithText) {
        Dialog(
            onDismissRequest = { },
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.wrapContentSize(),
            ) {

                ConstraintLayout(modifier = Modifier.wrapContentSize()) {
                    val (titleEntry, imageSelect, buttonCancel, buttonConfirm, buttonAddPhoto) = createRefs()
                    TextField(
                        value = titlePhoto,
                        onValueChange = { titlePhoto = it },
                        label = { Text("Enter Title Photo") },
                        maxLines = 2,
                        modifier = Modifier.constrainAs(titleEntry) {
                            top.linkTo(parent.top, margin = 15.dp)
                            start.linkTo(parent.start, margin = 25.dp)
                            end.linkTo(parent.end, margin = 25.dp)
                        }
                    )



                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                            .constrainAs(imageSelect) {
                                top.linkTo(titleEntry.bottom, margin = 15.dp)
                                start.linkTo(parent.start, margin = 30.dp)
                                end.linkTo(parent.end, margin = 30.dp)
                            }

                    ) {
                        GlideImage(
                            imageModel = {if(photoSelect != Uri.EMPTY) photoSelect else ""},
                            imageOptions = ImageOptions(contentScale = ContentScale.Crop, requestSize = IntSize(150,150)),
                            modifier = Modifier.size(150.dp)
                        )
                    }


                    Button(
                        modifier = Modifier.constrainAs(buttonAddPhoto) {
                            top.linkTo(imageSelect.bottom, margin = 15.dp)
                            start.linkTo(parent.start, margin = 25.dp)
                            end.linkTo(parent.end, margin = 25.dp)
                        },
                        onClick = {
                            when {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ) == PackageManager.PERMISSION_GRANTED -> {

                                    val i = Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    )
                                    someActivityResultLauncher.launch(i)

                                    // You can use the API that requires the permission.
                                }
                                ActivityCompat.shouldShowRequestPermissionRationale(activity, "") -> {}
                                else -> {
                                    // You can directly ask for the permission.
                                    // The registered ActivityResultCallback gets the result of this request.
                                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                                }
                            }
                        }


                    ) {
                        Text(text = "Add Photo")
                    }


                    TextButton(
                        modifier = Modifier.constrainAs(buttonCancel) {
                            top.linkTo(buttonAddPhoto.bottom, margin = 15.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                        },
                        onClick = {
                            photoSelect = Uri.EMPTY
                            titlePhoto = ""
                            closeDialogAddPhoto()
                            /*TODO*/
                        }
                    ) {
                        Text(text = "Cancel")
                    }
                    TextButton(
                        modifier = Modifier.constrainAs(buttonConfirm) {
                            top.linkTo(buttonAddPhoto.bottom, margin = 15.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        },
                        onClick = {
                            val photoWithText = PhotoWithTextFirebase(photoSelect.toString(),"", titlePhoto)
                            addPhotoWithText(photoWithText)
                            photoSelect = Uri.EMPTY
                            titlePhoto = ""
                            closeDialogAddPhoto()
                        }
                    ) {
                        Text(text = "Confirm")
                    }


                }
            }


        }
    }
}