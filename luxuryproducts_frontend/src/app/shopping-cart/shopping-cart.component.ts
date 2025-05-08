import {Component, inject} from '@angular/core';
import {ShoppingCartService} from '../services/shopping-cart.service';
import { NgIf, NgFor } from '@angular/common';
import {RouterModule} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {TranslateModule} from '@ngx-translate/core';

@Component({
  selector: 'app-shopping-cart',
  imports: [NgIf, NgFor, RouterModule, TranslateModule],
  templateUrl: './shopping-cart.component.html',
  styleUrl: './shopping-cart.component.scss'
})
export class ShoppingCartComponent {
  private cartService = inject(ShoppingCartService);
  private toastrService = inject(ToastrService);

  cart = this.cartService.getCart();

  protected removeProduct(productId: number): void{
    this.cartService.removeFromCart(productId);
    this.showSuccess("Product removed");
  }

  protected increaseQuantity(productId: number): void {
    this.cartService.increaseQuantity(productId);
  }

  protected decreaseQuantity(productId: number): void {
    this.cartService.decreaseQuantity(productId);
  }

  protected clearCart(): void {
    this.cartService.clearCart();
    this.showSuccess("Cart cleared");
  }

  protected getTotalPrice(): number {
    return this.cart().reduce((total, item) => total + item.product.price * item.quantity, 0);
  }

  public showSuccess(message: string): void {
    this.toastrService.success(`<b>${message}</b>`, 'Success!', {
      toastClass: 'custom-toast-class',
      enableHtml: true,
    });
  }
}
