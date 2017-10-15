package `in`.ceeq.define.firebase

import com.google.gson.annotations.SerializedName

class MyFirebaseMessage {

    @SerializedName("title")
    var title = ""

    @SerializedName("message")
    var message = ""

    @SerializedName("image_url")
    var imageUrl: String = ""

    @SerializedName(value = "sub_text")
    var subText = ""

    @SerializedName("summary")
    var summary = ""

    @SerializedName("url")
    var url = ""

    val isUrlNotification: Boolean
        get() = url.isEmpty()
}
