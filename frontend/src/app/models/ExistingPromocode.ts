export interface ExistingPromocode {
    id: number;
    code: string;
    active: boolean;
    creationDate: string;
    expiryDate: string;
    discountType: string;
    discountValue: number;
    minimumOrderAmount: number;
    usedCount: number;
    maxUsesPerEmail: number;
}