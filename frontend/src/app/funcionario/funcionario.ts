import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AgendamentoService } from '../services/agendamento.service';

@Component({
  selector: 'app-funcionario',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './funcionario.html',
  styleUrls: ['./funcionario.css']
})
export class FuncionarioComponent implements OnInit {

  usuarioLogado: any = null;
  tipo: string = '';

  // Controle de Abas
  abaAtual: string = 'AGENDAMENTOS';

  // --- NOVAS VARIÁVEIS (Listas) ---
  listaClientes: any[] = [];
  listaFuncionarios: any[] = [];

  // --- VARIÁVEIS DO MODAL E EDIÇÃO ---
  modalAberto: boolean = false;
  perfilCadastro: string = 'CLIENTE'; // Define se o modal é de Cliente ou Funcionario

  // Objeto que liga com o formulário do Modal
  usuarioEdicao: any = {
    id: null, nome: '', email: '', telefone: '', senha: '', cargo: '', ativo: true
  };

  // Dados da Agenda (Visualização do dia)
  agendaProfissional: any[] = [];
  dataVisualizacaoAgenda: string = '';

  // Dados de Horários (Configuração/Sanfona)
  diaExpandido: string | null = null;
  diasSemana: string[] = ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'];
  slotsHorariosConfig: string[] = [];
  horariosConfigurados: { [key: string]: string[] } = {};

  // Dados de Áreas
  areas: any[] = [];

  constructor(
    private agendamentoService: AgendamentoService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.verificarPermissao();
    this.inicializarDatas();
    this.gerarSlotsConfiguracao();

    // Carregamentos iniciais
    if (this.usuarioLogado) {
      this.carregarHorariosSalvos();
      this.carregarAgendaDoProfissional();
    }
  }

  verificarPermissao() {
    const userJson = localStorage.getItem('usuario');
    if (userJson) {
      this.usuarioLogado = JSON.parse(userJson);
      this.tipo = this.usuarioLogado.tipo;

      if (this.tipo === 'CLIENTE') {
        this.router.navigate(['/home']);
      }
    } else {
      this.router.navigate(['/login']);
    }
  }

  // --- NAVEGAÇÃO ENTRE ABAS ---
  mudarAba(novaAba: string) {
    this.abaAtual = novaAba;

    if (novaAba === 'AGENDAMENTOS') this.carregarAgendaDoProfissional();
    if (novaAba === 'AREAS') this.carregarAreas();

    // Se clicar em Usuários ou Funcionários, carrega os dados
    if (novaAba === 'USUARIOS' || novaAba === 'FUNCIONARIOS') {
      this.carregarUsuariosDoSistema();
    }
  }

  // --- LÓGICA DE GERENCIAMENTO DE USUÁRIOS (POPULAR TABELA) ---

  carregarUsuariosDoSistema() {
    // MOCK TEMPORÁRIO: Simula dados vindos do banco para você ver a tela funcionando
    // Futuramente, troque isso pela chamada do seu Service (ex: this.service.listarTodos())

    if (this.listaClientes.length === 0) {
      this.listaClientes = [
        { id: 1, nome: 'Maria Silva', email: 'maria@gmail.com', telefone: '(31) 99999-8888', perfil: 'CLIENTE', ativo: true },
        { id: 2, nome: 'João Souza', email: 'joao@hotmail.com', telefone: '(31) 98888-7777', perfil: 'CLIENTE', ativo: true },
        { id: 3, nome: 'Cliente Inativo', email: 'inativo@teste.com', telefone: '(31) 0000-0000', perfil: 'CLIENTE', ativo: false }
      ];
    }

    if (this.listaFuncionarios.length === 0) {
      this.listaFuncionarios = [
        { id: 10, nome: 'Rafaella Corcini', email: 'rafa@salao.com', cargo: 'Gerente', ativo: true, perfil: 'GERENTE' },
        { id: 11, nome: 'Ana Souza', email: 'ana@salao.com', cargo: 'Manicure', ativo: true, perfil: 'FUNCIONARIO' },
        { id: 12, nome: 'Pedro Santos', email: 'pedro@salao.com', cargo: 'Cabelereiro', ativo: false, perfil: 'FUNCIONARIO' }
      ];
    }
  }

  // --- LÓGICA DO MODAL (NOVO / EDITAR / SALVAR) ---

  abrirModalCadastro(tipo: string) {
    this.perfilCadastro = tipo;
    // Limpa o formulário para um cadastro novo
    this.usuarioEdicao = {
      id: null, nome: '', email: '', telefone: '', senha: '', cargo: '', ativo: true
    };
    this.modalAberto = true;
  }

