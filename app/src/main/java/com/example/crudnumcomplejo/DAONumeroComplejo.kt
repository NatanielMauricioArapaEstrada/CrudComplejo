package com.example.crudnumcomplejo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DAONumeroComplejo {

    @Query("SELECT * FROM numeros_complejos ORDER BY id")
    suspend fun obtenerNumeros(): List<NumeroComplejo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarNumero(numero: NumeroComplejo)

    @Query("UPDATE numeros_complejos SET pr = :pr, pi = :pi, pr_r = :prReflejada, pr_i = :piReflejada WHERE id = :id")
    suspend fun actualizarNumero(
        id: Int,
        pr: Double,
        pi: Double,
        prReflejada: Double,
        piReflejada: Double
    )

    @Query("DELETE FROM numeros_complejos WHERE id = :id")
    suspend fun eliminarNumero(id: Int)
}
