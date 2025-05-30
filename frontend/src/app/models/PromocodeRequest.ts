export interface PromocodeRequest {
    code: string;
    scopeType: 'CATEGORY' | 'PRODUCT';
    scopeValue: number | string;
    discountType: 'PERCENTAGE' | 'FIXED';
    discountValue: number;
    minOrderAmount: number;
    maxUsesPerUser?: number;
    expiryDate: string | Date;
}