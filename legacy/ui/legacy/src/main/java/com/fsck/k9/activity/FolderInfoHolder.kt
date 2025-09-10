package com.fsck.k9.activity

import app.k9mail.legacy.ui.folder.FolderNameFormatter
import com.fsck.k9.mailstore.LocalFolder
import net.thunderbird.core.android.account.LegacyAccountDto
import net.thunderbird.feature.mail.folder.api.Folder
import net.thunderbird.feature.mail.folder.api.FolderType

class FolderInfoHolder(
    private val folderNameFormatter: FolderNameFormatter,
    localFolder: LocalFolder,
    account: LegacyAccountDto,
) {
    @JvmField
    val databaseId = localFolder.databaseId

    @JvmField
    val displayName = getDisplayName(account, localFolder)

    @JvmField
    var loading = false

    @JvmField
    var moreMessages = localFolder.hasMoreMessages()

    private fun getDisplayName(account: LegacyAccountDto, localFolder: LocalFolder): String {
        val folderId = localFolder.databaseId
        val folder = Folder(
            id = folderId,
            name = localFolder.name,
            type = getFolderType(account, folderId),
            isLocalOnly = localFolder.isLocalOnly,
        )
        return folderNameFormatter.displayName(folder)
    }

    companion object {
        @JvmStatic
        fun getFolderType(account: LegacyAccountDto, folderId: Long): FolderType {
            return when (folderId) {
                account.inboxFolderId -> FolderType.INBOX
                account.outboxFolderId -> FolderType.OUTBOX
                account.archiveFolderId -> FolderType.ARCHIVE
                account.draftsFolderId -> FolderType.DRAFTS
                account.sentFolderId -> FolderType.SENT
                account.spamFolderId -> FolderType.SPAM
                account.trashFolderId -> FolderType.TRASH
                else -> FolderType.REGULAR
            }
        }
    }
}
