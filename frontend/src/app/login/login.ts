import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth'; 
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html', 
  styleUrls: ['./login.css']   
})
export class LoginComponent {
  loginForm: FormGroup;
  mostrarSenha = false;
  erroLogin = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', Validators.required]
    });
  }

  toggleSenha() {
    this.mostrarSenha = !this.mostrarSenha;
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe({
        next: (usuario) => {
          console.log('Login Sucesso:', usuario);
          localStorage.setItem('usuario', JSON.stringify(usuario));
          alert("Login realizado com sucesso! Bem-vindo(a) " + usuario.nome);
          // this.router.navigate(['/home']); 
        },
        error: (err) => {
          console.error('Erro:', err);
          this.erroLogin = true;
        }
      });
    }
  }
}