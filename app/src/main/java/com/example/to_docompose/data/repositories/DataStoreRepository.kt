package com.example.to_docompose.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.data.models.TodoTask
import com.example.to_docompose.utils.Constants.PREFERENCE_KEY
import com.example.to_docompose.utils.Constants.PREFERENCE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOError
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(
   @ApplicationContext val context: Context
) {
    private object PreferenceKey{
        val sortState = stringPreferencesKey(name = PREFERENCE_KEY)
    }
    private val dataStore = context.dataStore

    suspend fun persistStoreData(priority: Priority){
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferenceKey.sortState] = priority.name
        }
    }

    val readSortData: Flow<String> = dataStore.data
        .catch {ex ->
            if(ex is IOError){
                emit(emptyPreferences())
            }else{
                throw ex
            }
        }.map { preferences: Preferences ->
            preferences[PreferenceKey.sortState] ?: Priority.NONE.name
        }

}