package domain;

public class OcorrenciaCritica extends Ocorrencia {

    public OcorrenciaCritica(int id, String regiao, String dataLeitura, double nivelRio, double volumeChuva) {
        super(id, regiao, dataLeitura, nivelRio, volumeChuva);
    }

    @Override
    public void exibirDados() {
        if ("ENCERRADA".equals(getStatus())) {
            System.out.println("== ARQUIVO HISTORICO: CRISE LOCAL CONCLUIDA ==");
        } else {
            System.out.println("!! TRANSMISSAO DE CRITICIDADE MAXIMA DETECTADA POR SATELITE !!");
        }
        super.exibirDados();
    }

    @Override
    public void emitirAlerta() {
        if ("ENCERRADA".equals(getStatus())) {
            return;
        }
        System.out.println("[ALERTA CRITICO]: Risco extremo de inundacao iminente detectado via satelite na " + getRegiao().toUpperCase() + "!");
    }
}