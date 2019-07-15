import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable, Subject } from 'rxjs';
// import { map } from 'rxjs/operators';

export const USER_AUTHENTICATION_ENDPOINT = 'user-auth';

@Injectable({
  providedIn: 'root'
})
export class UserAuthService {

  private tokenVerificationStream = new Subject<boolean> ();

  constructor(private http: HttpClient) { }

  public getTokenVerificationStream(): Observable<boolean> {
    return this.tokenVerificationStream.asObservable();
  }

  public checkUserAuthenticated(currentToken: string): void {

    this.checkValidTokenID(currentToken).subscribe(
      tokenStatus => {
        sessionStorage.setItem('verifiedToken', JSON.stringify(tokenStatus));
        this.tokenVerificationStream.next(tokenStatus);
      }
    );
  }

  /**
   * This method sends http request to the backend to verify the
   *  token ID stored inside the local storage.
   *
   * @param tokenID token ID
   */
  private checkValidTokenID(tokenID: string): Observable<boolean> {
    const requestURL = `${environment.apiUrl}/${USER_AUTHENTICATION_ENDPOINT}`;
    return this.http.post<boolean>(
      requestURL,
      JSON.stringify({token: tokenID}),
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );
  }

}
