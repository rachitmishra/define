package `in`.ceeq.define.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @Entity
 *
 * @Entity(primaryKeys = {"firstName", "lastName"})
 *
 * @Entity(indices = {@Index("name"), @Index(value = {"last_name", "address"})})
 *
 * @Entity(indices = {@Index(value = {"first_name", "last_name"}, unique = true)})
 *
 * @Entity(foreignKeys = @ForeignKey(entity = Definition.class, parentColumns = "id", childColumns = "user_id"))
 *
 * @Embedded
 *
 * @PrimaryKey
 *
 * @ColumnInfo
 *
 * @Relation
 *
 * Table names in SQLite are case-insensitive.
 * Embedded fields can also include other embedded fields.
 */
@Parcelize
@Entity(tableName = "definitions", primaryKeys = ["phrase", "from", "dest"])
data class Definition(
        @Ignore
        val result: String = "",
        @SerializedName("tuc")
        @Ignore
        val tucs: ArrayList<Tuc>? = arrayListOf(),
        var phrase: String = "love",
        var from: String = "en",
        var dest: String = "hi") : Parcelable {

    companion object {
        const val MAX_DEFINITIONS = 10
    }
}
