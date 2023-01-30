package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.flowlayout.FlowRow
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.notifications.NotificationHelper
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel
import com.openclassrooms.realestatemanager.ui.NewRealEstateActivity
import com.openclassrooms.realestatemanager.ui.components.TopBar
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import org.joda.time.LocalDate
import org.joda.time.chrono.ISOChronology


@SuppressLint("LongLogTag", "UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun NewRealEstateScreen(
    realEstateViewModel: RealEstateViewModel,
    userViewModel: UserViewModel
) {

    var openDialogAddPhotoWithText by remember { mutableStateOf(false) }

    val user by userViewModel.userData.collectAsState()

    val listPhotos = realEstateViewModel.listPhotoNewScreenState.collectAsState()
    val activity = LocalContext.current as Activity
    val context = LocalContext.current

    var openDialogUpdatePhotoWithText by remember { mutableStateOf(false) }
    val idEditPhoto = remember{ mutableStateOf("")}
    val photoSourceEditPhoto = remember{ mutableStateOf("")}
    val textEditPhoto = remember{ mutableStateOf("")}

    DialogAddPhotoWithText(openDialogAddPhotoWithText, addPhotoWithText = {
        realEstateViewModel.addListPhotoNewScreenState(it)
    }, closeDialogAddPhoto = {openDialogAddPhotoWithText = false})

    DialogUpdatePhotoWithText(
        openDialogUpdatePhotoWithText = openDialogUpdatePhotoWithText,
        closeDialogUpdatePhoto = {openDialogUpdatePhotoWithText = false},
        idEditPhoto,
        photoSourceEditPhoto,
        textEditPhoto,
        updatePhotoWithTextFirebase = { s: String, s1: String, s2: String ->
            realEstateViewModel.updatePhotoSourceElementNewScreen(s,s1)
            realEstateViewModel.updatePhotoTextElementNewScreen(s,s2)
        }
    )



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

        var textDateOfEntry by rememberSaveable { mutableStateOf("") }
        var textDateOfSale by rememberSaveable { mutableStateOf("") }

        val checkedStateHopital = remember { mutableStateOf(false) }
        val checkedStateSchool = remember { mutableStateOf(false) }
        val checkedStateShops = remember { mutableStateOf(false) }
        val checkedStateParks = remember { mutableStateOf(false) }

        val listType = listOf("Apartment", "Loft", "Mansion", "House")
        val listStatus = listOf("For Sale", "Sold")

        ConstraintLayout(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxHeight()
        ) {

            val (fieldType, fieldPrice, fieldArea, fieldNumberRoom, fieldDescription, fieldAddress, fieldStatus, rowDateSaleButtonAndText, centerAlignedTopAppBar, confirmAddButton, lazyColumnPhoto, buttonAddPhoto, dropdownMenu) = createRefs()

            val (rowHopital, rowSchool, rowShops, rowParks ,dropdownMenuStatus,fieldNumberAndStreet,fieldNumberApartement,fieldCity,fieldRegion,fieldPostalCode,fieldCountry) = createRefs()

            TopBar(
                title = stringResource(R.string.TitleNewEstateScreen),
                backNavigate = true,
                filterScreen = false,
                drawerButton = false,
                navigateToFilterScreen = { /*TODO*/ },
                navigateToBack = {activity.finish()},
                openDrawer = { /*TODO*/ },
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
                modifier = Modifier
                    .constrainAs(fieldType) {
                        top.linkTo(centerAlignedTopAppBar.bottom, margin = 10.dp)
                        start.linkTo(parent.start, margin = 50.dp)
                        end.linkTo(parent.end, margin = 50.dp)
                    }
                    .onGloballyPositioned { coordinates ->
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
                modifier = Modifier
                    .constrainAs(fieldStatus) {
                        top.linkTo(rowParks.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 50.dp)
                        end.linkTo(parent.end, margin = 50.dp)
                    }
                    .onGloballyPositioned { coordinates ->
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







            val iso = ISOChronology.getInstance()
            val today = LocalDate(iso)
            textDateOfEntry = today.toString()


            val dateOfSalePickerDialog = DatePickerDialog(
                context,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->

                    textDateOfSale = today.toString()


                }, today.year, today.monthOfYear-1, today.dayOfMonth
            )



                Column(modifier = Modifier.constrainAs(rowDateSaleButtonAndText) {
                    top.linkTo(fieldStatus.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                }) {
                    Text(textDateOfEntry)
                    if (entryStatus == "Sold") {
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
                listPhotos.value?.forEach {photo->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                            .fillMaxWidth(0.50f)
                            .padding(5.dp)
                    ) {
                        GlideImage(
                            imageModel = { photo.photoSource },
                            modifier = Modifier
                                .aspectRatio(0.9f)
                                .clip(RoundedCornerShape(15.dp))
                                .clickable {
                                    idEditPhoto.value = photo.id
                                    photoSourceEditPhoto.value = photo.photoSource
                                    textEditPhoto.value = photo.text
                                    openDialogUpdatePhotoWithText = true
                                },
                            imageOptions = ImageOptions(contentScale = ContentScale.FillBounds)
                        )
                        Text(text = photo.text)
                        Button(onClick = {
                            realEstateViewModel.deleteListPhotoNewScreenState(photo)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {

                    openDialogAddPhotoWithText = true
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



                                    listPhotos.value?.size!! >= 1


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
                                        listPhotos.value!!,
                                        textDateOfEntry,
                                        textDateOfSale,
                                        user?.username.toString(),
                                        checkedStateHopital.value,
                                        checkedStateSchool.value,
                                        checkedStateShops.value,
                                        checkedStateParks.value
                                    )









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

    when(realEstateViewModel.createRealEstateResponse){
        is Response.Success ->{
            Toast.makeText(
                context,
                "Ajout reussi",
                Toast.LENGTH_SHORT
            ).show()

            NotificationHelper.sendSimpleNotification(
                context = context,
                title = "Real Estate Manager",
                message = "Succefully added new Estate",
                intent = Intent(context, NewRealEstateActivity::class.java),
                reqCode = 10001
            )

            activity.finish()
        }
        else -> {}
    }

}




























