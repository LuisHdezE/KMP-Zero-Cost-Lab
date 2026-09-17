package dev.eliasworks.kmpzerocostlab.data.local

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase

fun createDatabase(context: Context): AppDatabase =
    buildDatabase(getDatabaseBuilder(context))

private fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("kmp-zero-cost-lab.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}
