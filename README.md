# Sistema de Agendamento para Pequenos Negócios

Sistema web completo de agendamento para pequenos negócios como salões, barbearias, manicures e oficinas mecânicas, desenvolvido em Java com Spring Boot.

## 🌟 Características

- ✅ **Agendamento por Horário**: Agenda flexível com horários configuráveis
- ✅ **Confirmação Automática**: Agendamentos com confirmação automática via email/WhatsApp
- ✅ **Lembretes Automáticos**: Envio de lembretes por email e WhatsApp antes do agendamento
- ✅ **Painel do Proprietário**: Dashboard completo para gerenciar negócio
- ✅ **Gestão de Clientes**: Cadastro e gestão de clientes
- ✅ **Gestão de Serviços**: Criação e edição de serviços com preços
- ✅ **Página Pública de Agendamento**: URL pública para clientes agendarem
- ✅ **Configurações Personalizáveis**: Horários, lembretes, duração de agendamentos

## 🚀 Tecnologias Utilizadas

- **Backend**: Java 17 + Spring Boot 3.2.0
- **Banco de Dados**: MySQL 8.0 / H2 (para desenvolvimento)
- **Template Engine**: Thymeleaf
- **Segurança**: Spring Security
- **Email**: Spring Mail (Gmail)
- **WhatsApp**: Twilio API
- **Build**: Maven
- **ORM**: JPA/Hibernate

## 📋 Pré-requisitos

- Java 17 ou superior
- Maven 3.8+
- MySQL 8.0 ou superior (opcional - pode usar H2 para desenvolvimento)
- Conta Gmail (para envio de emails)
- Conta Twilio (para integração WhatsApp - opcional)

## ⚙️ Instalação e Configuração

### 1. Clonar o Repositório

```bash
git clone <url-do-repositorio>
cd sistema
```

### 2. Configurar o Banco de Dados

#### Para MySQL:

```sql
CREATE DATABASE agendamento;
USE agendamento;
```

#### Para H2 (Desenvolvimento):
O banco H2 será criado automaticamente

### 3. Configurar o arquivo `application.properties`

```properties
# Database - MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/agendamento?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=sua-senha

# Email Configuration
spring.mail.username=seu-email@gmail.com
spring.mail.password=sua-senha-aplicacao

# Ou para H2 (Desenvolvimento):
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true
```

### 4. Compilar e Executar

```bash
# Compilar
mvn clean install

# Executar
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 📖 Uso

### 1. Registrar Proprietário

1. Acesse `http://localhost:8080/auth/register`
2. Preencha os dados como "Proprietário de Negócio"
3. Clique em "Registrar"

### 2. Criar Negócio

1. Faça login como proprietário
2. Clique em "Criar Novo Negócio"
3. Preencha os dados do negócio e configurações
4. Clique em "Criar Negócio"

### 3. Adicionar Serviços

1. No dashboard, clique em "Serviços" do negócio
2. Clique em "Novo Serviço"
3. Preencha nome, descrição, preço e duração
4. Clique em "Criar Serviço"

### 4. Adicionar Clientes

1. No dashboard, clique em "Clientes" do negócio
2. Clique em "Novo Cliente"
3. Preencha os dados (telefone e WhatsApp obrigatórios)
4. Clique em "Criar Cliente"

### 5. Gerenciar Agendamentos

1. No dashboard, clique em "Agendamentos" do negócio
2. Clique em "Novo Agendamento" para criar
3. Ou clique em "Confirmar" para confirmar agendamentos pendentes
4. Use "Cancelar" para cancelar agendamentos

## 🔗 URLs Importantes

- **Login**: `http://localhost:8080/auth/login`
- **Registro**: `http://localhost:8080/auth/register`
- **Dashboard**: `http://localhost:8080/admin/dashboard` (após login)
- **Página de Agendamento Pública**: `http://localhost:8080/book/{businessId}`

## 🔐 Configuração de Email

### Gmail (Recomendado)

