import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

export const USER_REGISTRATION_ENDPOINT = 'register';

@Injectable({
  providedIn: 'root'
})
export class UserSigninService {

  constructor(private http: HttpClient) { }

  public registerUser(registrationForm: object): void {
    console.log(registrationForm);

    const requestUrl = `${environment.apiUrl}/${USER_REGISTRATION_ENDPOINT}`;
    this.http.get<string>(requestUrl).subscribe(res => {
      console.log(res);
    });
  }
}
