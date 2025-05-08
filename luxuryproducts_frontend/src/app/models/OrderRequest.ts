export interface OrderRequest {
  userId: number;
  shippingAddress: string;
  orderItems: {
    productId: number;
    productName: string;
    quantity: number;
    price: number;
  }[];
}
