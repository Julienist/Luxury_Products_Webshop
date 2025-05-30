import { Component } from '@angular/core';
import {RouterModule} from "@angular/router";
import {TranslateModule} from '@ngx-translate/core';
import {SwitchLanguageComponent} from '../switch-language/switch-language.component';

@Component({
  selector: 'app-homepage',
  imports: [RouterModule, TranslateModule],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent {

}
