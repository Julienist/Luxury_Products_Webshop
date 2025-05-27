import { Routes } from '@angular/router';
import {ShoppingCartComponent} from './shopping-cart/shopping-cart.component';
import {HomepageComponent} from './homepage/homepage.component';
import {ProductListComponent} from './product-list/product-list.component';
import {OrderComponent} from './order/order.component';
import {LoginComponent} from './authentication/login/login.component';
import {RegisterComponent} from './authentication/register/register.component';
import {ProductDetailComponent} from './product-list/product-details/product-details.component';
import {UserComponent} from './user/user.component';
import {authGuard} from './auth.guard';
import {AdminpaneelComponent} from "./adminpaneel/adminpaneel.component";
import {adminGuard} from "./services/admin.guard";

export const routes: Routes = [
  {
    path: '',
    component: HomepageComponent
  },
  {
    path: 'products',
    component: ProductListComponent
  },
  {
    path: 'cart',
    component: ShoppingCartComponent
  },
  {
    path: 'order',
    component: OrderComponent,
    canActivate: [authGuard]
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'products/:productId',
    component: ProductDetailComponent,
  },
  {
    path: 'user/:userId',
    component: UserComponent,
  },
  {
    path: 'adminpanel',
    component: AdminpaneelComponent,
    canActivate: [adminGuard]
  }
];
