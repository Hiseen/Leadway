import { Component, OnInit, Input } from '@angular/core';
import { AdminTaskService } from 'src/app/services/task/admin/admin-task.service';
import { UserMetadataService } from 'src/app/services/user-info/user-metadata.service';
import { LeadwayTask } from 'src/app/types/leadway-task.interface';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { from } from 'rxjs';
import { Router } from '@angular/router';

export interface AdminTableDataSource {
  id: number;
  name: string;
  startDate: Date;
  endDate: Date;
  openDate: Date;
}


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

  public adminTasks: LeadwayTask[] = [];
  public adminTasksDataSource: AdminTableDataSource[] = [];
  public adminTasksColumns = ['name', 'startDate', 'endDate', 'openDate', 'edit', 'delete'];

  public userInformation = this.userMetadataService.getUserInfo();
  public userName = this.userInformation.name;
  public userEmail = this.userInformation.email;
  public userType = this.userInformation.type;

  constructor(private adminTaskService: AdminTaskService,
              private userMetadataService: UserMetadataService,
              private modalService: NgbModal,
              private router: Router) {

    adminTaskService.listTasks();

    adminTaskService.getUploadedTaskStream().subscribe(
      tasks => {
        this.adminTasks = tasks;
        this.adminTasksDataSource = tasks.map(task => {
          return {
            id: task.id,
            name: task.taskName,
            startDate: new Date(`${task.startDate[1]}/${task.startDate[2]}/${task.startDate[0]}`),
            endDate: new Date(`${task.endDate[1]}/${task.endDate[2]}/${task.endDate[0]}`),
            openDate: new Date(`${task.openDate[1]}/${task.openDate[2]}/${task.openDate[0]}`)
          };
        });
      }
    );
  }

  ngOnInit() { }

  public deleteOptionClicked(element: AdminTableDataSource): void {
    const modalRef = this.modalService.open(LeadwayTaskDeleteModalComponent);
    modalRef.componentInstance.taskName = element.name;

    from(modalRef.result).subscribe(
      (confirmDelete: string) => {
        if (confirmDelete && confirmDelete === 'confirm') {
          // delete confirmed, send requeset to backend to change
          this.adminTaskService.deleteTask(element.id);
        }
      }
    );
  }

  public editOptionClicked(element: AdminTableDataSource): void {
    const editTask = this.adminTasks.filter(task => task.id === element.id)[0];
    this.router.navigate(['uploadtask'], {queryParams: {editTaskID: editTask.id}});
  }

}


@Component({
  selector: 'leadway-task-delete-modal',
  templateUrl: './leadway-task-delete-modal.html'
})
export class LeadwayTaskDeleteModalComponent {
  @Input() taskName;

  constructor(private activeModal: NgbActiveModal) {}

  public confirmRemove(): void {
    this.activeModal.close('confirm');
  }
}
