function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            async function (position) {
                let lat = position.coords.latitude;
                let lon = position.coords.longitude;

                // Get location (Nominatim API)
                let response = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lon}`);
                let data = await response.json();

                let city = data.address.city || data.address.town || data.address.village || "Unknown City";
                let postcode = data.address.postcode || "00000";

                let locationText = `${city} ${postcode}`;
                document.getElementById("userLocation").innerText = locationText;
            },
            function (error) {
                console.error("Cannot get location.", error);
                document.getElementById("userLocation").innerText = "Location unavailable";
            }
        );
    } else {
        console.error("This browser does not support Geolocation.");
        document.getElementById("userLocation").innerText = "Not supported";
    }
}

window.onload = getLocation;

// (Nominatim API)
async function getAddress(lat, lon) {
    try {
        let response = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lon}`);
        let data = await response.json();

        // City + postcode
        let city = data.address.city || data.address.town || data.address.village || "Unknown City";
        let postcode = data.address.postcode || "00000";

        let locationText = `${city} ${postcode}`;
        document.getElementById("userLocation").innerText = locationText;
    } catch (error) {
        console.error("Failed to get location.", error);
        document.getElementById("userLocation").innerText = "Error fetching location";
    }
}


window.onload = getLocation;
