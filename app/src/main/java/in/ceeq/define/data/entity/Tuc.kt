package `in`.ceeq.define.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tuc(@SerializedName("phrase")
               val phrase: Meaning?,
               @SerializedName("meanings")
               val meanings: ArrayList<Meaning>?) : Parcelable
