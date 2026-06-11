package com.nai.routinetracker.data.remote

object ApiConfig {
    const val BASE_URL = "http://10.0.2.2:8000/v1/api/"

    const val TIMEOUT_MILLIS = 15_000L
}

object ApiRoutes {
    object Auth {
        const val LOGIN = "auth/login"
    }

    object Routines {
        const val LIST = "routines"
    }
}
