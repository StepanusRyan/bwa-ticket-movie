package com.stepanusryan.bwamovie.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film (
    //lateinit var desc:String
    var desc:String? = "",
    var director:String? = "",
    var genre:String? = "",
    var judul:String? = "",
    var poster:String? = "",
    var rating:String? = ""
):Parcelable