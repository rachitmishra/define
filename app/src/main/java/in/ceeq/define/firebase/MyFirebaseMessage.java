package in.ceeq.define.firebase;

import com.google.gson.annotations.SerializedName;

import in.ceeq.define.utils.Utils;

public class MyFirebaseMessage {

    @SerializedName("title")
    public String title = "";

    @SerializedName("message")
    public String message = "";

    @SerializedName("image_url")
    public String imageUrl = null;

    @SerializedName(value="sub_text")
    public String subText = "";

    @SerializedName("summary")
    public String summary = "";

    @SerializedName("url")
    public String url = "";

    public boolean isUrlNotification() {
        return !Utils.isEmptyString(url);
    }
}