1. Acesse sua conta Google
2. Ative a autenticação de dois fatores
3. Gere uma senha de aplicação em: https://myaccount.google.com/apppasswords
4. Use essa senha em `spring.mail.password`

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seu-email@gmail.com
spring.mail.password=senha-de-aplicacao
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## 📱 Integração WhatsApp (Twilio)

1. Crie uma conta em https://www.twilio.com
2. Obtenha suas credenciais (Account SID, Auth Token, WhatsApp Phone)
3. Configure em `NotificationService.java`

```java
private static final String TWILIO_ACCOUNT_SID = "sua_conta_sid";
private static final String TWILIO_AUTH_TOKEN = "seu_auth_token";
private static final String TWILIO_PHONE = "+seu_numero_whatsapp";
```

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/agendamento/sistema/
│   │   ├── controller/        # Controllers MVC
│   │   ├── model/             # Entidades JPA
│   │   ├── repository/        # Repositories Spring Data
│   │   ├── service/           # Serviços de negócio
│   │   ├── dto/               # Data Transfer Objects
│   │   └── config/            # Configurações
│   └── resources/
│       ├── templates/         # Templates Thymeleaf
│       ├── static/
│       │   ├── css/
│       │   └── js/
│       └── application.properties
└── test/                      # Testes
```

## 🗄️ Esquema do Banco de Dados

### Principais Tabelas:
- **users**: Usuários do sistema
- **businesses**: Negócios
- **services**: Serviços oferecidos
- **customers**: Clientes
- **appointments**: Agendamentos

## 🔄 Fluxo de Agendamento

1. Cliente acessa URL pública: `/book/{businessId}`
2. Preenche formulário com seus dados e escolhe serviço/horário
3. Sistema cria cliente automaticamente (se não existir)
4. Agendamento é criado com status "PENDING"
5. Email de confirmação é enviado automaticamente
6. Proprietário recebe agendamento pendente
7. Proprietário confirma agendamento
8. Cliente recebe notificação de confirmação
9. 24h antes do agendamento (configurável), lembretes são enviados
10. Proprietário marca como "Realizado" ou "Não Compareceu"

## 🤖 Agendador de Lembretes

O sistema executa verificações a cada hora para enviar lembretes:
- Verifica agendamentos confirmados próximos
- Envia emails (se habilitado)
- Envia mensagens WhatsApp (se configurado)

Configure a antecedência em "Lembrete com Antecedência (horas)" nas configurações do negócio.

## 🛠️ Desenvolvimento e Testes

```bash
# Compilar
mvn compile

# Executar testes
mvn test

# Criar pacote
mvn package

# Executar com perfil de desenvolvimento
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

## 🐛 Troubleshooting

### Erro de conexão ao banco de dados
- Verifique se MySQL está rodando
- Confirme as credenciais em `application.properties`
- Para H2, nenhuma configuração adicional é necessária

### Email não é enviado
- Verifique se a senha de aplicação do Gmail está correta
- Confirme que autenticação de dois fatores está ativada
- Verifique logs do aplicativo

### WhatsApp não funciona
- Configure credenciais Twilio e números de telefone
- Certifique-se que o teste de SMS está funcionando primeiro
- Verifique que o número está no formato E.164 (+55...)

## 📝 Licença

Este projeto é fornecido como é, para fins educacionais e comerciais.

## 👤 Autor

Sistema desenvolvido como solução de agendamento para pequenos negócios.

## 🤝 Contribuições

Contribuições são bem-vindas! Abra uma issue ou pull request.

## 📧 Suporte

Para suporte, abra uma issue no repositório.

---

**Nota Importante**: Este sistema é uma solução base. Para produção, adicione:
- Validações mais robustas
- Criptografia de senhas (BCrypt)
- Testes unitários abrangentes
- Tratamento de exceções mais detalhado
- Backup de banco de dados
- HTTPS/SSL
- Rate limiting
- Autenticação JWT
- Logs detalhados
