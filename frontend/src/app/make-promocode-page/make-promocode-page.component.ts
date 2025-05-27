import {Component, inject} from '@angular/core';
import {Router} from "@angular/router";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-make-promocode-page',
    imports: [
        TranslatePipe
    ],
  templateUrl: './make-promocode-page.component.html',
  styleUrl: './make-promocode-page.component.scss'
})
export class MakePromocodePageComponent {
    private router = inject(Router);

    onReturn() {
        this.router.navigate(['/adminpanel']);
    }
}
