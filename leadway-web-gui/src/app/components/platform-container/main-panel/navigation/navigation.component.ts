import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { UserSigninService } from 'src/app/services/login/user-signin.service';
import { Router } from '@angular/router';
import { UserMetadataService } from 'src/app/services/user-info/user-metadata.service';

@Component({
  selector: 'leadway-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit {

  // gather user information stored when login
  public userInformation = this.userMetadataService.getUserInfo();

  // fetch basic information from the user info to display on the navigation
  public userInitial = this.userInformation.name.charAt(0).toUpperCase();
  public userName = this.userInformation.name;
  public userEmail = this.userInformation.email;
  public userType = this.userInformation.type;

  public showHiddenSearchBar = false;
  public searchFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder, private userSigninService: UserSigninService,
              private router: Router, public userMetadataService: UserMetadataService) {
    this.searchFormGroup = formBuilder.group({
      query: new FormControl('', Validators.compose([

      ])),
      location: new FormControl('', Validators.compose([

      ]))
    });
  }

  ngOnInit() {
  }

  /**
   * Open the minimized search bar when user clicks
   *  minimized search icon.
   */
  public miniSearchClick(): void {
    this.showHiddenSearchBar = true;
  }

  /**
   * Close the minimized search bar when user clicks
   *  minimized back icon.
   */
  public miniBackClick(): void {
    this.showHiddenSearchBar = false;
  }

  public logoutUser(): void {
    this.userSigninService.signOutUser();
  }

  /**
   * When user search something in the form in the navigation bar,
   *  navigate them to a new search path.
   */
  public submitSearch(): void {
    if (this.searchFormGroup.get('query').value === '') {
      return;
    }
    this.router.navigate(['search'], {queryParams: this.searchFormGroup.value});
  }

  /**
   * This navigates the admin user to the uploadtask form page
   */
  public adminUploadClick(): void {
    if (this.userType !== 3) {
      return;
    }
    // console.log('Navigate to admin upload form page');
    this.router.navigate(['uploadtask']);
  }

  /**
   * This method will navigate back to the home page when the
   *  user clicks the website icon / title on the top-left.
   */
  public navigateToHome(): void {
    this.router.navigate(['']);
  }

}
