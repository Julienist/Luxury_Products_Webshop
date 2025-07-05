import {Component, inject, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Product } from '../../models/Product';
import { ProductService } from '../../services/product.service';
import {NgIf} from '@angular/common';
import {ShoppingCartService} from '../../services/shopping-cart.service';
import {TranslateModule} from '@ngx-translate/core';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-product-detail',
  imports: [NgIf, TranslateModule],
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.scss']
})
export class ProductDetailComponent implements OnInit {
  @Input({required: true}) product!: Product;

  private route = inject(ActivatedRoute);
  private productService = inject(ProductService);
  private cartService = inject(ShoppingCartService);
  private toastrService = inject(ToastrService);

  public ngOnInit(): void {
    this.route.params.subscribe(params => {
      const productId = +params['productId'];
      if (!isNaN(productId)) {
        this.loadProduct(productId);
      } else {
        console.error("Invalid product ID:", params['productId']);
      }
    });
  }

  private loadProduct(id: number): void {
    this.productService.getProductById(id).subscribe({
      next: (product) => {
        this.product = product;
      },
      error: (err) => {
        console.error("Error loading product:", err);
      }
    });
  }

  protected addToCart(): void {
    this.cartService.addToCart(this.product);
    this.showSuccess('product added successfully!');
  }

  protected showSuccess(message: string): void {
    this.toastrService.success(`<b>${message}</b>`, '', {
      toastClass: 'custom-toast-class',
      enableHtml: true,
    });
  }
}

