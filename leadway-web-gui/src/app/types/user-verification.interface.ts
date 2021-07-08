import { LoginInfo } from './user-registration.interface';

export interface VerificationResponse extends Readonly<{
  verified: boolean;
  userInfo?: LoginInfo;
}> { }
