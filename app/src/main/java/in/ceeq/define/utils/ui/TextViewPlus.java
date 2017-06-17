package in.ceeq.define.utils.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import in.ceeq.define.R;
import in.ceeq.define.utils.FontUtils;

public class TextViewPlus extends AppCompatTextView {

    private FontUtils mFontUtils;
    private CharSequence mOriginalText = "";
    private float mLetterSpacing = FontUtils.LETTER_SPACING_NORMAL;

    public TextViewPlus(Context context) {
        super(context);
    }

    public TextViewPlus(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public TextViewPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mFontUtils = new FontUtils();
        parseAttributes(context, attrs);
    }

    @Override
    public float getLetterSpacing() {
        return mLetterSpacing;
    }

    @Override
    public void setLetterSpacing(float letterSpacing) {
        mLetterSpacing = letterSpacing;
        applyLetterSpacing();
    }

    /**
     * Parse the attributes.
     *
     * @param context The Context the widget is running in, through which it can access the current theme, resources,
     *                etc.
     * @param attrs   The attributes of the XML tag that is inflating the widget.
     */
    public final void parseAttributes(Context context, AttributeSet attrs) {
        // Typeface.createFromAsset doesn't work in the layout editor, so skipping.
        if (isInEditMode()) {
            return;
        }

        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
        int typefaceValue = values.getInt(R.styleable.TextViewPlus_fontTypeface, FontUtils.TYPEFACE_REGULAR);
        mLetterSpacing = values.getFloat(R.styleable.TextViewPlus_textSpacing, FontUtils.LETTER_SPACING_NORMAL);
        values.recycle();

        mOriginalText = getText();
        setTypeface(mFontUtils.obtainTypeface(context, typefaceValue));
    }


    private void applyLetterSpacing() {
        if (mLetterSpacing == FontUtils.LETTER_SPACING_NORMAL) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mOriginalText.length(); i++) {
            builder.append(mOriginalText.charAt(i));
            if (i + 1 < mOriginalText.length()) {
                builder.append("\u00A0");
            }
        }
        SpannableString finalText = new SpannableString(builder.toString());
        if (builder.toString().length() > 1) {
            for (int i = 1; i < builder.toString().length(); i += 2) {
                finalText.setSpan(new ScaleXSpan((mLetterSpacing + 1) / 10), i, i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        setText(finalText, TextView.BufferType.SPANNABLE);
    }

}
