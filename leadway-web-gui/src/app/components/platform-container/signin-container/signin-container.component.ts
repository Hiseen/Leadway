import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { UserSigninService } from 'src/app/services/login/user-signin.service';

@Component({
  selector: 'leadway-signin-container',
  templateUrl: './signin-container.component.html',
  styleUrls: ['./signin-container.component.scss']
})
export class SigninContainerComponent implements OnInit {

  // default the component starts at sign-in panel
  public signInNow = true;

  // reactive form validators for signin and signup form
  public signInFormGroup: FormGroup;
  public signUpFormGroup: FormGroup;

  constructor(private router: Router, private formBuilder: FormBuilder,
              private userSigninService: UserSigninService) {
    this.signInFormGroup = formBuilder.group({
      email: new FormControl('', Validators.compose([
        Validators.email,
        Validators.required
      ])),
      password: new FormControl('', Validators.compose([
        Validators.required
      ]))
    });

    this.signUpFormGroup = formBuilder.group({
      email: new FormControl('', Validators.compose([
        Validators.email,
        Validators.required
      ])),
      password: new FormControl('', Validators.compose([
        Validators.required
      ]))
    });
  }

  ngOnInit() {
  }

  public resetSignInEmail(): void {
    this.signInFormGroup.get('email').setValue('');
  }

  public resetSignInPassword(): void {
    this.signInFormGroup.get('password').setValue('');
  }

  public resetSignUpEmail(): void {
    this.signUpFormGroup.get('email').setValue('');
  }

  public resetSignUpPassword(): void {
    this.signUpFormGroup.get('password').setValue('');
  }

  public signUpToggle(): void {
    this.signInNow = !this.signInNow;
  }

  public signInUser(): void {
    this.userSigninService.signInUser(this.signInFormGroup.value);
  }

  public registerNewUser(): void {
    this.userSigninService.registerUser(this.signUpFormGroup.value);
  }
}
