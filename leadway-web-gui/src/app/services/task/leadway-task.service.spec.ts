import { TestBed } from '@angular/core/testing';

import { LeadwayTaskService } from './leadway-task.service';

describe('LeadwayTaskService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: LeadwayTaskService = TestBed.get(LeadwayTaskService);
    expect(service).toBeTruthy();
  });
});
