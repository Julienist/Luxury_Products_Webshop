import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { UserService } from './user.service';

export const adminGuard: CanActivateFn = () => {
    const userService = inject(UserService);
    return userService.getUserRoles().includes('Admin');
};

export const superAdminGuard: CanActivateFn = () => {
    const userService = inject(UserService);
    return userService.getUserRoles().includes('SuperAdmin');
}

export const hasInsightRights = (): boolean => {
    const userService = inject(UserService);
    return userService.getUserRoles().includes('All_insights') || userService.getUserRoles().includes('SuperAdmin');
}

export const hasUsageInsightsRights = (): boolean => {
    const userService = inject(UserService);
    return userService.getUserRoles().includes('Insight_promocode_usage') || userService.getUserRoles().includes('SuperAdmin');
}

export const hasMakeDeactivateRights = (): boolean => {
    const userService = inject(UserService);
    return userService.getUserRoles().includes('Make_and_deactivate_promocodes') || userService.getUserRoles().includes('SuperAdmin');
}

export const hasPromocodeManagementRights = (): boolean => {
    const userService = inject(UserService);
    return userService.getUserRoles().includes('All_insights') ||
           userService.getUserRoles().includes('Make_and_deactivate_promocodes') ||
           userService.getUserRoles().includes('Insight_promocode_usage') ||
           userService.getUserRoles().includes('SuperAdmin');
}