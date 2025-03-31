package br.com.bitsolutions.mercadolivre.data.favorite.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.bitsolutions.mercadolivre.data.favorite.local.entity.ItemEntity

@Dao
interface ItemDao {
    @Query("SELECT * FROM items")
    fun getItemsList(): List<ItemEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(vararg item: ItemEntity)

    @Delete
    fun deleteItem(item: ItemEntity)
}