  editarUsuario(usuario: any) {
    this.perfilCadastro = this.abaAtual === 'FUNCIONARIOS' ? 'FUNCIONARIO' : 'CLIENTE';

    // Cria uma cópia do objeto para não alterar a tabela em tempo real antes de salvar
    this.usuarioEdicao = { ...usuario, senha: '' }; // Senha vazia
    this.modalAberto = true;
  }

  fecharModal() {
    this.modalAberto = false;
  }

  salvarUsuario() {
    // Validação simples
    if (!this.usuarioEdicao.nome || !this.usuarioEdicao.email) {
      alert('Por favor, preencha nome e email.');
      return;
    }

    console.log('Enviando para o Back-end:', this.usuarioEdicao);

    // AQUI ENTRARIA A CHAMADA DO SERVIÇO (POST ou PUT)
    // Exemplo:
    // this.agendamentoService.salvarUsuario(this.usuarioEdicao).subscribe(...)

    // --- SIMULAÇÃO VISUAL (MOCK) ---
    if (this.usuarioEdicao.id) {
      // Editando existente: atualiza na lista local
      if (this.perfilCadastro === 'CLIENTE') {
        const index = this.listaClientes.findIndex(c => c.id === this.usuarioEdicao.id);
        if (index !== -1) this.listaClientes[index] = { ...this.usuarioEdicao };
      } else {
        const index = this.listaFuncionarios.findIndex(f => f.id === this.usuarioEdicao.id);
        if (index !== -1) this.listaFuncionarios[index] = { ...this.usuarioEdicao };
      }
    } else {
      // Novo cadastro: adiciona na lista local
      this.usuarioEdicao.id = Math.floor(Math.random() * 1000); // Gera ID falso
      if (this.perfilCadastro === 'CLIENTE') {
        this.listaClientes.push({ ...this.usuarioEdicao });
      } else {
        this.listaFuncionarios.push({ ...this.usuarioEdicao });
      }
    }

    alert('Salvo com sucesso!');
    this.fecharModal();
  }

  // --- LÓGICA DE ATIVAR / DESATIVAR ---

  alternarStatus(usuario: any) {
    const acao = usuario.ativo ? 'desativar' : 'ativar';

    if (confirm(`Tem certeza que deseja ${acao} o usuário ${usuario.nome}?`)) {
      // AQUI ENTRARIA A CHAMADA DO SERVIÇO
      // this.agendamentoService.mudarStatus(usuario.id, !usuario.ativo).subscribe(...)

      // Simulação visual:
      usuario.ativo = !usuario.ativo;
    }
  }

  // --- LÓGICA DE HORÁRIOS (MANTIDA) ---

  carregarHorariosSalvos() {
    if (!this.usuarioLogado?.id) return;

    this.agendamentoService.buscarHorariosTrabalho(this.usuarioLogado.id).subscribe({
      next: (intervalos: any[]) => {
        this.horariosConfigurados = {};

        intervalos.forEach(item => {
          const diaNome = this.converterEnumParaDiaNome(item.diaSemana);

          if (!this.horariosConfigurados[diaNome]) {
            this.horariosConfigurados[diaNome] = [];
          }

          this.preencherSlotsNoIntervalo(diaNome, item.horarioInicio, item.horarioFim);
        });

        this.cdr.detectChanges();
      },
      error: (err) => console.error('Erro ao carregar horários salvos', err)
    });
  }

  salvarHorarios() {
    if (!this.usuarioLogado || !this.usuarioLogado.id) return;

    let totalSalvo = 0;

    for (const diaNome of this.diasSemana) {
      const slots = this.horariosConfigurados[diaNome];

      if (slots && slots.length > 0) {
        slots.sort();
        const intervalos = this.converterSlotsEmIntervalos(slots);

        intervalos.forEach(intervalo => {
          const dto = {
            idFuncionario: this.usuarioLogado.id,
            diaSemana: this.converterDiaNomeParaEnum(diaNome),
            horarioInicio: intervalo.inicio,
            horarioFim: intervalo.fim
          };

          this.agendamentoService.salvarHorarioTrabalho(dto).subscribe({
            next: (res) => console.log(`Salvo ${diaNome}: ${intervalo.inicio}-${intervalo.fim}`),
            error: (err) => console.error('Erro ao salvar', err)
          });
          totalSalvo++;
        });
      }
    }

    if (totalSalvo > 0 || Object.keys(this.horariosConfigurados).length === 0) {
      alert('Processamento de horários iniciado.');
    } else {
      alert('Nenhum horário selecionado para salvar.');
    }
  }

