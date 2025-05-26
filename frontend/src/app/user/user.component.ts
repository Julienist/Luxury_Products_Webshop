import {Component, inject, Input, OnInit, signal} from '@angular/core';
import {UserService} from '../services/user.service';
import {ActivatedRoute, RouterModule} from '@angular/router';
import {CustomUser} from '../models/CustomUser';
import {NgFor, NgIf} from '@angular/common';
import {OrderService} from '../services/order.service';
import {Order} from '../models/Order';
import {TranslatePipe} from '@ngx-translate/core';
import {SwitchLanguageComponent} from '../switch-language/switch-language.component';

@Component({
  selector: 'app-user',
  imports: [NgIf, TranslatePipe, NgFor, RouterModule, SwitchLanguageComponent],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss'
})
export class UserComponent implements OnInit {
  @Input({required: true})  user!: CustomUser;
  error: string | null = null;

  private userService = inject(UserService);
  private route = inject(ActivatedRoute);
  private orderService = inject(OrderService);

  userOrders = signal<Order[]>([]);

  public ngOnInit(): void {
    this.route.params.subscribe(params => {
      const userId = +params['userId'];
      if (!isNaN(userId)) {
        this.loadUser(userId);
        this.loadOrdersByUserId();
      } else {
        console.error("Not logged in");
      }
    });
  }

  private loadUser(id: number): void {
    this.userService.getUserById(id).subscribe({
      next: (user) => {
        this.user = user;
      },
      error: (error) => {
        console.error("Error loading product:", error);
      }
    });
  }

  protected loadOrdersByUserId(): void{
    console.log('load orders was called');
    const userId = this.userService.getUserId();
    this.orderService.fetchOrdersByUserId(userId).subscribe({
      next: (orders) => {
        this.userOrders.set(orders);
      },
      error: (err) => {
        console.log('Error fetching order', err);
      }
    })
  }

}
