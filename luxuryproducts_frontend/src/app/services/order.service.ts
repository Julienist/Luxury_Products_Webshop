import {inject, Injectable} from '@angular/core';
import {OrderRequest} from '../models/OrderRequest';
import {ShoppingCartService} from './shopping-cart.service';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ResponseOrderData} from '../models/ResponseOrderData';
import {Order} from '../models/Order';
import {environment} from '../../environments/environment';
import {catchError} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private httpClient = inject(HttpClient);
  private cartService = inject(ShoppingCartService);

  public createOrder(userId: number, shippingAddress: string) {
    const cartItems = this.cartService.getCart()();

    const orderData: OrderRequest = {
      userId: userId,
      shippingAddress: shippingAddress,
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

  public fetchOrdersByUserId(userId: string | null){
    const apiUrl = environment.baseApiUrl + `/orders/${userId}`;
    return this.httpClient.get<Order[]>(apiUrl);
  }

}
