import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { LeadwayTask, ListTasksResponse, GetTaskResponse } from 'src/app/types/leadway-task.interface';
import { Subject, Observable } from 'rxjs';
import { MatSnackBar } from '@angular/material';
import { Router } from '@angular/router';

export const ADMIN_ADD_TASK_ENDPOINT = 'admin-create';
export const ADMIN_LIST_TASKS_ENDPOINT = 'admin-list';
export const ADMIN_DELETE_TASK_ENDPOINT = 'admin-delete';

export const GET_TASK_ENDPOINT = 'get-task';

@Injectable({
  providedIn: 'root'
})
export class AdminTaskService {

  constructor(private http: HttpClient, private snackBar: MatSnackBar, private router: Router) { }

  private uploadedTaskStream = new Subject<LeadwayTask[]> ();
  private editTaskStream = new Subject<LeadwayTask> ();

  public getUploadedTaskStream(): Observable<LeadwayTask[]> {
    return this.uploadedTaskStream.asObservable();
  }

  public getEditTaskStream(): Observable<LeadwayTask> {
    return this.editTaskStream.asObservable();
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

  /**
   * This method will list all the tasks available in the database.
   */
  public listTasks(): void {
    const requestURL = `${environment.apiUrl}/${ADMIN_LIST_TASKS_ENDPOINT}`;
    this.http.get<ListTasksResponse>(requestURL).subscribe(res => {
      this.uploadedTaskStream.next(res.tasks);
    });
  }

  /**
   * This method will remove one of the leadway task based on the ID
   *  provided. After the removal is completed, this method will update
   *  all the tasks info stored locally by sending the updated tasks
   *  into the uploadedTaskStream.
   *
   * @param id Task ID
   */
  public deleteTask(id: number): void {
    const requestURL = `${environment.apiUrl}/${ADMIN_DELETE_TASK_ENDPOINT}`;
    this.http.post<ListTasksResponse>(
      requestURL,
      JSON.stringify({taskId: id}),
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    ).subscribe(res => {
      this.uploadedTaskStream.next(res.tasks);
    });
  }

  /**
   * This method gets the task information based on the ID provided in
   *  the function parameter. After fetching the task from the backend,
   *  this function will send the task to editTaskStream to notify
   *  the component waiting for this information.
   *
   * @param id Task ID
   */
  public getTask(id: number): void {
    const requestURL = `${environment.apiUrl}/${GET_TASK_ENDPOINT}`;
    this.http.post<GetTaskResponse>(
      requestURL,
      JSON.stringify({taskID: id}),
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    ).subscribe(res => {
      if (res.code === 0) {
        this.editTaskStream.next(res.task);
        return;
      }

      this.router.navigate(['']);
      this.snackBar.open(`Task with ID ${id} does not exist`, 'ok', {
        duration: 10000
      });

    });
  }
}
