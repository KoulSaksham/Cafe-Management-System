import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { SnackbarService } from './snackbar.service';
import { jwtDecode } from "jwt-decode";
import { GlobalConstants } from '../shared/global-constants';
@Injectable({
  providedIn: 'root'
})
export class RouteGuardService {
  expectedRoleArray : Array<string>=[];
  constructor(public auth: AuthService,
    public router: Router,
    private snacknBar: SnackbarService,
  ) { }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    this.expectedRoleArray= route.data['expectedRole'];
    
    console.log(route.data['expectedRole']);
    //this.expectedRoleArray = expectedRoleArray.expectedRole;

    const token: any = localStorage.getItem('token');

    var tokenPayLoad: any;
    try {
      tokenPayLoad = jwtDecode(token);
    } catch (err) {
      localStorage.clear();
      this.router.navigate(['/']);
    }
    console.log(tokenPayLoad);

    let expectedRole = '';

    for (let i = 0; i < this.expectedRoleArray.length; i++) {
      if (this.expectedRoleArray[i] == tokenPayLoad.role) {
        expectedRole = tokenPayLoad.role;
      }
    }

    if (tokenPayLoad.role == 'user' || tokenPayLoad.role == 'admin') {
      if (this.auth.isAuthenticated() && tokenPayLoad.role == expectedRole) {
        return true;
      }
      this.snacknBar.openSnackBar(GlobalConstants.unauthorize, GlobalConstants.error);
      this.router.navigate(['/cafe/dashboard']);
      return false;
    }
    else {
      this.router.navigate(['/']);
      localStorage.clear();
      return false;
    }

  }
}
