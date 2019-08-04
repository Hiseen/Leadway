import { TestBed } from '@angular/core/testing';

import { AdminTaskService } from './admin-task.service';

describe('AdminTaskService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AdminTaskService = TestBed.get(AdminTaskService);
    expect(service).toBeTruthy();
  });
});
