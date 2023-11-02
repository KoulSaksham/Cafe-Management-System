import { MediaMatcher } from '@angular/cdk/layout';
import { ChangeDetectorRef, Component, OnDestroy, AfterViewInit } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { MenuItems } from 'src/app/shared/menu-items';

@Component({
  selector: 'app-full-layout',
  templateUrl: './full.component.html',
  styleUrls: []
})
export class FullComponent implements OnDestroy, AfterViewInit {
  mobileQuery: MediaQueryList;
  userRole : any;
  token : any = localStorage.getItem('token');
  tokenPayLoad :any;

  private _mobileQueryListener: () => void;

  constructor(
    changeDetectorRef: ChangeDetectorRef,
    media: MediaMatcher,
    public menuItems : MenuItems
  ) {
    this.tokenPayLoad = jwtDecode(this.token);
    this.userRole = this.tokenPayLoad?.role;
    this.mobileQuery = media.matchMedia('(min-width: 768px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }
  ngAfterViewInit() { }
}

