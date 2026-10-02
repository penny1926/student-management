import { Routes } from '@angular/router';
import { StudentListComponent } from './students/student-list/student-list.component';
import { StudentDetailComponent } from './students/student-detail/student-detail.component';
import { StudentFormComponent } from './students/student-form/student-form.component';
import { CourseRegistrationComponent } from './students/course-registration/course-registration.component';
import { CourseListComponent } from './courses/course-list/course-list.component';
import { CourseFormComponent } from './courses/course-form/course-form.component';

export const routes: Routes = [
  { path: '', redirectTo: 'students', pathMatch: 'full' },
  { path: 'students', component: StudentListComponent },
  { path: 'students/new', component: StudentFormComponent },
  { path: 'students/:id/register-courses', component: CourseRegistrationComponent },
  { path: 'students/:id/edit', component: StudentFormComponent },
  { path: 'students/:id', component: StudentDetailComponent },
  { path: 'courses', component: CourseListComponent },
  { path: 'courses/new', component: CourseFormComponent },
  { path: 'courses/:id/edit', component: CourseFormComponent },
  { path: '**', redirectTo: 'students' }
];