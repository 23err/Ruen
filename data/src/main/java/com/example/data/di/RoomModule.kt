package com.example.data.di

import androidx.room.Room
import com.example.data.db.AppDatabase
import com.example.data.db.migrations.Migration_1_2
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val roomModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, get(named("DB_NAME")))
            .addMigrations(Migration_1_2())
            .build()
    }

    single(named("DB_NAME")) {
        "RUEN.db"
    }

    // TranslatedWordDao
    factory { get<AppDatabase>().translatedWordDao() }
    // CardDao
    factory { get<AppDatabase>().cardDao() }
    // GroupDao
    factory { get<AppDatabase>().groupDao() }
}