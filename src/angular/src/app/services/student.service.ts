import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Student } from '../models/student';
import { StudentRequest } from '../models/student-request';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = '/studentmanagement/students';

  getAllStudents(): Observable<Student[]> {
    return this.http.get<Student[]>(this.apiUrl);
  }

  getStudent(studentId: number): Observable<Student> {
    return this.http.get<Student>(
      `${this.apiUrl}/${studentId}`
    );
  }

  createStudent(request: StudentRequest): Observable<void> {
    return this.http.post<void>(
      this.apiUrl,
      request
    );
  }

  updateStudent(
    studentId: number,
    request: StudentRequest
  ): Observable<void> {
    return this.http.put<void>(
      `${this.apiUrl}/${studentId}`,
      request
    );
  }

  deleteStudent(studentId: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${studentId}`
    );
  }

  registerCourses(
    studentId: number,
    courseIds: number[]
  ): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/${studentId}/courses`,
      courseIds
    );
  }

  unregisterCourses(
    studentId: number,
    courseIds: number[]
  ): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${studentId}/courses`,
      {
        body: courseIds
      }
    );
  }
}