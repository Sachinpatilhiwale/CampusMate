package com.mountreach.campusmanagementsystem.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CampusManagement.db";
    private static final int DATABASE_VERSION = 16; // 🔥 BUMPED VERSION TO CREATE TIMETABLE TABLE

    // USERS TABLE
    private static final String TABLE_USERS = "Users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USER_NAME = "name";
    private static final String COL_USER_EMAIL = "email";
    private static final String COL_USER_PASSWORD = "password";
    private static final String COL_USER_ROLE = "role";

    // PROFILE TABLE
    private static final String TABLE_PROFILE = "UserProfile";
    private static final String COL_PROFILE_ID = "id";
    private static final String COL_PROFILE_NAME = "name";
    private static final String COL_PROFILE_EMAIL = "email";
    private static final String COL_PROFILE_BRANCH = "branch";
    private static final String COL_PROFILE_YEAR = "year";
    private static final String COL_PROFILE_GENDER = "gender";

    // STUDENTS TABLE
    private static final String TABLE_STUDENTS = "Students";
    private static final String COL_STUDENT_ID = "id";
    private static final String COL_STUDENT_ROLL = "rollno";
    private static final String COL_STUDENT_ENROLL = "enrollment";
    private static final String COL_STUDENT_NAME = "student_name";

    // ATTENDANCE TABLE
    private static final String TABLE_ATTENDANCE = "Attendance";
    private static final String COL_A_ID = "id";
    private static final String COL_A_NAME = "student_name";
    private static final String COL_A_ROLL = "roll_no";
    private static final String COL_A_ENROLL = "enroll_no";
    private static final String COL_A_SUBJECT = "subject";
    private static final String COL_A_STATUS = "status";
    private static final String COL_A_DATE = "date";

    // NOTICE TABLE
    private static final String CREATE_NOTICE_TABLE =
            "CREATE TABLE Notice (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "notice_text TEXT, " +
                    "timestamp TEXT)";

    // =======================================================================
    // 🔥🔥 TIMETABLE TABLE (NEW)
    // =======================================================================
    public static final String TABLE_TIMETABLE = "timetable";

    public static final String CREATE_TIMETABLE =
            "CREATE TABLE " + TABLE_TIMETABLE + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "subject TEXT," +
                    "teacher TEXT," +
                    "start_time TEXT," +
                    "end_time TEXT," +
                    "day TEXT," +
                    "room TEXT)";
    // =======================================================================

    // StudyMaterial Table
    private static final String TABLE_STUDY = "study_material";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_DESC = "description";
    private static final String COL_SUBJECT = "subject";
    private static final String COL_URI = "file_uri";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // USERS
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NAME + " TEXT, " +
                COL_USER_EMAIL + " TEXT UNIQUE, " +
                COL_USER_PASSWORD + " TEXT, " +
                COL_USER_ROLE + " TEXT)");

        // PROFILE
        db.execSQL("CREATE TABLE " + TABLE_PROFILE + " (" +
                COL_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PROFILE_NAME + " TEXT, " +
                COL_PROFILE_EMAIL + " TEXT UNIQUE, " +
                COL_PROFILE_BRANCH + " TEXT, " +
                COL_PROFILE_YEAR + " TEXT, " +
                COL_PROFILE_GENDER + " TEXT)");

        // STUDENTS
        db.execSQL("CREATE TABLE " + TABLE_STUDENTS + " (" +
                COL_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STUDENT_ROLL + " INTEGER, " +
                COL_STUDENT_ENROLL + " TEXT UNIQUE, " +
                COL_STUDENT_NAME + " TEXT)");

        // ATTENDANCE
        // ATTENDANCE (UPDATED WITH UNIQUE CONSTRAINT)
        db.execSQL("CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                COL_A_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_A_NAME + " TEXT, " +
                COL_A_ROLL + " TEXT, " +
                COL_A_ENROLL + " TEXT, " +
                COL_A_SUBJECT + " TEXT, " +
                COL_A_STATUS + " TEXT, " +
                COL_A_DATE + " TEXT, " +
                "UNIQUE(" + COL_A_ENROLL + ", " + COL_A_SUBJECT + ", " + COL_A_DATE + "))");

        // NOTICE
        db.execSQL(CREATE_NOTICE_TABLE);

        // 🔥 TIMETABLE
        db.execSQL(CREATE_TIMETABLE);

        //Study Material
        db.execSQL("CREATE TABLE " + TABLE_STUDY + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_DESC + " TEXT, " +
                COL_SUBJECT + " TEXT, " +
                COL_URI + " TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS Notice");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMETABLE); // 🔥 drop timetable
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDY);
        onCreate(db);
    }


    // =======================================================================
    // 🔥🔥 TIMETABLE CRUD METHODS
    // =======================================================================

    // Insert timetable row
    public void insertTimetable(String subject, String teacher,
                                String start, String end,
                                String day, String room) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("subject", subject);
        cv.put("teacher", teacher);
        cv.put("start_time", start);
        cv.put("end_time", end);
        cv.put("day", day);
        cv.put("room", room);

        db.insert(TABLE_TIMETABLE, null, cv);
    }

    // Fetch timetable by day
    public Cursor getTimetableByDay(String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_TIMETABLE + " WHERE day=?",
                new String[]{day}
        );
    }


    // Update
    public void updateTimetable(long id, String subject, String teacher,
                                String start, String end, String room) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("subject", subject);
        cv.put("teacher", teacher);
        cv.put("start_time", start);
        cv.put("end_time", end);
        cv.put("room", room);

        db.update(TABLE_TIMETABLE, cv, "id=?",
                new String[]{String.valueOf(id)});
    }


    // Delete
    public void deleteTimetable(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TIMETABLE, "id=?",
                new String[]{String.valueOf(id)});
    }



    // =======================================================================
    // EXISTING CODE (UNCHANGED)
    // =======================================================================

    public boolean insertUser(String name, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_NAME, name);
        cv.put(COL_USER_EMAIL, email);
        cv.put(COL_USER_PASSWORD, password);
        cv.put(COL_USER_ROLE, role);
        return db.insert(TABLE_USERS, null, cv) != -1;
    }

    public String checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM Users WHERE email=? AND password=?", new String[]{email, password});
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return null;
    }

    public boolean insertUserProfile(String name, String email, String branch, String year, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PROFILE_NAME, name);
        cv.put(COL_PROFILE_EMAIL, email);
        cv.put(COL_PROFILE_BRANCH, branch);
        cv.put(COL_PROFILE_YEAR, year);
        cv.put(COL_PROFILE_GENDER, gender);
        return db.insert(TABLE_PROFILE, null, cv) != -1;
    }

    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM UserProfile WHERE email=?", new String[]{email});
    }

    public String getUserYear(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT year FROM UserProfile WHERE email=?", new String[]{email});
        String year = null;
        if (cursor.moveToFirst()) {
            year = cursor.getString(cursor.getColumnIndexOrThrow("year"));
        }
        cursor.close();
        return year;
    }


    public boolean updateUserProfile(String email, String name, String branch, String year, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PROFILE_NAME, name);
        cv.put(COL_PROFILE_BRANCH, branch);
        cv.put(COL_PROFILE_YEAR, year);
        cv.put(COL_PROFILE_GENDER, gender);
        return db.update(TABLE_PROFILE, cv, "email=?", new String[]{email}) > 0;
    }

    public boolean addStudent(int roll, String enrollment, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_STUDENT_ROLL, roll);
        cv.put(COL_STUDENT_ENROLL, enrollment);
        cv.put(COL_STUDENT_NAME, name);
        return db.insert(TABLE_STUDENTS, null, cv) != -1;
    }

    public Cursor getStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Students", null);
    }


    public boolean insertNotice(String noticeText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("notice_text", noticeText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            cv.put("timestamp", java.time.LocalDateTime.now().toString());
        } else {
            cv.put("timestamp", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new java.util.Date()));
        }

        return db.insert("Notice", null, cv) != -1;
    }

    public Cursor getAllNotices() {
        return getReadableDatabase()
                .rawQuery("SELECT * FROM Notice ORDER BY id DESC", null);
    }

    //Study Material
    // Insert Study Material
    public long insertStudyMaterial(String title, String fileUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_URI, fileUri);
        return db.insert(TABLE_STUDY, null, cv);
    }

        // Get all materials
        public Cursor getAllStudyMaterial () {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("SELECT * FROM " + TABLE_STUDY + " ORDER BY id DESC", null);
        }

    // Delete user by email (removes both user login & profile)
    public boolean deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete from Users table
        int userDeleted = db.delete("Users", "email=?", new String[]{email});

        // Delete from UserProfile table
        int profileDeleted = db.delete("UserProfile", "email=?", new String[]{email});

        return userDeleted > 0 || profileDeleted > 0;
    }

    public boolean isAttendanceMarked(String enroll, String subject, String date) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_ATTENDANCE +
                        " WHERE " + COL_A_ENROLL + "=? AND " +
                        COL_A_SUBJECT + "=? AND " +
                        COL_A_DATE + "=?",
                new String[]{enroll, subject, date});

        boolean exists = cursor.getCount() > 0;
        cursor.close();

        return exists;
    }

    public boolean updateAttendance(String enroll, String subject, String date, String status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_A_STATUS, status);

        int result = db.update(TABLE_ATTENDANCE,
                cv,
                COL_A_ENROLL + "=? AND " +
                        COL_A_SUBJECT + "=? AND " +
                        COL_A_DATE + "=?",
                new String[]{enroll, subject, date});

        return result > 0;
    }

    public boolean deleteAttendance(String enroll, String subject, String date) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_ATTENDANCE,
                COL_A_ENROLL + "=? AND " +
                        COL_A_SUBJECT + "=? AND " +
                        COL_A_DATE + "=?",
                new String[]{enroll, subject, date});

        return result > 0;
    }

    public Cursor getAttendanceByDate(String date) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_ATTENDANCE +
                        " WHERE " + COL_A_DATE + "=?",
                new String[]{date});
    }

    public Cursor getAttendanceBySubject(String enroll, String subject) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_ATTENDANCE +
                        " WHERE " + COL_A_ENROLL + "=? AND " +
                        COL_A_SUBJECT + "=?",
                new String[]{enroll, subject});
    }

    public int getTotalClasses(String enroll, String subject) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_ATTENDANCE +
                        " WHERE " + COL_A_ENROLL + "=? AND " +
                        COL_A_SUBJECT + "=?",
                new String[]{enroll, subject});

        int total = 0;

        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }

        cursor.close();
        return total;
    }

    public int getTotalPresent(String enroll, String subject) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_ATTENDANCE +
                        " WHERE " + COL_A_ENROLL + "=? AND " +
                        COL_A_SUBJECT + "=? AND " +
                        COL_A_STATUS + "='Present'",
                new String[]{enroll, subject});

        int total = 0;

        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }

        cursor.close();
        return total;
    }

    public float getAttendancePercentage(String enroll, String subject) {

        int totalClasses = getTotalClasses(enroll, subject);
        int totalPresent = getTotalPresent(enroll, subject);

        if (totalClasses == 0) {
            return 0;
        }

        return (totalPresent * 100f) / totalClasses;
    }

    public Cursor getFullAttendanceReport(String enroll) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " + COL_A_SUBJECT + ", " +
                        "COUNT(*) as total_classes, " +
                        "SUM(CASE WHEN " + COL_A_STATUS + "='Present' THEN 1 ELSE 0 END) as total_present " +
                        "FROM " + TABLE_ATTENDANCE +
                        " WHERE " + COL_A_ENROLL + "=? " +
                        "GROUP BY " + COL_A_SUBJECT,
                new String[]{enroll});
    }

}
