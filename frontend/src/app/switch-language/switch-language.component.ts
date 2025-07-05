import {Component, inject} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'app-switch-language',
    imports: [
        NgOptimizedImage
    ],
  templateUrl: './switch-language.component.html',
  styleUrl: './switch-language.component.scss'
})
export class SwitchLanguageComponent {
  private translateService = inject(TranslateService)

  protected switchLanguage(selectedLanguage: string): void {
    this.translateService.use(selectedLanguage);
    localStorage.setItem('selectedLanguage', selectedLanguage);
  }

}
