package Camilaronzzani.com.github;

import entity.*;
import service.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

public class Visualizacao {
    Scanner scanner = new Scanner(System.in);
    DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    AlunoService alunoService = new AlunoService();
    PersonalService personalService = new PersonalService();
    UserService userService = new UserService();
    AgendamentoService agendamentoService = new AgendamentoService();
    DisponibilidadeService disponibilidadeService = new DisponibilidadeService();
    CheckinService checkinService = new CheckinService();

    public void menuPrincipal() {
        while (true) {
            System.out.println("\n=== SISTEMA ACADEMIA ===");

            if (!personalService.existeAlgumPersonal()) {
                System.out.println("Nenhum personal cadastrado ainda.");
                System.out.println("1 - Cadastrar personal");
                System.out.println("2 - Sair");
                System.out.print("Escolha: ");

                int opcao = lerInt();
                if (opcao == 1) {
                    cadastrarPersonal();
                } else if (opcao == 2) {
                    System.out.println("Ate logo!");
                    break;
                } else {
                    System.out.println("Opcao invalida.");
                }
            } else {
                System.out.println("1 - Sou Aluno");
                System.out.println("2 - Sou Personal");
                System.out.println("3 - Cadastrar Personal");
                System.out.println("4 - Sair");
                System.out.print("Escolha: ");

                int opcao = lerInt();
                if (opcao == 1) {
                    AlunoEntity aluno = loginAluno();
                    if (aluno != null) menuAluno(aluno);
                } else if (opcao == 2) {
                    PersonalEntity personal = loginPersonal();
                    if (personal != null) menuPersonal(personal);
                } else if (opcao == 3) {
                    System.out.println("\nConfirme sua identidade primeiro:");
                    PersonalEntity personal = loginPersonal();
                    if (personal != null) cadastrarPersonal();
                } else if (opcao == 4) {
                    System.out.println("Ate logo!");
                    break;
                } else {
                    System.out.println("Opcao invalida.");
                }
            }
        }
        scanner.close();
    }

