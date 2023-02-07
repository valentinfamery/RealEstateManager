package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.*
import com.openclassrooms.realestatemanager.presentation.viewModels.EstateViewModel
import com.openclassrooms.realestatemanager.ui.components.TopBar
import com.openclassrooms.realestatemanager.utils.Screen
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import org.joda.time.LocalDate
import org.joda.time.chrono.ISOChronology

@SuppressLint("SimpleDateFormat", "UnusedMaterial3ScaffoldPaddingParameter",
    "MutableCollectionMutableState", "UnrememberedMutableState"
)
@ExperimentalMaterial3Api
@Composable
fun EditScreenRealEstate(
    estateViewModel: EstateViewModel,
    itemRealEstate: Estate?,
    navController: NavHostController
) {

    var openDialogAddPhotoWithText by remember { mutableStateOf(false) }

    var openDialogUpdatePhotoWithText by remember { mutableStateOf(false) }
    val listPhotos = estateViewModel.listPhotoEditScreenState.collectAsState()

    val context = LocalContext.current

    val idEditPhoto = remember{ mutableStateOf("")}
    val photoSourceEditPhoto = remember{ mutableStateOf("")}
    val textEditPhoto = remember{ mutableStateOf("")}




    DialogAddPhotoWithText(openDialogAddPhotoWithText = openDialogAddPhotoWithText, addPhotoWithText ={
        it.toAddLatter = true
        estateViewModel.addPhoto(it)
    }, closeDialogAddPhoto = {
        openDialogAddPhotoWithText = false
    })

    DialogUpdatePhotoWithText(
        openDialogUpdatePhotoWithText = openDialogUpdatePhotoWithText,
        closeDialogUpdatePhoto = {openDialogUpdatePhotoWithText = false},
        idEditPhoto,
        photoSourceEditPhoto,
        textEditPhoto,
        updatePhotoWithTextFirebase = { s: String, s1: String, s2: String ->
            estateViewModel.updateAttributePhotoSource(
                s,
                s1
            )
            estateViewModel.updateAttributePhotoText(
                s,
                s2
            )
            estateViewModel.updateAttributeToUpdate(
                s
            )
        }
    )

    Scaffold {
        if(itemRealEstate !=null) {


            var entryType by rememberSaveable { mutableStateOf(itemRealEstate.type.toString()) }
            var entryPrice by rememberSaveable { mutableStateOf(itemRealEstate.price.toString()) }
            var entryArea by rememberSaveable { mutableStateOf(itemRealEstate.area.toString()) }
            var entryNumberRoom by rememberSaveable { mutableStateOf(itemRealEstate.numberRoom.toString()) }
            var entryDescription by rememberSaveable { mutableStateOf(itemRealEstate.description.toString()) }
            var entryNumberAndStreet by rememberSaveable { mutableStateOf(itemRealEstate.numberAndStreet.toString()) }
            var entryNumberApartment by rememberSaveable { mutableStateOf(itemRealEstate.numberApartment.toString()) }
            var entryCity by rememberSaveable { mutableStateOf(itemRealEstate.city.toString()) }
            var entryRegion by rememberSaveable { mutableStateOf(itemRealEstate.region.toString()) }
            var entryPostalCode by rememberSaveable { mutableStateOf(itemRealEstate.postalCode.toString()) }
            var entryCountry by rememberSaveable { mutableStateOf(itemRealEstate.country.toString()) }
            var entryStatus by rememberSaveable { mutableStateOf(itemRealEstate.status.toString()) }

            val textDateOfEntry by rememberSaveable { mutableStateOf(itemRealEstate.dateOfEntry.toString()) }
            var textDateOfSale by rememberSaveable { mutableStateOf("00/00/0000") }

            val checkedStateHospital = remember { mutableStateOf(itemRealEstate.hospitalsNear) }
            val checkedStateSchool = remember { mutableStateOf(itemRealEstate.schoolsNear) }
            val checkedStateShops = remember { mutableStateOf(itemRealEstate.shopsNear) }
            val checkedStateParks = remember { mutableStateOf(itemRealEstate.parksNear) }



            val listType = listOf("Apartment", "Loft", "Mansion", "House")
            val listStatus = listOf("For Sale", "Sold")


            ConstraintLayout(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxHeight()
            ) {

                val (fieldType, fieldPrice, fieldArea, fieldNumberRoom, fieldDescription, fieldStatus, rowDateSaleButtonAndText, topAppBar, confirmAddButton, lazyColumnPhoto, buttonAddPhoto, dropdownMenu) = createRefs()

                val (rowHospital, rowSchool, rowShops, rowParks, dropdownMenuStatus, fieldNumberAndStreet, fieldNumberApartment, fieldCity, fieldRegion, fieldPostalCode, fieldCountry) = createRefs()

                TopBar(
                    title = Screen.EditScreen.title,
                    backNavigate = true,
                    drawerButton = false,
                    filterScreen = false,
                    navigateToFilterScreen = {},
                    navigateToBack = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.constrainAs(topAppBar) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    },
                    openDrawer = {}
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
                    label = { Text(stringResource(R.string.editInfoType)) },
                    modifier = Modifier
                        .constrainAs(fieldType) {
                            top.linkTo(topAppBar.bottom, margin = 10.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        }
                        .onGloballyPositioned { coordinates ->
                            // This value is used to assign to
                            // the DropDown the same width
                            mTextFieldSize = coordinates.size.toSize()
                        }
                        .fillMaxWidth(0.8f),
                    trailingIcon = {
                        Icon(icon, "",
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
                    label = { Text(stringResource(R.string.editInfoPrice)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .constrainAs(fieldPrice) {
                            top.linkTo(fieldType.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        }
                        .fillMaxWidth(0.8f)
                )

                TextField(
                    value = entryArea,
                    onValueChange = { entryArea = it },
                    label = { Text(stringResource(R.string.editInfoArea)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .constrainAs(fieldArea) {
                            top.linkTo(fieldPrice.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        }
                        .fillMaxWidth(0.8f)
                )

                TextField(
                    value = entryNumberRoom,
                    onValueChange = { entryNumberRoom = it },
                    label = { Text(stringResource(R.string.editInfoNumberOfRooms)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .constrainAs(fieldNumberRoom) {
                            top.linkTo(fieldArea.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        }
                        .fillMaxWidth(0.8f)
                )

                TextField(
                    value = entryDescription,
                    onValueChange = { entryDescription = it },
                    label = { Text(stringResource(R.string.editInfoDescription)) },
                    singleLine = false,
                    modifier = Modifier
                        .constrainAs(fieldDescription) {
                            top.linkTo(fieldNumberRoom.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        }
                        .fillMaxWidth(0.8f)
                )



                TextField(
                    value = entryNumberAndStreet,
                    onValueChange = { entryNumberAndStreet = it },
                    label = { Text(stringResource(R.string.editInfoNumberAndStreet)) },
                    singleLine = true,
                    modifier = Modifier
                        .constrainAs(fieldNumberAndStreet) {
                            top.linkTo(fieldDescription.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        }
                        .fillMaxWidth(0.8f)
                )

                TextField(
                    value = entryNumberApartment,
                    onValueChange = { entryNumberApartment = it },
                    label = { Text(stringResource(R.string.editInfoNumberApartment)) },
                    singleLine = true,
                    modifier = Modifier
                        .constrainAs(fieldNumberApartment) {
                            top.linkTo(fieldNumberAndStreet.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        }
                        .fillMaxWidth(0.8f)
                )

                TextField(
                    value = entryCity,
                    onValueChange = { entryCity = it },
                    label = { Text(stringResource(R.string.editInfoCity)) },
                    singleLine = true,
                    modifier = Modifier
                        .constrainAs(fieldCity) {
                            top.linkTo(fieldNumberApartment.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        }
                        .fillMaxWidth(0.8f)
                )

                TextField(
                    value = entryRegion,
                    onValueChange = { entryRegion = it },
                    label = { Text(stringResource(R.string.editInfoRegion)) },
                    singleLine = true,
                    modifier = Modifier
                        .constrainAs(fieldRegion) {
                            top.linkTo(fieldCity.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        }
                        .fillMaxWidth(0.8f)
                )

                TextField(
                    value = entryPostalCode,
                    onValueChange = { entryPostalCode = it },
                    label = { Text(stringResource(R.string.editInfoPostalCode)) },
                    singleLine = true,
                    modifier = Modifier
                        .constrainAs(fieldPostalCode) {
                            top.linkTo(fieldRegion.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        }
                        .fillMaxWidth(0.8f)
                )

                TextField(
                    value = entryCountry,
                    onValueChange = { entryCountry = it },
                    label = { Text(stringResource(R.string.editInfoCountry)) },
                    singleLine = true,
                    modifier = Modifier
                        .constrainAs(fieldCountry) {
                            top.linkTo(fieldPostalCode.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        }
                        .fillMaxWidth(0.8f)
                )


                FilterChip(
                    selected = checkedStateHospital.value,
                    onClick = { checkedStateHospital.value = !checkedStateHospital.value },
                    label = { Text(stringResource(R.string.editInfoHospital)) },
                    leadingIcon = if (checkedStateHospital.value) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                    modifier = Modifier
                        .constrainAs(rowHospital) {
                            top.linkTo(fieldCountry.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                            width = Dimension.percent(0.8f)
                            height = Dimension.wrapContent
                        },
                )

                FilterChip(
                    modifier = Modifier
                        .constrainAs(rowSchool) {
                            top.linkTo(rowHospital.bottom, margin = 5.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                            width = Dimension.percent(0.8f)
                            height = Dimension.wrapContent
                        },
                    selected = checkedStateSchool.value,
                    onClick = { checkedStateSchool.value = !checkedStateSchool.value },
                    label = { Text(stringResource(R.string.editInfoSchool)) },
                    leadingIcon = if (checkedStateSchool.value) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
                FilterChip(
                    modifier = Modifier
                        .constrainAs(rowShops) {
                            top.linkTo(rowSchool.bottom, margin = 5.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                            width = Dimension.percent(0.8f)
                            height = Dimension.wrapContent
                        },
                    selected = checkedStateShops.value,
                    onClick = { checkedStateShops.value = !checkedStateShops.value },
                    label = { Text(stringResource(R.string.editInfoShops)) },
                    leadingIcon = if (checkedStateShops.value) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
                FilterChip(
                    modifier = Modifier
                        .constrainAs(rowParks) {
                            top.linkTo(rowShops.bottom, margin = 5.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                            width = Dimension.percent(0.8f)
                            height = Dimension.wrapContent
                        },
                    selected = checkedStateParks.value,
                    onClick = { checkedStateParks.value = !checkedStateParks.value },
                    label = { Text(stringResource(R.string.editInfoParks)) },
                    leadingIcon = if (checkedStateParks.value) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )







                TextField(
                    value = entryStatus,
                    onValueChange = { entryStatus = it },
                    label = { Text(stringResource(R.string.editInfoStatus)) },
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
                        }
                        .fillMaxWidth(0.8f),
                    trailingIcon = {
                        Icon(iconStatus, "",
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
                textDateOfSale = today.toString()


                val dateOfSalePickerDialog = DatePickerDialog(
                    context,
                    { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->

                        textDateOfSale = LocalDate(year,month,dayOfMonth).toString()


                    }, today.year, today.monthOfYear-1, today.dayOfMonth
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
                            Text(stringResource(R.string.editInfoSetDateOfSale))
                        }
                    }

                }






                FlowRow(modifier = Modifier.constrainAs(lazyColumnPhoto) {
                    top.linkTo(fieldStatus.bottom, margin = 75.dp)
                    start.linkTo(parent.start, margin = 25.dp)
                    end.linkTo(parent.end, margin = 25.dp)
                }) {

                    listPhotos.value.filter { !it.toDeleteLatter }.forEach { photoWithText->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth(0.50f)
                                    .padding(5.dp)
                            ) {
                                GlideImage(
                                    imageModel = { photoWithText.photoSource },
                                    modifier = Modifier
                                        .clickable {
                                            idEditPhoto.value = photoWithText.id
                                            photoSourceEditPhoto.value = photoWithText.photoSource
                                            textEditPhoto.value = photoWithText.text
                                            openDialogUpdatePhotoWithText = true
                                        }
                                        .aspectRatio(0.9f)
                                        .clip(RoundedCornerShape(15.dp)),
                                    imageOptions = ImageOptions(contentScale = ContentScale.FillBounds)
                                )
                                Text(text = photoWithText.text)
                                Button(onClick = {
                                    estateViewModel.updatePhotoWithTextInListEditScreenToDeleteLatterToTrue(photoWithText.id)
                                    val gson = Gson()
                                    Log.e("1", gson.toJson(listPhotos))
                                    Log.e("2", gson.toJson(photoWithText))


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
                    Text(stringResource(R.string.editButtonAddPhoto))
                }
                Button(
                    onClick = {

                            estateViewModel.updateRealEstate(
                                itemRealEstate.id,
                                entryType ,
                                entryPrice,
                                entryArea,
                            entryNumberRoom ,
                            entryDescription ,
                            entryNumberAndStreet,
                            entryNumberApartment ,
                            entryCity ,
                            entryRegion ,
                            entryPostalCode ,
                            entryCountry ,
                            entryStatus ,
                            textDateOfEntry ,
                            textDateOfSale ,
                                itemRealEstate.realEstateAgent,
                                itemRealEstate.lat,
                                itemRealEstate.lng,
                            checkedStateHospital ,
                            checkedStateSchool ,
                            checkedStateShops ,
                            checkedStateParks,
                                listPhotos.value.toMutableList(),
                            itemRealEstate
                            )



                    },
                    modifier = Modifier.constrainAs(confirmAddButton) {
                        top.linkTo(buttonAddPhoto.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    },
                ) {
                    Text(stringResource(R.string.buttonConfirmEdit))
                }

            }
        }
    }
    when(estateViewModel.updateRealEstateResponse){
        is Response.Success->{
            LaunchedEffect(estateViewModel.updateRealEstateResponse){
                if(estateViewModel.updateRealEstateResponse is Response.Success){
                    navController.popBackStack()
                    estateViewModel.updateRealEstateResponse = Response.Empty
                }
            }
        }
        is Response.Failure -> {
            val e = (estateViewModel.updateRealEstateResponse as Response.Failure).e.message
            Toast.makeText(context,e,Toast.LENGTH_LONG).show()
        }
        else -> {}
    }
}