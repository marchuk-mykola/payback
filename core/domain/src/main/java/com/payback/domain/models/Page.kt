package com.payback.domain.models

data class Page(
    val list: List<PixabayImage>,
    val isLastPage: Boolean
)