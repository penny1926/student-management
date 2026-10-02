import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute,Router } from '@angular/router';

import { Course } from '../../models/course';
import { CourseService } from '../../services/course.service';

@Component({
  selector: 'app-course-list',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './course-list.component.html'
})
export class CourseListComponent implements OnInit {

  private readonly courseService = inject(CourseService);
  private readonly router = inject(Router);
  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  courses: Course[] = [];
  filteredCourses: Course[] = [];

  searchText = '';
  showResults = false;
  errorMessage = '';

  ngOnInit(): void {
    this.loadCourses();
  }

  loadCourses(): void {
    this.errorMessage = '';

    this.courseService.getAllCourses().subscribe({
      next: courses => {
        this.courses = courses;
      },

      error: error => {
        console.error(
          '[CourseList] Unable to load courses:',
          error
        );

        this.errorMessage = 'Unable to load courses.';
      }
    });
  }

  searchCourses(): void {
    const search = this.searchText.trim().toLowerCase();
   console.log("clicked");
    this.filteredCourses = this.courses.filter(course =>
      !search ||
      course.name.toLowerCase().includes(search)
    );

    this.showResults = true;
  }

  clearSearch(): void {
    this.searchText = '';
    this.filteredCourses = [];
    this.showResults = false;
  }

  createCourse(): void {
    this.router.navigate(['/courses/new']);
  }

  editCourse(courseId: number): void {
    this.router.navigate([
      '/courses',
      courseId,
      'edit'
    ]);
  }

  deleteCourse(course: Course): void {
    if (!window.confirm(`Are you sure you want to delete "${course.name}"?`)) {
      return;
    }

    this.courseService.deleteCourse(course.id).subscribe({
      next: () => {
        console.log('delete successful');

        this.courseService.getAllCourses().subscribe({
          next: courses => {
            console.log('courses after delete:', courses);

            this.courses = courses;

            this.searchText = '';
            this.filteredCourses = [...courses];
            this.showResults = true;

            this.changeDetectorRef.detectChanges();
          },
          error: error => {
            console.error(
              '[CourseList] Unable to reload courses:',
              error
            );

            this.errorMessage = 'Unable to reload courses.';
            this.changeDetectorRef.detectChanges();
          }
        });
      },
      error: error => {
        console.error(
          '[CourseList] Unable to delete course:',
          error
        );

        this.errorMessage = 'Unable to delete course.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }
}