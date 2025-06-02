import {Component, inject} from '@angular/core';
import {ShoppingCartService} from '../services/shopping-cart.service';
import { NgIf, NgFor } from '@angular/common';
import {RouterModule} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {TranslateModule} from '@ngx-translate/core';
import {FormsModule} from "@angular/forms";
import {PromocodeService} from "../services/promocode.service";

@Component({
  selector: 'app-shopping-cart',
    imports: [NgIf, NgFor, RouterModule, TranslateModule, FormsModule],
  templateUrl: './shopping-cart.component.html',
  styleUrl: './shopping-cart.component.scss'
})
export class ShoppingCartComponent {
  private cartService = inject(ShoppingCartService);
  private toastrService = inject(ToastrService);
  private promocodeService = inject(PromocodeService);
  private discount: number = 0;
  protected promocodeApplied: boolean = false;
  appliedDiscountValue: number = 0;

  cart = this.cartService.getCart();
  totalPrice: number = 0;
  promoCode: any;

  constructor() {
    this.updateTotalPrice();
    const applied = localStorage.getItem('promocodeApplied');
      if (applied === 'true') {
          this.promocodeApplied = true;
          this.appliedDiscountValue = Number(localStorage.getItem('appliedDiscountValue')) || 0;
          this.discount = this.appliedDiscountValue;
      } else {
            this.promocodeApplied = false;
            this.appliedDiscountValue = 0;
            this.discount = 0;
      }
  }

  protected removeProduct(productId: number): void{
    this.cartService.removeFromCart(productId);
    this.updateTotalPrice();
    this.showSuccess("Product removed");
  }

  protected increaseQuantity(productId: number): void {
    this.cartService.increaseQuantity(productId);
    this.updateTotalPrice();
  }

  protected decreaseQuantity(productId: number): void {
    this.cartService.decreaseQuantity(productId);
    this.updateTotalPrice();
  }

  protected clearCart(): void {
    this.cartService.clearCart();
    this.showSuccess("Cart cleared");
  }

  // protected getTotalPrice(): number {
  //   return this.cart().reduce((total, item) => total + item.product.price * item.quantity, 0);
  // }

  private updateTotalPrice(): void {
    const baseTotal = this.cart().reduce((total, item) => total + item.product.price * item.quantity, 0);
    this.totalPrice = baseTotal - this.discount;
  }



  protected applyPromocode(): void {
    this.promocodeService.sendCodeToAPI(this.promoCode).subscribe({
        next: (response) => {
            if (response.valid) {
            this.discount = response.discountValue;
            this.appliedDiscountValue = response.discountValue;
            this.showSuccess('Promo code applied!');
            this.cartService.setDiscountValue(this.appliedDiscountValue);
            this.promocodeApplied = true;
            localStorage.setItem('promocodeApplied', 'true');
            localStorage.setItem('appliedDiscountValue', String(this.appliedDiscountValue));
            } else {
            this.discount = 0;
            this.appliedDiscountValue = 0;
            this.showSuccess('Invalid promo code');
            localStorage.removeItem('promocodeApplied');
            localStorage.removeItem('appliedDiscountValue');
            }
            this.updateTotalPrice();
        },
        error: (error) => {
            console.error('Error applying promo code:', error);
            this.showSuccess('Error applying promo code');
        }
    })
  }

  public showSuccess(message: string): void {
    this.toastrService.success(`<b>${message}</b>`, 'Success!', {
      toastClass: 'custom-toast-class',
      enableHtml: true,
      timeOut: 5000,
    });
  }


}
