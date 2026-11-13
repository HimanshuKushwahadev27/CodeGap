import { Component, inject, signal } from '@angular/core';
import { AuthService } from '../auth/service/auth.service';

@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  protected readonly authService = inject(AuthService);
  protected readonly isMenuOpen = signal(false);

  protected toggleMenu(): void {
    this.isMenuOpen.update((isOpen) => !isOpen);
  }

  protected closeMenu(): void {
    this.isMenuOpen.set(false);
  }

  protected login(): void {
    this.closeMenu();
    this.authService.login();
  }

  protected logout(): void {
    this.closeMenu();
    this.authService.logout();
  }
}
