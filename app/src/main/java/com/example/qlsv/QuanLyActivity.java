package com.example.qlsv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv.adapter.StudentAdapter;
import com.example.qlsv.model.Student;
import com.example.qlsv.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class QuanLyActivity extends AppCompatActivity implements StudentAdapter.OnStudentClickListener {

    private EditText editTextStudentName, editTextStudentId, editTextClassName;
    private Button buttonAddStudent, buttonUpdateStudent, buttonDeleteStudent, buttonOut;
    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private DatabaseHelper databaseHelper;
    private List<Student> studentList;
    private Student selectedStudent; // Sinh viên được chọn để sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);

        // Khởi tạo các thành phần giao diện
        editTextStudentName = findViewById(R.id.editTextStudentName);
        editTextStudentId = findViewById(R.id.editTextStudentId);
        editTextClassName = findViewById(R.id.editTextClassName);
        buttonAddStudent = findViewById(R.id.buttonAddStudent);
        buttonUpdateStudent = findViewById(R.id.buttonUpdateStudent);
        buttonDeleteStudent = findViewById(R.id.buttonDeleteStudent);
        recyclerView = findViewById(R.id.recyclerView);
        buttonOut = findViewById(R.id.buttonLogout);

        // Khởi tạo DatabaseHelper và danh sách sinh viên
        databaseHelper = new DatabaseHelper(this);
        studentList = new ArrayList<>(databaseHelper.getAllStudents());
        studentAdapter = new StudentAdapter(studentList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(studentAdapter);

        // Thiết lập sự kiện nhấn nút thêm sinh viên
        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });

        // Thiết lập sự kiện nhấn nút sửa sinh viên
        buttonUpdateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent();
            }
        });

        // Thiết lập sự kiện nhấn nút xóa sinh viên
        buttonDeleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStudent != null) {
                    databaseHelper.deleteStudent(selectedStudent.getStudentId());
                    refreshStudentList();
                    clearInputFields();
                    Toast.makeText(QuanLyActivity.this, "Sinh viên đã được xóa", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuanLyActivity.this, "Vui lòng chọn sinh viên để xóa", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Thiết lập sự kiện nhấn nút đăng xuất
        buttonOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }

    private void addStudent() {
       //lấy dữ liệu từ các trường nập liệu
        String name = editTextStudentName.getText().toString().trim();
        String studentId = editTextStudentId.getText().toString().trim();
        String className = editTextClassName.getText().toString().trim();

        if (!name.isEmpty() && !studentId.isEmpty() && !className.isEmpty()) {
            Student student = new Student(name, studentId, className);
            databaseHelper.addStudent(student);
            refreshStudentList();
            clearInputFields();
        } else {
            Toast.makeText(QuanLyActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateStudent() {
        String name = editTextStudentName.getText().toString();
        String studentId = editTextStudentId.getText().toString();
        String className = editTextClassName.getText().toString();
           // ktra xem các trường nhập có hợp lệ k
        if (selectedStudent != null && !name.isEmpty() && !studentId.isEmpty() && !className.isEmpty()) {
            selectedStudent.setName(name);
            selectedStudent.setClassName(className);
            databaseHelper.updateStudent(selectedStudent);
            refreshStudentList();
            clearInputFields();
            selectedStudent = null; // cập nhật lại ds
        } else {
            Toast.makeText(QuanLyActivity.this, "Vui lòng chọn sinh viên để cập nhật", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshStudentList() {
        studentList.clear();
        studentList.addAll(databaseHelper.getAllStudents());
        studentAdapter.notifyDataSetChanged();
    }

    private void clearInputFields() {
        editTextStudentName.setText("");
        editTextStudentId.setText("");
        editTextClassName.setText("");
    }

    private void logOut() {
        startActivity(new Intent(QuanLyActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onStudentClick(Student student) {
        // Điền thông tin sinh viên vào các trường để sửa
        editTextStudentName.setText(student.getName());
        editTextStudentId.setText(student.getStudentId());
        editTextClassName.setText(student.getClassName());
        selectedStudent = student; // Lưu sinh viên được chọn
    }

    @Override
    public void onStudentLongClick(Student student) {
        // Xóa sinh viên
        databaseHelper.deleteStudent(student.getStudentId());
        refreshStudentList();
        Toast.makeText(this, "Sinh viên đã được xóa", Toast.LENGTH_SHORT).show();
    }
}