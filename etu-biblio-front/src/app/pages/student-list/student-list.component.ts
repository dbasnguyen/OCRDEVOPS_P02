import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StudentService, Student } from '../../services/student.service';

@Component({
  selector: 'app-student-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student-list.component.html',
  styleUrls: ['./student-list.component.css']
})
export class StudentListComponent implements OnInit {

  students: Student[] = [];
  newStudent: Student = { firstName: '', lastName: '', email: '' };

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    this.loadStudents();
  }

  loadStudents() {
    this.studentService.getStudents().subscribe(students => {
      this.students = students;
    });
  }

  addStudent() {
    if (!this.newStudent.firstName || !this.newStudent.lastName || !this.newStudent.email) {
      return;
    }
    this.studentService.addStudent(this.newStudent).subscribe(() => {
      this.newStudent = { firstName: '', lastName: '', email: '' };
      this.loadStudents();
    });
  }

  deleteStudent(id?: number) {
    if (!id) return;
    this.studentService.deleteStudent(id).subscribe(() => {
      this.loadStudents();
    });
  }
}