  converterSlotsEmIntervalos(slots: string[]): any[] {
    const intervalos = [];
    if (!slots || slots.length === 0) return [];

    let inicioAtual = slots[0];
    let ultimoSlot = slots[0];

    for (let i = 1; i < slots.length; i++) {
      const slotAtual = slots[i];

      if (this.saoConsecutivos(ultimoSlot, slotAtual)) {
        ultimoSlot = slotAtual;
      } else {
        intervalos.push({
          inicio: inicioAtual,
          fim: this.adicionar15Min(ultimoSlot)
        });
        inicioAtual = slotAtual;
        ultimoSlot = slotAtual;
      }
    }

    intervalos.push({
      inicio: inicioAtual,
      fim: this.adicionar15Min(ultimoSlot)
    });

    return intervalos;
  }

  saoConsecutivos(time1: string, time2: string): boolean {
    const d1 = this.toMinutes(time1);
    const d2 = this.toMinutes(time2);
    return (d2 - d1) === 15;
  }

  adicionar15Min(time: string): string {
    let mins = this.toMinutes(time) + 15;
    const h = Math.floor(mins / 60);
    const m = mins % 60;
    return `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`;
  }

  toMinutes(time: string): number {
    const [h, m] = time.split(':').map(Number);
    return h * 60 + m;
  }

  converterDiaNomeParaEnum(nome: string): string {
    const mapa: { [key: string]: string } = {
      'Domingo': 'DOM', 'Segunda': 'SEG', 'Terça': 'TER',
      'Quarta': 'QUA', 'Quinta': 'QUI', 'Sexta': 'SEX', 'Sábado': 'SAB'
    };
    return mapa[nome] || 'SEG';
  }

  converterEnumParaDiaNome(enumVal: string): string {
    const mapa: { [key: string]: string } = {
      'DOM': 'Domingo', 'SEG': 'Segunda', 'TER': 'Terça',
      'QUA': 'Quarta', 'QUI': 'Quinta', 'SEX': 'Sexta', 'SAB': 'Sábado'
    };
    return mapa[enumVal] || 'Segunda';
  }

  preencherSlotsNoIntervalo(dia: string, inicio: string, fim: string) {
    if (!inicio || !fim) return;

    const inicioLimpo = inicio.substring(0, 5);
    const fimLimpo = fim.substring(0, 5);

    let [hI, mI] = inicioLimpo.split(':').map(Number);
    let [hF, mF] = fimLimpo.split(':').map(Number);

    let minutosAtuais = hI * 60 + mI;
    const minutosFim = hF * 60 + mF;

    if (!this.horariosConfigurados[dia]) {
      this.horariosConfigurados[dia] = [];
    }

    while (minutosAtuais < minutosFim) {
      const h = Math.floor(minutosAtuais / 60);
      const m = minutosAtuais % 60;
      const horarioFormatado = `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`;

      if (!this.horariosConfigurados[dia].includes(horarioFormatado)) {
        this.horariosConfigurados[dia].push(horarioFormatado);
      }
      minutosAtuais += 15;
    }
  }

  // --- LÓGICA DE VISUALIZAÇÃO ---

  inicializarDatas() {
    const hoje = new Date();
    hoje.setMinutes(hoje.getMinutes() - hoje.getTimezoneOffset());
    this.dataVisualizacaoAgenda = hoje.toISOString().split('T')[0];
  }

  carregarAgendaDoProfissional() {
    if (!this.usuarioLogado?.id || !this.dataVisualizacaoAgenda) return;

    this.agendamentoService.buscarAgendaDoDia(this.usuarioLogado.id, this.dataVisualizacaoAgenda).subscribe({
      next: (dados) => {
        this.agendaProfissional = dados.sort((a: any, b: any) => a.dataHora.localeCompare(b.dataHora));
        this.cdr.detectChanges();
      },
      error: (e) => console.error(e)
    });
  }

  toggleDiaExpandido(dia: string) {
    this.diaExpandido = (this.diaExpandido === dia) ? null : dia;
  }

  gerarSlotsConfiguracao() {
    const slots = [];
    for (let h = 8; h <= 18; h++) {
      for (let m = 0; m < 60; m += 15) {
        slots.push(`${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`);
      }
    }
    this.slotsHorariosConfig = slots;
  }

  toggleHorarioConfig(dia: string, horario: string) {
    if (!this.horariosConfigurados[dia]) this.horariosConfigurados[dia] = [];

    const index = this.horariosConfigurados[dia].indexOf(horario);
    if (index > -1) {
      this.horariosConfigurados[dia].splice(index, 1);
    } else {
      this.horariosConfigurados[dia].push(horario);
    }
  }

  ehHorarioAtivo(dia: string, horario: string): boolean {
    return this.horariosConfigurados[dia]?.includes(horario) || false;
  }

  carregarAreas() {
    this.agendamentoService.listarAreas().subscribe(d => {
      this.areas = d;
      this.cdr.detectChanges();
    });
  }
}