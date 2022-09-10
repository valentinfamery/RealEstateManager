package com.openclassrooms.realestatemanager.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterResult(var type : String ,
                        var city : String ,
                        var minSurface : String ,
                        var maxSurface : String ,
                        var minPrice : String,
                        var maxPrice : String ,
                        var onTheMarketLessALastWeek : Boolean,
                        var soldOn3LastMonth : Boolean,
                        var min3photos : Boolean,
                        var schools : Boolean,
                        var shops : Boolean,
) : Parcelable
