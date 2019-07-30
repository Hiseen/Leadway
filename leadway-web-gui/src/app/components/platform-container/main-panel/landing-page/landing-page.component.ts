import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserMetadataService } from 'src/app/services/user-info/user-metadata.service';

@Component({
  selector: 'leadway-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }

  /**
   * Based on which category user has selected, this method will
   *  navigate to the relative search path.
   *
   * @param category category user selected
   */
  public categoryNavigate(category: string) {
    this.router.navigate(['search'], {queryParams: {query: category, location: ''}});
  }

}
