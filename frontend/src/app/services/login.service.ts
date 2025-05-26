import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ResponseAuthData } from '../models/ResponseAuthData';
import { BehaviorSubject, tap } from 'rxjs';
import {environment} from '../../environments/environment';
import {UserService} from './user.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private httpClient = inject(HttpClient);
  private userService = inject(UserService);
  private authTokenKey = 'authToken';

  private loggedInSubject = new BehaviorSubject<boolean>(this.userService.hasValidToken());

  public login(login: { email: string | null | undefined; password: string | null | undefined }) {
    return this.httpClient.post<ResponseAuthData>(
      environment.baseApiUrl + '/auth/login',
      login
    ).pipe(
      tap(resData => {
        if (resData.token) {
          this.saveTokenInLocalStorage(resData.token);
          this.loggedInSubject.next(true);
        }
      })
    );
  }

  private saveTokenInLocalStorage(token: string): void {
    localStorage.setItem(this.authTokenKey, token);
  }

}
