import {Component, inject} from '@angular/core';
import {LoginService} from '../../services/login.service';
import {Router, RouterModule} from '@angular/router';
import {FormControl, FormsModule, ReactiveFormsModule, FormGroup, Validators} from '@angular/forms';
import {ShoppingCartService} from '../../services/shopping-cart.service';
import {TranslateModule} from '@ngx-translate/core';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterModule, ReactiveFormsModule, TranslateModule],
  templateUrl: './login.component.html',
  styleUrls: ['../../main-styles/auth-form.scss']
})
export class LoginComponent {

  private loginService = inject(LoginService);
  private cartService = inject(ShoppingCartService);
  private router = inject(Router);

  protected loginForm = new FormGroup({
    "email" : new FormControl("", [Validators.required, Validators.email]),
    "password" : new FormControl("", [Validators.required, Validators.minLength(2)])
  });

  get email() {
    return this.loginForm.get("email");
  }

  get password() {
    return this.loginForm.get("password");
  }

  protected login(): void {
    const loginData = {
      email: this.loginForm.get("email")?.value,
      password: this.loginForm.get("password")?.value,
    };

    console.log(loginData);
    this.loginService.login(loginData).subscribe({
      next: (resData) =>{
        const userId = resData.userId;
        if (userId) {
          this.cartService.setUser(userId);
          this.router.navigate(["products"]);
        }else{
          console.error("No userId recieved from LoginResponse");
        }
      },
      error: (error) => {
        console.log(error.message);
      }
    })
  }
}
