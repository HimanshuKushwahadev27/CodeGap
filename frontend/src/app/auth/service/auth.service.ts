import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { authConfig } from '../auth.config';

@Injectable({
  providedIn: 'root',
})
export class AuthService {



  isAuthenticated = signal(false);

  constructor (    
    public oauthService: OAuthService,
    private router: Router
  ){}

  initLogin(){
    this.oauthService.configure(authConfig);

    this.oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
            this.updateAuthState();
    })

    this.oauthService.events.subscribe(() => {
      this.updateAuthState();
    })
  }

  private updateAuthState(){
      this.isAuthenticated.set(
      this.oauthService.hasValidAccessToken()
    );
  }

  login(){
    this.oauthService.initCodeFlow();
  }

  logout(){
    this.oauthService.logOut();
  }

  getToken(){
    return this.oauthService.getAccessToken();
  }

  isLoggedIn(){
    return this.oauthService.hasValidAccessToken();
  }

  getUser() {
    const claims: any = this.oauthService.getIdentityClaims();
    console.log(claims);
  }

}
