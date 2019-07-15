import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { MatSnackBar } from '@angular/material';
import { Router } from '@angular/router';

export const USER_REGISTRATION_ENDPOINT = 'register';
export const USER_SIGNIN_ENDPOINT = 'login';

export interface LoginRegisterResponse {
  code: number;
  error?: string;
  data?: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserSigninService {

  constructor(private http: HttpClient, private router: Router,
              private snackBar: MatSnackBar) { }

  public registerUser(registrationForm: object): void {

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
        if (res.code === 0) {
          // shows pop-up alerts to the user
          this.snackBar.open('Verify your account in your email', 'confirm', {
            duration: 5000
          });
        } else {
          // when there is an error
          this.snackBar.open(res.error, 'ok', {
            duration: 10000
          });
        }
      },
      error => console.log(error)
    );
  }

  public signInUser(signInForm: object): void {

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
        if (res.code === 0) {

          // saves login token in local storage
          if (res.data) {
            localStorage.setItem('tokenID', res.data);
          } else {
            localStorage.removeItem('tokenID');
          }
          sessionStorage.setItem('verifiedToken', 'true');
          this.router.navigate(['main']);
        } else {
          sessionStorage.setItem('verifiedToken', 'false');
          localStorage.removeItem('tokenID');

          this.snackBar.open(res.error, 'ok', {
            duration: 10000
          });
        }
      },
      error => console.log(error)
    );
  }
}
