// Utilidades para o Sistema de Agendamento

// Formatar data para datetime-local
function formatDateForInput(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
}

// Formatar telefone
function formatPhone(phone) {
    return phone.replace(/\D/g, '')
               .replace(/(\d{2})(\d)/, '($1) $2')
               .replace(/(\d)(\d{4})$/, '$1-$2');
}

// Formatar moeda
function formatCurrency(amount) {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(amount);
}

// Validar telefone
function isValidPhone(phone) {
    const regex = /^\(\d{2}\) \d{4,5}-\d{4}$/;
    return regex.test(phone);
}

// Validar email
function isValidEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
}

// Mostrar loading
function showLoading(show = true) {
    const loader = document.getElementById('loader');
    if (loader) {
        loader.style.display = show ? 'block' : 'none';
    }
}

// Confirmar ação
function confirmAction(message = 'Tem certeza que deseja realizar esta ação?') {
    return confirm(message);
}

// Local storage helpers
const Storage = {
    set: (key, value) => localStorage.setItem(key, JSON.stringify(value)),
    get: (key) => JSON.parse(localStorage.getItem(key)),
    remove: (key) => localStorage.removeItem(key),
    clear: () => localStorage.clear()
};

// API helpers
const API = {
    get: async (url) => {
        const response = await fetch(url);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return response.json();
    },
    post: async (url, data) => {
        const response = await fetch(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return response.json();
    },
    put: async (url, data) => {
        const response = await fetch(url, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return response.json();
    },
    delete: async (url) => {
        const response = await fetch(url, {method: 'DELETE'});
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return response.ok;
    }
};
