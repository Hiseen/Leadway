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

  // reactive form validators for signin and signup form
  public signInFormGroup: FormGroup;

  constructor(private router: Router, private formBuilder: FormBuilder,
              public userSigninService: UserSigninService) {
    this.signInFormGroup = formBuilder.group({
      email: new FormControl('', Validators.compose([
        Validators.email,
        Validators.required
      ])),
      password: new FormControl('', Validators.compose([
        Validators.required
      ])),
      rememberMe: new FormControl('')
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

  /**
   * This method will use angular's router to navigate
   *  user to the signup container page.
   */
  public signUpToggle(): void {
    this.router.navigate(['signup']);
  }

  public signInUser(): void {
    this.userSigninService.signInUser(this.signInFormGroup.value);
  }
}
