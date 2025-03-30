package br.com.bitsolutions.mercadolivre.data.home.response

import com.google.gson.annotations.SerializedName

data class SearchResultItemPictures(
    @SerializedName("secure_url")
    val url: String,
)
