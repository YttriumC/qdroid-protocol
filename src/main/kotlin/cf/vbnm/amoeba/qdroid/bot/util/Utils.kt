package cf.vbnm.amoeba.qdroid.bot.util

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*


fun LocalDateTime.toDate(): Date {
    return Timestamp.valueOf(this)
}