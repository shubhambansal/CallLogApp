package test.app.calllog.data.local

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import timber.log.Timber

class ContactDBHelper(private val context: Context) {

    /**
     * Function to resolve contactName from given phoneNumber from local contact book in the device
     *
     * @return empty when no contact found else contactName
     */
    fun resolveContactNameFromNumber(phoneNumber: String): String {

        var contactName = ""

        if (phoneNumber.isEmpty()) {
            return contactName
        }


        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )
        val cursor = context.contentResolver.query(
            uri,
            arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),
            null,
            null,
            null
        )

        if (cursor?.moveToFirst() == true) {
            contactName =
                cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
            Timber.d("name = $contactName")
        } else {
            Timber.d("Not able to resolve contact name for $phoneNumber")
        }

        if (cursor != null && cursor.isClosed.not()) {
            cursor.close()
        }

        return contactName
    }
}