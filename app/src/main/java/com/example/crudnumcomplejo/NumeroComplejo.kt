package com.example.crudnumcomplejo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "numeros_complejos")
data class NumeroComplejo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pr: Double = 0.0,
    val pi: Double = 0.0,
    val pr_r: Double = 0.0,
    val pr_i: Double = 0.0
)
