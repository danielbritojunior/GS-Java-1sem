# 🌎 Space Connect - Sistema de Monitoramento de Enchentes

## 📌 Sobre o Projeto

O **Space Connect** é um sistema desenvolvido em Java para monitoramento de áreas com risco de enchentes por meio de leituras simuladas de satélites.

O sistema permite registrar regiões monitoradas, receber novas telemetrias, identificar níveis de risco, emitir alertas automáticos e gerar relatórios gerenciais e técnicos para apoio à tomada de decisão.

O projeto foi desenvolvido aplicando conceitos fundamentais de Programação Orientada a Objetos (POO), como:

* Encapsulamento
* Herança
* Polimorfismo
* Sobrecarga de métodos
* Organização em camadas

---

## 🏗️ Estrutura do Projeto

### Camada Domain

Responsável pelas entidades de negócio.

#### Ocorrencia

Representa uma região monitorada.

Atributos principais:

* ID
* Região
* Data da leitura
* Nível do rio
* Volume de chuva
* Risco
* Status
* Histórico de telemetrias

#### OcorrenciaCritica

Especialização de Ocorrencia para situações de risco alto.

Possui comportamento específico para exibição de dados e emissão de alertas críticos.

#### Telemetria

Representa uma leitura recebida do satélite contendo:

* Data
* Nível do rio
* Volume de chuva

---

### Camada Application

#### OcorrenciaService

Responsável pelas regras de negócio do sistema:

* Cadastro de regiões monitoradas
* Busca por ID
* Atualização de leituras
* Encerramento de alertas
* Emissão de relatórios
* Consulta de alertas ativos

---

### Camada Infrastructure

#### OcorrenciaRepositorio

Responsável pelo armazenamento dos dados em memória.

Funcionalidades:

* Salvar ocorrências
* Buscar ocorrências
* Atualizar leituras
* Converter ocorrências normais em críticas
* Encerrar alertas

---

### Camada Presentation

#### Main

Responsável pela interação com o usuário através do terminal.

---

## 🚨 Regras de Negócio

### Classificação de Risco

| Condição                  | Risco | Status  |
| ------------------------- | ----- | ------- |
| Rio < 5m e Chuva < 100mm  | BAIXO | INATIVO |
| Rio ≥ 5m ou Chuva ≥ 100mm | MÉDIO | ATIVO   |
| Rio ≥ 5m e Chuva ≥ 100mm  | ALTO  | ATIVO   |

---

### Ocorrências Críticas

Sempre que uma ocorrência atingir risco **ALTO**, ela passa a ser tratada como uma **OcorrenciaCritica**, emitindo alertas mais severos.

---

### Encerramento de Alertas

Um alerta ativo pode ser encerrado manualmente.

Quando isso acontece:

* O status passa para ENCERRADA.
* O histórico permanece armazenado.
* O sistema registra que a situação foi mitigada.

---

## 📋 Funcionalidades

### 1. Iniciar Monitoramento

Permite cadastrar uma nova região com:

* Nome da região
* Data da telemetria
* Nível do rio
* Volume de chuva

### 2. Listar Áreas Monitoradas

Exibe:

* Dados da região
* Situação atual
* Nível de risco
* Alertas emitidos

### 3. Buscar Monitoramento por ID

Consulta detalhada de uma região específica.

### 4. Encerrar Alerta

Permite finalizar alertas ativos.

### 5. Atualizar Dados de Satélite

Recebe uma nova telemetria para uma região monitorada.

### 6. Gerar Relatórios

#### Relatório Consolidado

Apresenta:

* Total de monitoramentos
* Quantidade de alertas ativos
* Quantidade de alertas encerrados
* Distribuição dos níveis de risco
* Maior nível de rio registrado
* Maior volume de chuva registrado

#### Relatório Técnico por ID

Apresenta:

* Dados completos da ocorrência
* Histórico de telemetrias
* Análise hídrica
* Análise pluviométrica
* Situação da mitigação

### 7. Encerrar Sistema

Finaliza a execução do programa.

---

## 🧠 Conceitos de POO Aplicados

### Herança

* OcorrenciaCritica herda de Ocorrencia.

### Polimorfismo

* Sobrescrita dos métodos:

    * exibirDados()
    * emitirAlerta()

### Sobrecarga

Métodos:

* analisarImpacto(double rio)
* analisarImpacto(double rio, double chuva)

### Encapsulamento

Todos os atributos são privados e acessados através de getters e setters.

---

## ▶️ Como Executar

1. Clone o projeto.
2. Abra em uma IDE Java (IntelliJ IDEA, Eclipse ou NetBeans).
3. Compile o projeto.
4. Execute a classe:

presentation.Main

5. Utilize o menu interativo para operar o sistema.

---

## 👨‍💻 Desenvolvido por

Daniel Brito dos Santos Junior - RM: 566236

Gustavo Palomares Borsato - RM: 564621

Vitor Rampazzi Franco - RM: 562270