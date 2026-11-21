# Arena Games

E-commerce de games (consoles, jogos, acessórios e periféricos) desenvolvido como atividade final do 4º bimestre.

O fluxo completo de venda está implementado: vitrine, detalhes do produto, carrinho em sessão, login/cadastro, checkout com pagamento simulado, baixa de estoque e painel administrativo.

## Tecnologias

- **Front-end:** HTML5, CSS3, Bootstrap 5 e JavaScript
- **Back-end:** Java 17 com Servlets e JSP (Jakarta EE)
- **Banco de dados:** MySQL / MariaDB (XAMPP) acessado via JDBC com `PreparedStatement`
- **Servidor:** Jetty (pelo Maven) ou Apache Tomcat 10
- **Build:** Maven

## Requisitos atendidos

| Requisito | Onde está |
|---|---|
| Credenciais fora do código (.env) | `config/Env.java`, `config/Conexao.java`, `.env.example` |
| Prepared Statements (sem concatenar valores) | todas as classes em `dao/` |
| Senhas criptografadas (BCrypt) | `CadastroServlet` (`BCrypt.hashpw`) e `LoginServlet` (`BCrypt.checkpw`) |
| Controle de acesso no servidor | `filter/AdminFilter.java` e `filter/AutenticacaoFilter.java` |
| Vitrine dinâmica, busca e filtro por categoria | `VitrineServlet` + `vitrine.jsp` |
| Página do produto com estoque | `ProdutoServlet` + `produto.jsp` |
| Carrinho em sessão (adicionar, alterar, remover, totais) | `model/Carrinho.java` + `CarrinhoServlet` |
| Checkout só para usuário logado, PIX/Boleto/Cartão | `CheckoutServlet` + `checkout.jsp` |
| Pedido + itens + baixa de estoque + limpeza do carrinho (em transação) | `PedidoDAO.finalizar()` |
| Painel admin: CRUD de produtos e listagem de pedidos | `servlet/admin/` + `views/admin/` |
| Script do banco com carga inicial | `database/schema.sql` |

## Estrutura

```
arena-games/
├── database/schema.sql          script de criação do banco + dados de teste
├── src/main/java/br/com/arenagames/
│   ├── config/                  leitura do .env e conexão com o banco
│   ├── dao/                     acesso ao banco (UsuarioDAO, ProdutoDAO, PedidoDAO...)
│   ├── filter/                  filtros de login e de administrador
│   ├── model/                   classes de domínio (Produto, Pedido, Carrinho...)
│   ├── servlet/                 controladores da loja
│   ├── servlet/admin/           controladores do painel administrativo
│   └── util/                    classes de apoio
├── src/main/webapp/
│   ├── WEB-INF/views/           páginas JSP
│   ├── css/  js/  img/
├── .env.example
├── .gitignore
└── pom.xml
```

## Como executar

### 1. Pré-requisitos

- JDK 17 ou superior
- Maven 3.8 ou superior
- XAMPP (MySQL/MariaDB) ou MySQL instalado

### 2. Banco de dados

1. Inicie o **MySQL** no painel do XAMPP.
2. Abra o phpMyAdmin (`http://localhost/phpmyadmin`), vá em **Importar** e selecione o arquivo `database/schema.sql`.
   Pelo terminal também funciona:
   ```
   mysql -u root -p < database/schema.sql
   ```

### 3. Arquivo .env

Copie o modelo e ajuste os dados de acesso:

```
cp .env.example .env        (Linux/Mac)
copy .env.example .env      (Windows)
```

No XAMPP padrão o usuário é `root` e a senha fica vazia.

### 4. Subir a aplicação

Na pasta do projeto:

```
mvn jetty:run
```

Acesse **http://localhost:8080**.

Para gerar o `.war` e publicar no Tomcat 10: `mvn package` (o arquivo sai em `target/arena-games.war`). Nesse caso informe o caminho do `.env` com `-Denv.file=C:\caminho\do\projeto\.env` nas opções da JVM do Tomcat ou defina as variáveis `DB_*` no sistema.

## Contas de teste

| Perfil | E-mail | Senha |
|---|---|---|
| Administrador | admin@arenagames.com | admin123 |
| Cliente | cliente@arenagames.com | cliente123 |

## Modelo do banco

- `usuarios` (perfil `cliente` ou `admin`, e-mail único, senha em hash)
- `categorias`
- `produtos` (FK para categoria, preço `DECIMAL(10,2)`, estoque `INT UNSIGNED`, imagem, ativo)
- `pedidos` (FK para usuário, data, forma de pagamento, total)
- `itens_pedido` (FK para pedido e produto, quantidade e preço unitário congelado no momento da compra)
