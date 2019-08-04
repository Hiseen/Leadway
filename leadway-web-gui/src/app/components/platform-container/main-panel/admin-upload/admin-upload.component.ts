import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminTaskService } from 'src/app/services/task/admin/admin-task.service';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'leadway-admin-upload',
  templateUrl: './admin-upload.component.html',
  styleUrls: ['./admin-upload.component.scss']
})
export class AdminUploadComponent implements OnInit {

  public taskFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder, private adminTaskService: AdminTaskService,
              private snackBar: MatSnackBar) {

    this.taskFormGroup = formBuilder.group({
      taskFunding: new FormControl(2, Validators.compose([
        Validators.pattern(/^[0-9]+$/),
        Validators.required
      ])),
      taskPenalty: new FormControl(2, Validators.compose([
        Validators.pattern(/^[0-9]+$/),
        Validators.required
      ])),
      startDate: new FormControl(123, Validators.compose([
        Validators.required
      ])),
      endDate: new FormControl(334, Validators.compose([
        Validators.required
      ])),
      taskDescription: new FormControl(),
      taskType: new FormControl('1'),
      taskName: new FormControl('henryTest')
    });

   }

  ngOnInit() { }


  public createNewTask(): void {
    console.log(this.taskFormGroup.value);

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

