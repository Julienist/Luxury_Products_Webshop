import {Component, inject, OnInit} from '@angular/core';
import {PromocodeService} from "../services/promocode.service";
import {ToastrService} from "ngx-toastr";
import {FormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {TranslatePipe} from "@ngx-translate/core";
import {MatButton} from "@angular/material/button";
import {MatFormField, MatLabel, MatError} from "@angular/material/input";
import {NgForOf, NgIf} from "@angular/common";
import {MatOption} from "@angular/material/core";
import {MatSelect} from "@angular/material/select";
import {Router} from "@angular/router";

@Component({
  selector: 'app-deactivate-promocode-page',
  imports: [
    ReactiveFormsModule,
    MatLabel,
    TranslatePipe,
    MatButton,
    MatFormField,
    MatFormField,
    NgIf,
    MatError,
    MatOption,
    MatSelect,
    NgForOf
  ],
  templateUrl: './deactivate-promocode-page.component.html',
  styleUrl: './deactivate-promocode-page.component.scss'
})
export class DeactivatePromocodePageComponent implements OnInit {

  private router = inject(Router);
  private promocodeService = inject(PromocodeService);
  private toastrService = inject(ToastrService);
  private fb = inject(FormBuilder);

  public promocodesList: string[] = [];
  public deactivatePromocodeForm = this.fb.group({
    promocode: ['']
  });

  ngOnInit(): void {
    this.loadPromocodes();
    this.deactivatePromocodeForm = this.fb.group({
      promocode: ['', [Validators.required]]
    });
  }

  loadPromocodes(): void {
    this.promocodeService.getPromocodes().subscribe({
      next: (response) => {
        this.promocodesList = response.promoCodes;
      },
      error: (error) => {
        console.error('Error loading promocodes:', error);
        this.toastrService.error('Failed to load promocodes');
      }
    });
  }

  protected submitDeactivatePromocode(): void {
    const promocode = this.deactivatePromocodeForm.get('promocode')?.value;
    if (!promocode) {
      this.toastrService.error('Please enter a promocode');
      return;
    }

    this.promocodeService.deactivatePromocode(promocode).subscribe({
      next: () => {
        this.toastrService.success('Promocode deactivated successfully');
        this.loadPromocodes();
        this.deactivatePromocodeForm.reset();
      },
      error: (error: any) => {
        console.error('Error deactivating promocode:', error);
        this.toastrService.error('Failed to deactivate promocode');
      }
    });
  }

  public cancel(): void {
    this.deactivatePromocodeForm.reset();
    this.toastrService.info('Deactivation cancelled');
    this.router.navigate(['adminpanel/promocode_beheer']);
  }


}
