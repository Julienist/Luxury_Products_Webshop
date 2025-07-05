import {Injectable, signal, effect} from '@angular/core';
import {Product} from '../models/Product';
import {CartItem} from '../models/CartItem';

@Injectable({
  providedIn: 'root'
})
export class ShoppingCartService {
  private userId: string | null = null;

  private cart = signal<CartItem[]>([]);

  private discountValue = 0;
  setDiscountValue(value: number): void { this.discountValue = value; }
  getDiscountValue(): number { return this.discountValue; }

  constructor() {
    const savedUserId = localStorage.getItem('loggedInUserId');
    if (savedUserId) {
      this.setUser(savedUserId);
    } else {
      this.setUser('guest');
    }

    effect(() => {
      if (this.userId) {
        this.saveCartToLocalStorage();
      }
    });

    // load cart state from localstorage
    this.loadCartFromLocalStorage();
  }

  public setUser(userId: string): void {
    this.userId = userId;
    localStorage.setItem('loggedInUserId', userId);
    this.cart.set(this.loadCartFromLocalStorage());
  }

  public getCart() {
    return this.cart;
  }

  public addToCart(product: Product): void {
    const existingItem = this.cart().find(item => item.product.id === product.id);

    if (existingItem) {
      existingItem.quantity++;
    } else {
      this.cart.set([...this.cart(), { product, quantity: 1 }]);
    }

    this.saveCartToLocalStorage();
  }

  public removeFromCart(productId: number): void {
    this.cart.set(this.cart().filter(item => item.product.id !== productId));
    this.saveCartToLocalStorage();
  }

  public increaseQuantity(productId: number): void {
    const item = this.cart().find(item => item.product.id === productId);
    if (item) {
      item.quantity++;
      this.saveCartToLocalStorage();
    }
  }

  public decreaseQuantity(productId: number): void {
    this.cart.set(this.cart().map(item => {
      if (item.product.id === productId) {
        return item.quantity > 1 ? { ...item, quantity: item.quantity - 1 } : item;
      }
      return item;
    }));
    this.saveCartToLocalStorage();
  }

  public clearCart(): void {
    this.cart.set([]);
    this.saveCartToLocalStorage();
  }

  public deletePromocodeLocalStorageData(): void {
    localStorage.removeItem('promocodeApplied');
    localStorage.removeItem('appliedDiscountValue');
  }

  private saveCartToLocalStorage(): void {
    if (this.userId) {
      const key = this.getCartKey();
      localStorage.setItem(key, JSON.stringify(this.cart()));
    }
  }

  private loadCartFromLocalStorage(): CartItem[] {
    if (this.userId) {
      const key = this.getCartKey();
      const savedCart = localStorage.getItem(key);
      return savedCart ? JSON.parse(savedCart) : [];
    }
    return [];
  }

  private getCartKey(): string {
    return `shoppingCart_${this.userId}`;
  }

  public transferGuestCartToUser(userId: string): void {
    const guestCartKey = 'shoppingCart_guest';
    const userCartKey = `shoppingCart_${userId}`;
    const guestCart = localStorage.getItem(guestCartKey);

    if (guestCart) {
      localStorage.setItem(userCartKey, guestCart);
      localStorage.removeItem(guestCartKey); // remove guest cart after transfer.
    }
  }
}
