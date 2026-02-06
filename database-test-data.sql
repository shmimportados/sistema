-- Script de exemplo para inserir dados de teste

USE agendamento;

-- Inserir usuário de teste (proprietário)
INSERT INTO users (email, password, full_name, phone, role, active) 
VALUES ('proprietario@test.com', 'senha123', 'João Silva', '11999999999', 'BUSINESS_OWNER', TRUE);

-- Inserir negócio de teste  
INSERT INTO businesses (name, type, description, phone, email, address, city, state, zip_code, owner_id, whatsapp_phone, smtp_email, smtp_password, active)
VALUES (
    'Salão de Beleza Premium',
    'salão',
    'Salão completo de beleza com serviços de cabelo, unhas e estética',
    '11988888888',
    'salao@example.com',
    'Rua das Belezas, 100',
    'São Paulo',
    'SP',
    '01234-567',
    1,
    '11988888888',
    'test@gmail.com',
    'password123',
    TRUE
);

-- Inserir serviços de teste
INSERT INTO services (name, description, price, duration_minutes, business_id, active)
VALUES 
    ('Corte de Cabelo', 'Corte técnico com profissional experiente', 50.00, 45, 1, TRUE),
    ('Manicure Completa', 'Manicure com esmaltação de alta qualidade', 40.00, 60, 1, TRUE),
    ('Pedicure Completa', 'Pedicure com hidratação e esmaltação', 45.00, 60, 1, TRUE),
    ('Progressiva', 'Progressiva com alisamento profissional', 120.00, 120, 1, TRUE);

-- Inserir clientes de teste
INSERT INTO customers (full_name, phone, email, whatsapp_phone, business_id, active)
VALUES 
    ('Maria Santos', '11987654321', 'maria@email.com', '11987654321', 1, TRUE),
    ('Ana Costa', '11986543210', 'ana@email.com', '11986543210', 1, TRUE),
    ('Paula Oliveira', '11985432109', 'paula@email.com', '11985432109', 1, TRUE);

-- Inserir agendamentos de teste
INSERT INTO appointments (customer_id, business_id, service_id, appointment_date_time, status, confirmation_sent, email_reminder_sent, whatsapp_reminder_sent)
VALUES 
    (1, 1, 1, DATE_ADD(NOW(), INTERVAL 2 DAY), 'CONFIRMED', TRUE, FALSE, FALSE),
    (2, 1, 2, DATE_ADD(NOW(), INTERVAL 3 DAY), 'PENDING', FALSE, FALSE, FALSE),
    (3, 1, 4, DATE_ADD(NOW(), INTERVAL 5 DAY), 'CONFIRMED', TRUE, FALSE, FALSE);
