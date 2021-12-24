package com.stepanusryan.bwamovie.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tiket (
    var genre:String?= "",
    var judul:String?= "",
    var poster:String?="",
//    var kursi: Kursi
):Parcelable