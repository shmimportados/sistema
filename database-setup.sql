-- Script de inicialização do banco de dados MySQL para Sistema de Agendamento

-- Criar banco de dados
CREATE DATABASE IF NOT EXISTS agendamento;
USE agendamento;

-- Tabela de usuários
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    role VARCHAR(50) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Tabela de negócios
CREATE TABLE businesses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100),
    state VARCHAR(50),
    zip_code VARCHAR(20),
    owner_id BIGINT NOT NULL,
    whatsapp_phone VARCHAR(20) NOT NULL,
    whatsapp_api_key VARCHAR(500),
    smtp_host VARCHAR(100) DEFAULT 'smtp.gmail.com',
    smtp_port INT DEFAULT 587,
    smtp_email VARCHAR(255),
    smtp_password VARCHAR(255),
    enable_email_reminder BOOLEAN DEFAULT TRUE,
    enable_whatsapp_reminder BOOLEAN DEFAULT TRUE,
    reminder_hours_before INT DEFAULT 24,
    appointment_duration_minutes INT DEFAULT 60,
    business_hours_start VARCHAR(5) DEFAULT '08:00',
    business_hours_end VARCHAR(5) DEFAULT '18:00',
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(id),
    INDEX idx_owner (owner_id)
);

-- Tabela de serviços
CREATE TABLE services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10, 2) NOT NULL,
    duration_minutes INT NOT NULL DEFAULT 60,
    business_id BIGINT NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (business_id) REFERENCES businesses(id),
    INDEX idx_business (business_id)
);

-- Tabela de clientes
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255),
    whatsapp_phone VARCHAR(20) NOT NULL,
    business_id BIGINT NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (business_id) REFERENCES businesses(id),
    INDEX idx_business (business_id),
    INDEX idx_phone (phone)
);

-- Tabela de agendamentos
CREATE TABLE appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    business_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    appointment_date_time DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    notes VARCHAR(500),
    email_reminder_sent BOOLEAN DEFAULT FALSE,
    whatsapp_reminder_sent BOOLEAN DEFAULT FALSE,
    confirmation_sent BOOLEAN DEFAULT FALSE,
    confirmed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (business_id) REFERENCES businesses(id),
    FOREIGN KEY (service_id) REFERENCES services(id),
    INDEX idx_business (business_id),
    INDEX idx_customer (customer_id),
    INDEX idx_appointment_date (appointment_date_time),
    INDEX idx_status (status)
);

-- Criar índices adicionais para melhor performance
CREATE INDEX idx_appointments_date_status ON appointments(appointment_date_time, status);
CREATE INDEX idx_businesses_owner_active ON businesses(owner_id, active);
CREATE INDEX idx_customers_business_phone ON customers(business_id, phone);
