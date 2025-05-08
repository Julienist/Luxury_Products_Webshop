import {Component, Input} from '@angular/core';
import {Category} from '../../models/Category';
import {TranslateModule} from '@ngx-translate/core';

@Component({
  selector: 'app-category',
  imports: [TranslateModule],
  templateUrl: './category.component.html',
  styleUrl: './category.component.scss'
})
export class CategoryComponent {

  @Input() category: Category | undefined;

}
