import {Component, inject, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {PromocodeService} from "../services/promocode.service";
import {ExistingPromocode} from "../models/ExistingPromocode";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from "@angular/material/table";
import {NgIf} from "@angular/common";


@Component({
  selector: 'app-all-promocodes-insight-page',
  imports: [
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatTable,
    MatCellDef,
    MatHeaderCellDef,
    MatHeaderRow,
    MatRowDef,
    MatHeaderRowDef,
    MatRow,
    NgIf
  ],
  templateUrl: './all-promocodes-insight-page.component.html',
  styleUrl: './all-promocodes-insight-page.component.scss'
})
export class AllPromocodesInsightPageComponent implements OnInit {
  private router = inject(Router);
  private promocodeService = inject(PromocodeService);
  displayedColumns: string[] = [
    'id', 'code', 'active', 'creationDate', 'expiryDate',
    'discountType', 'discountValue', 'minimumOrderAmount',
    'usedCount', 'maxUsesPerEmail'
  ];
  dataSource: ExistingPromocode[] = [];
  errorMessage: string | undefined;

  ngOnInit() {
    this.loadAllExistingPromocodeDataFromApi();
  }

  loadAllExistingPromocodeDataFromApi(): void {
    this.promocodeService.getAllExisitingPromocodeData().subscribe({
      next: (data) => {
        if (typeof data === 'string') {
          this.errorMessage = data;
        } else {
          this.dataSource = data;
        }
      },
      error: (error) => {
        this.errorMessage = 'Failed to load promocodes';
        // console.log(error);
      }
    });
  }
}
