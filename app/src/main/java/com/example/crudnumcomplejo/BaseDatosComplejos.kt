package com.example.crudnumcomplejo

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NumeroComplejo::class], version = 1, exportSchema = false)
abstract class BaseDatosComplejos : RoomDatabase() {
    abstract fun daoNumeroComplejo(): DAONumeroComplejo
}
