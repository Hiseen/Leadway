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

  private sessionVerificationStream = new Subject<boolean> ();

  constructor(private http: HttpClient) { }

  public getSessionVerificationStream(): Observable<boolean> {
    return this.sessionVerificationStream.asObservable();
  }

  public checkUserAuthenticated(): void {

    // sessionID is a encrypted user info to verify user if user selects 'remember me'.
    //  this info is stored in local storage so that it is still there when browser closees

    const currentSessionID = localStorage.getItem('sessionID');

    this.checkValidSessionID(currentSessionID).subscribe(
      sessionStatus => {
        sessionStorage.setItem('verifiedSession', JSON.stringify(sessionStatus));
        this.sessionVerificationStream.next(sessionStatus);
      }
    );
  }

  /**
   * This method sends http request to the backend to verify the
   *  session ID stored inside the local storage.
   *
   * @param sessionID session ID
   */
  private checkValidSessionID(sessionID: string): Observable<boolean> {
    const requestURL = `${environment.apiUrl}/${USER_AUTHENTICATION_ENDPOINT}`;
    return this.http.post<boolean>(
      requestURL,
      JSON.stringify({session: sessionID}),
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );
  }

}
