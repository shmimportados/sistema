# API Endpoints - Sistema de Agendamento

## Base URL
```
http://localhost:8080
```

## Autenticação

Todas as requisições devem incluir a sessão do usuário. Use cookies para manter a sessão.

---

## 🔐 Endpoints de Autenticação

### Login
```
POST /auth/login
Content-Type: application/x-www-form-urlencoded

email=user@example.com&password=senha123
```

**Response (302 Redirect):**
- Sucesso: Redireciona para `/admin/dashboard` ou `/client/appointments`
- Erro: Retorna para `/auth/login` com mensagem de erro

### Registrar
```
POST /auth/register
Content-Type: application/x-www-form-urlencoded

email=novo@example.com
&password=senha123
&fullName=João Silva
&phone=11999999999
&userType=BUSINESS_OWNER
```

**Response:**
- Sucesso: Redireciona para `/auth/login`
- Erro: Retorna para `/auth/register` com mensagem de erro

### Logout
```
GET /auth/logout
```

---

## 📊 Endpoints do Painel Admin

### Dashboard - Listar Negócios
```
GET /admin/dashboard
```

**Response:**
- HTML com lista de negócios do proprietário

### Criar Negócio
```
GET /admin/business/new
```

Exibe formulário de criação.

```
POST /admin/business/create
Content-Type: application/x-www-form-urlencoded

name=Meu Salão
&type=salão
&description=Descrição
&phone=11988888888
&whatsappPhone=11988888888
&email=salao@example.com
&address=Rua X, 100
&city=São Paulo
&state=SP
&zipCode=01234-567
&businessHoursStart=08:00
&businessHoursEnd=18:00
&appointmentDurationMinutes=60
&reminderHoursBefore=24
&enableEmailReminder=on
&enableWhatsappReminder=on
```

**Response (201):**
```json
{
  "id": 1,
  "name": "Meu Salão",
  "type": "salão",
  ...
}
```

### Ver Detalhes do Negócio
```
GET /admin/business/{businessId}
```

### Editar Negócio
```
GET /admin/business/{businessId}/edit
POST /admin/business/{businessId}/update
Content-Type: application/x-www-form-urlencoded

[mesmos parâmetros do create]
```

---

## 📅 Endpoints de Agendamentos

### Listar Agendamentos
```
GET /admin/appointments/{businessId}
```

**Response:**
```json
[
  {
    "id": 1,
    "customerId": 1,
    "serviceId": 1,
    "appointmentDateTime": "2024-02-15T10:00:00",
    "status": "PENDING",
    "notes": "Cliente novo",
    "confirmationSent": true
  }
]
```

### Criar Agendamento
```
POST /admin/appointments/{businessId}/create
Content-Type: application/x-www-form-urlencoded

customerId=1
&serviceId=1
&appointmentDateTime=2024-02-15T10:00:00
&notes=Observação
```

### Confirmar Agendamento
```
POST /admin/appointments/{businessId}/{appointmentId}/confirm
```

### Cancelar Agendamento
```
POST /admin/appointments/{businessId}/{appointmentId}/cancel
```

---

## 💼 Endpoints de Serviços

### Listar Serviços
```
GET /admin/services/{businessId}
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Corte de Cabelo",
    "description": "Corte profissional",
    "price": 50.00,
    "durationMinutes": 45
  }
]
```

### Criar Serviço
```
POST /admin/services/{businessId}/create
Content-Type: application/x-www-form-urlencoded

name=Novo Serviço
&description=Descrição
&price=85.00
&durationMinutes=60
```

### Editar Serviço
```
POST /admin/services/{businessId}/{serviceId}/update
Content-Type: application/x-www-form-urlencoded

[mesmos parâmetros do create]
```

### Deletar Serviço
```
POST /admin/services/{businessId}/{serviceId}/delete
```

---

## 👥 Endpoints de Clientes

### Listar Clientes
```
GET /admin/customers/{businessId}
```

**Response:**
```json
[
  {
    "id": 1,
    "fullName": "Maria Silva",
    "phone": "11987654321",
    "email": "maria@email.com",
    "whatsappPhone": "11987654321"
  }
]
```

### Criar Cliente
```
POST /admin/customers/{businessId}/create
Content-Type: application/x-www-form-urlencoded

fullName=João Santos
&phone=11987654321
&email=joao@email.com
&whatsappPhone=11987654321
```

---

## 🌐 Endpoints Públicos

### Página de Agendamento Pública
```
GET /book/{businessId}
```

**Response:**
HTML com formulário de agendamento público

### Criar Agendamento (Cliente Público)
```
POST /book/{businessId}/create-appointment
Content-Type: application/x-www-form-urlencoded

customerName=João Silva
&customerPhone=11987654321
&customerEmail=joao@email.com
&customerWhatsapp=5511987654321
&serviceId=1
&appointmentDateTime=2024-02-15T10:00:00
&notes=Observação
```

**Response (200):**
```html
<!-- Página de sucesso com mensagem de confirmação -->
```

---

## 📋 Códigos de Status HTTP

| Código | Significado |
|--------|-------------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Recurso criado |
| 302 | Found - Redirecionamento |
| 400 | Bad Request - Dados inválidos |
| 401 | Unauthorized - Não autenticado |
| 403 | Forbidden - Sem permissão |
| 404 | Not Found - Recurso não encontrado |
| 500 | Internal Server Error - Erro no servidor |

---

## 🔍 Filtros e Parâmetros

### Agendamentos por Data
```
GET /admin/appointments/{businessId}?startDate=2024-02-01&endDate=2024-02-28
```

### Agendamentos por Status
```
Status disponíveis:
- PENDING (Pendente)
- CONFIRMED (Confirmado)
- COMPLETED (Realizado)
- CANCELLED (Cancelado)
- NO_SHOW (Não Compareceu)
```

---

## ⚠️ Erros Comuns

### 400 - Bad Request
```
Causas: Dados faltando, formato inválido
Solução: Verifique os parâmetros obrigatórios
```

### 401 - Unauthorized
```
Causas: Usuário não autenticado
Solução: Faça login primeiro em /auth/login
```

### 404 - Not Found
```
Causas: Recurso não existe
Solução: Verifique o ID do recurso
```

### 500 - Internal Server Error
```
Causas: Erro na aplicação
Solução: Verifique os logs no console
```

---

## 📝 Exemplos cURL

### Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "email=user@example.com&password=senha123" \
  -c cookies.txt
```

### Criar Serviço
```bash
curl -X POST http://localhost:8080/admin/services/1/create \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -b cookies.txt \
  -d "name=Corte&description=Corte profissional&price=50.00&durationMinutes=45"
```

### Criar Agendamento
```bash
curl -X POST http://localhost:8080/admin/appointments/1/create \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -b cookies.txt \
  -d "customerId=1&serviceId=1&appointmentDateTime=2024-02-15T10:00:00"
```

### Listar Agendamentos
```bash
curl -X GET http://localhost:8080/admin/appointments/1 \
  -b cookies.txt
```

---

## 🧪 Testando com Postman

1. Importe a coleção de endpoints
2. Configure a variável `baseUrl`: `http://localhost:8080`
3. Autentique-se primeiro (fazer login)
4. Use os tokens/cookies nas requisições subsequentes

---

## 🔗 URLs Úteis

- **Swagger UI** (se habilitado): `http://localhost:8080/swagger-ui/`
- **H2 Console**: `http://localhost:8080/h2-console`
- **Actuator**: `http://localhost:8080/actuator`

---

## 📞 Suporte

Documentação: `/README.md`
Setup: `/SETUP.md`
