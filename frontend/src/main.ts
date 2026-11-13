import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';
import { OAuthService } from 'angular-oauth2-oidc';

bootstrapApplication(App, appConfig)
 .then(appRef => {
    const authService = appRef.injector.get(OAuthService);

    authService.loadDiscoveryDocumentAndTryLogin();
    authService.setupAutomaticSilentRefresh();
 })
  .catch((err) => console.error(err));
