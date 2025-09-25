function actualizarPrecio() {
    const productoSelect = document.getElementById('producto');
    const precioUnitarioInput = document.getElementById('precioUnitario');
    
    if (productoSelect.selectedIndex > 0) {
        const selectedOption = productoSelect.options[productoSelect.selectedIndex];
        const precio = selectedOption.getAttribute('data-precio');
        precioUnitarioInput.value = precio;
        calcularSubtotal();
    } else {
        precioUnitarioInput.value = '';
        document.getElementById('subtotal').value = '';
    }
}

function calcularSubtotal() {
    const precioUnitario = parseFloat(document.getElementById('precioUnitario').value) || 0;
    const cantidad = parseInt(document.getElementById('cantidad').value) || 0;
    const subtotal = precioUnitario * cantidad;
    
    document.getElementById('subtotal').value = subtotal.toFixed(2);
}

document.addEventListener('DOMContentLoaded', function() {
    if (document.getElementById('precioUnitario') && document.getElementById('cantidad')) {
        if (document.getElementById('precioUnitario').value && document.getElementById('cantidad').value) {
            calcularSubtotal();
        }
    }
}); 