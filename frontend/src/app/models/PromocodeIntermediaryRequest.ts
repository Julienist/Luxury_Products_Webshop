export interface PromocodeIntermediaryRequest {
    code: string;
    email: string;
    cartItems: {
        productId: number;
        productName: string;
        quantity: number;
        price: number;
    }[]
    totalPrice: number;
}