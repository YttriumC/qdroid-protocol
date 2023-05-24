package cf.vbnm.amoeba.qdroid.cq.api.data

import com.fasterxml.jackson.annotation.JsonProperty

data class QFile(
    @JsonProperty("group_id") val groupId: Long,
    @JsonProperty("file_id") val fileId: String,
    @JsonProperty("file_name") val fileName: String,
    @JsonProperty("busid") val busid: Int,
    @JsonProperty("file_size") val fileSize: Long,
    @JsonProperty("upload_time") val uploadTime: Long,
    @JsonProperty("dead_time") val deadTime: Long,
    @JsonProperty("modify_time") val modifyTime: Long,
    @JsonProperty("download_times") val downloadTimes: Int,
    @JsonProperty("uploader") val uploader: Long,
    @JsonProperty("uploader_name") val uploaderName: String,
)

data class QFolder(
    @JsonProperty("group_id") val groupId: Long,
    @JsonProperty("file_id") val fileId: String,
    @JsonProperty("folder_name") val folderName: String,
    @JsonProperty("create_time") val createTime: Long,
    @JsonProperty("creator") val creator: Long,
    @JsonProperty("creator_name") val creatorName: String,
    @JsonProperty("total_file_count") val totalFileCount: Int,
)