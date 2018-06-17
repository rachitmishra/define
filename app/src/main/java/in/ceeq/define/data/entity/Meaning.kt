package `in`.ceeq.define.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Meaning(@SerializedName("language")
                   val language: String,
                   @SerializedName("text")
                   val text: String?): Parcelable
