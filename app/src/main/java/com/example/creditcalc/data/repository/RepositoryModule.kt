package com.example.creditcalc.data.repository

import android.app.Application
import androidx.room.Room
import com.example.creditcalc.data.maindb.MainDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMainDB(app: Application): MainDB {
        return Room.databaseBuilder(
            context = app,
            klass = MainDB::class.java,
            name = "Main.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePeopleRepository(
        mainDB: MainDB
    ): Repository {
        return Repository(mainDB = mainDB)
    }
}