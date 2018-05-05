package com.kleshchin.danil.ordermaker.models

import android.os.Parcel
import android.os.Parcelable

data class Meal(var categoryId : Int, var name: String, var iconUrl: String, var price: Int,
                var isChecked: Boolean, var info: String) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readString())

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeInt(this.categoryId)
        p0?.writeString(this.name)
        p0?.writeString(this.iconUrl)
        p0?.writeInt(this.price)
        p0?.writeByte((if (this.isChecked) 1 else 0).toByte())
        p0?.writeString(this.info)
    }

    companion object CREATOR : Parcelable.Creator<Meal> {
        override fun createFromParcel(parcel: Parcel): Meal {
            return Meal(parcel)
        }

        override fun newArray(size: Int): Array<Meal?> {
            return arrayOfNulls(size)
        }
    }
}
