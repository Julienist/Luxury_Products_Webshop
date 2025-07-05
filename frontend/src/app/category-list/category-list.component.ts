import {Component, DestroyRef, inject, OnInit, signal} from '@angular/core';
import {Category} from '../models/Category';
import {CategoryService} from '../services/category.service';
import {NgFor} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';

@Component({
  selector: 'app-category-list',
  imports: [NgFor, TranslateModule],
  templateUrl: './category-list.component.html',
  styleUrl: './category-list.component.scss'
})
export class CategoryListComponent implements OnInit {
  categories = signal<Category[]>([]);
  isFetching = signal(false);
  error = signal('');

  private categoryService = inject(CategoryService);
  private destroyRef = inject(DestroyRef);

  public ngOnInit(): void {
    this.isFetching.set(true);
    const subscription = this.categoryService.loadCategories().subscribe({
      next: (categories) => {
        this.categories.set(categories);
      },
      error: (error: Error) => {
        console.log('error loading content: ' + error);
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

  protected setSelectedCategory(event: Event): void {
    const selectedCategoryId = Number((event.target as HTMLSelectElement).value);

    const selectedCategory = this.categories().find(cat => cat.id === selectedCategoryId) || null;
    this.categoryService.selectedCategory.set(selectedCategory);
  }
}
