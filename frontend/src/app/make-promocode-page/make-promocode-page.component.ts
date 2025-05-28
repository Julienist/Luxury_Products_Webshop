import {Component, inject} from '@angular/core';
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
export class MakePromocodePageComponent {
    private router = inject(Router);
    private productService = inject(ProductService);
    private categoryService = inject(CategoryService);
    private fb = inject(FormBuilder);

    public categoriesList: any[] = [];
    public productsList: any[] = [];

    // public products = this.productService.loadProducts();
    // public categories = this.categoryService.loadCategories();

    // categories: string[] = ['test1', 'test2']; // of bijvoorbeeld ['Drank', 'Eten']
    // products: string[] = [];   // of bijvoorbeeld ['Product1', 'Product2']

    promocodeForm!: FormGroup;



    selectScope: 'CATEGORY' | 'PRODUCT' | null = null;
    selectType: 'PERCENTAGE' | 'FIXED' | null = null;

    ngOnInit(): void {

        // ... (form initialization)
        this.categoryService.loadCategories().subscribe(categories => {
            this.categoriesList = categories;
        });
        this.productService.loadProducts().subscribe(products => {
            this.productsList = products;
        });

        this.promocodeForm = this.fb.group({
            code: ['', Validators.required],
            scopeType: ['', Validators.required],
            scopeValue: ['', Validators.required],
            discountType: ['', Validators.required],
            discountValue: [null, [Validators.required, Validators.min(0)]],
            minOrderAmount: [null, [Validators.required, Validators.min(0)]],
            maxUsesPerUser: [null, [Validators.min(1)]],
            expiryDate: ['', Validators.required]
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

    submit() {
        if (this.promocodeForm.valid) {
            console.log('Promocode aangemaakt:', this.promocodeForm.value);
            // Call API / Service hier
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
        this.router.navigate(['/adminpanel']);
    }
}
