package com.nai.routinetracker.data.remote

import com.nai.routinetracker.BuildConfig

object ApiConfig {
    const val BASE_URL: String = BuildConfig.API_BASE_URL

    const val TIMEOUT_MILLIS = 15_000L
}

object ApiRoutes {
    object Auth {
        const val LOGIN = "auth/login"
    }

    object Routines {
        const val LIST = "routines"
        const val DETAIL = "routines/{routineId}"
    }

    object Tasks {
        const val LIST = "tasks"
        const val COMPLETION = "tasks/{taskId}/completion"
    }
}
