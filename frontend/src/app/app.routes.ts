import { Routes } from '@angular/router';
import { LoginComponent } from './login/login';
import { HomeComponent } from './home/home';
import { MeusAgendamentosComponent } from './meus-agendamentos/meus-agendamentos';
import { FuncionarioComponent } from './funcionario/funcionario';


export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'home', component: HomeComponent },
    { path: 'meus-agendamentos', component: MeusAgendamentosComponent },
    { path: 'funcionario', component: FuncionarioComponent },
    
];