import { ChangeDetectorRef, Component, OnDestroy } from '@angular/core';
import { MediaMatcher } from '@angular/cdk/layout';
import { Menu, MenuItems } from 'src/app/shared/menu-items';
import { jwtDecode } from "jwt-decode";
@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: []
})
export class AppSidebarComponent implements OnDestroy {
  mobileQuery: MediaQueryList;
  userRole: any;
  token: any = localStorage.getItem('token');
  tokenPayLoad: any;


  private _mobileQueryListener: () => void;

  constructor(
    changeDetectorRef: ChangeDetectorRef,
    media: MediaMatcher,
    public menuItems: MenuItems
  ) {
    this.tokenPayLoad = jwtDecode(this.token);
    this.userRole = this.tokenPayLoad?.role;
    console.log("inside constructor",this.menuItems.getMenuItem());
    this.mobileQuery = media.matchMedia('(min-width: 768px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }
  shouldDisplay(menuitem : Menu) : boolean{
    return menuitem.type === 'link' && (menuitem.role === '' || menuitem.role === this.userRole);
  }
}
