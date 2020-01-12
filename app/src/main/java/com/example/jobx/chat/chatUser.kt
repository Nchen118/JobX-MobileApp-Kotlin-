package com.example.jobx.chat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class chatUser(var id: String, var name: String):Parcelable {
    constructor() : this("", "")
}