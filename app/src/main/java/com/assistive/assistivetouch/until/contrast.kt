package com.assistive.assistivetouch.until

class contrast {
    companion object {
        val TAG ="thiennu"
        val DATABASE_NAME="touch_db.db"
        val TABLE_NAME="eventcustom"
        val ID="id"
        val COLUMNS_1="idevent"
        val COLUMNS_2 ="iconevent"
        val COLUMNS_3 ="nameevent"
        val CREATE_TABLE: String="CREATE TABLE IF NOT EXISTS $TABLE_NAME " +
                "($ID Integer PRIMARY KEY AUTOINCREMENT, $COLUMNS_1 INTEGER, $COLUMNS_2 INTEGER, $COLUMNS_3 TEXT )"
       // table 2 save package app name
        val TABLE_NAME2="appcustom"
        val ID2="id2"
        val COLUMNS_32 ="packagename"
        val COLUMNS_33 ="appname"
        val CREATE_TABLE2: String="CREATE TABLE IF NOT EXISTS $TABLE_NAME2 " +
                "($ID2 Integer PRIMARY KEY AUTOINCREMENT,$COLUMNS_32 TEXT ,$COLUMNS_33)"
        //putID Button App
        val PUTIDAPP="put_idbutton_packagename"

        //tablename 3 Fakedata
        val TABLE_NAMEFAKE="eventfake"
        val IDFAKE="idfake"
        val COLUMNS_1FAKE="ideventfake"
        val COLUMNS_2FAKE ="iconeventfake"
        val COLUMNS_3FAKE ="nameeventfake"
        val CREATE_TABLE_FAKE: String="CREATE TABLE IF NOT EXISTS $TABLE_NAMEFAKE " +
                "($IDFAKE Integer PRIMARY KEY AUTOINCREMENT, $COLUMNS_1FAKE INTEGER, $COLUMNS_2FAKE INTEGER, $COLUMNS_3FAKE TEXT )"
        val DROPTABLE_FAKE="DROP TABLE IF EXISTS "+ TABLE_NAMEFAKE
        val DROPTABLE_1="DROP TABLE IF EXISTS "+ TABLE_NAME

        val DROPTABLE_2="DROP TABLE IF EXISTS "+ TABLE_NAME2


    }
}