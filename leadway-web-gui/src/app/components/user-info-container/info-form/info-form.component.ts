import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UserSigninService } from 'src/app/services/login/user-signin.service';

import { RookieInfo } from '../../../services/enter_info/rookie-info';

@Component({
  selector: 'leadway-info-form',
  templateUrl: './info-form.component.html',
  styleUrls: ['./info-form.component.scss']
})
export class InfoFormComponent implements OnInit {

  // default the component starts at sign up as a new customer(enterprise 1, expert 2)
  public customer = 0;

  // reactive form validators for signin and signup form
  public signUpFormGroup: FormGroup;

  constructor(private router: Router, private formBuilder: FormBuilder,
    private userSigninService: UserSigninService) {

    let MOBILE_PATTERN = /[0-9\+\-\ ]/;

    this.signUpFormGroup = formBuilder.group({
      email: new FormControl('', Validators.compose([
        Validators.email,
        Validators.required
      ])),
      password: new FormControl('', Validators.compose([
        Validators.required
      ])),
      firstname: new FormControl('', Validators.compose([
        Validators.required
      ])),
      lastname: new FormControl('', Validators.compose([
        Validators.required
      ])),
      street: new FormControl('', Validators.compose([
        Validators.required
      ])),
      city: new FormControl('', Validators.compose([
        Validators.required
      ])),
      state: new FormControl('', Validators.compose([
        Validators.required
      ])),
      zip: new FormControl('', Validators.compose([
        Validators.required
      ])),
      phone: new FormControl('', Validators.compose([
        Validators.required,
        Validators.pattern(MOBILE_PATTERN)
      ])),
      companyname: new FormControl('', Validators.compose([
        Validators.required
      ])),
      summary: new FormControl('', Validators.compose([
        Validators.required
      ])),
      experience: new FormControl('', Validators.compose([
        Validators.required
      ])),
      certification: new FormControl('', Validators.compose([
        Validators.required
      ]))
    });
  }

  ngOnInit() {
  }

  rookietest1 = new RookieInfo('chenanyi1997@yahoo.com','937 Tigher Street','Heaven','47284','6381938462','Anyi','Chen');

  submitted = false;
  onSubmit() {this.submitted = true;}

  public resetSignUpEmail(): void {
    this.signUpFormGroup.get('email').setValue('');
  }

  public resetSignUpPassword(): void {
    this.signUpFormGroup.get('password').setValue('');
  }

  public resetSignUpFirstname(): void {
    this.signUpFormGroup.get('firstname').setValue('');
  }

  public resetSignUpLastname(): void {
    this.signUpFormGroup.get('lastname').setValue('');
  }
  
  public resetSignUpStreet(): void {
    this.signUpFormGroup.get('street').setValue('');
  }

  public resetSignUpCity(): void {
    this.signUpFormGroup.get('city').setValue('');
  }

  public resetSignUpState(): void {
    this.signUpFormGroup.get('state').setValue('');
  }

  public resetSignUpZip(): void {
    this.signUpFormGroup.get('zip').setValue('');
  }

  public resetSignUpPhone(): void {
    this.signUpFormGroup.get('phone').setValue('');
  }

  public resetSignUpCompanyname(): void {
    this.signUpFormGroup.get('companyname').setValue('');
  }

  public resetSignUpSummary(): void {
    this.signUpFormGroup.get('summary').setValue('');
  }

  public resetSignUpExperience(): void {
    this.signUpFormGroup.get('experience').setValue('');
  }

  public resetSignUpCertification(): void {
    this.signUpFormGroup.get('certification').setValue('');
  }

  public registerNewUser(): void {
    this.userSigninService.registerUser(this.signUpFormGroup.value);
  }

  public EnterpriseToggle(): void {
    this.customer = 1;
  }

  public ExpertToggle(): void {
    this.customer = 2;
  }

  public NewUserToggle(): void {
    this.customer = 0;
  }

}
