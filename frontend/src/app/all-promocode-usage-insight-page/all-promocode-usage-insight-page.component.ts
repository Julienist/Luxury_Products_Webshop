import {Component, inject, OnInit} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {PromocodeService} from "../services/promocode.service";
import {ExistingPromocodeUsage} from "../models/ExistingPromocodeUsage";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow, MatRowDef, MatTable
} from "@angular/material/table";
import {DatePipe, NgIf} from "@angular/common";
import {MatButton} from "@angular/material/button";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-all-promocode-usage-insight-page',
    imports: [
        MatCell,
        MatCellDef,
        MatColumnDef,
        MatHeaderCell,
        MatHeaderRow,
        MatHeaderRowDef,
        MatRow,
        MatRowDef,
        MatTable,
        NgIf,
        MatHeaderCellDef,
        MatButton,
        TranslatePipe,
        RouterLink,
        DatePipe
    ],
  templateUrl: './all-promocode-usage-insight-page.component.html',
  styleUrl: './all-promocode-usage-insight-page.component.scss'
})
export class AllPromocodeUsageInsightPageComponent implements OnInit {
  private router = inject(Router);
  private promocodeService = inject(PromocodeService);

  displayedColumns: string[] = [
      'id', 'usedAt','discountApplied','promocode'
  ];
  dataSource: ExistingPromocodeUsage[] = [];
  errorMessage: string | undefined;

  ngOnInit() {
    this.loadAllPromocodeUsageDataFromAPI();
  }

  loadAllPromocodeUsageDataFromAPI(): void {
    this.promocodeService.getAllPromocodeUsageData().subscribe({
      next: (data) => {
        if (typeof data === 'string') {
          this.errorMessage = data; // Handle error message
        } else {
          this.dataSource = [...data]; // Handle data
        }
      },
      error: (error) => {
        this.errorMessage = 'An unexpected error occurred';
        // console.error(error);
      }
    });
  }

}
