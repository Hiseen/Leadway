import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

export interface TaskItem extends Readonly<{
  taskName: string;
  taskType: string;
  taskAddress: string;
  taskSummary: string;
  taskStart: Date;
  taskEnd: Date;
}> { }

@Component({
  selector: 'leadway-business-search',
  templateUrl: './business-search.component.html',
  styleUrls: ['./business-search.component.scss']
})
export class BusinessSearchComponent implements OnInit {

  public currentQuery = '';
  public currentType = '';

  public taskLists: TaskItem[] = [
    {
      taskName: 'henryTask1',
      taskType: 'home utilities',
      taskAddress: 'henry hawaii 92262',
      taskSummary: 'henry summary 1 zan',
      taskStart: new Date(),
      taskEnd: new Date()
    },
    {
      taskName: 'henryTask2',
      taskType: 'henry transportation',
      taskAddress: 'sushi hawaii 92262',
      taskSummary: 'shenquan summary sushi nice',
      taskStart: new Date(),
      taskEnd: new Date()
    }
  ];

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      console.log('Activate route event occured');
      console.log(params);

      this.currentQuery = params.get('taskQuery');
      this.currentType = params.get('taskType');
    });
  }

}
