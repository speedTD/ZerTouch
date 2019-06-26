package com.assistive.assistivetouch.model

import android.os.Parcel
import android.os.Parcelable

class Appmodel(var nameapp: String? = null, var packagename: String? = null) :Parcelable {

    constructor(parcel: Parcel) : this() {
        packagename = parcel.readString()
        nameapp = parcel.readString()
    }



    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(packagename)
        parcel.writeString(nameapp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Appmodel> {
        override fun createFromParcel(parcel: Parcel): Appmodel {
            return Appmodel(parcel)
        }

        override fun newArray(size: Int): Array<Appmodel?> {
            return arrayOfNulls(size)
        }
    }


}