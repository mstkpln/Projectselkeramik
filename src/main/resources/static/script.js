// ===============================
// CERAMIC STUDIO MAIN SCRIPT-script.js
// ===============================

let cart = JSON.parse(localStorage.getItem("cart")) || [];
let currentProduct = null;
let currentQty = 1;

// ===============================
// PAGE INIT
// ===============================
document.addEventListener("DOMContentLoaded", () => {
    initFadeIn();
    loadGallery();
    loadProducts();
    loadProductDetail();
    updateCart();
});

// ===============================
// FADE IN
// ===============================
function initFadeIn() {
    const elements = document.querySelectorAll(".fade-in");

    const observer = new IntersectionObserver(entries => {
        entries.forEach(entry => {
            if (entry.isIntersecting) entry.target.classList.add("visible");
        });
    }, { threshold: 0.2 });

    elements.forEach(el => observer.observe(el));
}

// ===============================
// PRODUCTS LIST (SHOP PAGE)
// ===============================
async function loadProducts() {
    const container = document.getElementById("product-list");
    if (!container) return;

    const res = await fetch("/api/products");
    const products = await res.json();

    container.innerHTML = "";

    products.forEach(p => {
        const card = document.createElement("div");
        card.className = "card product-card";

        card.innerHTML = `
    		<img src="${p.imageUrl}" alt="${p.name}" style="width:100%; height:200px; object-fit:cover; border-radius:8px; margin-bottom:10px;">
    		<h3>${p.name}</h3>
    		<p>$${p.price}</p>
    		<button onclick="goToProduct(${p.id})">View details</button>
			`;

        container.appendChild(card);
    });
}

function goToProduct(id) {
    window.location.href = `product.html?id=${id}`;
}

// ===============================
// PRODUCT DETAIL PAGE
// ===============================
async function loadProductDetail() {
    const nameEl = document.getElementById("product-name");
    if (!nameEl) return;

    const params = new URLSearchParams(window.location.search);
    const productId = params.get("id");

    const res = await fetch("/api/products");
    const products = await res.json();

    currentProduct = products.find(p => p.id == productId);
    if (!currentProduct) return;

    nameEl.innerText = currentProduct.name;
    document.getElementById("product-price").innerText = `$${currentProduct.price}`;
    document.getElementById("product-image").src = currentProduct.imageUrl;
        
    const addBtn = document.querySelector(".add-to-cart");
    const qtyButtons = document.querySelectorAll(".quantity button");
    const qtyDisplay = document.getElementById("qty");

    if (currentProduct.stock === 0) {
        addBtn.innerText = "Sold Out";
        addBtn.disabled = true;
        addBtn.style.opacity = "0.5";
        addBtn.style.cursor = "not-allowed";
        
        // Disable quantity buttons for sold out items
        qtyButtons.forEach(btn => {
            btn.disabled = true;
            btn.style.opacity = "0.5";
            btn.style.cursor = "not-allowed";
        });
    }
    
    // Set quantity to 1 and lock it (unique items)
    currentQty = 1;
    qtyDisplay.innerText = currentQty;
}

function changeProductQty(amount) {
    // Products are unique - quantity locked at 1
    if (currentProduct && currentProduct.stock === 0) {
        return; // Don't allow changes for sold out items
    }
    // Keep quantity at 1 for unique items
    currentQty = 1;
    document.getElementById("qty").innerText = currentQty;
}

function addProductToCart() {
    if (currentProduct.stock === 0) {
        alert("This item is currently sold out.");
        return;
    }

    if (!currentProduct) return;

    const existing = cart.find(i => i.id === currentProduct.id);

    if (existing) {
        // Item already in cart - unique items can't be added twice
        alert("This unique item is already in your cart.");
        return;
    } else {
        cart.push({
            id: currentProduct.id,
            name: currentProduct.name,
            price: currentProduct.price,
            quantity: 1 // Always 1 for unique items
        });
    }

    currentQty = 1;
    saveCart();
    updateCart();
    alert("Added to cart ✨");
}

// ===============================
// GALLERY
// ===============================
async function loadGallery() {
    const grid = document.getElementById("gallery-grid");
    if (!grid) return;

    const res = await fetch("/api/gallery");
    const items = await res.json();

    grid.innerHTML = "";

    items.forEach(item => {
    const div = document.createElement("div");
    div.className = "gallery-item clickable";

    div.innerHTML = `
        <img src="${item.imageUrl}">
        <p>${item.title}</p>
    `;

    div.onclick = () => {
        window.location.href = `product.html?id=${item.id}`;
    };

    grid.appendChild(div);
});
}

// ===============================
// CART
// ===============================
function addToCart(id) {
    fetch("/api/products")
        .then(r => r.json())
        .then(products => {
            const p = products.find(x => x.id === id);
            const existing = cart.find(i => i.id === id);

            if (existing) existing.quantity++;
            else cart.push({ id:p.id, name:p.name, price:p.price, quantity:1 });

            saveCart();
            updateCart();
        });
}

function changeQty(index, delta) {
    cart[index].quantity += delta;
    if (cart[index].quantity <= 0) cart.splice(index, 1);

    saveCart();
    updateCart();
}

function updateCart() {
    const countEl = document.getElementById("cart-count");
    const itemsEl = document.getElementById("cart-items");
    const totalEl = document.getElementById("cart-total");

    if (!itemsEl) return;

    itemsEl.innerHTML = "";
    let total = 0;
    let count = 0;

    cart.forEach((item, i) => {
        total += item.price * item.quantity;
        count += item.quantity;

        itemsEl.innerHTML += `
            <div class="cart-item">
                <h4>${item.name}</h4>
                <p>$${item.price}</p>
                <div class="qty-controls">
                    <button onclick="changeQty(${i}, -1)">-</button>
                    <span>${item.quantity}</span>
                    <button onclick="changeQty(${i}, 1)">+</button>
                </div>
            </div>
        `;
    });

    if (countEl) countEl.innerText = count;
    if (totalEl) totalEl.innerText = total.toFixed(2);
}

function saveCart() {
    localStorage.setItem("cart", JSON.stringify(cart));
}

function toggleCart() {
    const modal = document.getElementById("cart-modal");
    if (modal) modal.classList.toggle("active");
}

// ===============================
// CHECKOUT
// ===============================
async function checkout() {
	alert("Store is not open yet. All items are currently unavailable.");
	/*
    if (!cart || cart.length === 0) {
        alert("Cart is empty!");
        return;
    }

    const stripeItems = cart.map(item => ({
        productId: item.id,
        quantity: item.quantity
    }));

    try {
        const response = await fetch("/api/checkout/create-session", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(stripeItems)
        });

        if (!response.ok) {
            const text = await response.text();
            throw new Error(text);
        }

        const data = await response.json();

        const stripe = Stripe("pk_test_51T1H3rQeKmG7ARPUK15lOQnAlRAiGpPoUpvepsYadpnnThwsp0PtDeH4DaOgHW1wnYaymvyjbFEyIZIV2DjtJI4z00ilPOrwnZ");

        await stripe.redirectToCheckout({ sessionId: data.id });

    } catch (err) {
        console.error("Failed to create checkout session:", err);
        alert("Checkout failed. See console for details.");
    }
    */
}




// ===============================
// GLOBAL (for HTML onclick)
// ===============================
window.goToProduct = goToProduct;
window.changeProductQty = changeProductQty;
window.addProductToCart = addProductToCart;
window.addToCart = addToCart;
window.changeQty = changeQty;
window.toggleCart = toggleCart;
window.checkout = checkout;
