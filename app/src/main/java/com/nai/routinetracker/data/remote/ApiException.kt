package com.nai.routinetracker.data.remote

import java.io.IOException

class ApiException(
    val statusCode: Int,
    override val message: String
) : IOException(message)
