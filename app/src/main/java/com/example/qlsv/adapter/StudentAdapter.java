package com.example.qlsv.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv.R;
import com.example.qlsv.model.Student;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<Student> studentList;
    private OnStudentClickListener onStudentClickListener;

    public StudentAdapter(List<Student> studentList, OnStudentClickListener listener) {
        this.studentList = studentList;
        this.onStudentClickListener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.textViewName.setText(student.getName());
        holder.textViewStudentId.setText(student.getStudentId());
        holder.textViewClassName.setText(student.getClassName());

        // Thiết lập sự kiện nhấn vào mỗi mục sinh viên
        holder.itemView.setOnClickListener(v -> onStudentClickListener.onStudentClick(student));
        holder.itemView.setOnLongClickListener(v -> {
            onStudentClickListener.onStudentLongClick(student);
            return true; // Trả về true để thông báo rằng sự kiện đã được xử lý
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewStudentId, textViewClassName;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewStudentId = itemView.findViewById(R.id.textViewStudentId);
            textViewClassName = itemView.findViewById(R.id.textViewClassName);
        }
    }

    // Interface để xử lý sự kiện nhấn vào sinh viên
    public interface OnStudentClickListener {
        void onStudentClick(Student student);
        void onStudentLongClick(Student student);
    }
}