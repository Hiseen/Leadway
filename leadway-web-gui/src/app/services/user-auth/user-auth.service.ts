import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable, Subject } from 'rxjs';
import { VerificationResponse } from 'src/app/types/user-verification.interface';
// import { map } from 'rxjs/operators';

export const USER_AUTHENTICATION_ENDPOINT = 'user-auth';

@Injectable({
  providedIn: 'root'
})
export class UserAuthService {

  private tokenVerificationStream = new Subject<VerificationResponse> ();

  constructor(private http: HttpClient) { }

  public getTokenVerificationStream(): Observable<VerificationResponse> {
    return this.tokenVerificationStream.asObservable();
  }

  public checkUserAuthenticated(currentToken: string): void {

    this.checkValidTokenID(currentToken).subscribe(
      tokenStatus => {
        // console.log('tokenStatus: ' + tokenStatus);
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
  private checkValidTokenID(tokenID: string): Observable<VerificationResponse> {
    const requestURL = `${environment.apiUrl}/${USER_AUTHENTICATION_ENDPOINT}`;
    return this.http.post<VerificationResponse>(
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
