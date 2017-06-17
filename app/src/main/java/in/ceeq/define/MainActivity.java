package in.ceeq.define;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.ceeq.define.databinding.ActivityMainBinding;
import in.ceeq.define.databinding.DefinitionViewModel;
import in.ceeq.define.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static final String DEFINITION_VIEW_MODEL = "definition_view_model";
    private DefinitionViewModel mDefinitionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null != savedInstanceState) {
            mDefinitionViewModel = savedInstanceState.getParcelable(DEFINITION_VIEW_MODEL);
        }

        if (null == mDefinitionViewModel) {
            mDefinitionViewModel = new DefinitionViewModel(this);
        } else {
            mDefinitionViewModel.init(this);
        }

        ActivityMainBinding activityMainBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setDefinitionViewModel(mDefinitionViewModel);

        setTitle("");

        CharSequence text = getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);

        if (null != text) {
            mDefinitionViewModel.setPhrase(text.toString());
            loadDefinition();
        } else {
            mDefinitionViewModel.loadRandomWord();
        }
    }

    private void loadDefinition() {
        if (Utils.isNetConnected(this)) {
            mDefinitionViewModel.loadDefinition();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(DEFINITION_VIEW_MODEL, mDefinitionViewModel);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDefinitionViewModel.onResume();
    }
}
