package application;

import domain.Ocorrencia;
import domain.Telemetria;
import infrastructure.OcorrenciaRepositorio;
import java.util.ArrayList;

public class OcorrenciaService {
    private OcorrenciaRepositorio repositorio = new OcorrenciaRepositorio();

    public void cadastrarRegiaoMonitorada(String regiao, String data, double nivelRio, double volumeChuva) {
        Ocorrencia o = new Ocorrencia(0, regiao, data, nivelRio, volumeChuva);
        repositorio.salvar(o);
    }

    public ArrayList<Ocorrencia> obterTodasRegioes() { return repositorio.listarTodos(); }
    public Ocorrencia buscarRegiaoPorId(int id) { return repositorio.buscarPorId(id); }
    public boolean mitigarAlerta(int id) { return repositorio.forcarEncerramentoAlerta(id); }
    public boolean atualizarLeituraSatelite(int id, double nivelRio, double volumeChuva, String data) {
        return repositorio.receberDadosSatelite(id, nivelRio, volumeChuva, data);
    }

    public boolean listarApenasAlertasAtivos() {
        ArrayList<Ocorrencia> lista = repositorio.listarTodos();
        boolean possuiAtivos = false;
        System.out.println("\n--- REGIOES COM ALERTAS ATIVOS NO MOMENTO ---");
        for (Ocorrencia o : lista) {
            if ("ATIVO".equals(o.getStatus())) {
                System.out.println("ID: " + o.getId() + " | Regiao: " + o.getRegiao() + " | Risco: " + o.getRisco());
                possuiAtivos = true;
            }
        }
        if (!possuiAtivos) System.out.println("Nao ha nenhuma regiao com alerta ativo no momento.");
        System.out.println("---------------------------------------------");
        return possuiAtivos;
    }

    public void emitirRelatorioGeral() {
        ArrayList<Ocorrencia> lista = repositorio.listarTodos();
        int total = lista.size();
        int ativas = 0;
        int encerradas = 0;
        int baixos = 0; int medios = 0; int altos = 0;
        double maiorRio = 0; double maiorChuva = 0;

        for (Ocorrencia o : lista) {
            if ("ATIVO".equals(o.getStatus())) ativas++;
            if ("ENCERRADA".equals(o.getStatus())) encerradas++;

            switch (o.getRisco()) {
                case "BAIXO" -> baixos++;
                case "MEDIO" -> medios++;
                case "ALTO" -> altos++;
            }

            for (Telemetria t : o.getHistoricoLeituras()) {
                if (t.getNivelRio() > maiorRio) maiorRio = t.getNivelRio();
                if (t.getVolumeChuva() > maiorChuva) maiorChuva = t.getVolumeChuva();
            }
        }

        System.out.println("\n====================================================");
        System.out.println("RELATORIO CONSOLIDADO DO MONITORAMENTO ORBITAL");
        System.out.println("====================================================");
        System.out.println("Total de monitoramentos registrados: " + total);
        System.out.println("Monitoramentos com alertas ativos no momento: " + ativas);
        System.out.println("Monitoramentos com crises controladas no momento: " + encerradas);
        System.out.println("----------------------------------------------------");
        System.out.println("Quantidade de riscos baixos: " + baixos);
        System.out.println("Quantidade de riscos medios: " + medios);
        System.out.println("Quantidade de riscos altos: " + altos);
        System.out.println("----------------------------------------------------");
        System.out.println("Maior nivel de rio registrado no historico: " + maiorRio + "m");
        System.out.println("Maior volume de chuva registrado no historico: " + maiorChuva + "mm");
        System.out.println("====================================================");

        System.out.println("\nResumo rapido dos registros em cache:");
        for (Ocorrencia o : lista) {
            o.gerarRelatorio();
        }
    }

    public void emitirRelatorioEspecifico(int id) {
        Ocorrencia o = repositorio.buscarPorId(id);
        if (o == null) {
            System.out.println("Erro: Monitoramento ID " + id + " nao encontrado para emissao de dossie.");
        } else {
            o.gerarRelatorio(id);
        }
    }
}