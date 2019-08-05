import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { LeadwayTask, ListTasksResponse } from 'src/app/types/leadway-task.interface';
import { Subject, Observable } from 'rxjs';
import { MatSnackBar } from '@angular/material';
import { Router } from '@angular/router';

export const ADMIN_ADD_TASK_ENDPOINT = 'admin-create';
export const ADMIN_LIST_TASKS_ENDPOINT = 'admin-list';
export const ADMIN_MODIFY_TASK_ENDPOINT = 'admin-modify';
export const ADMIN_DELETE_TASK_ENDPOINT = 'admin-delete';

@Injectable({
  providedIn: 'root'
})
export class AdminTaskService {

  constructor(private http: HttpClient, private snackBar: MatSnackBar, private router: Router) { }

  private uploadedTaskStream = new Subject<LeadwayTask[]> ();

  public getUploadedTaskStream(): Observable<LeadwayTask[]> {
    return this.uploadedTaskStream.asObservable();
  }

  public createTask(taskForm: object): void {
    const requestURL = `${environment.apiUrl}/${ADMIN_ADD_TASK_ENDPOINT}`;
    this.http.post(
      requestURL,
      JSON.stringify(taskForm),
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    ).subscribe(res => {
      this.snackBar.open('Task creation successful', 'ok', {
        duration: 10000
      });
      this.router.navigate(['']);
    });
  }

  public listTasks(): void {
    const requestURL = `${environment.apiUrl}/${ADMIN_LIST_TASKS_ENDPOINT}`;
    this.http.get<ListTasksResponse>(requestURL).subscribe(res => {
      this.uploadedTaskStream.next(res.tasks);
    });
  }

  public modifyTask(taskForm: object): void {
    const requestURL = `${environment.apiUrl}/${ADMIN_MODIFY_TASK_ENDPOINT}`;
    this.http.post(
      requestURL,
      JSON.stringify(taskForm),
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    ).subscribe(res => {
      console.log(res);
    });
  }

  public deleteTask(id: number): void {
    const requestURL = `${environment.apiUrl}/${ADMIN_DELETE_TASK_ENDPOINT}`;
    this.http.post(
      requestURL,
      JSON.stringify({taskId: id}),
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    ).subscribe(res => {
      console.log(res);
    });
  }
}
