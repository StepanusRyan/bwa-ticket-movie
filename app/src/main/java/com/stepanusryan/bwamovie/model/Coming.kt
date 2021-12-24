package com.stepanusryan.bwamovie.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coming (
    var desc:String? = "",
    var director:String? = "",
    var genre:String? = "",
    var judul:String? = "",
    var poster:String? = ""
):Parcelable