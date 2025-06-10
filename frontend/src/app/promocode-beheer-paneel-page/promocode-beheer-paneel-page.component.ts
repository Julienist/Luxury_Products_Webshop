import {Component, inject} from '@angular/core';
import {UserService} from "../services/user.service";
import {NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-promocode-beheer-paneel-page',
  imports: [
    NgIf,
    RouterLink,
    TranslatePipe
  ],
  templateUrl: './promocode-beheer-paneel-page.component.html',
  styleUrl: './promocode-beheer-paneel-page.component.scss'
})
export class PromocodeBeheerPaneelPageComponent {
  protected userService = inject(UserService);

  protected hasInsightRights(): boolean {
    const roles = this.userService.getUserRoles();
    return roles.includes('All_insights') || roles.includes('SuperAdmin');
  }

  protected hasMakeDeactivateRights(): boolean {
    const roles = this.userService.getUserRoles();
    return roles.includes('Make_and_deactivate_promocodes') || roles.includes('SuperAdmin');
  }

  protected hasUsageInsightsRights(): boolean {
    const roles = this.userService.getUserRoles();
    return roles.includes('Insight_promocode_usage') || roles.includes('SuperAdmin');
  }

  protected hasPromocodeManagementRights(): boolean {
    const roles = this.userService.getUserRoles();
    return roles.includes('All_insights') ||
           roles.includes('Make_and_deactivate_promocodes') ||
           roles.includes('Insight_promocode_usage') ||
           roles.includes('SuperAdmin');
  }
}
