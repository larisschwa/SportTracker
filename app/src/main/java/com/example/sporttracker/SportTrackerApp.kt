package com.example.sporttracker

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import com.example.sporttracker.sync.ScheduleSyncWorker

class SportTrackerApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        // Automatically sync schedule once per day when connected to Wi-Fi
        ScheduleSyncWorker.enqueueDailySync(this)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(SvgDecoder.Factory())
            }
            .crossfade(true)
            .build()
    }
}
