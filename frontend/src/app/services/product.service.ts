import { HttpClient } from '@angular/common/http';
import {inject, Injectable } from '@angular/core';
import {Product} from '../models/Product';
import {catchError, map, Observable, throwError} from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private httpClient = inject(HttpClient);
  private apiUrl = environment.baseApiUrl;

    public loadProducts() {
        return new Observable<Product[]>(observer => {
            this.loadProductsFromCache().then(cachedProducts => {
                if (cachedProducts) {
                    observer.next(cachedProducts);
                    observer.complete();
                } else {
                    this.fetchProducts(
                        this.apiUrl + '/products',
                        'Something went wrong fetching the products. Please try again later.'
                    ).subscribe({
                        next: products => {
                            this.saveProductsToCache(products);
                            observer.next(products);
                            observer.complete();
                        },
                        error: err => observer.error(err)
                    });
                }
            });
        });
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

  public async saveProductsToCache(products: Product[]): Promise<void> {
    const cache = await caches.open('product-cache');
    const response = new Response(JSON.stringify(products), {
        headers: { 'Content-Type': 'application/json' }
    });
    await cache.put(this.apiUrl + '/products', response);
  }

  public async loadProductsFromCache(): Promise<Product[] | null> {
    const cache = await caches.open('product-cache');
    const cachedResponse = await cache.match(this.apiUrl + '/products');
    if (cachedResponse) {
        return await cachedResponse.json();
    }
    return null;
  }


}
