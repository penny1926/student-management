import { ChangeDetectorRef,Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { StudentService } from '../../services/student.service';
import { StudentRequest } from '../../models/student-request';

@Component({
  selector: 'app-student-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './student-form.component.html'
})
export class StudentFormComponent implements OnInit {
  private readonly changeDetectorRef = inject(ChangeDetectorRef);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly studentService = inject(StudentService);

  studentId?: number;

  firstName = '';
  lastName = '';
  email = '';

  errorMessage = '';

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.studentId = Number(id);
      this.loadStudent(this.studentId);
    }
  }

  private loadStudent(studentId: number): void {
    this.studentService.getStudent(studentId).subscribe({
      next: student => {
        console.log('[StudentForm] Student loaded:', student);
        this.firstName = student.firstName;
        this.lastName = student.lastName;
        this.email = student.email;
        this.changeDetectorRef.detectChanges();
      },
      error: error => {
        console.error(
          '[StudentForm] Unable to load student:',
          error
        );

        this.errorMessage = 'Unable to load student.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  save(): void {
    if (this.studentId === undefined) {
      this.createStudent();
      return;
    }

    const request: StudentRequest = {
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email
    };

    this.studentService.updateStudent(
      this.studentId,
      request
    ).subscribe({
      next: () => {
        this.router.navigate([
          '/students',
          this.studentId
        ]);
      },
      error: error => {
        console.error(
          '[StudentForm] Unable to update student:',
          error
        );

        this.errorMessage = 'Unable to update student.';
      }
    });
  }

  private createStudent(): void {
    const request: StudentRequest = {
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email
    };

    this.studentService.createStudent(request).subscribe({
      next: () => {
        this.router.navigate(['/students']);
      },

      error: error => {
        console.error(
          '[StudentForm] Unable to create student:',
          error
        );

        this.errorMessage = 'Unable to create student.';
      }
    });
  }

  cancel(): void {
    if (this.studentId !== undefined) {
      this.router.navigate([
        '/students',
        this.studentId
      ]);
    } else {
      this.router.navigate(['/students']);
    }
  }
}