// Funções JavaScript para o site SobreRodas

// Função para inicializar o site
document.addEventListener('DOMContentLoaded', function() {
    // Atualiza o ano no rodapé
    document.getElementById('year').textContent = new Date().getFullYear();
    
    // Inicializa os componentes interativos
    initializeNewsletterForm();
    initializeMenuHighlight();
    initializeImageLazyLoading();
});

// Função para destacar o item de menu atual
function initializeMenuHighlight() {
    const currentPath = window.location.pathname;
    const menuLinks = document.querySelectorAll('.menu a');
    
    menuLinks.forEach(link => {
        // Remove a classe active de todos os links
        link.classList.remove('active');
        
        // Adiciona a classe active ao link correspondente à página atual
        if (link.getAttribute('href') === currentPath || 
            (currentPath === '/' && link.getAttribute('href') === '/') ||
            (link.getAttribute('href') !== '/' && currentPath.includes(link.getAttribute('href')))) {
            link.classList.add('active');
        }
    });
}

// Função para inicializar o formulário de newsletter
function initializeNewsletterForm() {
    const newsletterForm = document.querySelector('.cta form');
    
    if (newsletterForm) {
        newsletterForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const emailInput = this.querySelector('input[type="email"]');
            const email = emailInput.value.trim();
            
            if (email) {
                // Simulação de envio para API
                // Em uma implementação real, isso seria uma chamada AJAX para o backend
                setTimeout(() => {
                    alert(`Obrigado! O e-mail ${email} foi inscrito com sucesso.`);
                    emailInput.value = '';
                }, 500);
            }
        });
    }
}

// Função para lazy loading de imagens
function initializeImageLazyLoading() {
    if ('IntersectionObserver' in window) {
        const imgOptions = {
            threshold: 0.1,
            rootMargin: "0px 0px 50px 0px"
        };
        
        const imgObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    const src = img.getAttribute('data-src');
                    
                    if (src) {
                        img.src = src;
                        img.removeAttribute('data-src');
                        img.classList.add('loaded');
                    }
                    
                    imgObserver.unobserve(img);
                }
            });
        }, imgOptions);
        
        const lazyImages = document.querySelectorAll('img[data-src]');
        lazyImages.forEach(img => {
            imgObserver.observe(img);
        });
    } else {
        // Fallback para navegadores que não suportam IntersectionObserver
        const lazyImages = document.querySelectorAll('img[data-src]');
        lazyImages.forEach(img => {
            img.src = img.getAttribute('data-src');
            img.removeAttribute('data-src');
        });
    }
}

// Função para mostrar/esconder o menu mobile
function toggleMobileMenu() {
    const mobileMenu = document.querySelector('.mobile-menu');
    if (mobileMenu) {
        mobileMenu.classList.toggle('active');
    }
}