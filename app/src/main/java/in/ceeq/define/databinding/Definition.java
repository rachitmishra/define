package in.ceeq.define.databinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.ceeq.define.BR;

public class Definition extends BaseObservable implements Parcelable {

    @SerializedName("result")
    public String result;

    @SerializedName("tuc")
    public ArrayList<Tuc> tucs;

    @SerializedName("phrase")
    public String phrase;

    @SerializedName("from")
    public String from;

    @SerializedName("dest")
    public String dest;

    public String definition;

    public String definition2;

    public String suggestedPhrase;

    @Bindable
    public String getDefinition2() {
        return definition2;
    }

    public void setDefinition2(String definition2) {
        this.definition2 = definition2;
        notifyPropertyChanged(BR.definition2);
    }

    @Bindable
    public String getSuggestedPhrase() {
        return suggestedPhrase;
    }

    public void setSuggestedPhrase(String suggestedPhrase) {
        this.suggestedPhrase = suggestedPhrase;
        notifyPropertyChanged(BR.suggestedPhrase);
    }

    @Bindable
    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
        notifyPropertyChanged(BR.phrase);
    }

    protected static class Tuc implements Parcelable {

        @SerializedName("phrase")
        public Meaning phrase;

        @SerializedName("meanings")
        public ArrayList<Meaning> meanings;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.phrase, flags);
            dest.writeTypedList(this.meanings);
        }

        public Tuc() {
        }

        protected Tuc(Parcel in) {
            this.phrase = in.readParcelable(Meaning.class.getClassLoader());
            this.meanings = in.createTypedArrayList(Meaning.CREATOR);
        }

        public static final Creator<Tuc> CREATOR = new Creator<Tuc>() {
            @Override
            public Tuc createFromParcel(Parcel source) {
                return new Tuc(source);
            }

            @Override
            public Tuc[] newArray(int size) {
                return new Tuc[size];
            }
        };
    }

    protected static class Meaning implements Parcelable {
        @SerializedName("language")
        String language;

        @SerializedName("text")
        String text;

        protected Meaning(Parcel in) {
            language = in.readString();
            text = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(language);
            dest.writeString(text);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Meaning> CREATOR = new Creator<Meaning>() {
            @Override
            public Meaning createFromParcel(Parcel in) {
                return new Meaning(in);
            }

            @Override
            public Meaning[] newArray(int size) {
                return new Meaning[size];
            }
        };
    }

    public Definition() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.result);
        dest.writeTypedList(this.tucs);
        dest.writeString(this.phrase);
    }

    protected Definition(Parcel in) {
        this.result = in.readString();
        this.tucs = in.createTypedArrayList(Tuc.CREATOR);
        this.phrase = in.readString();
    }

    public static final Creator<Definition> CREATOR = new Creator<Definition>() {
        @Override
        public Definition createFromParcel(Parcel source) {
            return new Definition(source);
        }

        @Override
        public Definition[] newArray(int size) {
            return new Definition[size];
        }
    };

    @Bindable
    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
        notifyPropertyChanged(BR.definition);
    }
}
