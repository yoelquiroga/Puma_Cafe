// Toast notifications
function showToast(mensaje, tipo, duracion) {
	var old = document.querySelector('.puma-toast');
	if (old) old.remove();
	var colors = { success: '#2e7d32', error: '#dc3545', info: '#0d6efd' };
	var icons = { success: '✓', error: '✕', info: 'ℹ' };
	var bg = colors[tipo] || colors.success;
	var toast = document.createElement('div');
	toast.className = 'puma-toast';
	toast.style.cssText = 'position:fixed;bottom:30px;right:30px;background:#fff;color:#333;padding:16px 24px;border-radius:10px;box-shadow:0 8px 32px rgba(0,0,0,0.15);display:flex;align-items:center;gap:12px;z-index:99999;font-size:14px;font-family:Arial,sans-serif;border-left:5px solid ' + bg + ';transform:translateX(120%);transition:transform 0.4s ease;max-width:400px;';
	toast.innerHTML = '<span style="display:inline-flex;align-items:center;justify-content:center;width:28px;height:28px;border-radius:50%;background:' + bg + ';color:#fff;font-size:16px;font-weight:bold;flex-shrink:0;">' + (icons[tipo] || '✓') + '</span><span>' + mensaje + '</span>';
	document.body.appendChild(toast);
	requestAnimationFrame(function() { toast.style.transform = 'translateX(0)'; });
	setTimeout(function() {
		toast.style.transform = 'translateX(120%)';
		setTimeout(function() { if (toast.parentNode) toast.remove(); }, 400);
	}, duracion || 2000);
}

// buscador
let seachForm = document.querySelector(".search-form");

document.querySelector("#search-btn").onclick = () => {
  seachForm.classList.toggle("active");
};


// carrito
// ABRIR Y CERRAR
// icono
const carIcon = document.querySelector("#cart-icon");
// caja padre
const cart = document.querySelector(".cart");
// icono cerrar
const closeCart = document.querySelector("#cart-close");
// contador de productos
const countProducts = document.querySelector('#contador-productos');

carIcon.addEventListener("click", () => {
  cart.classList.add("active");
});

closeCart.addEventListener("click", () => {
  cart.classList.remove("active");
});

// COMENZAR CUANDO EL DOCUMENO ESTE LISTO
if (document.readyState == "loading") {
  document.addEventListener("DOMContentLoaded", start);
} else {
  start();
}

// COMENZAR
function start() {
	cargarItemsDeLocalStorage();
	addEvents();
}

// ACTUALIZAR Y VOLVER A PRESENTAR
function update() {
  addEvents();
  updateTotal();
}

// Evento para el botón de sumar cantidad
function sumarCantidad(event) {
  let cartBox = event.target.closest('.cart-box');
  let input = cartBox.querySelector('.cart-quantity');
  let title = cartBox.querySelector('.cart-product-title').innerHTML;

  input.value = parseInt(input.value) + 1;

  let item = itemsAdded.find(i => i.title === title);
  if (item) {
    item.quantity = parseInt(input.value);
    localStorage.setItem('cartItems', JSON.stringify(itemsAdded));
  }

  update();
  showToast('Cantidad actualizada', 'info', 1000);
}

// Evento para el botón de restar cantidad
function restarCantidad(event) {
  let cartBox = event.target.closest('.cart-box');
  let input = cartBox.querySelector('.cart-quantity');
  let title = cartBox.querySelector('.cart-product-title').innerHTML;

  let newVal = parseInt(input.value) - 1;

  if (newVal <= 0) {
    itemsAdded = itemsAdded.filter(i => i.title !== title);
    localStorage.setItem('cartItems', JSON.stringify(itemsAdded));
    cartBox.remove();
    update();
    return;
  }

  input.value = newVal;

  let item = itemsAdded.find(i => i.title === title);
  if (item) {
    item.quantity = newVal;
    localStorage.setItem('cartItems', JSON.stringify(itemsAdded));
  }

  update();
  showToast('Cantidad actualizada', 'info', 1000);
}

// EVENTOS
function addEvents() {
  // QUITAR ARTICULOS DEL CARRITO
  let cartRemove_btns = document.querySelectorAll(".cart-remove");

  console.log(cartRemove_btns);

  cartRemove_btns.forEach((btn) => {
    btn.addEventListener("click", handle_removeCartItem);
  });

  // CAMBIAR CANTIDAD DE ARTICULOS
  let cartQuantity_inputs = document.querySelectorAll(".cart-quantity");

  cartQuantity_inputs.forEach((input) => {
    input.addEventListener("change", handle_changeItemQuantity);
  });

  //   AÑADIR ARTICULOS AL CARRITO
  let addCart_btns = document.querySelectorAll(".add-cart");
  addCart_btns.forEach((btn) => {
    btn.addEventListener("click", handle_addCartItem);
  });


  // Agregar funcionalidad a los botones de sumar y restar cantidad
  let botonesSumarCantidad = document.getElementsByClassName('sumar-cantidad');
  for (let i = 0; i < botonesSumarCantidad.length; i++) {
    botonesSumarCantidad[i].addEventListener('click', sumarCantidad);
  }

  let botonesRestarCantidad = document.getElementsByClassName('restar-cantidad');
  for (let i = 0; i < botonesRestarCantidad.length; i++) {
    botonesRestarCantidad[i].addEventListener('click', restarCantidad);
  }
}

// funciones de manejos de eventos
let itemsAdded = JSON.parse(localStorage.getItem('cartItems')) || [];

