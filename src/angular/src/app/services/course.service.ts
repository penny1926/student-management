import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Course } from '../models/course';
import { CourseRequest } from '../models/course-request';

@Injectable({ providedIn: 'root' })
export class CourseService {
    
    private readonly http = inject(HttpClient);
    private readonly apiUrl = '/studentmanagement/courses';

    getAllCourses(): Observable<Course[]> { 
        return this.http.get<Course[]>(this.apiUrl);
     }
    getCourse(courseId: number): Observable<Course> { 
        return this.http.get<Course>(`${this.apiUrl}/${courseId}`); 
    }
    createCourse(request: CourseRequest): Observable<void> { 
        return this.http.post<void>(
              this.apiUrl,
              request
            );
    }
    updateCourse(courseId: number, request: CourseRequest): Observable<void> { 
        return this.http.put<void>(`${this.apiUrl}/${courseId}`, request); 
    }
    deleteCourse(courseId: number): Observable<void> { 
        return this.http.delete<void>(`${this.apiUrl}/${courseId}`); 
    }
}