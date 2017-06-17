package in.ceeq.define.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.Spanned;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {

   public static boolean isNetConnected(Context context) {
        if (context != null) {
            final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return !(networkInfo == null || !networkInfo.isConnectedOrConnecting());
        } else {
            return false;
        }
    }

    public static boolean isEmptyString(String value) {
        return !(value != null && !value.trim().isEmpty());
    }

    public static boolean isEmptyMap(HashMap map) {
        return (map == null || map.size() <= 0);
    }

    public static boolean isEmptyList(ArrayList arrayList) {
        return (arrayList == null || arrayList.size() <= 0);
    }

    public static boolean isEmptyList(List arrayList) {
        return (arrayList == null || arrayList.size() <= 0);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

}
