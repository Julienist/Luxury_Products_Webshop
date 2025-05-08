import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {UserService} from './services/user.service';

export const authGuard: CanActivateFn = (route, state) => {
  const userService = inject(UserService);
  const router = inject(Router);

  if (userService.hasValidToken()) {
    return true;
  } else {
    const userId = userService.getUserId();
    router.navigate([`/user/${userId || 'guest'}`], {
      queryParams: { notLoggedIn: true }
    });
    return false;
  }
};
