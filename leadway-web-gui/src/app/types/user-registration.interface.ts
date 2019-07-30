// Registration Types

export interface ErrorResponse extends Readonly<{ }> {
  code: 1;
  error: string;
}

export interface SuccessRegistrationResponse extends Readonly<{
  code: 0;
}> {}

export type RegistrationResponse = SuccessRegistrationResponse | ErrorResponse;

// Login Types

export interface LoginInfo extends Readonly<{
  userID: number;
  name: string;
  type: number;
  email: string;
}> {}

export interface SuccessLoginResponse extends Readonly<{
  code: 0;
  token: string;
  user: LoginInfo;
}> { }


export type LoginResponse = SuccessLoginResponse | ErrorResponse;

// Logout Types
export interface LogoutResponse extends Readonly<{
  code: 0;
}> { }
