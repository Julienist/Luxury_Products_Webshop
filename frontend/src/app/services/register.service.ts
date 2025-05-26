import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {ResponseAuthData, UserRole} from '../models/ResponseAuthData';
import {environment} from '../../environments/environment';
import {UserService} from './user.service';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  private httpClient = inject(HttpClient);
  private userService = inject(UserService);
  private loggedIn: boolean = false;
  private userRoles: string[] = [];
  private token: string | null = null;

  constructor() {
    this.loadTokenFromLocalStorage();
    if (this.token != null) {
      this.loggedIn = true;
    }
  }

  public register(registerData: { email: string | null | undefined; password: string | null | undefined;}): Observable<ResponseAuthData> {
    return this.httpClient.post<ResponseAuthData>(
      environment.baseApiUrl + '/auth/register',
      registerData
    ).pipe(
      tap(resData => {
        if (resData.token) {
          this.loggedIn = true;
          this.token = resData.token;
          this.userService.saveTokenInLocalStorage(resData.token);
          this.userService.saveUserEmailInLocalStorage(resData.email);
          if (resData.roles) {
            this.mapAndStoreRoles(resData.roles);
          }
        }
      },
      error => {
        console.error('Error during registration: ', error);
        if (error.status === 400) {
          console.log(error);
        }
      })
    );
  }

  private loadTokenFromLocalStorage(){
    this.token = localStorage.getItem('authToken');
  }

  private mapAndStoreRoles(roles: UserRole[]): void {
    this.userRoles = roles.map(role => role.name);
    localStorage.setItem('userRoles', JSON.stringify(this.userRoles));
  }
}
