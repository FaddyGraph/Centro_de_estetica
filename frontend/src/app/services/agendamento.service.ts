import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AgendamentoService {
  private apiUrl = 'http://localhost:8080'; // Endereço do seu Java

  constructor(private http: HttpClient) {}

  // 1. Busca Áreas
  listarAreas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/areas`);
  }

  // 2. Busca Serviços por Área
  listarServicosPorArea(idArea: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/servicos/area/${idArea}`);
  }

  // 3. Busca Horários de um Funcionário
  listarHorarios(idFuncionario: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/horarios/funcionario/${idFuncionario}`);
  }

  // 4. Busca Funcionários (A função que estava faltando!)
  listarFuncionarios(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/usuarios`);
  }
}