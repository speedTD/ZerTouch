package com.assistive.assistivetouch.model

import android.os.Parcel
import android.os.Parcelable

class Eventmodel() :Parcelable{
    var idevent:Int?=null
    var iconevent: Int ?=null
    var textevent: String?=null

    constructor(parcel: Parcel) : this() {
        idevent = parcel.readValue(Int::class.java.classLoader) as? Int
        iconevent = parcel.readValue(ByteArray::class.java.classLoader) as? Int
        textevent = parcel.readString()
    }

    constructor(idevent: Int?, iconevent: Int, textevent: String?) : this() {
        this.idevent = idevent
        this.iconevent = iconevent
        this.textevent = textevent
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(idevent)
        parcel.writeValue(iconevent)
        parcel.writeString(textevent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Eventmodel> {
        override fun createFromParcel(parcel: Parcel): Eventmodel {
            return Eventmodel(parcel)
        }

        override fun newArray(size: Int): Array<Eventmodel?> {
            return arrayOfNulls(size)
        }
    }

}