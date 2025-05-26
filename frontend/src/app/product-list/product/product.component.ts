import { Component, inject, Input, OnInit, SimpleChanges} from '@angular/core';
import { Product } from '../../models/Product';
import { ShoppingCartService } from '../../services/shopping-cart.service';
import { Router, ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Observable } from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {TranslateModule} from '@ngx-translate/core';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-product',
  imports: [TranslateModule, NgIf],
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {
  @Input() product!: Product;
  private cartService = inject(ShoppingCartService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private productService = inject(ProductService);
  private toastrService = inject(ToastrService);

  public ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('id');

    if (productId) {
      this.productService.getProductById(+productId).subscribe(
        (product) => {
          if (product) {
            this.product = product;
          } else {
            console.error('Product not found');
          }
        },
        (error) => {
          console.error('Error fetching product:', error);
        }
      );
    }
  }

  protected addToCart(event: Event): void {
    event.stopPropagation();
    this.cartService.addToCart(this.product);
    this.showSuccess('Product added successfully.');
  }


  protected goToProduct(productId: number): void{
    this.router.navigate(['/products/' + productId]);
  }

  protected showSuccess(message: string): void {
    this.toastrService.success(`<b>${message}</b>`, 'Success!', {
      toastClass: 'custom-toast-class',
      enableHtml: true,
    });
  }

}
