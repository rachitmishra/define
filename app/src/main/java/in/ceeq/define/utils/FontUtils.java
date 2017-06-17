package in.ceeq.define.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;

import java.io.IOException;
import java.util.Arrays;

public class FontUtils {

    public static final String TAG = "FontUtils";
    public static final int TYPEFACE_REGULAR = 1;
    public static final int TYPEFACE_BOLD = 2;
    public static final float LETTER_SPACING_NORMAL = 0;
    private static final String FONTS_DIRECTORY = "fonts";

    /**
     * List of created typefaces for later reused.
     */
    private static final SparseArray<Typeface> TYPEFACES = new SparseArray<>(4);
    private static final char DIRECTORY_SEPARATOR = '/';

    public static SparseArray<Typeface> getTypefaces() {
        return TYPEFACES;
    }

    /**
     * Create typeface from assets.
     *
     * @param context       The Context the widget is running in, through which it can
     *                      access the current theme, resources, etc.
     * @param typefaceValue values ​​for the "typeface" attribute
     * @return Roboto {@link Typeface}
     * @throws IllegalArgumentException if unknown `typeface` attribute value.
     */
    public static Typeface createTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface;
        switch (typefaceValue) {
            case TYPEFACE_REGULAR:
                String regularFontFile = "Regular.ttf"; // NON-NLS
                typeface = getTypeface(context, regularFontFile, Typeface.DEFAULT);
                break;
            case TYPEFACE_BOLD:
                String boldFontFile = "Bold.ttf"; // NON-NLS // NON-NLS
                typeface = getTypeface(context, boldFontFile, Typeface.DEFAULT_BOLD);
                break;
            default:
                throw new IllegalArgumentException("Unknown `typeface` attribute value " + typefaceValue);
        }

        return typeface;
    }

    private static Typeface getTypeface(Context context, String fontFile, Typeface defaultTypeface) {
        Typeface typeface = null;
        try {
            if (Arrays.asList(context.getResources().getAssets().list(FONTS_DIRECTORY))
                        .contains(fontFile)) {
                typeface = Typeface.createFromAsset(context.getAssets(),
                        FONTS_DIRECTORY + DIRECTORY_SEPARATOR + fontFile);
            }
        } catch (IOException ignored) {
            typeface = defaultTypeface;
        }

        if (typeface == null) {
            typeface = defaultTypeface;
        }

        return typeface;
    }

    /**
     * Obtain typeface.
     *
     * @param context       The Context the widget is running in, through which it can
     *                      access the current theme, resources, etc.
     * @param typefaceValue values ​​for the "typeface" attribute
     * @return Roboto {@link Typeface}
     * @throws IllegalArgumentException if unknown `typeface` attribute value.
     */

    public Typeface obtainTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface = getTypefaces().get(typefaceValue);
        if (typeface == null) {
            typeface = createTypeface(context, typefaceValue);
            getTypefaces().put(typefaceValue, typeface);
        }
        return typeface;
    }
}
