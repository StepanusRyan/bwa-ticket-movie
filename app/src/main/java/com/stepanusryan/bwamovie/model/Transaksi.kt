package com.stepanusryan.bwamovie.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaksi (
    var id:String?= "",
    var action:String?= "",
    var date:String?="",
    var nominal:String?=""
):Parcelable