    private AlunoEntity loginAluno() {
        System.out.print("\nCPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        Optional<AlunoEntity> resultado = alunoService.buscarPorCpf(cpf);
        if (resultado.isEmpty()) { System.out.println("Credenciais invalidas."); return null; }
        AlunoEntity aluno = resultado.get();
        Optional<UserEntity> optUser = userService.buscarPorIdAluno(aluno.getId());
        if (optUser.isEmpty() || !userService.autenticar(optUser.get(), senha)) {
            System.out.println("Credenciais invalidas.");
            return null;
        }
        System.out.println("Bem-vindo, " + aluno.getNome() + "!");
        return aluno;
    }

    private PersonalEntity loginPersonal() {
        System.out.print("\nCPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        Optional<PersonalEntity> resultado = personalService.buscarPorCpf(cpf);
        if (resultado.isEmpty()) { System.out.println("Credenciais invalidas."); return null; }
        PersonalEntity personal = resultado.get();
        Optional<UserEntity> optUser = userService.buscarPorIdPersonal(personal.getId());
        if (optUser.isEmpty() || !userService.autenticar(optUser.get(), senha)) {
            System.out.println("Credenciais invalidas.");
            return null;
        }
        System.out.println("Bem-vindo, " + personal.getNome() + "!");
        return personal;
    }

    private void menuAluno(AlunoEntity aluno) {
        while (true) {
            System.out.println("\n--- MENU ALUNO ---");
            System.out.println("1 - Ver horarios disponiveis");
            System.out.println("2 - Fazer agendamento");
            System.out.println("3 - Meus agendamentos");
            System.out.println("4 - Cancelar agendamento");
            System.out.println("5 - Meu treino");
            System.out.println("6 - Fazer checkin");
            System.out.println("7 - Agendamentos pendentes");
            System.out.println("8 - Sair");
            System.out.print("Escolha: ");

            switch (lerInt()) {
                case 1: verHorariosDisponiveis(); break;
                case 2: fazerAgendamento(aluno); break;
                case 3: meusAgendamentos(aluno); break;
                case 4: cancelarAgendamento(aluno.getId()); break;
                case 5: meuTreino(aluno); break;
                case 6: checkinAluno(aluno); break;
                case 7: agendamentosPendentes(aluno); break;
                case 8: System.out.println("Ate logo!"); return;
                default: System.out.println("Opcao invalida."); break;
            }
        }
    }

    private void menuPersonal(PersonalEntity personal) {
        while (true) {
            System.out.println("\n--- MENU PERSONAL ---");
            System.out.println("1 - Cadastrar disponibilidade");
            System.out.println("2 - Ver minhas disponibilidades");
            System.out.println("3 - Cadastrar aluno");
            System.out.println("4 - Listar alunos");
            System.out.println("5 - Editar aluno");
            System.out.println("6 - Cancelar aluno");
            System.out.println("7 - Deletar aluno");
            System.out.println("8 - Registrar evolucao de aluno");
            System.out.println("9 - Criar treino para aluno");
            System.out.println("10 - Ver treinos");
            System.out.println("11 - Fazer checkin de aluno");
            System.out.println("12 - Ver agendamentos dos meus alunos");
            System.out.println("13 - Cancelar agendamento de aluno");
            System.out.println("14 - Sair");
            System.out.print("Escolha: ");

            switch (lerInt()) {
                case 1: cadastrarDisponibilidade(personal); break;
                case 2: verMinhasDisponibilidades(personal); break;
                case 3: cadastrarAluno(); break;
                case 4: listarAlunos(); break;
                case 5: editarAluno(); break;
                case 6: cancelarAluno(); break;
                case 7: deletarAluno(); break;
                case 8: registrarEvolucao(personal); break;
                case 9: criarTreino(personal); break;
                case 10: verTreinos(); break;
                case 11: checkinDeAluno(); break;
                case 12: verAgendamentosDoPersonal(personal); break;
                case 13: cancelarAgendamento(null); break;
                case 14: System.out.println("Ate logo!"); return;
                default: System.out.println("Opcao invalida."); break;
            }
        }
    }

    private void verHorariosDisponiveis() {
        List<DisponibilidadeEntity> lista = disponibilidadeService.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum horario disponivel agora.");
            return;
        }
        System.out.println("\n--- Horarios Disponiveis ---");
        for (DisponibilidadeEntity d : lista) {
            System.out.println("ID: " + d.getId()
                    + " | Personal: " + d.getPersonal().getNome()
                    + " | Data: " + d.getData().format(formatadorData)
                    + " | Horario: " + d.getHoraInicio() + " - " + d.getHoraFim());
        }
    }

    private void fazerAgendamento(AlunoEntity aluno) {
        System.out.println("\n--- Novo Agendamento ---");
        verHorariosDisponiveis();

        System.out.print("ID da disponibilidade escolhida: ");
        Long idDisp = lerLong();
        Optional<DisponibilidadeEntity> optDisp = disponibilidadeService.buscarPorId(idDisp);
        if (optDisp.isEmpty()) {
            System.out.println("Horario nao encontrado.");
            return;
        }

        DisponibilidadeEntity disp = optDisp.get();
        System.out.println("Agendando: " + disp.getData().format(formatadorData)
                + " das " + disp.getHoraInicio() + " as " + disp.getHoraFim()
                + " com " + disp.getPersonal().getNome());

        AgendamentoEntity agendamento = new AgendamentoEntity(aluno, disp, null);

        try {
            agendamentoService.agendar(agendamento);
            System.out.println("Agendado!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Nao foi possivel agendar: " + e.getMessage());
        }
    }

    private void meusAgendamentos(AlunoEntity aluno) {
        List<AgendamentoEntity> lista = agendamentoService.listarPorAluno(aluno.getId());
        if (lista.isEmpty()) {
            System.out.println("Voce nao tem nenhum agendamento ainda.");
            return;
        }
        System.out.println("\n--- Meus Agendamentos ---");
        for (AgendamentoEntity ag : lista) {
            System.out.println("ID: " + ag.getId()
                    + " | Data/Hora: " + ag.getDataHora().format(formatadorDataHora)
                    + " | Status: " + ag.getStatus().getDescricao());
        }
    }

    private void agendamentosPendentes(AlunoEntity aluno) {
        List<AgendamentoEntity> lista = agendamentoService.listarSemCheckin(aluno.getId());
        if (lista.isEmpty()) {
            System.out.println("Nenhum agendamento pendente.");
            return;
        }
        System.out.println("\n--- Agendamentos sem checkin ---");
        for (AgendamentoEntity ag : lista) {
            System.out.println("ID: " + ag.getId()
                    + " | Data/Hora: " + ag.getDataHora().format(formatadorDataHora)
                    + " | Status: " + ag.getStatus().getDescricao());
        }
    }

    private void meuTreino(AlunoEntity aluno) {
        System.out.println("Em breve.");
    }

    private void checkinAluno(AlunoEntity aluno) {
        System.out.println("\n--- Checkin ---");
        exibirMenuCheckin();
        int opcao = lerInt();
        if (opcao == 1) fazerCheckinSistema(aluno);
        else if (opcao == 2) System.out.println("Reconhecimento facial ainda nao ta disponivel.");
        else System.out.println("Opcao invalida.");
    }

    private void cadastrarDisponibilidade(PersonalEntity personal) {
        System.out.println("\n--- Nova Disponibilidade ---");
        LocalDate data = lerDataValidada("Data da disponibilidade (dd/MM/aaaa): ", d -> {
            if (d.isBefore(LocalDate.now())) throw new IllegalArgumentException("A data nao pode ser no passado.");
        });

        java.time.LocalTime inicio;
        java.time.LocalTime fim;
        while (true) {
            inicio = lerHorario("Hora de inicio (HH:mm): ");
            fim = lerHorario("Hora de fim (HH:mm): ");
            if (!inicio.isBefore(fim)) {
                System.out.println("O horario de inicio tem que ser antes do fim.");
            } else {
                break;
            }
        }

        try {
            DisponibilidadeEntity disp = new DisponibilidadeEntity(personal, data, inicio, fim);
            disponibilidadeService.cadastrar(disp);
            System.out.println("Horario salvo!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void verMinhasDisponibilidades(PersonalEntity personal) {
        List<DisponibilidadeEntity> lista = disponibilidadeService.listarPorPersonal(personal.getId());
        if (lista.isEmpty()) {
            System.out.println("Voce nao tem horarios cadastrados ainda.");
            return;
        }
        System.out.println("\n--- Minhas Disponibilidades ---");
        for (DisponibilidadeEntity d : lista) {
            System.out.println("ID: " + d.getId()
                    + " | Data: " + d.getData().format(formatadorData)
                    + " | Horario: " + d.getHoraInicio() + " - " + d.getHoraFim());
        }
    }

    private void cadastrarPersonal() {
        System.out.println("\n--- Cadastrar Personal ---");

        String nome = lerCampoValidado("Nome: ", Validador::validarNome);
        String cpf = lerCampoValidado("CPF (somente numeros, 11 digitos): ", v -> {
            Validador.validarCpf(v);
            if (personalService.buscarPorCpf(v).isPresent()) {
                throw new IllegalArgumentException("CPF " + v + " ja esta cadastrado.");
            }
        });
        String cref = lerCampoValidado("CREF (ex: 123456-G/SP): ", Validador::validarCref);
        String especialidade = lerCampoOpcionalValidado("Especialidade (Enter para pular): ", Validador::validarNome);
        String telefone = lerCampoOpcionalValidado("Telefone (Enter para pular): ", Validador::validarTelefone);
        String email = lerCampoOpcionalValidado("Email (Enter para pular): ", Validador::validarEmail);
        String senha = lerCampoValidado("Senha (minimo 6 caracteres): ", Validador::validarSenha);

        PersonalEntity personal = new PersonalEntity(nome, cpf, cref);
        if (especialidade != null) personal.setEspecialidade(especialidade);
        if (telefone != null) personal.setTelefone(telefone);
        if (email != null) personal.setEmail(email);

        try {
            personalService.cadastrar(personal, senha);
            System.out.println("Personal cadastrado! Agora e so fazer login.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void cadastrarAluno() {
        System.out.println("\n--- Cadastrar Aluno ---");

        String nome = lerCampoValidado("Nome: ", Validador::validarNome);
        String cpf = lerCampoValidado("CPF (somente numeros, 11 digitos): ", v -> {
            Validador.validarCpf(v);
            if (alunoService.buscarPorCpf(v).isPresent()) {
                throw new IllegalArgumentException("CPF " + v + " ja esta cadastrado.");
            }
        });
        LocalDate dataNascimento = lerDataValidada("Data de nascimento (dd/MM/aaaa): ", Validador::validarDataNascimento);
        LocalDate dataMatricula = lerDataValidada("Data de matricula (dd/MM/aaaa): ", Validador::validarDataMatricula);
        String telefone = lerCampoOpcionalValidado("Telefone (Enter para pular): ", Validador::validarTelefone);
        String email = lerCampoOpcionalValidado("Email (Enter para pular): ", Validador::validarEmail);
        String senha = lerCampoValidado("Senha (minimo 6 caracteres): ", Validador::validarSenha);

        AlunoEntity aluno = new AlunoEntity(nome, cpf, dataNascimento, dataMatricula);
        if (telefone != null) aluno.setTelefone(telefone);
        if (email != null) aluno.setEmail(email);

        try {
            alunoService.cadastrar(aluno, senha);
            System.out.println("Aluno cadastrado!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarAlunos() {
        List<AlunoEntity> lista = alunoService.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado ainda.");
            return;
        }
        System.out.println("\n--- Lista de Alunos ---");
        for (AlunoEntity a : lista) {
            System.out.println("ID: " + a.getId()
                    + " | Nome: " + a.getNome()
                    + " | CPF: " + a.getCpf()
                    + " | Status: " + a.getStatus());
        }
    }

    private void editarAluno() {
        listarAlunos();
        System.out.print("\nID do aluno a editar: ");
        Long id = lerLong();
        Optional<AlunoEntity> opt = alunoService.buscarPorId(id);
        if (opt.isEmpty()) {
            System.out.println("Aluno nao encontrado.");
            return;
        }
        AlunoEntity aluno = opt.get();
        System.out.println("\n--- Editar Aluno: " + aluno.getNome() + " ---");
        System.out.println("(deixe em branco pra nao mudar)");

        System.out.print("Nome [" + aluno.getNome() + "]: ");
        String nome = scanner.nextLine();
        if (!nome.isBlank()) aluno.setNome(nome);

        System.out.print("Telefone [" + (aluno.getTelefone() != null ? aluno.getTelefone() : "") + "]: ");
        String telefone = scanner.nextLine();
        if (!telefone.isBlank()) aluno.setTelefone(telefone);

        System.out.print("Email [" + (aluno.getEmail() != null ? aluno.getEmail() : "") + "]: ");
        String email = scanner.nextLine();
        if (!email.isBlank()) aluno.setEmail(email);

        try {
            alunoService.atualizar(aluno);
            System.out.println("Dados atualizados!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void cancelarAluno() {
        listarAlunos();
        System.out.print("\nID do aluno a cancelar: ");
        Long id = lerLong();
        Optional<AlunoEntity> opt = alunoService.buscarPorId(id);
        if (opt.isEmpty()) {
            System.out.println("Aluno nao encontrado.");
            return;
        }
        AlunoEntity aluno = opt.get();
        System.out.print("Tem certeza que quer cancelar " + aluno.getNome() + "? (S/N): ");
        String confirmacao = scanner.nextLine();
        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Ok, nenhuma alteracao feita.");
            return;
        }
        try {
            alunoService.cancelar(id);
            System.out.println(aluno.getNome() + " marcado como inativo.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void deletarAluno() {
        listarAlunos();
        System.out.print("\nID do aluno a deletar: ");
        Long id = lerLong();
        Optional<AlunoEntity> opt = alunoService.buscarPorId(id);
        if (opt.isEmpty()) {
            System.out.println("Aluno nao encontrado.");
            return;
        }
        AlunoEntity aluno = opt.get();
        System.out.print("Quer remover " + aluno.getNome() + "? O historico fica guardado. (S/N): ");
        String confirmacao = scanner.nextLine();
        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Ok, nenhuma alteracao feita.");
            return;
        }
        try {
            alunoService.deletar(id);
            System.out.println(aluno.getNome() + " removido. O historico ainda ta salvo.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void cancelarAgendamento(Long idAluno) {
        List<AgendamentoEntity> lista = idAluno != null
                ? agendamentoService.listarPorAluno(idAluno)
                : agendamentoService.listarTodos();

        if (lista.isEmpty()) {
            System.out.println("Nenhum agendamento encontrado.");
            return;
        }
        System.out.println("\n--- Agendamentos ---");
        for (AgendamentoEntity ag : lista) {
            System.out.println("ID: " + ag.getId()
                    + " | " + ag.getDataHora().format(formatadorDataHora)
                    + " | " + ag.getAluno().getNome()
                    + " | " + ag.getStatus().getDescricao());
        }
        System.out.print("ID do agendamento a cancelar: ");
        Long id = lerLong();

        if (idAluno != null) {
            boolean pertenceAoAluno = lista.stream().anyMatch(ag -> ag.getId().equals(id));
            if (!pertenceAoAluno) {
                System.out.println("Esse agendamento nao e seu.");
                return;
            }
        }

        try {
            agendamentoService.cancelar(id);
            System.out.println("Agendamento cancelado.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void registrarEvolucao(PersonalEntity personal) {
        System.out.println("Em breve.");
    }

    private void criarTreino(PersonalEntity personal) {
        System.out.println("Em breve.");
    }

    private void verTreinos() {
        System.out.println("Em breve.");
    }

    private void checkinDeAluno() {
        System.out.println("\n--- Checkin de Aluno ---");
        exibirMenuCheckin();
        int opcao = lerInt();
        if (opcao == 1) {
            System.out.print("CPF do aluno: ");
            String cpf = scanner.nextLine();
            Optional<AlunoEntity> optAluno = alunoService.buscarPorCpf(cpf);
            if (optAluno.isEmpty()) { System.out.println("Aluno nao encontrado."); return; }
            fazerCheckinSistema(optAluno.get());
        } else if (opcao == 2) {
            System.out.println("Reconhecimento facial ainda nao ta pronto.");
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    private void verAgendamentosDoPersonal(PersonalEntity personal) {
        List<AgendamentoEntity> lista = agendamentoService.listarPorPersonal(personal.getId());
        if (lista.isEmpty()) {
            System.out.println("Nenhum agendamento por aqui.");
            return;
        }
        System.out.println("\n--- Agendamentos dos Alunos ---");
        for (AgendamentoEntity ag : lista) {
            System.out.println("ID: " + ag.getId()
                    + " | Aluno: " + ag.getAluno().getNome()
                    + " | Data/Hora: " + ag.getDataHora().format(formatadorDataHora)
                    + " | Status: " + ag.getStatus().getDescricao());
        }
    }

    private void exibirMenuCheckin() {
        System.out.println("1 - Checkin pelo sistema");
        System.out.println("2 - Checkin por Face ID");
        System.out.print("Escolha: ");
    }

    private void fazerCheckinSistema(AlunoEntity aluno) {
        List<AgendamentoEntity> deHoje = agendamentoService.listarPorAlunoHoje(aluno.getId(), LocalDate.now());
        if (deHoje.isEmpty()) {
            System.out.println("Nenhum agendamento hoje.");
            return;
        }
        System.out.println("Agendamentos de hoje para " + aluno.getNome() + ":");
        for (AgendamentoEntity ag : deHoje) {
            System.out.println("ID: " + ag.getId() + " | Horario: " + ag.getDataHora().format(formatadorDataHora));
        }
        System.out.print("ID do agendamento: ");
        Long idAg = lerLong();
        Optional<AgendamentoEntity> optAg = agendamentoService.buscarPorId(idAg);
        if (optAg.isEmpty()) { System.out.println("Agendamento nao encontrado."); return; }
        CheckinEntity checkin = new CheckinEntity(optAg.get(), aluno);
        checkin.setMetodo(MetodoCheckin.SISTEMA);
        try {
            checkinService.registrar(checkin);
            System.out.println("Checkin feito! Bom treino, " + aluno.getNome() + ".");
        } catch (Exception e) {
            System.out.println("Nao foi possivel fazer o checkin: " + e.getMessage());
        }
    }

    private String lerCampoValidado(String mensagem, Consumer<String> validador) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine();
            try {
                validador.accept(valor);
                return valor;
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private String lerCampoOpcionalValidado(String mensagem, Consumer<String> validador) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine();
            if (valor.isBlank()) return null;
            try {
                validador.accept(valor);
                return valor;
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private int lerInt() {
        try {
            int valor = scanner.nextInt();
            scanner.nextLine();
            return valor;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }

    private Long lerLong() {
        try {
            Long valor = scanner.nextLong();
            scanner.nextLine();
            return valor;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1L;
        }
    }

    private java.time.LocalTime lerHorario(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();
            try {
                return java.time.LocalTime.parse(valor);
            } catch (Exception e) {
                System.out.println("Horario invalido, use HH:mm (ex: 08:30).");
            }
        }
    }

    private LocalDate lerData(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return LocalDate.parse(scanner.nextLine(), formatadorData);
            } catch (Exception e) {
                System.out.println("Formato invalido, use dd/MM/aaaa.");
            }
        }
    }

    private LocalDate lerDataValidada(String mensagem, Consumer<LocalDate> validador) {
        while (true) {
            try {
                System.out.print(mensagem);
                LocalDate data = LocalDate.parse(scanner.nextLine(), formatadorData);
                try {
                    validador.accept(data);
                    return data;
                } catch (IllegalArgumentException e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            } catch (Exception e) {
                System.out.println("Formato invalido, use dd/MM/aaaa.");
            }
        }
    }

}
