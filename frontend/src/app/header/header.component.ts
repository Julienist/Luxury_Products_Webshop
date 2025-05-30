import {Component, inject, signal} from '@angular/core';
import {ShoppingCartService} from '../services/shopping-cart.service';
import {Router, RouterModule} from '@angular/router';
import {NgIf} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import {UserService} from '../services/user.service';
import {SwitchLanguageComponent} from '../switch-language/switch-language.component';

@Component({
  selector: 'app-header',
  imports: [
    RouterModule,
    TranslateModule,
    NgIf
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  private router = inject(Router);
  protected userService = inject(UserService);

  protected logout(): void {
    this.userService.logout();
    this.router.navigate(['/register']);
  }

  protected goToUser(userId: string | null): void{
    this.router.navigate(['/user/' + userId]);
  }

  protected isAdmin(): boolean {
    const roles = this.userService.getUserRoles();
    return roles.includes('Admin');
  }

}
