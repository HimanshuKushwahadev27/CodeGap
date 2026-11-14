import { Component, EventEmitter, inject, Output } from '@angular/core';
import { AuthService } from '../auth/service/auth.service';

@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  @Output() readonly menuToggled = new EventEmitter<void>();
  @Output() readonly menuClosed = new EventEmitter<void>();

  protected readonly authService = inject(AuthService);
  protected toggleMenu(): void {
    this.menuToggled.emit();
  }

  protected closeMenu(): void {
    this.menuClosed.emit();
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
