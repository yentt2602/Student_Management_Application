package com.example.qlsv.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.qlsv.model.Student;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_STUDENTS = "students";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_STUDENT_ID = "student_id";
    private static final String COLUMN_CLASS_NAME = "class_name"; // Thêm trường lớp
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password"; // Lưu mật khẩu (nên mã hóa)

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStudentsTable = "CREATE TABLE " + TABLE_STUDENTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_STUDENT_ID + " TEXT, " +
                COLUMN_CLASS_NAME + " TEXT)";
        db.execSQL(createStudentsTable);

        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    public void addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_STUDENT_ID, student.getStudentId());
        values.put(COLUMN_CLASS_NAME, student.getClassName()); // Thêm trường lớp
        db.insert(TABLE_STUDENTS, null, values);
        db.close();
    }
    public void addUser (String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password); // Nên mã hóa mật khẩu trước khi lưu
        db.insert(TABLE_USERS, null, values);
        db.close();
    }
    public boolean validateUser (String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{username, password}, null, null, null);
        boolean isValid = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return isValid;
    }
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Chỉ định các cột cần thiết
            String[] columns = {COLUMN_NAME, COLUMN_STUDENT_ID, COLUMN_CLASS_NAME};
            cursor = db.query(TABLE_STUDENTS, columns, null, null, null, null, null);

            // Kiểm tra nếu có dữ liệu trong cursor
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                    String studentId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID));
                    String className = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_NAME));
                    studentList.add(new Student(name, studentId, className));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi nếu cần
        } finally {
            // Đảm bảo đóng cursor và database
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return studentList;
    }

    public void updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_STUDENT_ID, student.getStudentId());
        values.put(COLUMN_CLASS_NAME, student.getClassName());

        // Update the student record based on the student ID
        int rowsAffected = db.update(TABLE_STUDENTS, values, COLUMN_STUDENT_ID + " = ?", new String[]{student.getStudentId()});

        if (rowsAffected == 0) {
            System.out.println("Lỗi ");
        }

        db.close();
    }

    public void deleteStudent(String studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENTS, COLUMN_STUDENT_ID + " = ?", new String[]{studentId});
        db.close();
    }
}
