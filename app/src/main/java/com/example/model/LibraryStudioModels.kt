package com.example.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import org.json.JSONArray
import org.json.JSONObject

data class LibraryProject(
    val id: String,
    val title: String,
    val categoryName: String,
    val goalNote: String,
    val isCompleted: Boolean = false
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("title", title)
        put("categoryName", categoryName)
        put("goalNote", goalNote)
        put("isCompleted", isCompleted)
    }

    companion object {
        fun fromJson(obj: JSONObject): LibraryProject? {
            val id = obj.optString("id")
            val title = obj.optString("title")
            if (id.isBlank() || title.isBlank()) return null
            return LibraryProject(
                id = id,
                title = title,
                categoryName = obj.optString("categoryName", "SCIENCE"),
                goalNote = obj.optString("goalNote", ""),
                isCompleted = obj.optBoolean("isCompleted", false)
            )
        }

        fun listToJsonString(items: List<LibraryProject>): String {
            val arr = JSONArray()
            items.forEach { arr.put(it.toJson()) }
            return arr.toString()
        }

        fun listFromJsonString(raw: String?): List<LibraryProject> {
            if (raw.isNullOrBlank()) return emptyList()
            return try {
                val arr = JSONArray(raw)
                buildList {
                    for (i in 0 until arr.length()) {
                        arr.optJSONObject(i)?.let { fromJson(it) }?.let { add(it) }
                    }
                }
            } catch (_: Exception) {
                emptyList()
            }
        }
    }
}

data class ArtworkPoint(
    val x: Float,
    val y: Float
)

data class ArtworkStroke(
    val colorArgb: Long,
    val points: List<ArtworkPoint>
)

data class SavedArtwork(
    val id: String,
    val title: String,
    val strokes: List<ArtworkStroke>
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("title", title)
        val strokesArr = JSONArray()
        strokes.forEach { stroke ->
            val strokeObj = JSONObject().apply {
                put("color", stroke.colorArgb)
                val ptsArr = JSONArray()
                stroke.points.forEach { pt ->
                    ptsArr.put(JSONArray().apply {
                        put(pt.x.toDouble())
                        put(pt.y.toDouble())
                    })
                }
                put("points", ptsArr)
            }
            strokesArr.put(strokeObj)
        }
        put("strokes", strokesArr)
    }

    companion object {
        fun fromJson(obj: JSONObject): SavedArtwork? {
            val id = obj.optString("id")
            val title = obj.optString("title")
            if (id.isBlank() || title.isBlank()) return null
            val strokesArr = obj.optJSONArray("strokes") ?: JSONArray()
            val strokes = buildList {
                for (i in 0 until strokesArr.length()) {
                    val sObj = strokesArr.optJSONObject(i) ?: continue
                    val color = sObj.optLong("color", 0xFF6B1D2F)
                    val ptsArr = sObj.optJSONArray("points") ?: JSONArray()
                    val pts = buildList {
                        for (j in 0 until ptsArr.length()) {
                            val pair = ptsArr.optJSONArray(j) ?: continue
                            if (pair.length() >= 2) {
                                add(ArtworkPoint(pair.optDouble(0).toFloat(), pair.optDouble(1).toFloat()))
                            }
                        }
                    }
                    if (pts.isNotEmpty()) add(ArtworkStroke(color, pts))
                }
            }
            return SavedArtwork(id = id, title = title, strokes = strokes)
        }

        fun listToJsonString(items: List<SavedArtwork>): String {
            val arr = JSONArray()
            items.forEach { arr.put(it.toJson()) }
            return arr.toString()
        }

        fun listFromJsonString(raw: String?): List<SavedArtwork> {
            if (raw.isNullOrBlank()) return emptyList()
            return try {
                val arr = JSONArray(raw)
                buildList {
                    for (i in 0 until arr.length()) {
                        arr.optJSONObject(i)?.let { fromJson(it) }?.let { add(it) }
                    }
                }
            } catch (_: Exception) {
                emptyList()
            }
        }
    }
}

data class LibraryGalleryPlate(
    val id: String,
    val title: String,
    val subtitle: String,
    @DrawableRes val drawableRes: Int,
    val accentColor: Color
)
