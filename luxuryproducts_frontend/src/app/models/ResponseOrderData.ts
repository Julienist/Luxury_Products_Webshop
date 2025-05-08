export interface ResponseOrderData{
  id: number;
  user: {
    id: number;
    email: string;
  };
  orderItems: [
    {
      id: number;
      productId: number;
      productName: string;
      quantity: number;
      price: number;
    }
  ];
  shippingAddress: string;
  totalPrice: number;
}
