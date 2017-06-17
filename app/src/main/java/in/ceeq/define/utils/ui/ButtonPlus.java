package in.ceeq.define.utils.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import in.ceeq.define.R;
import in.ceeq.define.utils.FontUtils;

public class ButtonPlus extends AppCompatButton {

    public ButtonPlus(Context context) {
        this(context, null);
    }

    public ButtonPlus(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public ButtonPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttributes(context, attrs);
    }

    /**
     * Parse the attributes.
     *
     * @param context The Context the widget is running in, through which it can access the current theme, resources,
     *                etc.
     * @param attrs   The attributes of the XML tag that is inflating the widget.
     */
    private void parseAttributes(Context context, AttributeSet attrs) {
        // Typeface.createFromAsset doesn't work in the layout editor, so skipping.
        if (isInEditMode()) {
            return;
        }

        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
        int typefaceValue = values.getInt(R.styleable.TextViewPlus_fontTypeface, FontUtils.TYPEFACE_BOLD);
        values.recycle();

        setTypeface(obtainTypeface(context, typefaceValue));
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
    private Typeface obtainTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface = FontUtils.getTypefaces().get(typefaceValue);
        if (typeface == null) {
            typeface = FontUtils.createTypeface(context, typefaceValue);
            FontUtils.getTypefaces().put(typefaceValue, typeface);
        }
        return typeface;
    }
}
