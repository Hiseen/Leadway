import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UserSigninService } from 'src/app/services/login/user-signin.service';
import { MatSnackBar } from '@angular/material';

export const MOBILE_PATTERN = /[0-9]{3}-[0-9]{3}-[0-9]{4}/;

@Component({
  selector: 'leadway-info-form',
  templateUrl: './info-form.component.html',
  styleUrls: ['./info-form.component.scss']
})
export class InfoFormComponent implements OnInit {
  // reactive form validators for signin and signup form
  public signUpFormGroup: FormGroup;

  public registerClicked = false;

  constructor(private router: Router, private formBuilder: FormBuilder,
              private userSigninService: UserSigninService,
              private snackBar: MatSnackBar) {

    this.signUpFormGroup = formBuilder.group({
      // default the component starts at sign up as a new customer
      //  (regular user 0, expert 1, enterprise 2, admin 3)
      customertype: '0',
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
      // for company type user
      companyname: new FormControl('', Validators.compose([
        Validators.required
      ])),
      webaddress: new FormControl('', Validators.compose([
        Validators.required
      ])),
      // for expert type user
      experience: new FormControl('', Validators.compose([
        Validators.required
      ])),
      certification: new FormControl('', Validators.compose([
        Validators.required
      ])),
      // for admin type user
      admincode: new FormControl('', Validators.compose([
        Validators.required
      ]))
    });
  }

  ngOnInit() { }

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

  public resetSignUpWebaddress(): void {
    this.signUpFormGroup.get('webaddress').setValue('');
  }

  public resetSignUpExperience(): void {
    this.signUpFormGroup.get('experience').setValue('');
  }

  public resetSignUpCertification(): void {
    this.signUpFormGroup.get('certification').setValue('');
  }

  public resetSignUpAdmincode(): void {
    this.signUpFormGroup.get('admincode').setValue('');
  }

  /**
   * Sends a backend request to register the user based on
   *  the information provided on the form.
   *
   * If the form is invalid, frontend will open a snackBar to
   *  warn the user. Otherwise the backend request will be
   *  sent to the server.
   */
  public registerNewUser(): void {
    if (!this.isFormValid()) {
      this.snackBar.open('Registration Form is Invalid', 'Confirm', {
        duration: 5000
      });
    } else {
      this.userSigninService.registerUser(this.signUpFormGroup.value);
    }
  }

  /**
   * Based on the user type generate different string title
   *  for Step 2 of the registration.
   */
  public getUsertypeTitle(): string {
    const currentType = this.signUpFormGroup.get('customertype').value;
    if (currentType === '0') {
      return 'User';
    } else if (currentType === '1') {
      return 'Expert';
    } else if (currentType === '2') {
      return 'Enterprise';
    } else {
      return 'Adminstrator';
    }
  }

  /**
   * Check if the current form is valid for submit. This will
   *  be used to validate whether the submit will send a request to
   *  backend.
   */
  public isFormValid(): boolean {
    if (!this.isDefaultFormValid()) {
      return false;
    }
    const currentType = this.signUpFormGroup.get('customertype').value;
    if (currentType === '0') {
      return this.isRegularFormValid();
    } else if (currentType === '1') {
      return this.isExpertFormValid();
    } else if (currentType === '2') {
      return this.isEnterpriseFormValid();
    } else {
      return this.isAdminFormValid();
    }
  }

  /**
   * Check if the default field in the signup form is valid
   */
  public isDefaultFormValid(): boolean {
    return this.signUpFormGroup.get('email').valid && this.signUpFormGroup.get('password').valid
      && this.signUpFormGroup.get('street').valid && this.signUpFormGroup.get('city').valid
      && this.signUpFormGroup.get('state').valid && this.signUpFormGroup.get('zip').valid
      && this.signUpFormGroup.get('phone').valid;
  }

  /**
   * Check if the regular user field in the signup form is valid
   */
  public isRegularFormValid(): boolean {
    return this.signUpFormGroup.get('firstname').valid && this.signUpFormGroup.get('lastname').valid;
  }

  /**
   * Check if the expert field in the signup form is valid
   */
  public isExpertFormValid(): boolean {
    return this.signUpFormGroup.get('firstname').valid && this.signUpFormGroup.get('lastname').valid
      && this.signUpFormGroup.get('experience').valid && this.signUpFormGroup.get('certification').valid;
  }

  /**
   * Check if the enterprise field in the signup form is valid
   */
  public isEnterpriseFormValid(): boolean {
    return this.signUpFormGroup.get('companyname').valid && this.signUpFormGroup.get('webaddress').valid;
  }

  /**
   * Check if the admin field in the signup form is valid
   */
  public isAdminFormValid(): boolean {
    return this.signUpFormGroup.get('firstname').valid && this.signUpFormGroup.get('lastname').valid
      && this.signUpFormGroup.get('admincode').valid;
  }
}
