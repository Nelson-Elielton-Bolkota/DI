#  Desafio Integrador — Sistema de Pedidos

Sistema de gerenciamento de clientes, produtos e pedidos desenvolvido em **Java** com banco de dados **MySQL**. Possui processamento automático de pedidos em background via threads.

---

## Sumário

- [Pré-requisitos](#pré-requisitos)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Configuração do Banco de Dados](#configuração-do-banco-de-dados)
- [Configuração da Conexão](#configuração-da-conexão)
- [Compilação](#compilação)
- [Execução](#execução)
- [Executando pelo VS Code](#executando-pelo-vs-code)
- [Funcionalidades](#funcionalidades)
- [Modelo de Dados](#modelo-de-dados)

---

##  Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

| Ferramenta | Versão mínima | Download |
|---|---|---|
| Java JDK | 11+ | [adoptium.net](https://adoptium.net) |
| MySQL Server | 8.0+ | [mysql.com](https://dev.mysql.com/downloads/mysql/) |
| MySQL Workbench *(opcional)* | qualquer | [mysql.com](https://dev.mysql.com/downloads/workbench/) |

> **Verifique sua instalação** executando no terminal:
> ```bash
> java -version
> javac -version
> mysql --version
> ```

---

##  Estrutura do Projeto

```
DI/
├── lib/
│   └── mysql-connector-j-9.6.0.jar   # Driver JDBC do MySQL
├── src/
│   ├── Main.java                      # Ponto de entrada da aplicação
│   ├── DAO/                           # Acesso ao banco de dados
│   │   ├── ClienteDAO.java
│   │   ├── ProdutoDAO.java
│   │   ├── PedidoDAO.java
│   │   └── ItemPedidoDAO.java
│   ├── model/                         # Entidades do sistema
│   │   ├── Cliente.java
│   │   ├── Produto.java
│   │   ├── Pedido.java
│   │   └── ItemPedido.java
│   ├── menu/                          # Menus interativos do terminal
│   │   ├── MenuPrincipal.java
│   │   ├── MenuCliente.java
│   │   ├── MenuProduto.java
│   │   └── MenuPedido.java
│   ├── enums/
│   │   ├── CategoriaProduto.java      # ALIMENTOS | ELETRONICOS | LIVROS
│   │   └── StatusPedido.java          # FILA | PROCESSANDO | FINALIZADO
│   └── infra/
│       ├── Conexao.java               # Configuração da conexão JDBC
│       ├── ProcessadorThreads.java    # Thread de processamento automático
│       └── EstoqueInsuficienteException.java
├── bin/                               # Arquivos .class compilados (gerado automaticamente)
├── schema.sql                         # Script completo do banco de dados
└── .vscode/
    └── tasks.json                     # Atalho de build para o VS Code
```

---

##  Configuração do Banco de Dados

### 1. Inicie o MySQL Server

Confirme que o serviço do MySQL está em execução antes de continuar.

### 2. Execute o script SQL

Abra o terminal do MySQL ou o MySQL Workbench e execute o arquivo `schema.sql` localizado na raiz do projeto:

**Via terminal:**
```bash
mysql -u root -p < schema.sql
```

**Via MySQL Workbench:**
1. Abra o MySQL Workbench e conecte-se ao servidor local.
2. Vá em **File → Open SQL Script** e selecione o arquivo `schema.sql`.
3. Clique em ⚡ **Execute** (ou pressione `Ctrl+Shift+Enter`).

Isso criará o banco de dados `desafio_integrador` com as tabelas `cliente`, `produtos`, `pedidos` e `item_pedidos`.

---

## 🔧 Configuração da Conexão

As credenciais do banco de dados estão no arquivo `src/infra/Conexao.java`:

```java
private static final String URL  = "jdbc:mysql://localhost:3306/desafio_integrador?useUnicode=true&characterEncoding=UTF-8&connectionCollation=utf8mb4_general_ci";
private static final String USER = "root";
private static final String PASS = "";   // ← altere aqui se sua senha for diferente
```

> Se o seu MySQL tiver uma senha diferente (ou usuário diferente), edite os campos `USER` e `PASS` antes de compilar.

---

##  Compilação

Abra um terminal na **raiz do projeto** (pasta `DI/`) e execute o comando abaixo de acordo com seu sistema operacional.

### Windows (Prompt de Comando / PowerShell)

```cmd
javac -d bin -cp "lib/mysql-connector-j-9.6.0.jar" ^
  src/Main.java ^
  src/DAO/*.java ^
  src/model/*.java ^
  src/menu/*.java ^
  src/enums/*.java ^
  src/infra/*.java
```

### Linux / macOS

```bash
javac -d bin -cp "lib/mysql-connector-j-9.6.0.jar" \
  src/Main.java \
  src/DAO/*.java \
  src/model/*.java \
  src/menu/*.java \
  src/enums/*.java \
  src/infra/*.java
```

> Os arquivos `.class` compilados serão gerados automaticamente dentro da pasta `bin/`.

---

##  Execução

Após compilar, execute a aplicação com o comando abaixo (ainda na pasta raiz `DI/`):

### Windows

```cmd
java -cp "bin;lib/mysql-connector-j-9.6.0.jar" Main
```

### Linux / macOS

```bash
java -cp "bin:lib/mysql-connector-j-9.6.0.jar" Main
```

> **Atenção:** No Windows o separador de classpath é `;` (ponto e vírgula). No Linux/macOS é `:` (dois pontos).

---

##  Executando pelo VS Code

O projeto já inclui uma task configurada para o VS Code.

1. Abra a pasta `DI/` no VS Code.
2. Pressione `Ctrl+Shift+B` para executar a task de build padrão.
3. Uma janela do terminal será aberta pedindo o nome da classe principal — digite `Main` e pressione Enter.

> **Requisito:** Ter a extensão **[Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)** instalada no VS Code.

---

##  Funcionalidades

Ao iniciar, o sistema exibe um **menu principal no terminal** com as seguintes opções:

###  Clientes
- Cadastrar novo cliente (nome e e-mail único)
- Listar todos os clientes
- Atualizar dados de um cliente
- Remover cliente

###  Produtos
- Cadastrar produto com nome, preço, estoque e categoria
- Listar produtos (filtrável por categoria: `ALIMENTOS`, `ELETRONICOS`, `LIVROS`)
- Atualizar produto
- Remover produto

###  Pedidos
- Criar pedido vinculado a um cliente
- Adicionar itens ao pedido (com validação de estoque)
- Consultar pedidos e seus status
- Remover pedido

###  Processamento Automático (Background Thread)
Ao iniciar a aplicação, uma **thread de background** é ativada automaticamente. Ela verifica a fila de pedidos a cada **5 segundos** e os processa na seguinte sequência:

```
FILA  →  PROCESSANDO (3 segundos)  →  FINALIZADO
```

Mensagens do processador são exibidas no terminal com o prefixo `[THREAD]`.

---

##  Modelo de Dados

```
cliente
├── id_cliente  (PK, auto_increment)
├── nome        (varchar 150, not null)
└── email       (varchar 200, unique, not null)

produtos
├── id_produto  (PK, auto_increment)
├── nome        (varchar 150, not null)
├── preco       (decimal, > 0)
├── estoque     (int, >= 0, default 0)
└── categoria   (enum: ALIMENTOS | ELETRONICOS | LIVROS)

pedidos
├── id_pedido   (PK, auto_increment)
├── id_cliente  (FK → cliente)
├── status      (enum: FILA | PROCESSANDO | FINALIZADO, default FILA)
└── data_criacao (datetime, default now)

item_pedidos
├── id          (PK, auto_increment)
├── id_pedido   (FK → pedidos, on delete CASCADE)
├── id_produto  (FK → produtos, on delete RESTRICT)
├── quantidade  (int, > 0)
└── preco_unitario (decimal, >= 0)
```

---

##  Possíveis Erros e Soluções

| Erro | Causa provável | Solução |
|---|---|---|
| `Communications link failure` | MySQL não está rodando | Inicie o serviço do MySQL |
| `Access denied for user 'root'` | Senha incorreta | Atualize `PASS` em `Conexao.java` |
| `Unknown database 'desafio_integrador'` | Script SQL não foi executado | Execute o `schema.sql` novamente |
| `javac: command not found` | JDK não instalado ou não no PATH | Instale o JDK e configure a variável `JAVA_HOME` |
| `EstoqueInsuficienteException` | Produto sem estoque suficiente | Atualize o estoque do produto antes de criar o pedido |
