package br.com.bitsolutions.mercadolivre.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.bitsolutions.mercadolivre.data.favorite.local.dao.ItemDao
import br.com.bitsolutions.mercadolivre.data.favorite.local.entity.ItemEntity

@Database(entities = [(ItemEntity::class)], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
}
