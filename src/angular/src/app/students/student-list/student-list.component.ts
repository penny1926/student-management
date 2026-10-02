import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { StudentService } from '../../services/student.service';
import { Student } from '../../models/student';

@Component({
  selector: 'app-student-list',
  imports: [FormsModule],
  standalone: true,
  templateUrl: './student-list.component.html'
})
export class StudentListComponent implements OnInit {

  private readonly studentService = inject(StudentService);
  private readonly router = inject(Router);

  students: Student[] = [];
  filteredStudents: Student[] = [];

  firstName = '';
  lastName = '';
  email = '';

  showResults = false;
  errorMessage = '';

  ngOnInit(): void {
    this.loadStudents();
  }

  loadStudents(): void {
    this.errorMessage = '';
    this.studentService.getAllStudents().subscribe({
      next: students => {
        this.students = students;
        this.filteredStudents = students;
      },
      error: error => {
        console.error('[StudentList] Unable to load students:', error);
        this.errorMessage = 'Unable to load students.';
      }
    });
  }

  search(): void {
    const firstName = this.firstName.trim().toLowerCase();
    const lastName = this.lastName.trim().toLowerCase();
    const email = this.email.trim().toLowerCase();

    this.filteredStudents = this.students.filter(student => {
      const matchesFirstName =
        !firstName ||
        student.firstName.toLowerCase().includes(firstName);

      const matchesLastName =
        !lastName ||
        student.lastName.toLowerCase().includes(lastName);

      const matchesEmail =
        !email ||
        student.email.toLowerCase().includes(email);

      return (
        matchesFirstName &&
        matchesLastName &&
        matchesEmail
      );
    });
    
    this.showResults = true;
  }

  clearSearch(): void {
    this.firstName = '';
    this.lastName = '';
    this.email = '';
    this.filteredStudents = [];
    this.showResults = false;
  }

  viewStudent(studentId: number): void {
    this.router.navigate(['/students', studentId]);
  }

  createStudent(): void {
    this.router.navigate(['/students/new']);
  }
}