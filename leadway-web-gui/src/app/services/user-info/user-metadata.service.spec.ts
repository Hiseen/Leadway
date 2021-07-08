import { TestBed } from '@angular/core/testing';

import { UserMetadataService } from './user-metadata.service';

describe('UserMetadataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: UserMetadataService = TestBed.get(UserMetadataService);
    expect(service).toBeTruthy();
  });
});
