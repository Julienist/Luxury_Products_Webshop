export interface ExistingPromocodeUsage {
    id: number;
    email: string;
    usedAt: string; // ISO date string
    discountApplied: number; // The actual discount applied, if applicable
    promocode: object;
}