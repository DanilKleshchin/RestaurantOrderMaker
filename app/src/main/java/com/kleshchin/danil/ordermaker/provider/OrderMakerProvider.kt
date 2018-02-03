package com.kleshchin.danil.ordermaker.provider

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import com.kleshchin.danil.ordermaker.BuildConfig


class OrderMakerProvider : ContentProvider() {
    private val TABLE_CATEGORY_CODE = 0
    private val TABLE_MEAL_CODE = 1

    private lateinit var dbHelper_: DatabaseHelper
    private lateinit var database_: SQLiteDatabase

    private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH)

    init {
        URI_MATCHER.addURI(AUTHORITY, DatabaseHelper.CATEGORY_TABLE, TABLE_CATEGORY_CODE)
        URI_MATCHER.addURI(AUTHORITY, DatabaseHelper.MEAL_TABLE, TABLE_MEAL_CODE)
    }

    companion object {
        private val AUTHORITY = BuildConfig.APPLICATION_ID + ".db"
        fun createUrlForTable(table: String): Uri {
            return Uri.parse("content://$AUTHORITY/$table")
        }
    }


    override fun onCreate(): Boolean {
        val context: Context? = context
        if (context != null) {
            dbHelper_ = DatabaseHelper(context)
        }
        return true
    }

    override fun query(p0: Uri?, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor {
        database_ = dbHelper_.readableDatabase;
        val builder = SQLiteQueryBuilder()
        val cursor: Cursor
        when (URI_MATCHER.match(p0)) {
            TABLE_CATEGORY_CODE -> builder.tables = DatabaseHelper.CATEGORY_TABLE
            TABLE_MEAL_CODE -> builder.tables = DatabaseHelper.MEAL_TABLE
            else -> throw IllegalArgumentException("uri not recognized!")
        }
        cursor = builder.query(database_, p1, p2, p3, null, null, p4, null)
        val context = context
        if (context != null) {
            cursor.setNotificationUri(context.contentResolver, p0)
        }
        return cursor
    }

    override fun insert(p0: Uri?, p1: ContentValues?): Uri {
        var table = ""
        when (URI_MATCHER.match(p0)) {
            TABLE_CATEGORY_CODE -> table = DatabaseHelper.CATEGORY_TABLE
            TABLE_MEAL_CODE -> table = DatabaseHelper.MEAL_TABLE
        }
        val result = dbHelper_.writableDatabase.insertWithOnConflict(table, null, p1, SQLiteDatabase.CONFLICT_REPLACE)
        return ContentUris.withAppendedId(p0, result)
    }

    override fun delete(p0: Uri?, p1: String?, p2: Array<out String>?): Int {
        database_ = dbHelper_.writableDatabase
        var table = ""
        when (URI_MATCHER.match(p0)) {
            TABLE_CATEGORY_CODE -> table = DatabaseHelper.CATEGORY_TABLE
            TABLE_MEAL_CODE -> table = DatabaseHelper.MEAL_TABLE
        }
        return database_.delete(table, p1, p2)
    }

    override fun update(p0: Uri?, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return -1
    }

    override fun getType(p0: Uri?): String {
        return BuildConfig.APPLICATION_ID + ".item"
    }

}
