import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { UserSigninService } from 'src/app/services/login/user-signin.service';

@Component({
  selector: 'leadway-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit {

  public userInitial = 'W';
  public showHiddenSearchBar = false;

  public searchFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder, private userSigninService: UserSigninService) {
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

}
