import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { UserService } from '../services/user.service';

export const adminGuard: CanActivateFn = () => {
    const userService = inject(UserService);
    return userService.getUserRoles().includes('Admin');
};