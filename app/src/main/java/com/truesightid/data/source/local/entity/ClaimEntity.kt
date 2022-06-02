package com.truesightid.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "claim")
@Parcelize
data class ClaimEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "claim_id")
    var id: Int?,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "claimer")
    var claimer: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "image")
    var image: Int,

    @ColumnInfo(name = "fake")
    var fake: Int,

    @ColumnInfo(name = "upvote")
    var upvote: Int,

    @ColumnInfo(name = "downvote")
    var downvote: Int,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "votecount")
    var voteCount: Int
) : Parcelable
