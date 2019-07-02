import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SigninContainerComponent } from './signin-container.component';

describe('SigninContainerComponent', () => {
  let component: SigninContainerComponent;
  let fixture: ComponentFixture<SigninContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SigninContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SigninContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
