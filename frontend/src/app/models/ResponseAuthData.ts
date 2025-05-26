export interface UserRole {
    id: string;
    name: string;
}

export interface ResponseAuthData {
  userId: string;
  email: string;
  roles: UserRole[];
  token: string;
}
