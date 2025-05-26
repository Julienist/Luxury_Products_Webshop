import {HttpHandlerFn, HttpRequest} from '@angular/common/http';
import {inject} from '@angular/core';
import {UserService} from '../services/user.service';

export function authInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn){
  const userService = inject(UserService);
  const authToken = userService.getToken();

  if (authToken != null) {
    const newRequest = req.clone({
      headers : req.headers.set("Authorization", `Bearer ${authToken}`)
    });
    return next(newRequest);
  }

  return next(req);
}
