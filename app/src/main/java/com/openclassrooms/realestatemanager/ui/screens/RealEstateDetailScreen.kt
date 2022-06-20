package com.openclassrooms.realestatemanager.ui.screens

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme
import java.io.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEstateDetailScreen() {

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* do something */ },
                icon = { Icon(Icons.Filled.Edit, "Localized description") },
                text = { Text(text = "Edit") },
                modifier = Modifier.clip(RoundedCornerShape(15.dp))
            )
        },
        content = {

            ConstraintLayout(modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxHeight()) {

                val (centerAlignedTopAppBar,textType,textPrice,textArea,textNumberRoom,textDescription,textAddress,textPointsOfInterest,textStatus,textDateOfEntry,textDateOfSale,textRealEstateAgent) = createRefs()

                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Estate Manager")
                    },
                    navigationIcon = {
                        val activity = (LocalContext.current as? Activity)
                        IconButton(onClick = {
                            activity?.finish()
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



                Row(modifier = Modifier.constrainAs(textType) {
                    top.linkTo(centerAlignedTopAppBar.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }) {
                    Text(text = "")
                }

                Row(modifier = Modifier.constrainAs(textPrice) {
                    top.linkTo(textType.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }) {
                    Text(text = "")
                }

                Row(modifier = Modifier.constrainAs(textArea) {
                    top.linkTo(textPrice.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }) {
                    Icon(Icons.Filled.Place, contentDescription = "")
                    Text(text = "")
                }

                Row(modifier = Modifier.constrainAs(textNumberRoom) {
                    top.linkTo(textArea.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }) {
                    Icon(Icons.Filled.Place , contentDescription = "")
                    Text(text = "")
                }

                Row(modifier = Modifier.constrainAs(textDescription) {
                    top.linkTo(textNumberRoom.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }) {
                    Icon(Icons.Filled.Place, contentDescription = "")
                    Text(text = "")
                }

                Row(modifier = Modifier.constrainAs(textAddress) {
                    top.linkTo(textDescription.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "")
                    Text(text = "")
                }

                Row(modifier = Modifier.constrainAs(textPointsOfInterest) {
                    top.linkTo(textAddress.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "")
                    Text(text = "")
                }

                Row(modifier = Modifier.constrainAs(textStatus) {
                    top.linkTo(textPointsOfInterest.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "")
                    Text(text = "")
                }

                Row(modifier = Modifier.constrainAs(textDateOfEntry) {
                    top.linkTo(textStatus.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }) {
                    Text(text = "")
                }

                Row(modifier = Modifier.constrainAs(textDateOfSale) {
                    top.linkTo(textDateOfEntry.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }) {
                    Text(text = "")
                }

                Row(modifier = Modifier.constrainAs(textRealEstateAgent) {
                    top.linkTo(textDateOfSale.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "")
                    Text(text = "")
                }








            }
        }
    )





}

