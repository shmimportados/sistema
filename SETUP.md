# Guia de Setup - Sistema de Agendamento

## 🚀 Início Rápido (5 minutos)

### Opção 1: Desenvolvimento com H2 (Mais Fácil)

H2 é um banco de dados em memória - perfeito para testes!

1. **Clone e acesse o repositório:**
   ```bash
   git clone <url>
   cd sistema
   ```

2. **Execute:**
   ```bash
   mvn spring-boot:run
   ```

3. **Acesse:**
   - App: `http://localhost:8080/auth/login`
   - H2 Console: `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:mem:testdb`
     - User: `sa`
     - Password: (deixar vazio)

4. **Registre-se:**
   - Email: `teste@example.com`
   - Senha: `123456`
   - Tipo: Proprietário de Negócio

---

### Opção 2: Produção com MySQL

#### Passo 1: Instalar MySQL

**Windows:**
- Baixar em: https://dev.mysql.com/downloads/mysql/
- Executar instalador
- Configurar porta 3306 (padrão)

**Linux:**
```bash
sudo apt-get install mysql-server
sudo mysql_secure_installation
```

**macOS:**
```bash
brew install mysql
mysql.server start
```

#### Passo 2: Criar Banco de Dados

```bash
# Conectar ao MySQL
mysql -u root -p

# Dentro do MySQL shell
CREATE DATABASE agendamento;
USE agendamento;
```

Ou executar o script SQL:
```bash
mysql -u root -p agendamento < database-setup.sql
```

#### Passo 3: Configurar `application.properties`

Edite: `src/main/resources/application.properties`

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/agendamento?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=sua-senha-mysql
spring.j pa.hibernate.ddl-auto=update
```

#### Passo 4: Executar

```bash
mvn clean install
mvn spring-boot:run
```

---

## 📧 Configurar Email (Gmail)

Para receber notificações real!

1. **Ative 2FA no Gmail:**
   - https://myaccount.google.com/security

2. **Gere senha de aplicação:**
   - https://myaccount.google.com/apppasswords
   - Selecione "Mail" e "Windows Computer"
   - Copie a senha gerada

3. **Configure em `application.properties`:**
   ```properties
   spring.mail.username=seu-email@gmail.com
   spring.mail.password=senha-de-aplicacao-gerada
   ```

4. **Teste:**
   - Crie um agendamento
   - Você deve receber um email

---

## 💬 Integrar WhatsApp (Twilio)

Para enviar mensagens WhatsApp.

1. **Criar conta Twilio:**
   - https://www.twilio.com/console/sign-up

2. **Ativar WhatsApp Sandbox:**
   - https://www.twilio.com/console/sms/whatsapp/learn

3. **Obtenha:**
   - Account SID
   - Auth Token  
   - Número WhatsApp (+1415...)

4. **Configure em `NotificationService.java`:**
   ```java
   private static final String TWILIO_ACCOUNT_SID = "seu-sid";
   private static final String TWILIO_AUTH_TOKEN = "seu-token";
   private static final String TWILIO_PHONE = "+1415...";
   ```

5. **Envie primeiro SMS de teste:**
   ```java
   Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
   Message message = Message.creator(
       new PhoneNumber("+55119999999"),  // Para
       new PhoneNumber(TWILIO_PHONE),    // De
       "Teste WhatsApp"
   ).create();
   ```

---

## 🧪 Carregar Dados de Teste

```bash
mysql -u root -p agendamento < database-test-data.sql
```

Ou via MySQL:
```bash
mysql> USE agendamento;
mysql> source database-test-data.sql;
```

**Contas de teste criadas:**
- Email: `proprietario@test.com`
- Senha: `senha123`

---

## 🐛 Troubleshooting

### "Connection refused"
```
Problema: Não consegue conectar ao MySQL
Solução:
mysql -u root -p
SHOW VARIABLES LIKE 'port';  -- Verificar porta
```

### "Access denied"
```
Problema: Senha do MySQL incorreta
Solução:
mysql -u root
ALTER USER 'root'@'localhost' IDENTIFIED BY 'nova-senha';
```

### Email não é enviado
```
Problema: Credenciais Gmail incorretas
Solução:
1. Verific que 2FA está ativado
2. Gere nova senha de aplicação
3. Reinicie a aplicação
```

### WhatsApp não funciona
```
Problema: Twilio não configurado
Solução:
1. Confirme sandboxStatusCallbackUrl
2. Verifique número em formato +55...
3. Teste SMS primeiro
```

---

## 📊 Acessar Banco de Dados

### H2 Console
```
http://localhost:8080/h2-console
```

### MySQL com CLI
```bash
mysql -u root -p agendamento
SELECT * FROM users;
SELECT * FROM businesses;
SELECT * FROM appointments;
```

### MySQL GUI Tools
- **MySQL Workbench**: https://dev.mysql.com/downloads/workbench/
- **DBeaver**: https://dbeaver.io/ (multiplatform)
- **TablePlus**: https://tableplus.com/ (macOS/Windows)

---

## 🔧 Configurações Avançadas

### Alterar Porta da Aplicação

Em `application.properties`:
```properties
server.port=8081
```

Acesse: `http://localhost:8081`

### Ativar Logs Debug

Em `application.properties`:
```properties
logging.level.com.agendamento=DEBUG
logging.level.org.springframework.web=DEBUG
```

### Banco H2 Persistente

Em `application.properties`:
```properties
spring.datasource.url=jdbc:h2:~/agendamento-db
spring.jpa.hibernate.ddl-auto=update
```

---

## 📱 Acessar Sistema

### Após iniciar (`mvn spring-boot:run`):

| Página | URL |
|--------|-----|
| Login | http://localhost:8080/auth/login |
| Registrar | http://localhost:8080/auth/register |
| Dashboard | http://localhost:8080/admin/dashboard |
| Booking Público | http://localhost:8080/book/1 |
| H2 Console | http://localhost:8080/h2-console |

---

## ✅ Checklist de Setup Completo

- [ ] Java 17+ instalado (`java -version`)
- [ ] Maven instalado (`mvn -version`)
- [ ] Projeto clonado e aberto no VS Code
- [ ] Banco de dados criado (MySQL ou H2)
- [ ] `application.properties` configurado
- [ ] `mvn clean install` executado com sucesso
- [ ] `mvn spring-boot:run` rodando
- [ ] Acessar `http://localhost:8080` com sucesso
- [ ] Registrar nova conta
- [ ] Criar negócio de teste
- [ ] Adicionar serviço
- [ ] Testar agendamento

---

## 🆘 Suporte

Se tiver problemas:

1. Verifique os logs na console
2. Confira firewall (porta 8080 aberta)
3. Limpe cache Maven: `mvn clean`
4. Reinicie a aplicação
5. Abra uma issue no GitHub

---

**Pronto para começar!** 🎉
