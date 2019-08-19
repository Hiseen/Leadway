export interface LeadwayTask extends Readonly<{
  id: number;
  taskName: string;
  taskDescription: string;
  taskFunding: number;
  taskPenalty: number;
  taskType: number;
  startDate: Array<number>;
  endDate: Array<number>;
  openDate: Array<number>;
  taskStreet: string;
  taskCity: string;
  taskState: string;
  taskZip: number;
}> {}

export interface ListTasksResponse extends Readonly<{
  tasks: LeadwayTask[];
}> {}

export interface GetTaskResponse extends Readonly<{
  code: number,
  task?: LeadwayTask
}> { }

// endDate: (3) [2019, 8, 30]
// id: 4
// startDate: (3) [2019, 8, 5]
// taskDescription: "ewfwef"
// taskFunding: 2
// taskName: "henryTest123"
// taskPenalty: 2
// taskType: 2
