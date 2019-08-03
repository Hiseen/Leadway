import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'leadway-admin-upload',
  templateUrl: './admin-upload.component.html',
  styleUrls: ['./admin-upload.component.scss']
})
export class AdminUploadComponent implements OnInit {

  public taskFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder) {

    this.taskFormGroup = formBuilder.group({
      taskFunding: new FormControl('', Validators.compose([
        Validators.pattern(/^[0-9]+$/),
        Validators.required
      ])),
      taskPenalty: new FormControl('', Validators.compose([
        Validators.pattern(/^[0-9]+$/),
        Validators.required
      ])),
      startDate: new FormControl('', Validators.compose([
        Validators.required
      ])),
      endDate: new FormControl('', Validators.compose([
        Validators.required
      ])),
      taskDescription: new FormControl(),
      taskType: new FormControl(),
      taskName: new FormControl()
    });

   }

  ngOnInit() { }

}

