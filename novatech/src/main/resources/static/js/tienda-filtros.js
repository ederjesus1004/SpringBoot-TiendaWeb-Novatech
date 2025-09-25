
document.addEventListener('DOMContentLoaded', function() {
    const productoContainer = document.querySelector('#productos-container');
    const categoryFilters = document.querySelectorAll('.filtro-categoria');
    const brandFilters = document.querySelectorAll('.filtro-marca');
    const minPriceInput = document.querySelector('#price-min');
    const maxPriceInput = document.querySelector('#price-max');
    const priceRangeInput = document.querySelector('#price-range');
    const applyPriceButton = document.querySelector('#apply-price');
    const searchInput = document.querySelector('#search-products');
    const searchButton = document.querySelector('#search-button');
    const orderSelect = document.querySelector('#order-by');
    const noResultsMessage = document.createElement('div');
    
    noResultsMessage.className = 'col-12 text-center mt-4';
    noResultsMessage.innerHTML = `
        <div class="alert alert-warning">
            <i class="fas fa-exclamation-triangle fa-2x mb-3"></i>
            <h4>No se encontraron productos</h4>
            <p>Intente con otros criterios de búsqueda.</p>
        </div>
    `;
    
    let filters = {
        categories: [],
        brands: [],
        minPrice: 0,
        maxPrice: Number.MAX_SAFE_INTEGER,
        search: ''
    };
    
    const productos = Array.from(document.querySelectorAll('.producto-item'));
    
    let minPrice = Number.MAX_SAFE_INTEGER;
    let maxPrice = 0;
    
    productos.forEach(producto => {
        const precioText = producto.querySelector('.precio-producto').getAttribute('data-precio');
        const precio = parseFloat(precioText);
        
        if (!isNaN(precio)) {
            if (precio < minPrice) minPrice = precio;
            if (precio > maxPrice) maxPrice = precio;
        }
    });
    
    if (minPriceInput) minPriceInput.placeholder = `Min (${minPrice.toFixed(2)})`;
    if (maxPriceInput) maxPriceInput.placeholder = `Max (${maxPrice.toFixed(2)})`;
    if (priceRangeInput) {
        priceRangeInput.min = Math.floor(minPrice);
        priceRangeInput.max = Math.ceil(maxPrice);
        priceRangeInput.value = Math.ceil(maxPrice);
    }
    
    function applyFilters() {
        let visibleCount = 0;
        
        productos.forEach(producto => {
            const categoriaId = producto.getAttribute('data-categoria-id');
            const marcaId = producto.getAttribute('data-marca-id');
            const precioText = producto.querySelector('.precio-producto').getAttribute('data-precio');
            const precio = parseFloat(precioText);
            const nombre = producto.querySelector('.card-title').textContent.toLowerCase();
            const descripcion = producto.querySelector('.card-text:nth-child(3)').textContent.toLowerCase();
            
            const matchesCategory = filters.categories.length === 0 || filters.categories.includes(categoriaId);
            const matchesBrand = filters.brands.length === 0 || filters.brands.includes(marcaId);
            const matchesPrice = (!filters.minPrice || precio >= filters.minPrice) && 
                                (!filters.maxPrice || precio <= filters.maxPrice);
            const matchesSearch = filters.search === '' || 
                                nombre.includes(filters.search) || 
                                descripcion.includes(filters.search);
            
            if (matchesCategory && matchesBrand && matchesPrice && matchesSearch) {
                producto.style.display = '';
                visibleCount++;
            } else {
                producto.style.display = 'none';
            }
        });
        
        if (visibleCount === 0 && productoContainer) {
            if (!productoContainer.contains(noResultsMessage)) {
                productoContainer.appendChild(noResultsMessage);
            }
        } else {
            if (productoContainer && productoContainer.contains(noResultsMessage)) {
                productoContainer.removeChild(noResultsMessage);
            }
        }
    }
    
    function sortProducts(orderBy) {
        const productsArray = Array.from(productos);
        
        switch(orderBy) {
            case 'price-asc':
                productsArray.sort((a, b) => {
                    const precioA = parseFloat(a.querySelector('.precio-producto').getAttribute('data-precio'));
                    const precioB = parseFloat(b.querySelector('.precio-producto').getAttribute('data-precio'));
                    return precioA - precioB;
                });
                break;
            case 'price-desc':
                productsArray.sort((a, b) => {
                    const precioA = parseFloat(a.querySelector('.precio-producto').getAttribute('data-precio'));
                    const precioB = parseFloat(b.querySelector('.precio-producto').getAttribute('data-precio'));
                    return precioB - precioA;
                });
                break;
            case 'name-asc':
                productsArray.sort((a, b) => {
                    const nombreA = a.querySelector('.card-title').textContent;
                    const nombreB = b.querySelector('.card-title').textContent;
                    return nombreA.localeCompare(nombreB);
                });
                break;
            case 'name-desc':
                productsArray.sort((a, b) => {
                    const nombreA = a.querySelector('.card-title').textContent;
                    const nombreB = b.querySelector('.card-title').textContent;
                    return nombreB.localeCompare(nombreA);
                });
                break;
        }
        
        if (productoContainer) {
            productsArray.forEach(product => {
                productoContainer.appendChild(product);
            });
        }
    }
    
    if (categoryFilters) {
        categoryFilters.forEach(checkbox => {
            checkbox.addEventListener('change', function() {
                const categoriaId = this.getAttribute('data-id');
                
                if (this.checked) {
                    filters.categories.push(categoriaId);
                } else {
                    filters.categories = filters.categories.filter(id => id !== categoriaId);
                }
                
                applyFilters();
            });
        });
    }
    
    if (brandFilters) {
        brandFilters.forEach(checkbox => {
            checkbox.addEventListener('change', function() {
                const marcaId = this.getAttribute('data-id');
                
                if (this.checked) {
                    filters.brands.push(marcaId);
                } else {
                    filters.brands = filters.brands.filter(id => id !== marcaId);
                }
                
                applyFilters();
            });
        });
    }
    
    if (applyPriceButton) {
        applyPriceButton.addEventListener('click', function() {
            const minPrice = minPriceInput.value ? parseFloat(minPriceInput.value) : null;
            const maxPrice = maxPriceInput.value ? parseFloat(maxPriceInput.value) : null;
            
            filters.minPrice = minPrice;
            filters.maxPrice = maxPrice;
            
            applyFilters();
        });
    }
    
    if (priceRangeInput) {
        priceRangeInput.addEventListener('input', function() {
            if (maxPriceInput) {
                maxPriceInput.value = this.value;
            }
        });
    }
    
    if (searchButton) {
        searchButton.addEventListener('click', function() {
            filters.search = searchInput.value.toLowerCase();
            applyFilters();
        });
    }
    
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                filters.search = this.value.toLowerCase();
                applyFilters();
            }
        });
    }
    
    if (orderSelect) {
        orderSelect.addEventListener('change', function() {
            const orderBy = this.value;
            sortProducts(orderBy);
        });
    }
}); 