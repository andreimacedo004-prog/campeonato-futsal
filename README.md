# Campeonato Municipal de Salão — Site de resultados

Site simples para hospedar os jogos, resultados, classificação (saldo de
gols e afins) e artilheiros das duas divisões do campeonato:

- **1ª Divisão**: times separados em 2 chaves (grupos) que se enfrentam entre si.
- **2ª Divisão**: times separados em várias chaves, cada uma jogando dentro de si mesma.

O visitante só entra no site, escolhe a divisão, entra na chave e vê jogos,
resultados, classificação e artilheiros — sem precisar de login.
Você (organizador) usa um **painel admin com login** para lançar os resultados.

## Tecnologia

- Java 17 + Spring Boot (Web, Thymeleaf, Spring Data JPA, Spring Security)
- Banco H2 (arquivo local, criado automaticamente em `./data/campeonato`, não precisa instalar nada)

## Como rodar no IntelliJ

1. Abra o IntelliJ → **File → Open...** → selecione a pasta `campeonato-futsal` (a que tem o `pom.xml`).
2. Aguarde o IntelliJ baixar as dependências do Maven (ícone de carregamento no canto).
   Isso precisa de internet na primeira vez.
3. Abra `src/main/java/com/campeonato/futsal/FutsalApplication.java` e clique no ▶️ verde
   ao lado do `main` (ou botão "Run" no topo).
4. Acesse **http://localhost:8080** no navegador.

Na primeira execução o sistema já cria automaticamente uma estrutura de
exemplo (1ª Divisão com Chave A e B, 2ª Divisão com 4 chaves, times chamados
"Time 1", "Time 2"...) pra você já ver tudo funcionando. Depois é só
renomear os times pelos nomes reais no painel admin.

## Painel Admin

- Acesse **http://localhost:8080/admin**
- Usuário: `admin`
- Senha: `admin123`

> ⚠️ Troque usuário/senha antes de usar "pra valer": edite
> `src/main/resources/application.properties`, propriedades `admin.username`
> e `admin.password`.

### O que dá pra fazer no painel

- Criar novas chaves dentro de cada divisão
- Adicionar/editar/remover times de uma chave
- **Gerar a tabela de jogos automaticamente** (botão "Gerar tabela de jogos"
  dentro de "Gerenciar" chave) — cria os confrontos de turno único (todo
  time enfrenta todos os outros da chave uma vez), já dividido em rodadas
- Lançar o placar de cada jogo e marcar como "realizado" (isso é o que faz
  ele entrar na classificação e no saldo de gols)
- Lançar os gols/artilheiros de cada jogo (jogador + time + quantidade de gols)

### O que o visitante vê (sem login)

- Página inicial com as divisões
- Página da divisão com as chaves
- Página da chave com: tabela de classificação (P, J, V, E, D, GP, GC, SG),
  lista de jogos por rodada com o placar, e os artilheiros daquela chave
- Página de artilheiros de toda a divisão (soma de gols em todas as chaves)

## Sobre a 1ª Divisão (8 melhores avançam)

O site atual cuida da fase de grupos (chave A e chave B) com classificação e
resultados. A definição de quais 8 times avançam para a fase seguinte fica
a seu critério (olhando a classificação de cada chave); se depois você
quiser, dá pra evoluir o site para ter uma fase eliminatória (mata-mata)
automatizada também — é só pedir.

## Dados ficam salvos onde?

Numa pasta `data/` dentro do projeto (arquivo do banco H2). Se quiser
"zerar tudo" e recomeçar do zero, feche a aplicação e apague a pasta `data/`.

## Rodando só localmente por enquanto

Esse projeto está configurado pra rodar na sua máquina (`localhost:8080`).
Quando quiser que outras pessoas acessem pela internet, dá pra publicar em
um servidor/nuvem (Railway, Render, VPS, etc.) — é só avisar quando chegar
nessa etapa que ajudo a configurar o deploy.
