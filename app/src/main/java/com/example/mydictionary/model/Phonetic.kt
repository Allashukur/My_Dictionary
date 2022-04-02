package com.example.mydictionary.model

data class Phonetic(
    val audio: String,
    val license: LicenseX,
    val sourceUrl: String,
    val text: String
)