package com.example.ainoteapp.translator

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

fun translator(input: String = "", lang: String = "", output: (String?) -> Unit = {}) {
    val translator = TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(lang).build()
    val client = Translation.getClient(translator)
    client.downloadModelIfNeeded(
        DownloadConditions.Builder().requireCharging().requireCharging().build()
    ).addOnSuccessListener {
        client.translateLanguage(input, output)
    }.addOnFailureListener{
        output("Failed Translation")
    }
}

fun Translator.translateLanguage(input: String = "", output: (String?) -> Unit = {}) {
    translate(input).addOnSuccessListener {
        output(it.orEmpty())
    }.addOnFailureListener {
        output("")
    }
}