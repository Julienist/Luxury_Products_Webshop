import {OrderItem} from './OrderItem';

export interface Order {
  id: number;
  user: {
    id: number;
    email: string;
  },
  orderItems: OrderItem[];
  shippingAddress: string;
  totalPrice: number;
  orderDate: string;
}
