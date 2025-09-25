document.addEventListener('DOMContentLoaded', function() {
    const imagenInput = document.getElementById('imagen');
    if (imagenInput) {
        imagenInput.addEventListener('input', function() {
            const imageUrl = this.value;
            const previewContainer = document.querySelector('.mt-2');
            
            if (previewContainer) {
                previewContainer.innerHTML = imageUrl ? 
                    `<img src="${imageUrl}" alt="Vista previa" class="product-image-preview">` : '';
            } else if (imageUrl) {
                const newPreview = document.createElement('div');
                newPreview.className = 'mt-2';
                newPreview.innerHTML = `<img src="${imageUrl}" alt="Vista previa" class="product-image-preview">`;
                this.parentNode.appendChild(newPreview);
            }
        });
    }
}); 