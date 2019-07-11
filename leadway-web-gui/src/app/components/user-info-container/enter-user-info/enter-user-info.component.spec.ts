import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EnterUserInfoComponent } from './enter-user-info.component';

describe('EnterUserInfoComponent', () => {
  let component: EnterUserInfoComponent;
  let fixture: ComponentFixture<EnterUserInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EnterUserInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EnterUserInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
