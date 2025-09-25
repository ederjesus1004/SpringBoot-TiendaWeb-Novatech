function confirmarEliminacion(mensaje) {
    return confirm(mensaje || '¿Estás seguro de eliminar este elemento?');
}

document.addEventListener('DOMContentLoaded', function() {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    var alertList = document.querySelectorAll('.alert-dismissible');
    alertList.forEach(function(alert) {
        setTimeout(function() {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
    
    const estadoSelects = document.querySelectorAll('.estado-select');
    estadoSelects.forEach(select => {
        const currentValue = select.value;
        
        if (currentValue === 'PENDIENTE') select.style.backgroundColor = '#ffc107';
        else if (currentValue === 'PROCESANDO') select.style.backgroundColor = '#0dcaf0';
        else if (currentValue === 'ENVIADO') select.style.backgroundColor = '#0d6efd';
        else if (currentValue === 'ENTREGADO') select.style.backgroundColor = '#198754';
        else if (currentValue === 'CANCELADO') select.style.backgroundColor = '#dc3545';
    });
}); 