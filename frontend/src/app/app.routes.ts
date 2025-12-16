import { Routes } from '@angular/router';
import { LoginComponent } from './login/login'; // Importe seu componente

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' }, // Redireciona a raiz para o login
    { path: 'login', component: LoginComponent }          // Define a rota do login
];