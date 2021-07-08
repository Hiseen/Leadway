import { Injectable } from '@angular/core';
import { LoginInfo } from 'src/app/types/user-registration.interface';

@Injectable({
  providedIn: 'root'
})
export class UserMetadataService {

  private currentUserInfo: LoginInfo | undefined = undefined;

  constructor() { }

  public setUserInfo(newInfo: LoginInfo) {
    this.currentUserInfo = newInfo;
  }

  public getUserInfo() {
    return this.currentUserInfo;
  }
}
