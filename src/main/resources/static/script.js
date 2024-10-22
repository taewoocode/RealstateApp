let properties = []; // 매물 정보를 저장할 배열

// Kakao Maps API 설정
const KAKAO_API_KEY = '2fb3fb34b681d14991fb9ed1813ad206'; // JavaScript 키

// 매물 등록 폼 제출 이벤트
document.getElementById('property-form').addEventListener('submit', function(event) {
    event.preventDefault(); // 기본 폼 제출 방지

    const title = document.getElementById('title').value;
    const price = document.getElementById('price').value;
    const location = document.getElementById('location').value;
    const description = document.getElementById('description').value;
    const image = document.getElementById('image').files[0];

    if (title && price && location && description) {
        // 매물 정보를 객체로 구성
        const property = {
            title: title,
            price: price,
            location: location,
            description: description,
            image: image ? URL.createObjectURL(image) : null // 이미지 URL 생성
        };

        properties.push(property); // 매물 정보를 배열에 추가
        addPropertyToList(property); // 매물 목록에 추가
        updatePropertyCount(); // 매물 수 업데이트
        clearForm(); // 폼 초기화
        initializeMap(); // 맵 초기화
    } else {
        alert('모든 필드를 채워주세요.');
    }
});

// 매물 목록에 추가하는 함수
function addPropertyToList(property) {
    const propertyList = document.getElementById('property-list');

    const propertyItem = document.createElement('div');
    propertyItem.classList.add('property-item');
    propertyItem.innerHTML = `
        <h3>${property.title}</h3>
        <p>가격: ${property.price}원</p>
        <p>위치: ${property.location}</p>
        <p>${property.description}</p>
        ${property.image ? `<img src="${property.image}" alt="${property.title}" class="property-image">` : ''}
    `;

    propertyList.appendChild(propertyItem);
}

// 매물 수 업데이트 함수
function updatePropertyCount() {
    const propertyCount = document.getElementById('property-count');
    propertyCount.textContent = `현재 등록된 매물 수: ${properties.length}개`;
}

// 폼 초기화 함수
function clearForm() {
    document.getElementById('property-form').reset(); // 폼 리셋
}

// 매물 검색 이벤트
document.getElementById('search-button').addEventListener('click', function() {
    const subwayStation = document.getElementById('subway-station').value.toLowerCase();
    searchProperties(subwayStation);
});

// 매물 검색 함수
function searchProperties(station) {
    const propertyList = document.getElementById('property-list');
    propertyList.innerHTML = ''; // 기존 목록 초기화

    const filteredProperties = properties.filter(property =>
        property.location.toLowerCase().includes(station)
    );

    if (filteredProperties.length === 0) {
        propertyList.innerHTML = '<p>검색 결과가 없습니다.</p>';
    } else {
        filteredProperties.forEach(addPropertyToList); // 검색된 매물 목록 추가
        updateMap(filteredProperties); // 검색된 매물 위치 업데이트
    }

    // 검색된 매물 수 업데이트
    const resultCount = document.getElementById('result-count');
    resultCount.textContent = `검색 결과: ${filteredProperties.length}개`;
}

// 카카오 맵 초기화
function initializeMap() {
    const mapContainer = document.getElementById('map'); // 지도를 표시할 div
    const mapOption = {
        center: new kakao.maps.LatLng(37.5665, 126.978), // 초기 위치 (서울)
        level: 3 // 초기 줌 레벨
    };

    // Kakao Maps API 호출
    const script = document.createElement('script');
    script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${KAKAO_API_KEY}&autoload=false`;
    document.head.appendChild(script);

    script.onload = function () {
        const map = new kakao.maps.Map(mapContainer, mapOption); // 지도 생성
    };
}

// script.js

document.getElementById("search-button").addEventListener("click", function() {
    const subwayStation = document.getElementById("subway-station").value;

    // Kakao Maps API를 사용하여 지하철역의 좌표를 가져옵니다.
    const geocoder = new kakao.maps.services.Geocoder();

    geocoder.addressSearch(subwayStation, function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
            displayNearbyProperties(coords); // 매물 표시 함수 호출
        } else {
            alert("지하철역을 찾을 수 없습니다.");
        }
    });
});

function displayNearbyProperties(coords) {
    // 여기서 주변 매물을 가져와서 표시하는 로직을 구현합니다.
    const propertyList = document.getElementById("property-list");
    propertyList.innerHTML = ''; // 이전 결과 초기화

    // 예시 데이터 (실제 데이터는 서버에서 가져와야 합니다.)
    const properties = [
        { title: "아파트 1", price: "2억", location: "경기 광주", description: "경기 광주역 근처의 아파트입니다.", image: "apartment1.jpg" },
        { title: "빌라 1", price: "1.5억", location: "경기 광주", description: "경기 광주역 근처의 빌라입니다.", image: "villa1.jpg" },
        // 더 많은 매물 추가...
    ];

    // 매물 리스트 업데이트
    properties.forEach(property => {
        const propertyItem = document.createElement("div");
        propertyItem.classList.add("property-item");
        propertyItem.innerHTML = `
            <h3>${property.title}</h3>
            <p>가격: ${property.price}</p>
            <p>위치: ${property.location}</p>
            <p>${property.description}</p>
            <img src="${property.image}" alt="${property.title}" style="width:100%; height:auto;">
        `;
        propertyList.appendChild(propertyItem);
    });

    // 결과 개수 표시
    const resultCount = document.getElementById("result-count");
    resultCount.textContent = `${properties.length}개의 매물이 검색되었습니다.`;
}


// 매물 위치 업데이트 함수
function updateMap(filteredProperties) {
    const mapContainer = document.getElementById('map'); // 지도를 표시할 div
    const map = new kakao.maps.Map(mapContainer, {
        center: new kakao.maps.LatLng(37.5665, 126.978), // 초기 위치 (서울)
        level: 3 // 초기 줌 레벨
    });

    // 기존 마커 삭제
    const markers = [];
    filteredProperties.forEach(property => {
        const geocoder = new kakao.maps.services.Geocoder();
        geocoder.addressSearch(property.location, function(results, status) {
            if (status === kakao.maps.services.Status.OK) {
                const markerPosition = new kakao.maps.LatLng(results[0].y, results[0].x);
                const marker = new kakao.maps.Marker({
                    position: markerPosition
                });

                markers.push(marker);
                marker.setMap(map); // 맵에 마커 추가

                // 맵 중심을 마커 위치로 이동
                map.setCenter(markerPosition);
            }
        });
    });
}
