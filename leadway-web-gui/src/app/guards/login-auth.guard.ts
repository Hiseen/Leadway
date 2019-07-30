import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserAuthService } from '../services/user-auth/user-auth.service';
import { map, tap, take } from 'rxjs/operators';
import { UserMetadataService } from '../services/user-info/user-metadata.service';

/**
 * This is mainly used for components other than sign-in / sign-up
 *  to authenticate if the user is correct to persist login status
 *  even if the user go to the next route or close the browser.
 */
@Injectable({
  providedIn: 'root'
})
export class LoginAuthGuard implements CanActivate {

  constructor(private userAuthService: UserAuthService, private router: Router,
              private userMetadataService: UserMetadataService) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {


    // tokenID is a encrypted user info to verify user if user selects 'remember me'.
    //  this info is stored in local storage so that it is still there when browser closees

    const currentTokenID = localStorage.getItem('tokenID');
    if (!currentTokenID) {
      this.router.navigate(['signin']);
      return false;
    }

    console.log('Continue token verification now!');
    console.log('Token = ' + currentTokenID);

    const verification = this.userAuthService.getTokenVerificationStream()
      .pipe(
        map(verifiedJson => {
          if (!verifiedJson.verified) {
            // tokenID is no longer valid, remove it and navigate back to signin panel
            localStorage.removeItem('tokenID');
            this.router.navigate(['signin']);
            return false;
          }
          // tokenID is valid, get the userInfo before going to the main panel.
          this.userMetadataService.setUserInfo(verifiedJson.userInfo);
          return true;
        })
      );

    this.userAuthService.checkUserAuthenticated(currentTokenID);

    return verification;
  }

}
