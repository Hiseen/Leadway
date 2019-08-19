import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { AdminTaskService } from 'src/app/services/task/admin/admin-task.service';
import { MatSnackBar } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';
import { LeadwayTask } from 'src/app/types/leadway-task.interface';

@Component({
  selector: 'leadway-admin-upload',
  templateUrl: './admin-upload.component.html',
  styleUrls: ['./admin-upload.component.scss']
})
export class AdminUploadComponent implements OnInit {

  public taskFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder, private adminTaskService: AdminTaskService,
              private snackBar: MatSnackBar, private route: ActivatedRoute,
              private router: Router) {

    this.taskFormGroup = formBuilder.group({
      id: new FormControl(),
      taskFunding: new FormControl(2, Validators.compose([
        Validators.pattern(/^[0-9]+$/),
        Validators.required
      ])),
      taskPenalty: new FormControl(2, Validators.compose([
        Validators.pattern(/^[0-9]+$/),
        Validators.required
      ])),
      startDate: new FormControl(new Date('8/10/2019'), Validators.compose([
        Validators.required
      ])),
      endDate: new FormControl(new Date('8/25/2019'), Validators.compose([
        Validators.required
      ])),
      taskDescription: new FormControl(),
      taskType: new FormControl('1'),
      taskName: new FormControl('henryTest'),
      taskStreet: new FormControl('', Validators.compose([
        Validators.required
      ])),
      taskCity: new FormControl('', Validators.compose([
        Validators.required
      ])),
      taskState: new FormControl('', Validators.compose([
        Validators.required
      ])),
      taskZip: new FormControl('', Validators.compose([
        Validators.required
      ]))
    });

    // event when the router navigate to this component path
    this.route.queryParamMap.subscribe(params => {
      const editTaskID = params.get('editTaskID');
      if (editTaskID) {
        // casting string ID to number
        const numberedID = Number(editTaskID);

        // if a numberID is nan, it means the ID contains string that is not a number
        if (Number.isNaN(numberedID)) {
          this.snackBar.open(`${editTaskID} is not a valid task ID number`, 'ok', {
            duration: 10000
          });
          this.router.navigate(['']);
          return;
        }

        // send the request to fetch the task to edit
        this.adminTaskService.getTask(numberedID);
      }
    });

    this.adminTaskService.getEditTaskStream().subscribe(task => {
      this.setEditTaskForm(task);
    });

  }

  ngOnInit() { }

  /**
   * Depends on the information of the task to edit, update the
   *  upload form.
   * @param currentTask task to edit
   */
  public setEditTaskForm(currentTask: LeadwayTask): void {

    // remove the openDate param since it's not included in the form
    const {openDate: unwantedDate, ...task} = currentTask;

    // set the value of the group
    this.taskFormGroup.setValue(task);

    // some field of the form required special parsing technique.
    this.taskFormGroup.get('taskType').setValue(task.taskType.toString());
    this.taskFormGroup.get('startDate').setValue(
      new Date(`${task.startDate[1]}/${task.startDate[2]}/${task.startDate[0]}`));
    this.taskFormGroup.get('endDate').setValue(
      new Date(`${task.endDate[1]}/${task.endDate[2]}/${task.endDate[0]}`));
  }

  /**
   * Create a Leadway task that can be edit/delete by the administrator
   *  and view by other types of the user.
   */
  public createNewTask(): void {

    const startDate = new Date(this.taskFormGroup.get('startDate').value);
    const endDate = new Date(this.taskFormGroup.get('endDate').value);

    if (endDate <= startDate) {
      const error = 'End date should be after start date';
      this.snackBar.open(error, 'ok', {
        duration: 10000
      });
      return;
    }

    this.adminTaskService.createTask(this.taskFormGroup.value);
  }
}

