import { HttpClient } from '@angular/common/http';
import {inject, Injectable } from '@angular/core';
import {Product} from '../models/Product';
import {catchError, map, throwError } from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private httpClient = inject(HttpClient);
  private apiUrl = environment.baseApiUrl;

  public loadProducts() {
    return this.fetchProducts(
      this.apiUrl + '/products',
      'Something went wrong fetching the products. Please try again later.'
    )
  }

  public fetchProducts(url:string, errorMessage: string) {
    return this.httpClient
    .get<Product[]>(url)
      .pipe(
        map((resData) => {
          return resData;
        }),
        catchError((error) => {
          return throwError(() => new Error(errorMessage));
        })
      )
  }

  public getProductById(id: number) {
    const url = this.apiUrl + `/products/${id}`;
    return this.httpClient.get<Product>(url)
      .pipe(
        map((product) => {
          return product;
        }),
        catchError((error) => {
          return throwError(() => new Error("Something went wrong fetching the product. Please try again later."));
        })
      );
  }
}
