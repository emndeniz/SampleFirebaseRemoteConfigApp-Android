package com.example.firebaseconfigurationapp

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firebaseconfigurationapp.ui.theme.FirebaseConfigurationAppTheme
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import org.json.JSONObject
import java.util.Locale

class MainActivity : ComponentActivity() {
    // Add me a integer in here

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    private final val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpMobileConfig()
        val defaultConfigs = convertExistingConfigsToModel()
        setContent {
            FirebaseConfigurationAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting(
                        language = Resources.getSystem().configuration.locales.get(0).language,
                        region = Resources.getSystem().configuration.locales.get(0).country,
                        appVersion = packageManager.getPackageInfo(packageName, 0).versionName,
                        newFinanceEnabled = defaultConfigs.newFinanceEnabled,
                        newSearchMaskEnabled = defaultConfigs.newSearchMaskEnabled,
                        leasingEnabled = defaultConfigs.leasingEnabled,
                        afterLeadEnabled = defaultConfigs.afterLeadEnabled,
                        afterLeadVersion = defaultConfigs.afterLeadVersion,
                        smyleVersion = defaultConfigs.smyleVersion
                    )
                }
            }
        }
        fetchConfigs {

            it?.let {
                setContent {
                    FirebaseConfigurationAppTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                            Greeting(
                                language = stringResource(id = R.string.active_lang),
                                region = Resources.getSystem().configuration.locales.get(0).country,
                                appVersion = packageManager.getPackageInfo(packageName, 0).versionName,
                                newFinanceEnabled = it.newFinanceEnabled,
                                newSearchMaskEnabled = it.newSearchMaskEnabled,
                                leasingEnabled = it.leasingEnabled,
                                afterLeadEnabled = it.afterLeadEnabled,
                                afterLeadVersion = it.afterLeadVersion,
                                smyleVersion = it.smyleVersion
                            )
                        }
                    }
                }
            } ?: run {
                Toast.makeText(this, "Error fetching configs", Toast.LENGTH_SHORT).show()
            }

        }
    }

    //MARK: - Remote config functions -

    private fun setUpMobileConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    private fun fetchConfigs(completion: (ConfigUIModel?) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    completion(convertExistingConfigsToModel())
                } else {
                    completion(null)
                }
            }
    }

    fun convertExistingConfigsToModel(): ConfigUIModel {
        val newSearchMaskEnabled = remoteConfig.getBoolean(BooleanConfigToggle.LEASING_ENABLED.getKey())

        // please re-write following values accoridng to logic of value above

        val leasingEnabled = remoteConfig.getBoolean(BooleanConfigToggle.LEASING_ENABLED.getKey())
        val afterLeadEnabled = remoteConfig.getBoolean(BooleanConfigToggle.AFTER_LEAD_ENABLED.getKey())

        val afterLeadVersion = remoteConfig.getString(StringConfigToggle.AFTER_LEAD_VERSION.getKey())
        val smyleVersion = remoteConfig.getString(StringConfigToggle.HOME_SMYLE_BANNER_VERSION.getKey())

        val newFinanceEnabledJsonStr = remoteConfig.getString(BooleanConfigToggle.NEW_FINANCE_ENABLED.getKey())
        val newFinanceEnabled = toggleStatusForCurrentCountry(JSONObject(newFinanceEnabledJsonStr))

        val someAbsurdData = remoteConfig.getString("someAbsurdData")
        val someAbsurdData2 = remoteConfig.getString("someAbsurdData2")
        return ConfigUIModel(
            newFinanceEnabled = newFinanceEnabled,
            newSearchMaskEnabled = newSearchMaskEnabled,
            leasingEnabled = leasingEnabled,
            afterLeadEnabled = afterLeadEnabled,
            afterLeadVersion = afterLeadVersion,
            smyleVersion = smyleVersion
        )
    }

    private fun toggleStatusForCurrentCountry(toggle: JSONObject): Boolean {

        val activeCountry = Resources.getSystem().configuration.locales.get(0).country
        val availableCountries = toggle.getJSONArray("countries")

        return (0 until availableCountries.length()).any {
            availableCountries.getString(it) == activeCountry
        }
    }


}

@Composable
fun Greeting(language: String, region: String, appVersion: String,
             newFinanceEnabled: String,
             newSearchMaskEnabled: String,
             leasingEnabled: String,
             afterLeadEnabled: String,
             afterLeadVersion: String,
             smyleVersion: String,
             modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Language is $language", modifier = Modifier.padding(12.dp))
            Text(text = "Country: $region", modifier = Modifier.padding(12.dp))
            Text(text = "App version: $appVersion", modifier = Modifier.padding(12.dp))

            Spacer(modifier = modifier.padding(12.dp))
            Divider(color = Color.Blue, thickness = 1.dp)
            Spacer(modifier = modifier.padding(12.dp))

            Text(text = "New finance enabled: $newFinanceEnabled", modifier = Modifier.padding(12.dp))
            Text(text = "New search mask enabled: $newSearchMaskEnabled", modifier = Modifier.padding(12.dp))
            Text(text = "Leasing enabled: $leasingEnabled", modifier = Modifier.padding(12.dp))
            Text(text = "After lead enabled: $afterLeadEnabled", modifier = Modifier.padding(12.dp))
            Text(text = "After lead version: $afterLeadVersion", modifier = Modifier.padding(12.dp))
            Text(text = smyleVersion, modifier = Modifier.padding(12.dp))
        }
    }
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirebaseConfigurationAppTheme {
        Greeting(
            language = Resources.getSystem().configuration.locales.get(0).language,
            region = Resources.getSystem().configuration.locales.get(0).country,
            appVersion = "0",
            newFinanceEnabled = "Yes",
            newSearchMaskEnabled = "No",
            leasingEnabled = "Yes",
            afterLeadEnabled = "No",
            afterLeadVersion = "1.0.0",
            smyleVersion = "1.0.0"
        )
    }
}