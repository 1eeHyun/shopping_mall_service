document.addEventListener("DOMContentLoaded", function () {
    function adjustPadding() {
        const navbar = document.querySelector(".navbar");
        const content = document.querySelector(".content");

        if (navbar && content) {
            let navbarHeight = navbar.offsetHeight; // ✅ 네비게이션 바 높이 감지
            content.style.paddingTop = `${navbarHeight + 20}px`; // ✅ 여유 공간 추가
        }
    }

    adjustPadding(); // ✅ 초기 실행
    window.addEventListener("resize", adjustPadding); // ✅ 창 크기 변경 시 자동 조정
});

function showPopup(element) {
    let popup = element.querySelector(".image-popup");
    popup.style.display = "block";
}

function hidePopup() {
    document.querySelectorAll(".image-popup").forEach(popup => {
        popup.style.display = "none";
    });
}



