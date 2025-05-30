import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, map, throwError} from 'rxjs';
import {Category} from '../models/Category';
import {environment} from '../../environments/environment.dev';

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

  public async saveCategoriesToCache(categories: Category[]): Promise<void> {
    const cache = await caches.open('category-cache');
    const response = new Response(JSON.stringify(categories), {
      headers: { 'Content-Type': 'application/json' }
    });
    await cache.put(this.apiUrl + '/categories', response);
  }

  public async loadCategoriesFromCache(): Promise<Category[] | null> {
    const cache = await caches.open('category-cache');
    const cachedResponse = await cache.match(this.apiUrl + '/categories');
    if (cachedResponse) {
      return await cachedResponse.json();
    }
    return null;
  }

}
