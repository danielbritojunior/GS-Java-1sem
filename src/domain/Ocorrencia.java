package domain;

import java.util.ArrayList;

public class Ocorrencia {
    private int id;
    private String regiao;
    private String dataLeitura;
    private double nivelRio;
    private double volumeChuva;
    private String risco;
    private String status;
    private boolean foiMitigado = false;

    private ArrayList<Telemetria> historicoLeituras = new ArrayList<>();

    public Ocorrencia() {
        this.status = "INATIVO";
    }

    public Ocorrencia(int id, String regiao, String dataLeitura, double nivelRio, double volumeChuva) {
        this.id = id;
        this.regiao = regiao;
        this.dataLeitura = dataLeitura;
        this.nivelRio = nivelRio;
        this.volumeChuva = volumeChuva;
        this.historicoLeituras.add(new Telemetria(dataLeitura, nivelRio, volumeChuva));
        recalcularRiscoEStatus();
    }

    public void adicionarNovaLeitura(String data, double rio, double chuva) {
        this.dataLeitura = data;
        this.nivelRio = rio;
        this.volumeChuva = chuva;
        this.historicoLeituras.add(new Telemetria(data, rio, chuva));
        recalcularRiscoEStatus();
    }

    public void recalcularRiscoEStatus() {
        if (this.nivelRio >= 5.0 && this.volumeChuva >= 100.0) {
            this.risco = "ALTO";
            this.status = "ATIVO";
        } else if (this.nivelRio >= 5.0 || this.volumeChuva >= 100.0) {
            this.risco = "MEDIO";
            this.status = "ATIVO";
        } else {
            this.risco = "BAIXO";
            this.status = "INATIVO";
        }
    }

    public void exibirDados() {
        System.out.println("ID do Monitoramento: " + id);
        System.out.println("Regiao Geografica: " + regiao);

        if ("ENCERRADA".equals(status)) {
            System.out.println("Situacao do Alerta: RESOLVIDO / CONTIDO");
            System.out.println("[Historico de Telemetria]: Dados salvos do periodo critico. A situacao atual na area esta controlada.");
        } else {
            System.out.println("Situacao do Alerta: " + status);
        }

        System.out.println("Ultima Telemetria: " + dataLeitura + " | Rio: " + nivelRio + "m | Chuva: " + volumeChuva + "mm");
        System.out.println("Risco Atual de Inundacao: " + ("INATIVO".equals(status) ? "NENHUM" : risco));
    }

    public void emitirAlerta() {
        if ("ENCERRADA".equals(this.status)) {
            return;
        } else if ("BAIXO".equals(this.risco)) {
            System.out.println("[Status]: Condicoes meteorologicas estaveis. Area segura.");
        } else {
            System.out.println("[Aviso]: Atencao! Area sob monitoramento preventivo devido ao risco " + this.risco);
        }
    }

    public void gerarRelatorio() {
        System.out.println("ID: " + id + " | Regiao: " + regiao + " | Status: " + status + " | Risco: " + ("INATIVO".equals(status) ? "NENHUM" : risco));
    }

    public void gerarRelatorio(int idBusca) {
        System.out.println("=== DOSSIE TECNICO DE TELEMETRIA ===");
        if (this.id == idBusca) {
            exibirDados();

            System.out.println("\n--- LINHA DO TEMPO DE TRANSMISSOES DO SATELITE ---");
            for (int i = 0; i < historicoLeituras.size(); i++) {
                Telemetria t = historicoLeituras.get(i);
                System.out.println(" Leitura #" + (i + 1) + " [" + t.getData() + "] -> Rio: " + t.getNivelRio() + "m | Chuva: " + t.getVolumeChuva() + "mm");
            }

            System.out.println("\n--- ANALISE DE ENGENHARIA AMBIENTAL ---");
            if (foiMitigado) {
                System.out.println("Status do Solo: Em processo de drenagem e estabilizacao.");
                System.out.println("Resultado: Intervencao concluida. Area liberada para monitoramento de rotina.");
            } else {
                System.out.println("Analise Combinada: Monitoramento orbital ativo. Risco de saturacao monitorado.");
                analisarImpacto(this.nivelRio);
                analisarImpacto(this.nivelRio, this.volumeChuva);
            }
        }
        System.out.println("====================================");
    }

    public void analisarImpacto(double rio) {
        if (rio >= 5.0) {
            System.out.println("Analise Hidrica: Rio acima da cota de alerta. Risco iminente de transbordo de calha.");
        } else {
            System.out.println("Analise Hidrica: Fluxo do rio operando em niveis nominais e seguros.");
        }
    }

    public void analisarImpacto(double rio, double chuva) {
        if (rio >= 5.0 && chuva >= 100.0) {
            System.out.println("Analise Pluviometrica: Solo completamente saturado. Capacidade de escoamento esgotada.");
        } else if (chuva >= 100.0) {
            System.out.println("Analise Pluviometrica: Precipitacao severa detida por absorcao temporaria do solo.");
        } else {
            System.out.println("Analise Pluviometrica: Indice de chuvas dentro do esperado para o periodo.");
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getRegiao() { return regiao; }
    public String getDataLeitura() { return dataLeitura; }
    public double getNivelRio() { return nivelRio; }
    public double getVolumeChuva() { return volumeChuva; }
    public String getRisco() { return risco; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isFoiMitigado() { return foiMitigado; }
    public void setFoiMitigado(boolean foiMitigado) { this.foiMitigado = foiMitigado; }
    public ArrayList<Telemetria> getHistoricoLeituras() { return historicoLeituras; }
}