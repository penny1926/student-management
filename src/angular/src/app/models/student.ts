import { Course } from './course';

export interface Student {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  courses: Course[];
}