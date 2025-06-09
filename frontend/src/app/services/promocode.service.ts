import { inject, Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { catchError, map, Observable, of } from "rxjs";
import { PromoCodesListRequest } from "../models/promoCodesListRequest";
import { ResponsePromocodeData } from "../models/ResponsePromocodeData";
import { PromocodeRequest } from "../models/PromocodeRequest";
import { environment } from "../../environments/environment";
import { ShoppingCartService } from "./shopping-cart.service";
import { PromocodeIntermediaryRequest } from "../models/PromocodeIntermediaryRequest";

@Injectable({
    providedIn: 'root'
})
export class PromocodeService {

    private httpClient = inject(HttpClient);
    private cartService = inject(ShoppingCartService);

    public createPromocode(promocodeData: PromocodeRequest): Observable<string> {
        return this.httpClient.post<string>(
            environment.baseApiUrl + '/promocodes',
            promocodeData,
            { responseType: 'text' as 'json' }
        );
    }

    public sendCodeToAPI(promocode: string): Observable<ResponsePromocodeData> {
        const cartItems = this.cartService.getCart()();

        const code: PromocodeIntermediaryRequest = {
            code: promocode,
            email: localStorage.getItem('userEmail') ?? '',
            cartItems: cartItems.map(item => ({
                productId: item.product.id,
                productName: item.product.name,
                quantity: item.quantity,
                price: item.product.price
            })),
            totalPrice: cartItems.reduce((total, item) => total + item.product.price * item.quantity, 0)
        }

        return this.httpClient.post<ResponsePromocodeData>(
            environment.baseApiUrl + '/promocodes/validateAndApply',
            code
        );
    }

    public getPromocodes(): Observable<PromoCodesListRequest> {
        return this.httpClient.get<string[]>(
            environment.baseApiUrl + '/promocodes',
            { responseType: 'json' as 'json' }
        ).pipe(
            map(codes => ({ promoCodes: codes })),
            catchError(error => {
                console.error('Error fetching promocodes:', error);
                // Return an empty list as a fallback
                return of({ promoCodes: [] });
            })
        );
    }

    // public deactivatePromocode(promocode: string): Observable<string> {
    //     return this.httpClient.put<string>(
    //         environment.baseApiUrl + '/promocodes/' + promocode,
    //         { responseType: 'text' as 'json' }
    //     ).pipe(
    //         catchError(error => {
    //             console.error('Error deactivating promocode:', error);
    //             return of('Failed to deactivate promocode');
    //         })
    //     );
    // }

    public deactivatePromocode(promocode: string): Observable<string> {
        return this.httpClient.put<string>(
            environment.baseApiUrl + '/promocodes/disable_promocode_' + promocode,
            {},
            { responseType: 'text' as 'json' }
        ).pipe(
            catchError(error => {
                console.error('Error deactivating promocode:', error);
                return of('Failed to deactivate promocode');
            })
        );
    }

}