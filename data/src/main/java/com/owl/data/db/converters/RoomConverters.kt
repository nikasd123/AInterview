package com.owl.data.db.converters

import androidx.room.TypeConverter
import com.owl.data.network.model.InterviewSettingsDto
import com.owl.data.network.model.QuestionDto
import com.owl.data.network.model.toDomain
import com.owl.data.network.model.toDto
import com.owl.domain.model.InterviewSettings
import com.owl.domain.model.Question
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.map

class RoomConverters {
    private val json = Json { ignoreUnknownKeys = true }
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // --- List<Question> ---
    @TypeConverter
    fun fromQuestionsList(value: List<Question>): String {
        val dtos = value.map { it.toDto() }
        return json.encodeToString(dtos)
    }

    @TypeConverter
    fun toQuestionsList(value: String): List<Question> {
        return try {
            val dtos = json.decodeFromString<List<QuestionDto>>(value)
            dtos.map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // --- InterviewSettings ---
    @TypeConverter
    fun fromSettings(value: InterviewSettings): String {
        return json.encodeToString(value.toDto())
    }

    @TypeConverter
    fun toSettings(value: String): InterviewSettings {
        return try {
            json.decodeFromString<InterviewSettingsDto>(value).toDomain()
        } catch (e: Exception) {
            throw IllegalStateException("Corrupted settings in DB")
        }
    }

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
}