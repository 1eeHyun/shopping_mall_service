document.addEventListener("DOMContentLoaded", function () {
    console.log("DOM fully loaded!");

    // Add to a cart
    document.querySelectorAll(".cart-button").forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();
            let productId = this.getAttribute("data-product-id");

            console.log(`Adding product ${productId} to cart...`);

            fetch("/api/cart/add", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ productId: productId, quantity: 1 })
            })
            .then(response => response.text())  // Check response with text
            .then(text => {
                try {
                    const data = JSON.parse(text);
                    if (data.error) throw new Error(data.error);
                    alert("Added to cart!");
                    updateCartUI();
                } catch (error) {
                    console.error("Server returned non-JSON response:", text);
                }
            })
            .catch(error => console.error("Error adding to cart:", error));

        });
    });

    // Update quantity
    document.querySelectorAll(".update-button").forEach(button => {
        button.addEventListener("click", function () {
            let productId = this.getAttribute("data-product-id");
            let quantityElement = document.querySelector(`.quantity-select[data-product-id='${productId}']`);

            if (!quantityElement) {
                console.error(`Cannot find quantity input for product ${productId}`);
                return;
            }

            let quantity = quantityElement.value;

            console.log(`🔄 Updating cart: Product ${productId}, New Quantity: ${quantity}`);

            fetch("/api/cart/update", {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ productId: productId, quantity: quantity })
            })
            .then(response => response.json())
            .then(data => {
                console.log("Update response:", data);
                if (data.error) throw new Error(data.error);
                alert("Cart updated!");
                window.location.reload();
            })
            .catch(error => console.error("Error updating cart:", error));
        });
    });

    document.querySelectorAll(".remove-button").forEach(button => {
        button.addEventListener("click", function () {
            const productId = this.getAttribute("data-product-id");

            console.log("Removing product:", productId);  // Debugging log

            fetch(`/api/cart/remove?productId=${productId}`, {
                method: "DELETE"
            }).then(response => response.json())
              .then(data => {
                  console.log("Server Response:", data); // Server response
                  if (data.redirectUrl) {
                      window.location.href = data.redirectUrl; // redirect to /cart for empty cart
                  } else {
                      window.location.reload();
                  }
              })
              .catch(error => console.error("Error:", error));
        });
    });

    // Clear cart
    let clearButton = document.querySelector(".clear-button");

    if (clearButton) {
        clearButton.addEventListener("click", function () {
            console.log("Clearing cart...");

            fetch("/api/cart/clear", { method: "DELETE" })
            .then(response => response.json())
            .then(data => {
                console.log("✅ Cart cleared:", data);
                alert("Cart cleared!");
                window.location.reload();
            })
            .catch(error => console.error("Error clearing cart:", error));
        });
    } else {
        console.warn("No element found with class '.clear-button'");
    }
});
