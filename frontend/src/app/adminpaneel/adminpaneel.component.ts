import {Component, inject} from '@angular/core';
import {TranslatePipe} from "@ngx-translate/core";
import {RouterLink} from "@angular/router";
import {NgIf} from "@angular/common";
import {UserService} from "../services/user.service";

@Component({
  selector: 'app-adminpaneel',
  standalone: true,
  imports: [
    TranslatePipe,
    RouterLink,
    NgIf
  ],
  templateUrl: './adminpaneel.component.html',
  styleUrl: './adminpaneel.component.scss'
})
export class AdminpaneelComponent {
  protected userService = inject(UserService);

  protected hasInsightRights(): boolean {
    const roles = this.userService.getUserRoles();
    return roles.includes("All_insights") || roles.includes("SuperAdmin");
  }

  protected hasMakeDeactivateRights(): boolean {
    const roles = this.userService.getUserRoles();
    return roles.includes("Make_and_deactivate_promocodes") || roles.includes("SuperAdmin");
  }

  protected hasUsageInsightsRights(): boolean {
    const roles = this.userService.getUserRoles();
    return roles.includes("Insight_promocode_usage") || roles.includes("SuperAdmin");
  }

  protected hasPromocodeManagementRights(): boolean {
    const roles = this.userService.getUserRoles();
    return roles.includes("All_insights") || roles.includes("Make_and_deactivate_promocodes") || roles.includes("Insight_promocode_usage") || roles.includes("SuperAdmin");
  }
}
