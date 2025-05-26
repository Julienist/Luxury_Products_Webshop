import {inject, Injectable} from '@angular/core';
import {BehaviorSubject, catchError, map, Observable} from 'rxjs';
import {CustomUser} from '../models/CustomUser';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private httpClient = inject(HttpClient);

  private authTokenKey = 'authToken';
  private roleKey = 'role';
  private userIdKey = 'loggedInUserId';
  private apiUrl = environment.baseApiUrl;


  private loggedInSubject = new BehaviorSubject<boolean>(this.hasValidToken());

  public getUserById(id: number): Observable<CustomUser> {
    return this.httpClient.get<CustomUser>(this.apiUrl + '/users/' + id)
      .pipe(
        map((resData) => {
          return resData;
        }),
        catchError(error => {
          console.error('Error fetching user:', error);
          throw error;
        })
      );
  }

  public getUserId(): string | null{
    return localStorage.getItem(this.userIdKey);
  }

  public getToken(): string | null {
    return localStorage.getItem(this.authTokenKey);
  }


  public logout(): void {
    localStorage.removeItem('loggedInUserId');
    localStorage.removeItem('role');
    this.loggedInSubject.next(false);
  }

  public hasValidToken(): boolean {
    const token = localStorage.getItem(this.authTokenKey);
    return token !== null && !this.isTokenExpired(token);
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expiry = payload.exp * 1000;
      return Date.now() > expiry;
    } catch (e) {
      return true;
    }
  }

  public saveTokenInLocalStorage(token: string): void{
    localStorage.setItem('authToken', token);
  }

  public saveUserEmailInLocalStorage(email: string): void {
    localStorage.setItem('userEmail', email);
  }

}
