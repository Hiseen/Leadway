import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { MatSnackBar } from '@angular/material';
import { Router } from '@angular/router';

export const USER_REGISTRATION_ENDPOINT = 'register';
export const USER_SIGNIN_ENDPOINT = 'login';
export const USER_SIGNOUT_ENDPOINT = 'logout';

export interface LoginRegisterResponse {
  code: number;
  error?: string;
  token?: string;
  userID?: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserSigninService {

  private registeredClicked = false;
  private signinClicked = false;

  constructor(private http: HttpClient, private router: Router,
              private snackBar: MatSnackBar) { }

  public registerUser(registrationForm: object): void {
    this.registeredClicked = true;
    const requestUrl = `${environment.apiUrl}/${USER_REGISTRATION_ENDPOINT}`;
    this.http.post<LoginRegisterResponse>(
      requestUrl,
      JSON.stringify(registrationForm),
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    ).subscribe(
      res => {
        this.registeredClicked = false;
        if (res.code === 0) {
          this.router.navigate(['signup/verification']);
        } else {
          // when there is an error
          this.snackBar.open(res.error, 'ok', {
            duration: 10000
          });
        }
      },
      error => {
        this.registeredClicked = false;
        console.log(error);
        this.snackBar.open(error, 'ok', {
          duration: 10000
        });
      }
    );
  }

  public getRegisteredClicked(): boolean {
    return this.registeredClicked;
  }

  /**
   * This method signs in the user based on the user data provided
   *  in the form.
   */
  public signInUser(signInForm: object): void {

    this.signinClicked = true;
    // clean the data
    localStorage.removeItem('tokenID');
    localStorage.removeItem('userID');

    const requestUrl = `${environment.apiUrl}/${USER_SIGNIN_ENDPOINT}`;
    this.http.post<LoginRegisterResponse>(
      requestUrl,
      JSON.stringify(signInForm),
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    ).subscribe(
      res => {
        this.signinClicked = false;
        if (res.code === 0) {
          // saves login token in local storage
          localStorage.setItem('tokenID', res.token);
          localStorage.setItem('userID', res.userID);
          this.router.navigate(['main']);

        } else {
          this.snackBar.open(res.error, 'ok', {
            duration: 10000
          });
        }
      },
      error => {
        console.log(error);
        this.signinClicked = false;
        this.snackBar.open(error, 'ok', {
          duration: 10000
        });
      }
    );
  }

  public getSignInClicked(): boolean {
    return this.signinClicked;
  }

  /**
   * This method will sign out the current user from Leadway.
   *
   * The user token stored in the localstorage and sessionstorage will be removed.
   *
   * If a token is stored in the frontend, then an http request will be sent
   *  to remove the token stored inside the DB.
   */
  public signOutUser(): void {

    const localToken = localStorage.getItem('tokenID');

    // if there is no local token at the beginning (no remember me selected),
    //  return immediately without sending the request to remove token from DB
    if (!localToken) {
      this.router.navigate(['signin']);
      return;
    }

    localStorage.removeItem('tokenID');
    const userID = localStorage.getItem('userID');
    if (!userID) {
      this.router.navigate(['signin']);
      return;
    }

    const requestUrl = `${environment.apiUrl}/${USER_SIGNOUT_ENDPOINT}`;
    this.http.post<LoginRegisterResponse>(
      requestUrl,
      JSON.stringify({id: userID}),
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    ).subscribe(
      res => this.router.navigate(['signin']),
      error => console.log(error)
    );
  }
}
