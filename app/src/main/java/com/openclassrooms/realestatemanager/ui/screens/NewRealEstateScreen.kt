package com.openclassrooms.realestatemanager.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.google.accompanist.flowlayout.FlowRow
import com.openclassrooms.realestatemanager.domain.models.PhotoWithText
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.domain.models.User
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.InternalCoroutinesApi
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("LongLogTag", "UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun NewRealEstateScreen(
    realEstateViewModel: RealEstateViewModel,
    userViewModel: UserViewModel
) {

    val openDialog = remember { mutableStateOf(false) }
    var titlePhoto by remember { mutableStateOf("") }

    val listPhotos = remember { mutableStateListOf<PhotoWithText>() }
    val activity = LocalContext.current as Activity
    val context = LocalContext.current

    val userDataState = userViewModel.userData()

    var photoSelect by rememberSaveable { mutableStateOf<Uri>(Uri.EMPTY) }

    val someActivityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (it.resultCode == RESULT_OK) {
                val data: Intent? = it.data
                val uriImageSelected: Uri? = data?.data
                if (uriImageSelected != null) {


                    photoSelect = uriImageSelected
                    Log.e("photoSelectResultLauncher",photoSelect.toString())
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

    if (openDialog.value) {
        Dialog(
            onDismissRequest = { },
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.defaultMinSize(100.dp, 150.dp),
            ) {

                ConstraintLayout() {
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
                            .wrapContentSize()
                            .constrainAs(imageSelect) {
                                top.linkTo(titleEntry.bottom, margin = 15.dp)
                                start.linkTo(parent.start, margin = 30.dp)
                                end.linkTo(parent.end, margin = 30.dp)
                            }

                    ) {
                        if (photoSelect != Uri.EMPTY) {
                            GlideImage(
                                imageModel = photoSelect,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(150.dp)
                            )
                        } else {
                            GlideImage(
                                imageModel = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(150.dp)
                            )
                        }

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
                                shouldShowRequestPermissionRationale(activity, "") -> {}
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
                            openDialog.value = false
                            photoSelect = Uri.EMPTY
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
                            val photoWithText = PhotoWithText(photoSelect, titlePhoto)

                            listPhotos.add(photoWithText)
                            openDialog.value = false
                            /*TODO*/
                        }
                    ) {
                        Text(text = "Confirm")
                    }


                }
            }


        }
    }

    Scaffold {
        var entryType by rememberSaveable { mutableStateOf("") }
        var entryPrice by rememberSaveable { mutableStateOf("") }
        var entryArea by rememberSaveable { mutableStateOf("") }
        var entryNumberRoom by rememberSaveable { mutableStateOf("") }
        var entryDescription by rememberSaveable { mutableStateOf("") }
        var entryNumberAndStreet by rememberSaveable { mutableStateOf("") }
        var entryNumberApartement by rememberSaveable { mutableStateOf("") }
        var entryCity by rememberSaveable { mutableStateOf("") }
        var entryRegion by rememberSaveable { mutableStateOf("") }
        var entryPostalCode by rememberSaveable { mutableStateOf("") }
        var entryCountry by rememberSaveable { mutableStateOf("") }
        var entryStatus by rememberSaveable { mutableStateOf("") }

        var textDateOfEntry by rememberSaveable { mutableStateOf("00/00/0000") }
        var textDateOfSale by rememberSaveable { mutableStateOf("00/00/0000") }

        val checkedStateHopital = remember { mutableStateOf(false) }
        val checkedStateSchool = remember { mutableStateOf(false) }
        val checkedStateShops = remember { mutableStateOf(false) }
        val checkedStateParks = remember { mutableStateOf(false) }

        val listType = listOf("Appartement", "Loft", "Manoir", "Maison")
        val listStatus = listOf("For Sale", "Sold")

        ConstraintLayout(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxHeight()
        ) {

            val (fieldType, fieldPrice, fieldArea, fieldNumberRoom, fieldDescription, fieldAddress, fieldStatus, rowDateSaleButtonAndText, centerAlignedTopAppBar, confirmAddButton, lazyColumnPhoto, buttonAddPhoto, dropdownMenu) = createRefs()

            val (rowHopital, rowSchool, rowShops, rowParks ,dropdownMenuStatus,fieldNumberAndStreet,fieldNumberApartement,fieldCity,fieldRegion,fieldPostalCode,fieldCountry) = createRefs()
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "New Estate Manager")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        activity.finish()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "")
                    }
                },
                modifier = Modifier.constrainAs(centerAlignedTopAppBar) {
                    top.linkTo(parent.top, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }

            )

            var expanded by remember { mutableStateOf(false) }
            var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

            var expandedStatus by remember { mutableStateOf(false) }
            var mTextFieldSizeStatus by remember { mutableStateOf(Size.Zero) }

            val icon = if (expanded)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown

            val iconStatus = if (expandedStatus)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown

            TextField(
                value = entryType,
                onValueChange = { entryType = it },
                label = { Text("Type") },
                modifier = Modifier.constrainAs(fieldType) {
                    top.linkTo(centerAlignedTopAppBar.bottom, margin = 10.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                }.onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    mTextFieldSize = coordinates.size.toSize()
                },
                trailingIcon = {
                    Icon(icon, "contentDescription",
                        Modifier.clickable { expanded = !expanded })
                }

            )


            Box(
                modifier = Modifier.constrainAs(dropdownMenu) {
                    top.linkTo(fieldType.top, margin = 0.dp)
                    start.linkTo(fieldType.start, margin = 0.dp)

                },
            ) {

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
                ) {
                    listType.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                entryType = it
                                expanded = false
                                /* Handle edit! */
                            },
                        )
                    }
                }
            }

            TextField(
                value = entryPrice,
                onValueChange = { entryPrice = it },
                label = { Text("Price") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.constrainAs(fieldPrice) {
                    top.linkTo(fieldType.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                }
            )

            TextField(
                value = entryArea ,
                onValueChange = { entryArea = it },
                label = { Text("Area") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.constrainAs(fieldArea) {
                    top.linkTo(fieldPrice.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                }
            )

            TextField(
                value = entryNumberRoom,
                onValueChange = { entryNumberRoom = it },
                label = { Text("Number of rooms") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.constrainAs(fieldNumberRoom) {
                    top.linkTo(fieldArea.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                }
            )

            TextField(
                value = entryDescription,
                onValueChange = { entryDescription = it },
                label = { Text("Description") },
                singleLine = true,
                modifier = Modifier.constrainAs(fieldDescription) {
                    top.linkTo(fieldNumberRoom.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                }
            )



            TextField(
                value = entryNumberAndStreet,
                onValueChange = { entryNumberAndStreet = it },
                label = { Text("NumberAndStreet") },
                singleLine = true,
                modifier = Modifier.constrainAs(fieldNumberAndStreet) {
                    top.linkTo(fieldDescription.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                }
            )

            TextField(
                value = entryNumberApartement,
                onValueChange = { entryNumberApartement = it },
                label = { Text("NumberApartement") },
                singleLine = true,
                modifier = Modifier.constrainAs(fieldNumberApartement) {
                    top.linkTo(fieldNumberAndStreet.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                }
            )

            TextField(
                value = entryCity,
                onValueChange = { entryCity = it },
                label = { Text("City") },
                singleLine = true,
                modifier = Modifier.constrainAs(fieldCity) {
                    top.linkTo(fieldNumberApartement.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                }
            )

            TextField(
                value = entryRegion,
                onValueChange = { entryRegion = it },
                label = { Text("Region") },
                singleLine = true,
                modifier = Modifier.constrainAs(fieldRegion) {
                    top.linkTo(fieldCity.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                }
            )

            TextField(
                value = entryPostalCode,
                onValueChange = { entryPostalCode = it },
                label = { Text("Postal Code") },
                singleLine = true,
                modifier = Modifier.constrainAs(fieldPostalCode) {
                    top.linkTo(fieldRegion.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                }
            )

            TextField(
                value = entryCountry,
                onValueChange = { entryCountry = it },
                label = { Text("Country") },
                singleLine = true,
                modifier = Modifier.constrainAs(fieldCountry) {
                    top.linkTo(fieldPostalCode.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                }
            )


            Row(
                modifier = Modifier
                    .constrainAs(rowHopital) {
                        top.linkTo(fieldCountry.bottom, margin = 5.dp)
                        start.linkTo(parent.start, margin = 50.dp)
                        end.linkTo(parent.end, margin = 50.dp)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,

                ) {

                Checkbox(
                    checked = checkedStateHopital.value,
                    onCheckedChange = { checkedStateHopital.value = it },
                )
                Text(text = "Near Hopital")
            }

            Row(
                modifier = Modifier
                    .constrainAs(rowSchool) {
                        top.linkTo(rowHopital.bottom, margin = 5.dp)
                        start.linkTo(parent.start, margin = 50.dp)
                        end.linkTo(parent.end, margin = 50.dp)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,

                ) {
                Checkbox(
                    checked = checkedStateSchool.value,
                    onCheckedChange = { checkedStateSchool.value = it }
                )
                Text(text = "Near School")
            }

            Row(
                modifier = Modifier
                    .constrainAs(rowShops) {
                        top.linkTo(rowSchool.bottom, margin = 5.dp)
                        start.linkTo(parent.start, margin = 50.dp)
                        end.linkTo(parent.end, margin = 50.dp)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Checkbox(
                    checked = checkedStateShops.value,
                    onCheckedChange = { checkedStateShops.value = it }
                )
                Text(text = "Near Shops")
            }

            Row(
                modifier = Modifier
                    .constrainAs(rowParks) {
                        top.linkTo(rowShops.bottom, margin = 5.dp)
                        start.linkTo(parent.start, margin = 50.dp)
                        end.linkTo(parent.end, margin = 50.dp)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Checkbox(
                    checked = checkedStateParks.value,
                    onCheckedChange = { checkedStateParks.value = it }
                )
                Text(text = "Near Parks")
            }







            TextField(
                value = entryStatus,
                onValueChange = { entryStatus = it },
                label = { Text("Status") },
                singleLine = true,
                modifier = Modifier.constrainAs(fieldStatus) {
                    top.linkTo(rowParks.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                }.onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    mTextFieldSizeStatus = coordinates.size.toSize()
                },
                trailingIcon = {
                    Icon(iconStatus, "contentDescription",
                        Modifier.clickable { expandedStatus = !expandedStatus })
                }
            )

            Box(
                modifier = Modifier.constrainAs(dropdownMenuStatus) {
                    top.linkTo(fieldStatus.top, margin = 0.dp)
                    start.linkTo(fieldStatus.start, margin = 0.dp)

                },
            ) {

                DropdownMenu(
                    expanded = expandedStatus,
                    onDismissRequest = { expandedStatus = false },
                    modifier = Modifier.width(with(LocalDensity.current) { mTextFieldSizeStatus.width.toDp() })
                ) {
                    listStatus.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                entryStatus = it
                                expandedStatus = false
                                /* Handle edit! */
                            },
                        )
                    }
                }
            }


            val year: Int
            val month: Int
            val day: Int

            val calendar = Calendar.getInstance()
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
            calendar.time = Date()

            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            textDateOfEntry = dateFormat.format(calendar.time)



            val dateOfSalePickerDialog = DatePickerDialog(
                context,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->

                    textDateOfSale = "$dayOfMonth/$month/$year"


                }, year, month, day
            )

            if (entryStatus == "Sold") {

                Row(modifier = Modifier.constrainAs(rowDateSaleButtonAndText) {
                    top.linkTo(fieldStatus.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                }) {
                    Text(textDateOfSale)
                    Button(
                        onClick = {
                            dateOfSalePickerDialog.show()
                        },
                    )
                    {
                        Text("Set Date Of Sale")
                    }
                }

            }





            FlowRow(modifier = Modifier.constrainAs(lazyColumnPhoto) {
                top.linkTo(fieldStatus.bottom, margin = 75.dp)
                start.linkTo(parent.start, margin = 25.dp)
                end.linkTo(parent.end, margin = 25.dp)
            }) {
                repeat(listPhotos.size) {
                    Box(modifier = Modifier.size(184.dp)) {

                        Column() {
                            ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                                val (image, text) = createRefs()

                                GlideImage(
                                    imageModel = listPhotos[it].photoUri,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.constrainAs(image) {
                                        top.linkTo(parent.top, margin = 0.dp)
                                        start.linkTo(parent.start, margin = 0.dp)
                                        end.linkTo(parent.end, margin = 0.dp)
                                    }
                                )
                                Text(
                                    text = listPhotos[it].text,
                                    modifier = Modifier.constrainAs(text) {
                                        top.linkTo(image.bottom, margin = 0.dp)
                                        start.linkTo(parent.start, margin = 0.dp)
                                        end.linkTo(parent.end, margin = 0.dp)
                                    })

                            }


                        }
                    }
                }
            }

            Button(
                onClick = {

                    openDialog.value = true
                },
                modifier = Modifier.constrainAs(buttonAddPhoto) {
                    top.linkTo(lazyColumnPhoto.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
            ) {
                Text("Add Photo")
            }

            Button(
                onClick = {

                    try {
                        when(userViewModel.userDataResponse){
                            is Response.Success ->{
                                (userViewModel.userDataResponse as Response.Success<User?>).data.let { response ->

                                    listPhotos.size >= 1

                                    Log.e("listphotosItem1Uri", listPhotos[0].photoUri.toString())

                                    realEstateViewModel.createRealEstate(
                                        entryType,
                                        entryPrice,
                                        entryArea,
                                        entryNumberRoom,
                                        entryDescription,
                                        entryNumberAndStreet,
                                        entryNumberApartement,
                                        entryCity,
                                        entryRegion,
                                        entryPostalCode,
                                        entryCountry,
                                        entryStatus,
                                        listPhotos,
                                        textDateOfEntry,
                                        textDateOfSale,
                                        response!!.username.toString(),
                                        checkedStateHopital.value,
                                        checkedStateSchool.value,
                                        checkedStateShops.value,
                                        checkedStateParks.value
                                    )

                                    activity.finish()


                                }


                            }
                            else ->{}
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Please enter a valid number for price , area and number of room",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                },
                modifier = Modifier.constrainAs(confirmAddButton) {
                    top.linkTo(buttonAddPhoto.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                },
            ) {
                Text("Confirm")
            }

        }

    }

}




