function handle_addCartItem() {
	let product = this.closest(".card-product");
	let id = product.dataset.id;
	let imgSrc = product.querySelector(".product-img").src;
	let title = product.querySelector(".product-title").innerHTML;

	// Asegurarse de que el precio sea un número
	let price = product.querySelector(".price").childNodes[0].textContent.trim();
	price = parseFloat(price.replace("S/", "").trim()); // Convertir el precio en número

	if (isNaN(price)) {
		alert("Error al procesar el precio del producto");
		return; // Evitar añadir el producto si hay un error en el precio
	}

	let newToAdd = {
		id: parseInt(id),
		imgSrc,
		title,
		price, // Guardamos el precio como número
		quantity: 1
	};

	// Verificar si el producto ya existe en itemsAdded (por ID)
	let existingItem = itemsAdded.find(item => item.id === newToAdd.id);

	if (existingItem) {
		// Si existe, incrementar cantidad
		existingItem.quantity = (existingItem.quantity || 0) + 1;
		// Actualizar la cantidad en el carrito visualmente
		let cartBoxes = document.querySelectorAll(".cart-box");
		cartBoxes.forEach(cartBox => {
			let cartTitle = cartBox.querySelector(".cart-product-title").innerHTML;
			if (cartTitle === existingItem.title) {
				let quantityInput = cartBox.querySelector(".cart-quantity");
				quantityInput.value = existingItem.quantity;
			}
		});
	} else {
		// Si no existe, añadir un nuevo producto
		itemsAdded.push(newToAdd);
		// Añadir el nuevo nodo al DOM
		let carBoxElement = cartBoxComponent(title, price, imgSrc, newToAdd.quantity);
		let newNode = document.createElement("div");
		newNode.innerHTML = carBoxElement;
		const cartContent = cart.querySelector(".cart-content");
		cartContent.appendChild(newNode);
	}

	// Actualizar el localStorage
	localStorage.setItem('cartItems', JSON.stringify(itemsAdded));

	update();

	showToast(title + ' agregado al carrito', 'success', 2000);
}

function handle_removeCartItem() {
  // Remover el elemento visualmente del carrito
  const productTitle = this.parentElement.querySelector(".cart-product-title").innerHTML;
  this.parentElement.remove();
  
  // Filtrar el array itemsAdded para eliminar el producto correspondiente
  itemsAdded = itemsAdded.filter(item => item.title !== productTitle);
  
  // Actualizar el LocalStorage con los nuevos items
  localStorage.setItem('cartItems', JSON.stringify(itemsAdded));

  // Actualizar la vista
  update();

  showToast(productTitle + ' eliminado del carrito', 'info', 2000);
}

function handle_changeItemQuantity() {
  if (isNaN(this.value) || this.value < 1) {
    this.value = 1;
  }
  this.value = Math.floor(this.value); // PARA MANTENER EL NUMERO ENTERO

  // Actualizar la cantidad en itemsAdded y luego en el localStorage
  let productTitle = this.parentElement.parentElement.querySelector(".cart-product-title").innerHTML;
  
  itemsAdded = itemsAdded.map(item => {
    if (item.title === productTitle) {
      item.quantity = this.value;
    }
    return item;
  });

  localStorage.setItem('cartItems', JSON.stringify(itemsAdded));
  
  update();
  showToast('Cantidad actualizada', 'info', 1000);
}



// FUNCIONES DE ACTUALIZAR Y RENDERIZAR
function updateTotal() {
	let cartBoxer = document.querySelectorAll(".cart-box");
	const totalElement = cart.querySelector(".total-price");
	let total = 0;
	// Inicializamos totalOfProducts en 0
	let totalOfProducts = 0;

	cartBoxer.forEach((cartBox) => {
		let priceElement = cartBox.querySelector(".cart-price");
		let priceText = priceElement.innerHTML.replace("S/", "").trim();
		let price = parseFloat(priceText);

		// Verifica si el valor es un número
		if (!isNaN(price)) {
			let quantity = cartBox.querySelector(".cart-quantity").value;

			// Sumamos el precio total para el carrito
			total += price * quantity;

			// Sumamos la cantidad de productos
			totalOfProducts += parseInt(quantity);
		}
	});

	// MANTENER 2 DIJITOS DESPUES DEL PUNTO DECIMAL
	total = total.toFixed(2);

	// Mostrar el total en el carrito
	totalElement.innerHTML = "S/" + total;

	// Actualizamos el contador de productos
	countProducts.innerText = totalOfProducts;
}

function cargarItemsDeLocalStorage() {
	const cartContent = document.querySelector(".cart-content");
	cartContent.innerHTML = ""; // Limpiar contenido previo

	// Obtener los ítems del LocalStorage
	let storedItems = JSON.parse(localStorage.getItem('cartItems')) || [];

	// Recorrer los ítems y agregarlos visualmente al carrito con la cantidad correcta
	storedItems.forEach(item => {
		let carBoxElement = cartBoxComponent(item.title, item.price, item.imgSrc, item.quantity);
		let newNode = document.createElement("div");
		newNode.innerHTML = carBoxElement;
		cartContent.appendChild(newNode);
	});

	// Actualizar la información del carrito
	update();
}



// COMPONENTES HTML
function cartBoxComponent(title, price, imgSrc, quantity) {
  var qty = quantity || 1;

  return `
    <div class="cart-box">

        <img src="${imgSrc}" alt="" class="cart-img">

        <div class="detail-box">
            <span class="cart-product-title">${title}</span>

            <div class="selector-cantidad">
              <i class="fa-solid fa-minus restar-cantidad"></i>
              <input type="text" value="${qty}" class="carrito-item-cantidad cart-quantity" disabled>
              <i class="fa-solid fa-plus sumar-cantidad"></i>
            </div>

            <span class="cart-price">${price}</span>
        </div>

        <!---- ELIMINAR CART ----->
          <i class="fa-solid fa-xmark cart-remove"></i>
    </div>

    `;

}