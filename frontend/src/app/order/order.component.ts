import {Component, inject } from '@angular/core';
import {OrderService} from '../services/order.service';
import {ShoppingCartService} from '../services/shopping-cart.service';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router, RouterModule} from '@angular/router';
import {TranslateModule} from '@ngx-translate/core';
import {UserService} from '../services/user.service';

@Component({
  selector: 'app-order',
  imports: [
    FormsModule,
    RouterModule,
    ReactiveFormsModule,
    TranslateModule
  ],
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss'
})
export class OrderComponent {
  private orderService = inject(OrderService);
  private cartService = inject(ShoppingCartService);
  private userService = inject(UserService);
  private router = inject(Router);

  private discountValue = this.cartService.getDiscountValue();

  protected orderForm = new FormGroup({
    "street": new FormControl("", [Validators.required]),
    "city": new FormControl("", [Validators.required]),
    "zip_code": new FormControl("", [Validators.required]),
  });

  protected submitOrder(): void {
    const userId = this.userService.getUserId();
    const userIdAsNumber = Number(userId);
    if (isNaN(userIdAsNumber)) {
      return;
    }

    const shippingAddress = `${this.orderForm.get('street')?.value} ${this.orderForm.get('city')?.value} ${this.orderForm.get('zip_code')?.value}`;

    this.orderService.createOrder(userIdAsNumber, shippingAddress, this.discountValue).subscribe({
      next: (resData) => {
        this.cartService.clearCart();
        this.cartService.deletePromocodeLocalStorageData();
        this.goToUser(resData.user.id);
      },
      error: (err) => {
        console.log('Error placing order', err);
      }
    });
  }

  private goToUser(userId: number): void {
    this.router.navigate(['/user/' + `${userId}`]);
  }
}
