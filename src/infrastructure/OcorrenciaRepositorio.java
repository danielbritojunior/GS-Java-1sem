package infrastructure;

import domain.Ocorrencia;
import domain.OcorrenciaCritica;
import java.util.ArrayList;

public class OcorrenciaRepositorio {
    private ArrayList<Ocorrencia> historico = new ArrayList<>();
    private int proximoId = 1;

    public void salvar(Ocorrencia o) {
        o.setId(proximoId++);
        if ("ALTO".equals(o.getRisco()) && !(o instanceof OcorrenciaCritica)) {
            o = new OcorrenciaCritica(o.getId(), o.getRegiao(), o.getDataLeitura(), o.getNivelRio(), o.getVolumeChuva());
        }
        historico.add(o);
    }

    public ArrayList<Ocorrencia> listarTodos() { return historico; }

    public Ocorrencia buscarPorId(int id) {
        for (Ocorrencia o : historico) {
            if (o.getId() == id) return o;
        }
        return null;
    }

    public boolean receberDadosSatelite(int id, double novoRio, double novoVolume, String novaData) {
        Ocorrencia o = buscarPorId(id);

        if (o != null) {
            o.adicionarNovaLeitura(novaData, novoRio, novoVolume);

            if ("ALTO".equals(o.getRisco()) || "MEDIO".equals(o.getRisco())) {
                o.setFoiMitigado(false);
            }

            int index = historico.indexOf(o);
            if ("ALTO".equals(o.getRisco()) && !(o instanceof OcorrenciaCritica)) {
                OcorrenciaCritica critica = new OcorrenciaCritica(o.getId(), o.getRegiao(), novaData, novoRio, novoVolume);
                critica.setFoiMitigado(o.isFoiMitigado());
                critica.getHistoricoLeituras().clear();
                critica.getHistoricoLeituras().addAll(o.getHistoricoLeituras());
                historico.set(index, critica);
            }
            else if (!"ALTO".equals(o.getRisco()) && (o instanceof OcorrenciaCritica)) {
                Ocorrencia normal = new Ocorrencia(o.getId(), o.getRegiao(), novaData, novoRio, novoVolume);
                normal.setFoiMitigado(o.isFoiMitigado());
                normal.setStatus(o.getStatus());

                normal.getHistoricoLeituras().clear();
                normal.getHistoricoLeituras().addAll(o.getHistoricoLeituras());

                historico.set(index, normal);
            }

            return true;
        }
        return false;
    }

    public boolean forcarEncerramentoAlerta(int id) {
        Ocorrencia o = buscarPorId(id);
        if (o != null && "ATIVO".equals(o.getStatus())) {
            o.setStatus("ENCERRADA");
            o.setFoiMitigado(true);
            return true;
        }
        return false;
    }
}