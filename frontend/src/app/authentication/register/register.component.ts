import { Component, inject } from '@angular/core';
import { RegistrationService } from '../../services/register.service';
import {Router, RouterModule} from '@angular/router';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {ShoppingCartService} from '../../services/shopping-cart.service';
import {TranslateModule} from '@ngx-translate/core';
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [FormsModule, ReactiveFormsModule, RouterModule, TranslateModule],
  templateUrl: './register.component.html',
  styleUrls: ['../../main-styles/auth-form.scss']
})
export class RegisterComponent {

  private registrationService = inject(RegistrationService);
  private cartService = inject(ShoppingCartService);
  private router = inject(Router);

  private passwordsMatchValidator: ValidatorFn = (formGroup: AbstractControl): ValidationErrors | null => {
    const password = formGroup.get("password")?.value;
    const passwordConfirmation = formGroup.get("password_confirmation")?.value;

    return password === passwordConfirmation ? null : { passwordsMismatch: true };
  };

  protected registerForm = new FormGroup({
    "email" : new FormControl("", [Validators.required, Validators.email]),
    "password" : new FormControl("", [Validators.required, Validators.minLength(2)]),
    "password_confirmation" : new FormControl("", [Validators.required, Validators.minLength(2)])
  },
    { validators: this.passwordsMatchValidator }
  );

  get email() {
    return this.registerForm.get("email");
  }

  get password() {
    return this.registerForm.get("password");
  }

  get password_confirmation() {
    return this.registerForm.get("password_confirmation");
  }

  protected register(): void {
    localStorage.removeItem('authToken');
    const registerData = {
      email: this.registerForm.get("email")?.value,
      password: this.registerForm.get("password")?.value
    };
    this.registrationService.register(registerData).subscribe({
      next: (resData): void => {
        const userId = resData.userId;
        if (userId) {
          this.cartService.transferGuestCartToUser(userId);
          this.cartService.setUser(userId);
          this.router.navigate(["products"]);
        }},
      error: (error) => {
        console.log('Error during registration: ', error);
        alert('make sure your password contains 1 caps, 1 number and 1 special char');
      }
    });
  }

}
