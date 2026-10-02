import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { Course } from '../../models/course';
import { Student } from '../../models/student';
import { CourseService } from '../../services/course.service';
import { StudentService } from '../../services/student.service';

@Component({
  selector: 'app-course-registration',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './course-registration.component.html'
})
export class CourseRegistrationComponent implements OnInit {

  private readonly studentService = inject(StudentService);
  private readonly courseService = inject(CourseService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  student?: Student;

  courses: Course[] = [];
  availableCourses: Course[] = [];
  filteredCourses: Course[] = [];

  selectedCourseIds = new Set<number>();

  searchText = '';

  saving = false;
  errorMessage = '';

  ngOnInit(): void {
    const studentId = Number(
      this.route.snapshot.paramMap.get('id')
    );

    this.loadData(studentId);
  }

  private loadData(studentId: number): void {
    this.errorMessage = '';

    this.studentService.getStudent(studentId).subscribe({
      next: student => {
        console.log('[CourseRegistration] Student loaded:', student);

        this.student = student;

        this.loadCourses();
      },
      error: error => {
        console.error(
          '[CourseRegistration] Unable to load student:',
          error
        );

        this.errorMessage = 'Unable to load student.';

        this.changeDetectorRef.detectChanges();
      }
    });
  }

  private loadCourses(): void {
    this.courseService.getAllCourses().subscribe({
      next: courses => {
        console.log(
          '[CourseRegistration] Courses loaded:',
          courses
        );

        this.courses = courses;

        const registeredCourseIds = new Set(
          this.student?.courses?.map(course => course.id) ?? []
        );

        this.availableCourses = courses.filter(
          course => !registeredCourseIds.has(course.id)
        );

        // Initially show all available courses
        this.filteredCourses = [...this.availableCourses];

        this.changeDetectorRef.detectChanges();
      },
      error: error => {
        console.error(
          '[CourseRegistration] Unable to load courses:',
          error
        );

        this.errorMessage = 'Unable to load courses.';

        this.changeDetectorRef.detectChanges();
      }
    });
  }

  searchCourses(): void {
    const search = this.searchText.trim().toLowerCase();

    this.filteredCourses = this.availableCourses.filter(course =>
      !search ||
      course.name.toLowerCase().includes(search)
    );

    this.changeDetectorRef.detectChanges();
  }

  toggleCourse(courseId: number): void {
    if (this.selectedCourseIds.has(courseId)) {
      this.selectedCourseIds.delete(courseId);
    } else {
      this.selectedCourseIds.add(courseId);
    }

    this.changeDetectorRef.detectChanges();
  }

  isSelected(courseId: number): boolean {
    return this.selectedCourseIds.has(courseId);
  }

  registerCourses(): void {
    if (!this.student || !this.selectedCourseIds.size) {
      return;
    }

    this.errorMessage = '';

    this.studentService.registerCourses(
      this.student.id,
      Array.from(this.selectedCourseIds)
    ).subscribe({
      next: () => {
        this.router.navigate([
          '/students',
          this.student!.id
        ]);
      },
      error: error => {
        console.error(
          '[CourseRegistration] Unable to register courses:',
          error
        );

        this.errorMessage = 'Unable to register courses.';
        this.saving = false;

        this.changeDetectorRef.detectChanges();
      }
    });
  }

  cancel(): void {
    if (this.student) {
      this.router.navigate([
        '/students',
        this.student.id
      ]);
    }
  }
}