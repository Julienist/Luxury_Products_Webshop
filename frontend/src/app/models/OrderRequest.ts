export interface OrderRequest {
  userId: number;
  shippingAddress: string;
  discountValue?: number;
  orderItems: {
    productId: number;
    productName: string;
    quantity: number;
    price: number;
  }[];
}
