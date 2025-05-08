import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, map, throwError} from 'rxjs';
import {Category} from '../models/Category';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  selectedCategory = signal<Category | null>(null)

  private httpClient = inject(HttpClient);
  private apiUrl = environment.baseApiUrl;

  public loadCategories() {
    return this.fetchCategories(
      this.apiUrl + '/categories',
      'Something went wrong fetching the categories. Please try again later.'
    )
  }

  private fetchCategories(url:string, errorMessage: string) {
    return this.httpClient
      .get<Category[]>(url)
      .pipe(
        map((resData) => {
          return resData;
        }),
        catchError((error) => {
          return throwError(() => new Error(errorMessage));
        })
      )
  }

}
