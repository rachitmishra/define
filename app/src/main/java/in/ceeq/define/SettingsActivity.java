package in.ceeq.define;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.ceeq.define.databinding.ActivitySettingsBinding;
import in.ceeq.define.databinding.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity {

    private static final String SETTINGS_VIEW_MODEL = "settings_view_model";
    private SettingsViewModel mSettingsViewModel;

    public static void start(Context context) {
        Intent starter = new Intent(context, SettingsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null != savedInstanceState) {
            mSettingsViewModel = savedInstanceState.getParcelable(SETTINGS_VIEW_MODEL);
        }

        if (null == mSettingsViewModel) {
            mSettingsViewModel = new SettingsViewModel(this);
        } else {
            mSettingsViewModel.init(this);
        }

        ActivitySettingsBinding activitySettingsBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_settings);
        activitySettingsBinding.setSettingsViewModel(mSettingsViewModel);

        setTitle("");
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SETTINGS_VIEW_MODEL, mSettingsViewModel);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
