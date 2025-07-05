import {Component, inject, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {TranslatePipe} from "@ngx-translate/core";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatNativeDateModule} from "@angular/material/core";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatSelectModule} from "@angular/material/select";
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {ProductService} from "../services/product.service";
import {CategoryService} from "../services/category.service";
import {NgForOf, NgIf} from "@angular/common";
import {PromocodeRequest} from "../models/PromocodeRequest";
import {PromocodeService} from "../services/promocode.service";
import {Product} from "../models/Product";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-make-promocode-page',
    imports: [
        TranslatePipe,
        MatInputModule,
        MatButtonModule,
        MatSelectModule,
        MatDatepickerModule,
        MatNativeDateModule,
        ReactiveFormsModule,
        NgIf,
        NgForOf
    ],
  templateUrl: './make-promocode-page.component.html',
  styleUrl: './make-promocode-page.component.scss'
})
export class MakePromocodePageComponent implements OnInit {
    private router = inject(Router);
    private productService = inject(ProductService);
    private categoryService = inject(CategoryService);
    private promocodeService = inject(PromocodeService);
    private toastrService = inject(ToastrService);
    private fb = inject(FormBuilder);

    public categoriesList: any[] = [];
    public productsList: any[] = [];

    promocodeForm!: FormGroup;

    selectScope: 'CATEGORY' | 'PRODUCT' | null = null;
    selectType: 'PERCENTAGE' | 'FIXED' | null = null;

    ngOnInit(): void {

        // Load categories from cache first
        (async (): Promise<void> => {
            const categories = await this.categoryService.loadCategoriesFromCache();
            if (!categories || categories.length === 0) {
                console.warn('No categories found in cache, loading from server.');
                this.categoryService.loadCategories().subscribe(serverCategories => {
                    this.categoriesList = serverCategories;
                    this.categoryService.saveCategoriesToCache(serverCategories);
                });
            } else {
                this.categoriesList = categories;
            }
        })();

        // Load products from the product service
        (async (): Promise<void> => {
            const products: Product[] | null = await this.productService.loadProductsFromCache();
            if (!products || products.length === 0) {
                console.warn('No products found in cache, loading from server.');
                this.productService.loadProducts().subscribe(serverProducts => {
                    this.productsList = serverProducts;
                });
            } else {
                this.productsList = products;
            }
        })();

        this.promocodeForm = this.fb.group({
                code: [
                    '',
                    [
                        Validators.required,
                        Validators.pattern('^[a-zA-Z0-9_-]+$'),
                        Validators.minLength(3)
                    ]
            ],
            scopeType: ['', Validators.required],
            scopeValue: ['', Validators.required],
            discountType: ['', Validators.required],
            discountValue: [null, [Validators.required, Validators.min(0)]],
            minOrderAmount: [null, [Validators.required, Validators.min(0)]],
            maxUsesPerEmail: [null, [Validators.min(1)]],
            expiryDate: ['', Validators.required],
        });
    }

    selectScopeType(type: 'CATEGORY' | 'PRODUCT') {
        this.selectScope = type;
        this.promocodeForm.patchValue({ scopeType: type });
        this.promocodeForm.get('scopeValue')?.setValue('');
    }

    selectDiscountType(type: 'PERCENTAGE' | 'FIXED') {
        this.selectType = type;
        this.promocodeForm.patchValue({ discountType: type });
    }

    protected submitPromocode(): void {
        if (this.promocodeForm.valid) {
            const promocodeData = this.promocodeForm.value as PromocodeRequest;
            this.promocodeService.createPromocode(promocodeData).subscribe({
                next: (response) => {
                    this.toastrService.success(
                        `<b>Promocode created successfully:</b> ${JSON.stringify(response)}`,
                        '',
                        { toastClass: 'custom-toast-class', enableHtml: true, timeOut: 5000 }
                    );
                    this.onReturn();
                },
                error: (error) => {
                    this.toastrService.error(
                        `<b>Error creating promocode:</b> ${error.message || error}`,
                        'Error!',
                        { toastClass: 'custom-toast-class', enableHtml: true, timeOut: 5000 }
                    );
                }
            });
        } else {
            this.promocodeForm.markAllAsTouched();
        }
    }

    cancel() {
        this.promocodeForm.reset();
        this.selectScope = null;
        this.selectType = null;
    }

    onReturn() {
        this.router.navigate(['admin/promocodes']);
    }
}
