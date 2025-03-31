package br.com.bitsolutions.mercadolivre.data.favorite.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchItemPrice
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey var id: String,
    var title: String,
    var thumbnail: String,
    var condition: String,
    var freeShipping: Boolean,
    var seller: String,
    var formatedAmount: String,
    var formatedOriginalAmount: String,
) {
    fun toSearchResultItem(): SearchResultItem {
        return SearchResultItem(
            id,
            title,
            thumbnail,
            condition,
            SearchItemPrice(
                amount = 0.0,
                null,
                "",
                formatedAmount,
                formatedOriginalAmount,
                null,
            ),
            freeShipping,
            "",
            emptyList(),
            seller,
            null,
            true,
        )
    }

    companion object {

        fun fromSearchResultItem(searchResultItem: SearchResultItem): ItemEntity {
            return ItemEntity(
                searchResultItem.id,
                searchResultItem.title,
                searchResultItem.thumbnail,
                searchResultItem.condition,
                searchResultItem.freeShipping,
                searchResultItem.seller,
                searchResultItem.price.formatedAmount,
                searchResultItem.price.formatedOriginalAmount,
            )
        }
    }
}
