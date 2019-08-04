import { Component, OnInit } from '@angular/core';
import { AdminTaskService } from 'src/app/services/task/admin/admin-task.service';
import { UserMetadataService } from 'src/app/services/user-info/user-metadata.service';

@Component({
  selector: 'leadway-account-info',
  templateUrl: './account-info.component.html',
  styleUrls: ['./account-info.component.scss']
})
export class AccountInfoComponent implements OnInit {

  // Basic Info Showing, other than that:
  // 1. company will show all interested tasks that still valid
  // 2. admin will show all the tasks uploaded and can modify / delete
  // 3. user / expert will show all the companies reviewed

  constructor(private adminTaskService: AdminTaskService,
              private userMetadataService: UserMetadataService) {

    adminTaskService.listTasks();
  }

  ngOnInit() { }

}
