import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { CourseRequest } from '../../models/course-request';
import { CourseService } from '../../services/course.service';

@Component({
  selector: 'app-course-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './course-form.component.html'
})
export class CourseFormComponent implements OnInit {

  private readonly courseService = inject(CourseService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  courseId?: number;

  name = '';
  description = '';

  errorMessage = '';

  get editMode(): boolean {
    return this.courseId !== undefined;
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.courseId = Number(id);
      this.loadCourse();
    }
  }

  private loadCourse(): void {
    this.courseService.getCourse(this.courseId!).subscribe({
      next: course => {
        this.name = course.name;
        this.description = course.description;
        this.changeDetectorRef.detectChanges();
      },
      error: error => {
        console.error(
          '[CourseForm] Unable to load course:',
          error
        );

        this.errorMessage = 'Unable to load course.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  save(): void {
    if (!this.name.trim() || !this.description.trim()) {
      this.errorMessage = 'All fields are required.';
      return;
    }

    this.errorMessage = '';

    const request: CourseRequest = {
      name: this.name.trim(),
      description: this.description.trim()
    };

    if (this.editMode) {
      this.courseService.updateCourse(
        this.courseId!,
        request
      ).subscribe({
        next: () => {
          this.router.navigate(['/courses']);
        },
        error: error => {
          console.error(
            '[CourseForm] Unable to update course:',
            error
          );

          this.errorMessage = 'Unable to update course.';
        }
      });

      return;
    }

    this.courseService.createCourse(request).subscribe({
      next: () => {
        this.router.navigate(['/courses']);
      },
      error: error => {
        console.error(
          '[CourseForm] Unable to create course:',
          error
        );

        this.errorMessage = 'Unable to create course.';
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/courses']);
  }
}