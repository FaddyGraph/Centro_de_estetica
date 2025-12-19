import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth'; 
import { Router } from '@angular/router';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask'; // Importante para a máscara funcionar

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgxMaskDirective],
  providers: [provideNgxMask()], // Adicionado provider da máscara
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  cadastroForm: FormGroup;

  // Variável que decide qual tela mostrar (começa no login)
  isLoginMode = true;
  mostrarSenha = false;
  erroLogin = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    // Formulário de Login
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', Validators.required]
    });

    // Formulário de Cadastro (Nome, Email, WhatsApp, Senha)
    this.cadastroForm = this.fb.group({
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefone: ['', Validators.required], // Campo WhatsApp
      senha: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  // Função para trocar de tela (Login <-> Cadastro)
  toggleMode() {
    this.isLoginMode = !this.isLoginMode;
    this.erroLogin = false; // Limpa erros antigos
  }

  toggleSenha() {
    this.mostrarSenha = !this.mostrarSenha;
  }

  onSubmit() {
    // --- LÓGICA DE LOGIN ---
    if (this.isLoginMode) {
      if (this.loginForm.valid) {
        this.authService.login(this.loginForm.value).subscribe({
          next: (usuario) => {
            console.log('Login Sucesso:', usuario);
            
            // Salva no localStorage
            localStorage.setItem('usuario', JSON.stringify(usuario));
            
            if (usuario.tipo === 'CLIENTE') {
              this.router.navigate(['/home']);
            } else {
              this.router.navigate(['/funcionario']); 
            }
          },
          error: (err) => {
            console.error(err);
            this.erroLogin = true;
          }
        });
      }

    // --- LÓGICA DE CADASTRO ---
    } else {
      if (this.cadastroForm.valid) {
        // Um objeto novo misturando os dados do form + o perfil fixo
        const dadosCadastro = {
          ...this.cadastroForm.value,
          tipo: 'CLIENTE', // Todo mundo que se cadastra sozinho é cliente
          statusUsuario: 'ATIVO'
        };

        this.authService.cadastrar(dadosCadastro).subscribe({
          next: () => {
            alert('Cadastro realizado com sucesso! Faça login.');
            this.toggleMode(); // Volta para a tela de login automaticamente
          },
          error: (err) => {
            alert('Erro ao cadastrar. Verifique os dados.');
            console.error(err);
          }
        });
      }
    }
  }
}