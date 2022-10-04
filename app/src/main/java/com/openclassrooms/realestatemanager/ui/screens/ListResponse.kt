package com.openclassrooms.realestatemanager.ui.screens

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.openclassrooms.realestatemanager.utils.WindowSize
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(InternalCoroutinesApi::class)
@Composable
fun ListResponse(
    items: Response<List<RealEstateDatabase>>,
    filterState: Boolean,
    listFilter: MutableList<RealEstateDatabase>,
    navControllerDrawer: NavController,
    navControllerTwoPane: NavHostController,
    windowSize: WindowSize,
    realEstateViewModel: RealEstateViewModel = hiltViewModel()
) {

    LazyColumn {


        if (!filterState) {



            when(items){
                is Response.Success ->{
                    (items).data.let { response ->

                        Log.e("items", "listScreen")
                        items(response) { item ->
                            RowList(
                                item,
                                realEstateViewModel,
                                navControllerDrawer,
                                windowSize,
                                navControllerTwoPane
                            )
                        }
                    }
                }
                else -> {}
            }

        } else {

            items(listFilter) { item ->
                RowList(
                    item,
                    realEstateViewModel,
                    navControllerDrawer,
                    windowSize,
                    navControllerTwoPane
                )
            }
        }
    }

}