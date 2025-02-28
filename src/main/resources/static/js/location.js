// ✅ JavaScript에서 location 값을 가져와서 표시만 함 (검색 요청 X)
function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            async function (position) {
                let lat = position.coords.latitude;
                let lon = position.coords.longitude;

                // ✅ 위치 정보 가져오기 (Nominatim API)
                let response = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lon}`);
                let data = await response.json();

                let city = data.address.city || data.address.town || data.address.village || "Unknown City";
                let postcode = data.address.postcode || "00000";

                let locationText = `${city} ${postcode}`;
                document.getElementById("userLocation").innerText = locationText;
            },
            function (error) {
                console.error("위치 정보를 가져올 수 없습니다.", error);
                document.getElementById("userLocation").innerText = "Location unavailable";
            }
        );
    } else {
        console.error("Geolocation을 지원하지 않는 브라우저입니다.");
        document.getElementById("userLocation").innerText = "Not supported";
    }
}

// ✅ 페이지 로드 시 자동 실행
window.onload = getLocation;


// ✅ 위치를 주소로 변환하는 함수 (Nominatim API 사용)
async function getAddress(lat, lon) {
    try {
        let response = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lon}`);
        let data = await response.json();

        // ✅ city(도시) + postcode(우편번호) 추출
        let city = data.address.city || data.address.town || data.address.village || "Unknown City";
        let postcode = data.address.postcode || "00000";

        let locationText = `${city} ${postcode}`;
        document.getElementById("userLocation").innerText = locationText;
    } catch (error) {
        console.error("주소를 가져오는 데 실패했습니다.", error);
        document.getElementById("userLocation").innerText = "Error fetching location";
    }
}

// ✅ 페이지 로드 시 자동 실행
window.onload = getLocation;
