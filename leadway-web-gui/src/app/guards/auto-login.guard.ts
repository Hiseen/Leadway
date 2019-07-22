import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserAuthService } from '../services/user-auth/user-auth.service';
import { map } from 'rxjs/operators';

/**
 * This guard is mainly used for pages before user has
 *  signed in. This checks if the user already sign in previously.
 *
 * For instance:
 *  when the user already logined and they access sign-in page
 *  accidently again, they should be able to be redirected to
 *  the main page without logining in themselves again.
 */
@Injectable({
  providedIn: 'root'
})
export class AutoLoginGuard implements CanActivate {

  constructor(private router: Router, private userAuthService: UserAuthService) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const currentTokenID = localStorage.getItem('tokenID');
    // token ID is null, then the user must be in sign-in / sign-up page
    if (!currentTokenID) { return true; }

    const verification = this.userAuthService.getTokenVerificationStream()
      .pipe(
        map(verified => {
          // if tokenID is incorrect, then stay at sign-in page, else (token id is
          //  correct) go to main page
          if (!verified) { return true; }
          this.router.navigate(['']);
          return false;
        })
      );

    this.userAuthService.checkUserAuthenticated(currentTokenID);
    return verification;
  }

}
