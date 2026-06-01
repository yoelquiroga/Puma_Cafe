let c=1;
function carrusel() {
    const baner = document.getElementById("baner");
    baner.style.opacity = "0"; 

    setTimeout(() => {
        c++;
        if (c > 3) {
            c = 1;
        }
        baner.setAttribute("src", "img/baner" + c + ".jpg");
        
        baner.classList.add("fade-in");
        
        baner.style.opacity = "1";  
        
        setTimeout(() => {
            baner.classList.remove("fade-in");
        }, 1500); // Tiempo igual a la duración de la animación
    }, 1000);  // Tiempo para desvanecer la imagen actual

    setTimeout(carrusel, 5000);  // Cambia la imagen después de 3 segundos
}



document.body.setAttribute("onload", "carrusel()");