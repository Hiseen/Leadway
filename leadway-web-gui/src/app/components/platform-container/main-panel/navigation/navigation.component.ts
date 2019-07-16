import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { UserSigninService } from 'src/app/services/login/user-signin.service';
import { Router } from '@angular/router';

@Component({
  selector: 'leadway-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit {

  public userInitial = 'W';
  public showHiddenSearchBar = false;

  public searchFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder, private userSigninService: UserSigninService,
              private router: Router) {
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

}
