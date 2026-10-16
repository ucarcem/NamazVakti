package com.example.namazvakti.core.utils

import kotlin.math.*

object QiblaCalculator {

    // Kâbe'nin Enlem ve Boylam Koordinatları
    private const val KAABA_LATITUDE = 21.422487
    private const val KAABA_LONGITUDE = 39.826206

    /**
     * Verilen enlem ve boylam bilgisinden Kâbe'nin Gerçek Kuzey'e göre açısını (0°-360°) hesaplar.
     */
    fun calculateQiblaBearing(latitude: Double, longitude: Double): Float {
        val userLatRad = Math.toRadians(latitude)
        val userLngRad = Math.toRadians(longitude)
        val kaabaLatRad = Math.toRadians(KAABA_LATITUDE)
        val kaabaLngRad = Math.toRadians(KAABA_LONGITUDE)

        val deltaLng = kaabaLngRad - userLngRad

        val y = sin(deltaLng)
        val x = cos(userLatRad) * tan(kaabaLatRad) - sin(userLatRad) * cos(deltaLng)

        var qiblaBearing = Math.toDegrees(atan2(y, x))
        qiblaBearing = (qiblaBearing + 360) % 360

        return qiblaBearing.toFloat()
    }

    // Türkiye'nin 81 İlinin Enlem ve Boylam Koordinat Haritası
    val cityCoordinates = mapOf(
        "Adana" to Pair(37.0000, 35.3213),
        "Adıyaman" to Pair(37.7648, 38.2786),
        "Afyonkarahisar" to Pair(38.7507, 30.5567),
        "Ağrı" to Pair(39.7191, 43.0503),
        "Amasya" to Pair(40.6499, 35.8353),
        "Ankara" to Pair(39.9334, 32.8597),
        "Antalya" to Pair(36.8969, 30.7133),
        "Artvin" to Pair(41.1828, 41.8183),
        "Aydın" to Pair(37.8560, 27.8416),
        "Balıkesir" to Pair(39.6484, 27.8826),
        "Bilecik" to Pair(40.1506, 29.9792),
        "Bingöl" to Pair(38.8853, 40.4980),
        "Bitlis" to Pair(38.4006, 42.1095),
        "Bolu" to Pair(40.7358, 31.6061),
        "Burdur" to Pair(37.7204, 30.2908),
        "Bursa" to Pair(40.1885, 29.0610),
        "Çanakkale" to Pair(40.1553, 26.4142),
        "Çankırı" to Pair(40.6013, 33.6134),
        "Çorum" to Pair(40.5506, 34.9556),
        "Denizli" to Pair(37.7765, 29.0864),
        "Diyarbakır" to Pair(37.9144, 40.2306),
        "Edirne" to Pair(41.6771, 26.5557),
        "Elazığ" to Pair(38.6810, 39.2264),
        "Erzincan" to Pair(39.7500, 39.5000),
        "Erzurum" to Pair(39.9043, 41.2679),
        "Eskişehir" to Pair(39.7767, 30.5206),
        "Gaziantep" to Pair(37.0662, 37.3833),
        "Giresun" to Pair(40.9128, 38.3895),
        "Gümüşhane" to Pair(40.4603, 39.4814),
        "Hakkari" to Pair(37.5833, 43.7333),
        "Hatay" to Pair(36.2021, 36.1604),
        "Isparta" to Pair(37.7648, 30.5566),
        "Mersin" to Pair(36.8121, 34.6415),
        "İstanbul" to Pair(41.0082, 28.9784),
        "İzmir" to Pair(38.4237, 27.1428),
        "Kars" to Pair(40.6013, 43.0975),
        "Kastamonu" to Pair(41.3887, 33.7827),
        "Kayseri" to Pair(38.7312, 35.4787),
        "Kırklareli" to Pair(41.7333, 27.2167),
        "Kırşehir" to Pair(39.1425, 34.1709),
        "Kocaeli" to Pair(40.8533, 29.8815),
        "Konya" to Pair(37.8746, 32.4932),
        "Kütahya" to Pair(39.4167, 29.9833),
        "Malatya" to Pair(38.3552, 38.3095),
        "Manisa" to Pair(38.6191, 27.4289),
        "Kahramanmaraş" to Pair(37.5858, 36.9371),
        "Mardin" to Pair(37.3212, 40.7245),
        "Muğla" to Pair(37.2153, 28.3636),
        "Muş" to Pair(38.7432, 41.5064),
        "Nevşehir" to Pair(38.6244, 34.7144),
        "Niğde" to Pair(37.9667, 34.6833),
        "Ordu" to Pair(40.9839, 37.8764),
        "Rize" to Pair(41.0201, 40.5234),
        "Sakarya" to Pair(40.7569, 30.3783),
        "Samsun" to Pair(41.2928, 36.3313),
        "Siirt" to Pair(37.9333, 41.9500),
        "Sinop" to Pair(42.0231, 35.1531),
        "Sivas" to Pair(39.7477, 37.0179),
        "Tekirdağ" to Pair(40.9833, 27.5167),
        "Tokat" to Pair(40.3167, 36.5500),
        "Trabzon" to Pair(41.0015, 39.7178),
        "Tunceli" to Pair(39.1079, 39.5401),
        "Şanlıurfa" to Pair(37.1674, 38.7955),
        "Uşak" to Pair(38.6823, 29.4082),
        "Van" to Pair(38.4891, 43.4089),
        "Yozgat" to Pair(39.8181, 34.8147),
        "Zonguldak" to Pair(41.4564, 31.7987),
        "Aksaray" to Pair(38.3687, 34.0370),
        "Bayburt" to Pair(40.2552, 40.2249),
        "Karaman" to Pair(37.1759, 33.2287),
        "Kırıkkale" to Pair(39.8453, 33.5139),
        "Batman" to Pair(37.8812, 41.1351),
        "Şırnak" to Pair(37.5164, 42.4611),
        "Bartın" to Pair(41.6344, 32.3375),
        "Ardahan" to Pair(41.1105, 42.7022),
        "Iğdır" to Pair(39.9196, 44.0457),
        "Yalova" to Pair(40.6500, 29.2667),
        "Karabük" to Pair(41.2061, 32.6204),
        "Kilis" to Pair(36.7184, 37.1212),
        "Osmaniye" to Pair(37.0742, 36.2478),
        "Düzce" to Pair(40.8438, 31.1565)
    )
}