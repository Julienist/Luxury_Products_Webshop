import { inject, Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ResponsePromocodeData } from "../models/ResponsePromocodeData";
import {PromocodeRequest} from "../models/PromocodeRequest";
import {environment} from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class PromocodeService {

    private httpClient = inject(HttpClient);

    public createPromocode(promocodeData: PromocodeRequest): Observable<string> {
        return this.httpClient.post<string>(
            environment.baseApiUrl + '/promocodes',
            promocodeData,
            { responseType: 'text' as 'json' }
        );
    }

}