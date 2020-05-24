package com.kl.testsampleapp.model

import androidx.annotation.VisibleForTesting
import com.google.gson.annotations.SerializedName

/**
 * An entity representing GitHub's repository
 *
 * @author Fumihiko Shiroyama (fu.shiroyama@gmail.com)
 */
class Repo {
    @SerializedName("id")
    var id: Long = 0
        private set
    @SerializedName("name")
    var name: String? = null
        private set
    @SerializedName("full_name")
    var fullName: String? = null
        private set
    @SerializedName("html_url")
    val htmlUrl: String? = null
    @SerializedName("private")
    var isPrivate = false
        private set
    @SerializedName("owner")
    var owner: Owner? = null
        private set

    constructor() {}
    constructor(
        id: Long,
        name: String?,
        fullName: String?,
        isPrivate: Boolean,
        owner: Owner?
    ) {
        this.id = id
        this.name = name
        this.fullName = fullName
        this.isPrivate = isPrivate
        this.owner = owner
    }

    override fun toString(): String {
        return "Repo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", isPrivate=" + isPrivate +
                ", owner=" + owner +
                '}'
    }

    class Owner {
        @SerializedName("id")
        var id: Long = 0
            private set
        @SerializedName("login")
        var login: String? = null
            private set
        @SerializedName("avatar_url")
        var avatarUrl: String? = null
            private set
        @SerializedName("url")
        var url: String? = null
            private set

        constructor() {}
        @VisibleForTesting(otherwise = VisibleForTesting.NONE)
        constructor(
            id: Long,
            login: String?,
            avatarUrl: String?,
            url: String?
        ) {
            this.id = id
            this.login = login
            this.avatarUrl = avatarUrl
            this.url = url
        }

        override fun toString(): String {
            return "Owner{" +
                    "id=" + id +
                    ", login='" + login + '\'' +
                    ", avatarUrl='" + avatarUrl + '\'' +
                    ", url='" + url + '\'' +
                    '}'
        }
    }
}