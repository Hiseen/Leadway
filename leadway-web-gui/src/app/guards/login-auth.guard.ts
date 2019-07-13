import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserAuthService } from '../services/user-auth/user-auth.service';
import { map, tap, take } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class LoginAuthGuard implements CanActivate {

  constructor(private userAuthService: UserAuthService, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    // verifiedToken is a boolean variable (session variable disappear when user
    //  closes the browser / tab)
    const isSessionVerified = JSON.parse(sessionStorage.getItem('verifiedToken'));
    if (isSessionVerified) { return true; }

    // tokenID is a encrypted user info to verify user if user selects 'remember me'.
    //  this info is stored in local storage so that it is still there when browser closees

    const currentTokenID = localStorage.getItem('tokenID');
    if (!currentTokenID) {
      this.router.navigate(['signin']);
      return false;
    }

    const verification = this.userAuthService.getTokenVerificationStream()
      .pipe(
        map(verified => {
          if (!verified) {
            this.router.navigate(['signin']);
            return false;
          }
          return true;
        })
      );

    this.userAuthService.checkUserAuthenticated(currentTokenID);

    return verification;
  }

}
