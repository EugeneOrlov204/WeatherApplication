package com.eorlov.weatherapplication.api

import com.google.gson.annotations.SerializedName


class GeocodingResponse {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("local_names")
    var local_names: LocalNames? = null

    @SerializedName("lat")
    var lat: Float? = 0f

    @SerializedName("lon")
    var lon: Float? = 0f

    @SerializedName("country")
    var country: String? = null

    override fun toString(): String {
        return "GeocodingResponse(name=$name, local_names=$local_names, lat=$lat, lon=$lon, country=$country)"
    }
}

class LocalNames {

    @SerializedName("ar")
    val ar: String? = null

    @SerializedName("ascii")
    val ascii: String? = null

    @SerializedName("bg")
    val bg: String? = null

    @SerializedName("ca")
    val ca: String? = null

    @SerializedName("da")
    val da: String? = null

    @SerializedName("de")
    val de: String? = null

    @SerializedName("el")
    val el: String? = null

    @SerializedName("en")
    val en: String? = null

    @SerializedName("eu")
    val eu: String? = null

    @SerializedName("fa")
    val fa: String? = null

    @SerializedName("feature_name")
    val feature_name: String? = null

    @SerializedName("fi")
    val fi: String? = null

    @SerializedName("fr")
    val fr: String? = null

    @SerializedName("gl")
    val gl: String? = null

    @SerializedName("he")
    val he: String? = null


    @SerializedName("hr")
    val hr: String? = null

    @SerializedName("hu")
    val hu: String? = null

    @SerializedName("id")
    val id: String? = null

    @SerializedName("it")
    val it: String? = null

    @SerializedName("ja")
    val ja: String? = null

    @SerializedName("la")
    val la: String? = null

    @SerializedName("lt")
    val lt: String? = null

    @SerializedName("mk")
    val mk: String? = null

    @SerializedName("pl")
    val pl: String? = null

    @SerializedName("pt")
    val pt: String? = null

    @SerializedName("ro")
    val ro: String? = null

    @SerializedName("ru")
    val ru: String? = null

    @SerializedName("sk")
    val sk: String? = null

    @SerializedName("sl")
    val sl: String? = null

    @SerializedName("sr")
    val sr: String? = null

    @SerializedName("`tr`")
    val tr: String? = null

    @SerializedName("nl")
    val nl: String? = null

    @SerializedName("no")
    val no: String? = null

    @SerializedName("vi")
    val vi: String? = null

    @SerializedName("zu")
    val zu: String? = null

    override fun toString(): String {
        return "LocalNames(ar=$ar, ascii=$ascii, bg=$bg, ca=$ca, da=$da, de=$de, el=$el, en=$en, eu=$eu, fa=$fa, feature_name=$feature_name, fi=$fi, fr=$fr, gl=$gl, he=$he, hr=$hr, hu=$hu, id=$id, it=$it, ja=$ja, la=$la, lt=$lt, mk=$mk, pl=$pl, pt=$pt, ro=$ro, ru=$ru, sk=$sk, sl=$sl, sr=$sr, tr=$tr, nl=$nl, no=$no, vi=$vi, zu=$zu)"
    }

}
