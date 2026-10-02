import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize } from 'rxjs';
import { ChangeDetectorRef } from '@angular/core';

import { StudentService } from '../../services/student.service';
import { Student } from '../../models/student';
import { Course } from '../../models/course';

@Component({
  selector: 'app-student-detail',
  standalone: true,
  templateUrl: './student-detail.component.html'
})
export class StudentDetailComponent implements OnInit {

  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly studentService = inject(StudentService);
  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  student?: Student;
  registeredCourses: Course[] = [];

  loading = false;
  errorMessage = '';

  ngOnInit(): void {
  const studentId = Number(
    this.route.snapshot.paramMap.get('id')
  );

  console.log(
    '[StudentDetail] ngOnInit, studentId:',
    studentId
  );

  this.loadStudent(studentId);
}

  loadStudent(studentId: number): void {
  console.log('[StudentDetail] loadStudent:', studentId);

  this.loading = true;
  this.errorMessage = '';

  this.studentService.getStudent(studentId).subscribe({
    next: student => {
      console.log('[StudentDetail] API response:', student);

      this.student = student;
      this.registeredCourses = student.courses ?? [];

      console.log(
        '[StudentDetail] registeredCourses:',
        this.registeredCourses
      );

      this.loading = false;

      this.changeDetectorRef.detectChanges();

      console.log('[StudentDetail] UI updated');
    },

    error: error => {
      console.error(
        '[StudentDetail] API error:',
        error
      );

      this.errorMessage = 'Unable to load student.';
      this.loading = false;

      this.changeDetectorRef.detectChanges();
    }
  });
}

  editStudent(): void {
    if (!this.student) {
      return;
    }

    this.router.navigate([
      '/students',
      this.student.id,
      'edit'
    ]);
  }

  deleteStudent(): void {
    if (!this.student) {
      return;
    }

    if (!confirm('Are you sure you want to delete this student?')) {
      return;
    }

    this.studentService.deleteStudent(this.student.id).subscribe({
      next: () => {
        this.router.navigate(['/students']);
      },

      error: (error) => {
        console.error(
          '[StudentDetail] Failed to delete student:',
          error
        );

        this.errorMessage = 'Unable to delete student.';
      }
    });
  }

  registerCourses(): void {
    if (!this.student) {
      return;
    }

    this.router.navigate([
      '/students',
      this.student.id,
      'register-courses'
    ]);
  }
  
  unregisterCourse(courseId: number): void {
    if (!this.student) {
      return;
    }

    if (!window.confirm('Are you sure you want to unregister this course?')) {
      return;
    }

    this.studentService.unregisterCourses(
      this.student.id,
      [courseId]
    ).subscribe({
      next: () => {
        this.loadStudent(this.student!.id);
      },
      error: error => {
        console.error(
          '[StudentDetail] Unable to unregister course:',
          error
        );

        this.errorMessage = 'Unable to unregister course.';
      }
    });
  }

  backToStudents(): void {
    this.router.navigate(['/students']);
  }
}