package com.nai.routinetracker.data.remote.dto

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull

internal val RemoteJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

internal fun String.parseJsonElementOrPrimitive(): JsonElement {
    return runCatching {
        RemoteJson.parseToJsonElement(trim())
    }.getOrElse {
        JsonPrimitive(this)
    }
}

internal fun JsonElement?.nullIfJsonNull(): JsonElement? {
    return if (this == null || this is JsonNull) null else this
}

internal fun JsonElement?.jsonObjectOrNull(): JsonObject? {
    return nullIfJsonNull() as? JsonObject
}

internal fun JsonElement?.jsonArrayOrNull(): JsonArray? {
    return nullIfJsonNull() as? JsonArray
}

internal fun JsonElement?.jsonPrimitiveOrNull(): JsonPrimitive? {
    return nullIfJsonNull() as? JsonPrimitive
}

internal fun JsonObject.elementOrNull(name: String): JsonElement? {
    return get(name).nullIfJsonNull()
}

internal fun JsonObject.stringOrNull(name: String): String? {
    return elementOrNull(name)
        .jsonPrimitiveOrNull()
        ?.contentOrNull
        ?.ifBlank { null }
}

internal fun JsonObject.intOrNull(name: String): Int? {
    return elementOrNull(name).jsonPrimitiveOrNull()?.intOrNull
}

internal fun JsonObject.longOrNull(name: String): Long? {
    return elementOrNull(name).jsonPrimitiveOrNull()?.longOrNull
}

internal fun JsonObject.booleanOrNull(name: String): Boolean? {
    return elementOrNull(name).jsonPrimitiveOrNull()?.booleanOrNull
}

internal fun JsonObject.firstStringOrNull(vararg names: String): String? {
    return names.firstNotNullOfOrNull { name -> stringOrNull(name) }
}

internal fun JsonObject.firstIntOrNull(vararg names: String): Int? {
    return names.firstNotNullOfOrNull { name -> intOrNull(name) }
}

internal fun JsonObject.firstBooleanOrNull(vararg names: String): Boolean? {
    return names.firstNotNullOfOrNull { name -> booleanOrNull(name) }
}
