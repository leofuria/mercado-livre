package br.com.bitsolutions.mercadolivre.data.home.response

import br.com.bitsolutions.mercadolivre.domain.home.model.SearchItemAttribute
import com.google.gson.annotations.SerializedName

data class SearchItemAttributeResponse(
    val id: String,
    val name: String,
    @SerializedName("value_name")
    val value: String?,
) {
    fun toSearchItemAttribute(): SearchItemAttribute = SearchItemAttribute(
        id,
        name,
        value ?: "",
    )
}
