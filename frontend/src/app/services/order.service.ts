import {inject, Injectable} from '@angular/core';
import {OrderRequest} from '../models/OrderRequest';
import {ShoppingCartService} from './shopping-cart.service';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ResponseOrderData} from '../models/ResponseOrderData';
import {Order} from '../models/Order';
import {environment} from '../../environments/environment';
import {catchError, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private httpClient = inject(HttpClient);
  private cartService = inject(ShoppingCartService);

  public createOrder(userId: number, shippingAddress: string, discountValue?: number): Observable<ResponseOrderData> {
    const cartItems = this.cartService.getCart()();

    const orderData: OrderRequest = {
      userId: userId,
      shippingAddress: shippingAddress,
      discountValue: discountValue,
      orderItems: cartItems.map(item => ({
        productId: item.product.id,
        productName: item.product.name,
        quantity: item.quantity,
        price: item.product.price
      }))
    }

    return this.httpClient.post<ResponseOrderData>(
      environment.baseApiUrl + '/orders/create',
      orderData);
  }

  public fetchOrdersByUserId(userId: string | null): Observable<Order[]> {
    const apiUrl = environment.baseApiUrl + `/orders/${userId}`;
    return this.httpClient.get<Order[]>(apiUrl);
  }

}
