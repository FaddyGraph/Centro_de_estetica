import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { AgendamentoService } from '../services/agendamento.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule], 
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class HomeComponent implements OnInit {

  passoAtual: number = 1;
  
  areas: any[] = [];
  servicos: any[] = [];
  profissionais: any[] = [];

  areaSelecionada: any = null;
  servicoSelecionado: any = null;
  profissionalSelecionado: any = null;
  dataSelecionada: string = '';
  horaSelecionada: string = '';

  constructor(
    private agendamentoService: AgendamentoService,
    private cdr: ChangeDetectorRef 
  ) {}

  ngOnInit(): void {
    this.carregarAreas();
  }

  carregarAreas() {
    this.agendamentoService.listarAreas().subscribe({
      next: (d) => {
        this.areas = d;
        this.cdr.detectChanges();
      },
      error: (e) => console.error("Erro ao carregar áreas:", e)
    });
  }

  carregarServicos(idArea: number) {
    this.servicos = []; 
    this.agendamentoService.listarServicosPorArea(idArea).subscribe({
      next: (d) => {
        this.servicos = d;
        this.cdr.detectChanges();
      },
      error: (e) => console.error("Erro ao carregar serviços:", e)
    });
  }

  carregarProfissionais() {
    this.profissionais = [];
    const idServico = this.servicoSelecionado?.id;
    if (idServico) {
      this.agendamentoService.listarFuncionarios(idServico).subscribe({
        next: (d) => {
          this.profissionais = d;
          this.cdr.detectChanges();
        },
        error: (e) => console.error("Erro ao carregar profissionais:", e)
      });
    }
  }

  selecionarArea(area: any) { 
    this.areaSelecionada = area; 
    this.carregarServicos(area.id); 
    this.passoAtual = 2; 
  }

  selecionarServico(servico: any) { 
    this.servicoSelecionado = servico; 
    this.carregarProfissionais();
    this.passoAtual = 3; 
  }

  selecionarProfissional(prof: any) {
    this.profissionalSelecionado = prof;
    this.passoAtual = 4; 
  }

  voltar() {
    if (this.passoAtual > 1) {
      this.passoAtual--;
      this.cdr.detectChanges();
    }
  }

  finalizarAgendamento() {
    const idFunc = this.profissionalSelecionado?.id_usuario || this.profissionalSelecionado?.id;

    const agendamento = {
      idCliente: 5, 
      idFuncionario: idFunc, 
      idServico: this.servicoSelecionado.id,
      dataHora: `${this.dataSelecionada}T${this.horaSelecionada}:00`, 
      observacoes: "Agendado via Web"
    };

    this.agendamentoService.finalizarAgendamento(agendamento).subscribe({
      next: (res) => alert("Agendamento realizado!"),
      error: (err) => alert("Erro ao salvar. Verifique se criou o Controller de Agendamento no Java.")
    });
  }
}