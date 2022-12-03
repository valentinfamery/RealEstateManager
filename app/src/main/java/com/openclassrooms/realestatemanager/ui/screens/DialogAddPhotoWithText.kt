package com.openclassrooms.realestatemanager.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.openclassrooms.realestatemanager.domain.models.PhotoWithTextFirebase
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DialogAddPhotoWithText(openDialogAddPhotoWithText: Boolean,addPhotoWithText : (photoWithText : PhotoWithTextFirebase)-> Unit,closeDialogAddPhoto : () -> Unit) {
    var photoSelect by rememberSaveable { mutableStateOf<Uri>(Uri.EMPTY) }
    var titlePhoto by remember { mutableStateOf("") }

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

    val externalStoragePermissionState = rememberPermissionState(
        Manifest.permission.READ_EXTERNAL_STORAGE
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
                            if (externalStoragePermissionState.status.isGranted) {
                                val i = Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                                someActivityResultLauncher.launch(i)
                            } else {
                                externalStoragePermissionState.launchPermissionRequest()
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