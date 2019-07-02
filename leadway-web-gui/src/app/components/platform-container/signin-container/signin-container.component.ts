import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'leadway-signin-container',
  templateUrl: './signin-container.component.html',
  styleUrls: ['./signin-container.component.scss']
})
export class SigninContainerComponent implements OnInit {

  public signInFormGroup: FormGroup;

  constructor(private router: Router, private formBuilder: FormBuilder) {
    this.signInFormGroup = formBuilder.group({
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

  public resetEmail(): void {
    this.signInFormGroup.get('email').setValue('');
  }

  public resetPassword() {
    this.signInFormGroup.get('password').setValue('');
  }
}
