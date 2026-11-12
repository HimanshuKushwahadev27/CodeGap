
import { AuthConfig } from "angular-oauth2-oidc";

export const authConfig: AuthConfig = {
  issuer: 'http://localhost:8181/realms/Code-gap',
  redirectUri: window.location.origin,
  clientId: 'Code_Gap',
  responseType: 'code',
  scope: 'openid profile email',
  showDebugInformation: true,
  requireHttps: false
}