package com.example.fitfactory.data.database.creditCard

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitfactory.data.models.app.CreditCard

@Dao
interface CreditCardDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(creditCard: CreditCard)

    @Query("SELECT * FROM CreditCard WHERE ownerId =:ownerId")
    fun getCreditCard(ownerId: Long): LiveData<CreditCard?>

    @Query("DELETE FROM CreditCard WHERE ownerId =:ownerId")
    suspend fun deleteCreditCard(ownerId: Long)
}