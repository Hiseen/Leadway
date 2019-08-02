import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSelectionList, MatSelectionListChange, MatListOption } from '@angular/material';

@Component({
  selector: 'leadway-admin-upload',
  templateUrl: './admin-upload.component.html',
  styleUrls: ['./admin-upload.component.scss']
})
export class AdminUploadComponent implements OnInit {
  items: string[] = ['Boots', 'Clogs', 'Loafers', 'Moccasins', 'Sneakers'];
  public taskFormGroup: FormGroup;
  private selected :MatListOption =null;
  @ViewChild(MatSelectionList,{static: true}) selectionList: MatSelectionList;
  
  constructor(private router: Router, private formBuilder: FormBuilder) {
    
    this.taskFormGroup = formBuilder.group({
      taskFunding: new FormControl('', Validators.compose([
        Validators.pattern(/^[0-9]+$/),
        Validators.required
      ])),
      taskPenalty: new FormControl('', Validators.compose([
        Validators.pattern(/^[0-9]+$/),
        Validators.required
      ])),
      startDate:new FormControl('',Validators.compose([
        Validators.required
      ])),
      endDate:new FormControl('',Validators.compose([
        Validators.required
      ])),
      taskDescription:new FormControl(),
      taskType:new FormControl(),
      taskName:new FormControl()
    });

   }

  ngOnInit() {
    this.selectionList.selectionChange.subscribe((s: MatSelectionListChange) => {   
        this.selectionList.deselectAll();
        if(this.selected!=s.option){
          this.selected=s.option
          s.option.selected = true;
          //fill the form by using the current task
        }else{
          this.selected=null;
          s.option.selected=false;
          //clear all the values from the form
          this.taskFormGroup.reset()
        }
  });

  }

}

