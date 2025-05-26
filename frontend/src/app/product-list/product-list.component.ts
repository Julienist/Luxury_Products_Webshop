import {Component, DestroyRef, inject, signal, OnInit, computed} from '@angular/core';
import {ProductComponent} from './product/product.component';
import {Product} from '../models/Product';
import {ProductService} from '../services/product.service';
import {CategoryListComponent} from '../category-list/category-list.component';
import {CategoryService} from '../services/category.service';
import {NgFor} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';

@Component({
  selector: 'app-product-list',
  imports: [
    ProductComponent,
    CategoryListComponent,
    NgFor,
    TranslateModule
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss'
})
export class ProductListComponent implements OnInit {
  products = signal<Product[]>([]);
  isFetching = signal(false);
  error = signal('');

  private categoryService = inject(CategoryService);
  private productService = inject(ProductService);
  private destroyRef = inject(DestroyRef);

  selectedCategory = computed(() => this.categoryService.selectedCategory());

  displayedProducts = computed(() =>
    this.selectedCategory() ? this.selectedCategory()!.products : this.products()
  );

  public ngOnInit(): void {
    this.isFetching.set(true);
    const subscription = this.productService.loadProducts().subscribe({
        next: (products) => {
          this.products.set(products);
        },
        error: (error: Error) => {
          this.error.set(error.message);
        },
        complete: () => {
          this.isFetching.set(false);
        }
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    })
  }
}
