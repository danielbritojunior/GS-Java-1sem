package presentation;

import application.OcorrenciaService;
import domain.Ocorrencia;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main {

    private static final Pattern DATA_PATTERN = Pattern.compile(
            "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}$"
    );

    public static void main(String[] args) {
        OcorrenciaService service = new OcorrenciaService();
        Scanner sc = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int opcao = 0;

        do {
            System.out.println("\n====================================================");
            System.out.println(" SISTEMA SPACE CONNECT: MONITORAMENTO DE ENCHENTES");
            System.out.println("====================================================");
            System.out.println(" 1 - Iniciar Monitoramento de uma Area");
            System.out.println(" 2 - Listar todas as Areas Monitoradas");
            System.out.println(" 3 - Buscar dados atuais de Monitoramento");
            System.out.println(" 4 - Encerrar Alerta Ativo");
            System.out.println(" 5 - Enviar nova leitura de Satelite");
            System.out.println(" 6 - Gerar relatorio do sistema");
            System.out.println(" 7 - Sair");
            System.out.println("====================================================");
            System.out.print("Escolha uma opcao: ");

            try {
                opcao = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\n[Erro de Entrada]: Por favor, digite um numero inteiro de 1 a 7.");
                continue;
            }

            switch (opcao) {
                case 1 -> {
                    System.out.println("\n--- [1] CONFIGURAR NOVA AREA ---");

                    String regiao = "";
                    while (true) {
                        System.out.print("Informe o nome da Area: ");
                        regiao = sc.nextLine().trim();
                        if (!regiao.isEmpty()) {
                            break;
                        }
                        System.out.println("[Erro]: O nome da Area nao pode ficar em branco. Tente novamente.");
                    }

                    String data = "";
                    while (true) {
                        System.out.print("Informe a data da telemetria (DD/MM/AAAA): ");
                        data = sc.nextLine().trim();

                        if (!DATA_PATTERN.matcher(data).matches()) {
                            System.out.println("[Erro de Validacao]: Formato invalido! Use o padrao DD/MM/AAAA com 4 digitos no ano.");
                            continue;
                        }

                        try {
                            LocalDate.parse(data, formatter);
                            break;
                        } catch (DateTimeParseException e) {
                            System.out.println("[Erro de Validacao]: Esta data nao existe no calendario. Digite uma data valida.");
                        }
                    }

                    double rio = 0;
                    while (true) {
                        try {
                            System.out.print("Nivel atual do rio (m): ");
                            rio = Double.parseDouble(sc.nextLine().replace(",", "."));
                            if (rio >= 0) {
                                break;
                            }
                            System.out.println("[Erro de Validacao]: O nivel do rio nao pode ser negativo. Digite novamente.");
                        } catch (NumberFormatException e) {
                            System.out.println("[Erro de Entrada]: O nivel do rio deve ser um valor numerico valido.");
                        }
                    }

                    double chuva = 0;
                    while (true) {
                        try {
                            System.out.print("Volume atual de chuva (mm): ");
                            chuva = Double.parseDouble(sc.nextLine().replace(",", "."));
                            if (chuva >= 0) {
                                break;
                            }
                            System.out.println("[Erro de Validacao]: O volume de chuva nao pode ser negativo. Digite novamente.");
                        } catch (NumberFormatException e) {
                            System.out.println("[Erro de Entrada]: O volume de chuva deve ser um valor numerico valido.");
                        }
                    }

                    service.cadastrarRegiaoMonitorada(regiao, data, rio, chuva);
                    System.out.println("\n[Sucesso]: Regiao integrada a rede de satelites com sucesso!");
                }

                case 2 -> {
                    System.out.println("\n====================================================");
                    System.out.println("STATUS ATUAL DE TODAS AS AREAS MONITORADAS");
                    System.out.println("====================================================");
                    var lista = service.obterTodasRegioes();
                    if (lista.isEmpty()) {
                        System.out.println("Nenhuma regiao cadastrada para rastreamento.");
                    } else {
                        for (Ocorrencia o : lista) {
                            o.exibirDados();
                            o.emitirAlerta();
                            System.out.println("----------------------------------------------------");
                        }
                    }
                }

                case 3 -> {
                    System.out.println("\n--- [3] CONSULTAR DADOS POR ID ---");
                    int idBusca = 0;
                    try {
                        System.out.print("Digite o ID do Monitoramento: ");
                        idBusca = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("[Erro de Entrada]: ID deve ser um numero inteiro.");
                        continue;
                    }

                    Ocorrencia o = service.buscarRegiaoPorId(idBusca);
                    if (o == null) {
                        System.out.println("\n[Erro]: O ID " + idBusca + " nao possui registro no sistema.");
                    } else {
                        System.out.println("\n----------------------------------------------------");
                        o.exibirDados();
                        o.emitirAlerta();
                        System.out.println("----------------------------------------------------");
                    }
                }

                case 4 -> {
                    System.out.println("\n--- [4] MITIGAR CRISE LOCAL ---");

                    boolean temAtivos = service.listarApenasAlertasAtivos();
                    if (!temAtivos) {
                        continue;
                    }

                    int idFecha = 0;
                    try {
                        System.out.print("Digite o ID do Monitoramento para finalizar o alerta: ");
                        idFecha = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("[Erro de Entrada]: ID deve ser um numero inteiro.");
                        continue;
                    }

                    boolean sucesso = service.mitigarAlerta(idFecha);
                    if (!sucesso) {
                        System.out.println("\n[Erro]: Nao foi possivel encerrar. Verifique se o ID digitado esta correto e realmente ativo.");
                    } else {
                        System.out.println("\n[Sucesso]: Protocolo encerrado pelas autoridades locais.");
                    }
                }

                case 5 -> {
                    System.out.println("\n--- [5] ATUALIZAR DADOS DO SATELITE ---");
                    int idAtualiza = 0;
                    try {
                        System.out.print("Digite o ID do Monitoramento que recebera a nova leitura: ");
                        idAtualiza = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("[Erro de Entrada]: ID deve ser um numero inteiro.");
                        continue;
                    }

                    Ocorrencia existente = service.buscarRegiaoPorId(idAtualiza);
                    if (existente == null) {
                        System.out.println("\n[Erro]: ID de monitoramento nao localizado na base orbital.");
                        continue;
                    }

                    String novaData = "";
                    while (true) {
                        System.out.print("Nova data da leitura orbital (DD/MM/AAAA): ");
                        novaData = sc.nextLine().trim();

                        if (!DATA_PATTERN.matcher(novaData).matches()) {
                            System.out.println("[Erro de Validacao]: Formato de data invalido! Use o padrao DD/MM/AAAA.");
                            continue;
                        }

                        try {
                            LocalDate.parse(novaData, formatter);
                            break;
                        } catch (DateTimeParseException e) {
                            System.out.println("[Erro de Validacao]: Esta data nao existe no calendario. Digite uma data valida.");
                        }
                    }

                    double novoRio = 0;
                    while (true) {
                        try {
                            System.out.print("Novo Nivel aferido para o rio (m): ");
                            novoRio = Double.parseDouble(sc.nextLine().replace(",", "."));
                            if (novoRio >= 0) {
                                break;
                            }
                            System.out.println("[Erro de Validacao]: Medicoes nao podem ser negativas. Tente de novo.");
                        } catch (NumberFormatException e) {
                            System.out.println("[Erro de Entrada]: Insira um valor numerico valido.");
                        }
                    }

                    double novoChuva = 0;
                    while (true) {
                        try {
                            System.out.print("Novo Volume aferido para chuva (mm): ");
                            novoChuva = Double.parseDouble(sc.nextLine().replace(",", "."));
                            if (novoChuva >= 0) {
                                break;
                            }
                            System.out.println("[Erro de Validacao]: Medicoes nao podem ser negativas. Tente de novo.");
                        } catch (NumberFormatException e) {
                            System.out.println("[Erro de Entrada]: Insira um valor numerico valido.");
                        }
                    }

                    service.atualizarLeituraSatelite(idAtualiza, novoRio, novoChuva, novaData);
                    System.out.println("\n[Satelite]: Telemetria processada e atualizada com sucesso.");
                }

                case 6 -> {
                    System.out.println("\n--- [6] CONFIGURACAO DE RELATORIOS ---");
                    System.out.println("1 - Relatorio de Negocios Consolidado");
                    System.out.println("2 - Relatorio Tecnico por ID Especifico");
                    System.out.print("Escolha o formato: ");
                    try {
                        int formato = Integer.parseInt(sc.nextLine());
                        if (formato == 2) {
                            System.out.print("Digite o ID que deseja analisar: ");
                            int idTecnico = Integer.parseInt(sc.nextLine());
                            service.emitirRelatorioEspecifico(idTecnico);
                        } else {
                            service.emitirRelatorioGeral();
                        }
                    } catch (Exception e) {
                        System.out.println("[Erro]: Opcao ou ID invalido.");
                    }
                }
                case 7 -> System.out.println("\nConexao espacial desfeita. Fechando o monitoramento.");
                default -> System.out.println("\n[Aviso]: Opcao invalida. Digite um numero de 1 a 7.");
            }
        } while (opcao != 7);

        sc.close();
    }
}