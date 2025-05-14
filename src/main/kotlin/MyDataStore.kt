import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import java.io.File

fun createDataStore(): DataStore<Preferences> {
   return PreferenceDataStoreFactory.create{
        File("${System.getProperty("user.home")}/naruto_bp.preferences_pb")
    }
}

val dataStores = createDataStore()