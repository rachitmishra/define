package in.ceeq.define.databinding;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.Bindable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import in.ceeq.define.BR;
import in.ceeq.define.R;
import in.ceeq.define.SettingsActivity;
import in.ceeq.define.network.DefinitionService;
import in.ceeq.define.network.RandomWordService;
import in.ceeq.define.utils.PreferenceUtils;
import in.ceeq.define.utils.Utils;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class DefinitionViewModel extends Definition {

    private static final int MAX_DEFINITIONS_SIZE = 10;
    private boolean progressViewVisible;
    private boolean noResults;
    private boolean forceUpdate;
    private static final String DEFAULT_PHRASE = "love";
    private static final String DEFAULT_DEST = "en";
    private PreferenceUtils mPreferenceUtils;
    private Resources mResources;

    public DefinitionViewModel(Context context) {
        mPreferenceUtils = PreferenceUtils.newInstance(context);
        mResources = context.getResources();
        setDefaults();
    }

    private void setDefaults() {
        dest = getDest();
        from = "en";
    }

    public void init(Context context) {
        mPreferenceUtils = PreferenceUtils.newInstance(context);
        mResources = context.getResources();
        setDefaults();
    }

    private DefinitionService getContactService() {
        String BASE_URL = "https://glosbe.com/gapi/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        return retrofit.create(DefinitionService.class);
    }

    private RandomWordService getRandomWordService() {
        String BASE_URL_RANDOM_WORD = "http://setgetgo.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_RANDOM_WORD)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        return retrofit.create(RandomWordService.class);
    }

    public void loadDefinition() {
        loadDefinition(null);
    }

    private void loadDefinition(@Nullable String dest) {
        setProgressViewVisible(true);

        if (Utils.isEmptyString(dest)) {
            dest = getDest();
        }

        Call<Definition> definitionCall =
                getContactService().getDefinition("en", dest, "json", getPhrase().toLowerCase().trim());
        definitionCall.enqueue(new Callback<Definition>() {
            @Override
            public void onResponse(Call<Definition> call, Response<Definition> response) {
                if (null != response.body()) {
                    setProgressViewVisible(false);
                    setDefinition(response.body());
                }
            }

            @Override
            public void onFailure(Call<Definition> call, Throwable t) {
                setProgressViewVisible(false);
            }
        });
    }

    public void loadRandomWord() {
        setProgressViewVisible(true);
        Call<String> definitionCall =
                getRandomWordService().getRandomWord();
        definitionCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (null != response.body()) {
                    setPhrase(response.body());
                    loadDefinition();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                setProgressViewVisible(false);
                setPhrase(DEFAULT_PHRASE);
                loadDefinition();
            }
        });
    }

    @Bindable
    public boolean isNoResults() {
        return noResults;
    }

    public void setNoResults(boolean noResults) {
        this.noResults = noResults;
        notifyPropertyChanged(BR.noResults);
    }

    public void loadSuggestedPhrase() {
        setPhrase(suggestedPhrase);
        loadDefinition();
    }

    public void close(View view) {
        ((Activity) view.getContext()).finish();
    }

    public void openSettings(View view) {
        SettingsActivity.start(view.getContext());
    }

    public boolean copyDefinition(View view) {
        final ClipboardManager clipboardManager = (ClipboardManager) view.getContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(phrase, definition));
        Toast.makeText(view.getContext(), R.string.definition_copied, Toast.LENGTH_SHORT).show();
        return true;
    }

    public boolean copyDefinition2(View view) {
        final ClipboardManager clipboardManager = (ClipboardManager) view.getContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(phrase, definition2));
        Toast.makeText(view.getContext(), R.string.definition_copied, Toast.LENGTH_SHORT).show();
        return true;
    }

    private String getDest() {
        int selectedLanguagePosition = mPreferenceUtils.getSelectedLanguagePosition();
        return mResources.getStringArray(R.array.language_array)[selectedLanguagePosition].substring(0, 3).toLowerCase();
    }

    @Bindable
    public boolean isProgressViewVisible() {
        return progressViewVisible;
    }

    private void setProgressViewVisible(boolean progressViewVisible) {
        this.progressViewVisible = progressViewVisible;
        notifyPropertyChanged(BR.progressViewVisible);
    }

    private void setDefinition(Definition definition) {
        if (definition != null) {
            ArrayList<Tuc> tucs = definition.tucs;
            if (!Utils.isEmptyList(tucs)) {
                buildDefinition1(tucs);
            } else {
                setNoResults(true);
                buildSuggestedPhrase();
            }
        } else {
            super.setDefinition("");
        }

        notifyPropertyChanged(BR.definition);
    }

    private void buildDefinition2(ArrayList<Tuc> tucs) {
        for (Tuc tuc : tucs) {
            if (null != tuc) {
                if (!Utils.isEmptyList(tuc.meanings)) {
                    super.setDefinition2(Utils.fromHtml(tuc.meanings.get(0).text).toString());
                    break;
                } else {
                    if (isDestEn()) {
                        buildSuggestedPhrase();
                    } else {
                        loadDefinition(DEFAULT_DEST);
                    }
                }
            }
        }
    }

    private void buildDefinition1(ArrayList<Tuc> tucs) {
        ArrayList<String> definitions = new ArrayList<>(10);
        for (Tuc tuc : tucs) {
            if (null != tuc) {
                if (null != tuc.phrase) {
                    definitions.add(tuc.phrase.text);
                }
            }
            if (definitions.size() == MAX_DEFINITIONS_SIZE) {
                break;
            }
        }

        if (!Utils.isEmptyList(definitions)) {
            super.setDefinition(Utils.fromHtml(definitions.toString().replace("[", "").replace("]", "")).toString());
        }

        buildDefinition2(tucs);
    }

    private void buildSuggestedPhrase() {
        String suggestedPhrase = null;
        if (phrase.endsWith("ing")) {
            suggestedPhrase = phrase.replace("ing", "");
        } else if (phrase.endsWith("ed")) {
            suggestedPhrase = phrase.replace("ed", "e");
        } else if (phrase.endsWith("led")) {
            suggestedPhrase = phrase.replace("led", "");
        } else if (phrase.endsWith("ling")) {
            suggestedPhrase = phrase.replace("ling", "");
        } else if (phrase.endsWith("tted")) {
            suggestedPhrase = phrase.replace("tted", "t");
        } else if (phrase.endsWith("ted")) {
            suggestedPhrase = phrase.replace("ted", "");
        }

        if (Utils.isEmptyString(suggestedPhrase)) {
            setNoResults(true);
        } else {
            setSuggestedPhrase(suggestedPhrase);
        }
    }

    private boolean isDestEn() {
        return DEFAULT_DEST.equalsIgnoreCase(dest);
    }

    public void onResume() {
        setForceUpdate(mPreferenceUtils.isUpdateAvailable());
    }

    @Bindable
    public boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
        notifyPropertyChanged(BR.forceUpdate);
    }
}

