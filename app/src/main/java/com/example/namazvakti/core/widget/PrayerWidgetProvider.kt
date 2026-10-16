package com.example.namazvakti.core.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.example.namazvakti.MainActivity
import com.example.namazvakti.R
import com.example.namazvakti.data.local.datastore.UserPreferencesRepository
import com.example.namazvakti.data.repository.PrayerTimeRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class PrayerWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var prayerTimeRepository: PrayerTimeRepository

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
            updateWidgetLayout(context, appWidgetManager, appWidgetId, options)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        updateWidgetLayout(context, appWidgetManager, appWidgetId, newOptions)
    }

    private fun updateWidgetLayout(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        options: Bundle
    ) {
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, 100)
                val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 100)

                val settings = userPreferencesRepository.userSettingsFlow.first()
                val todayDate = LocalDate.now().toString()
                val prayerTimes = prayerTimeRepository.getPrayerTimesByDate(todayDate)

                val layoutRes = when {
                    minHeight >= 220 -> R.layout.widget_full
                    minWidth >= 200 -> R.layout.widget_wide
                    else -> R.layout.widget_compact
                }

                val views = RemoteViews(context.packageName, layoutRes)
                val cityText = settings.selectedCity.ifBlank { "Şehir Seçilmedi" }

                val intent = Intent(context, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                if (layoutRes == R.layout.widget_compact) {
                    views.setTextViewText(R.id.tv_widget_city, cityText)
                    views.setTextViewText(R.id.tv_widget_next_prayer, "İmsak")
                    views.setTextViewText(R.id.tv_widget_countdown, prayerTimes?.fajr ?: "--:--")
                } else if (layoutRes == R.layout.widget_wide) {
                    views.setTextViewText(R.id.tv_widget_city_wide, cityText)
                    views.setTextViewText(R.id.tv_widget_countdown_wide, "İmsak: ${prayerTimes?.fajr ?: "--:--"}")
                    views.setTextViewText(
                        R.id.tv_widget_times_summary,
                        "Güneş: ${prayerTimes?.sunrise ?: "--:--"} | Öğle: ${prayerTimes?.dhuhr ?: "--:--"}\nİkindi: ${prayerTimes?.asr ?: "--:--"} | Akşam: ${prayerTimes?.maghrib ?: "--:--"} | Yatsı: ${prayerTimes?.isha ?: "--:--"}"
                    )
                } else {
                    views.setTextViewText(R.id.tv_widget_title_full, "$cityText Namaz Vakitleri")
                    views.setTextViewText(R.id.tv_widget_countdown_full, "Bugünün Vakitleri")
                    views.setTextViewText(
                        R.id.tv_widget_details_full,
                        "İmsak: ${prayerTimes?.fajr ?: "--:--"}\n" +
                                "Güneş: ${prayerTimes?.sunrise ?: "--:--"}\n" +
                                "Öğle: ${prayerTimes?.dhuhr ?: "--:--"}\n" +
                                "İkindi: ${prayerTimes?.asr ?: "--:--"}\n" +
                                "Akşam: ${prayerTimes?.maghrib ?: "--:--"}\n" +
                                "Yatsı: ${prayerTimes?.isha ?: "--:--"}"
                    )
                }

                // Çökme hatasını çözen kısım: Tıklama olayı kendi widget_root ID'mize bağlandı
                views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pendingResult.finish()
            }
        }
    }
}