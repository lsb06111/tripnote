var container = document.getElementById('rcmd-map'); //지도를 담을 영역의 DOM 레퍼런스
var options = { //지도를 생성할 때 필요한 기본 옵션
	center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
	level: 3 //지도의 레벨(확대, 축소 정도)
};
var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

// 주소-좌표 변환 객체를 생성합니다
var geocoder = new kakao.maps.services.Geocoder();
var markers = [];

function drawMarkerWithXY(mapx, mapy){
	var coords = new kakao.maps.LatLng(mapy, mapx);
    
    // 2. 결과 좌표로 지도에 마커를 표시합니다
    var marker = new kakao.maps.Marker({
        map: map,
        position: coords
    });

    // 3. 생성된 마커를 마커 배열에 추가합니다
    markers.push(marker);

    // 4. 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
    map.setCenter(coords);
}


function drawRcmdMarker(el){
    const $el = $(el);
    const address = $el.find('.trip-sl-address').text();
    
    // 주소로 좌표 얻기
    geocoder.addressSearch(address, function(result, status) {
    
        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {
            
            var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
            
            // 결과값으로 받은 위치를 마커로 표시합니다
            var marker = new kakao.maps.Marker({
                map: map,
                position: coords
            });

            markers.push(marker);

            // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
            map.setCenter(coords);
        }
    });
}

function relayoutKeepCenter(map) {
	const center = map.getCenter();
	map.relayout();
	map.setCenter(center);
}

// 장소 검색 객체를 생성합니다
var ps = new kakao.maps.services.Places();
let isRestaurant = false;
////
//모든 검색 결과를 담을 배열입니다
var allPlaces = [];
var currentPage = 1;

// 키워드로 장소를 검색하고, 모든 페이지를 순회하는 함수
function searchAndGetAll() {
    var keyword = '부산 식당';
    isRestaurant = true;
    console.log(`'${keyword}'에 대한 전체 검색을 시작합니다...`);
    
    // 첫 페이지 검색을 시작합니다.
    ps.keywordSearch(keyword, placesSearchCB);
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다


// ✨ 검색 실행
searchAndGetAll();

////

// 키워드 검색을 요청하는 함수입니다
function searchPlaces() {

    var keyword = document.getElementById('trip-search-input').value;

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }

    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
    ps.keywordSearch( keyword, placesSearchCB); 
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
	if(isRestaurant){
		if (status === kakao.maps.services.Status.OK) {
	        
	        // 현재 페이지의 결과를 allPlaces 배열에 추가합니다
	        allPlaces = allPlaces.concat(data);
	        console.log(`페이지 ${currentPage} 로드 완료. (현재까지 총 ${allPlaces.length}개)`);
	        
	        // 다음 페이지가 있으면
	        if (pagination.hasNext) {
	            currentPage++;
	            // 다음 페이지를 검색하도록 요청합니다
	            pagination.nextPage();
	        } else {
	            // 모든 페이지 검색이 완료되면 최종 결과를 콘솔에 출력합니다
	            console.log('--- 모든 페이지 검색 완료 ---');
	            console.log(allPlaces);
	            console.log(`최종 검색된 장소의 수: ${allPlaces.length}개`);
	        }

	    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
	        alert('검색 결과가 존재하지 않습니다.');
	        // 검색 결과가 없더라도 이전에 쌓인 결과가 있으면 출력
	        if (allPlaces.length > 0) {
	            console.log('--- 최종 검색 결과 ---');
	            console.log(allPlaces);
	        }
	    } else if (status === kakao.maps.services.Status.ERROR) {
	        alert('검색 결과 중 오류가 발생했습니다.');
	    }
		isRestaurant = false;
		return;
	}else{
		if (status === kakao.maps.services.Status.OK) {

	        // 정상적으로 검색이 완료됐으면
	        // 검색 목록과 마커를 표출합니다
	        displayPlaces(data);

	        // 페이지 번호를 표출합니다
	        displayPagination(pagination);

	    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {

	        alert('검색 결과가 존재하지 않습니다.');
	        return;

	    } else if (status === kakao.maps.services.Status.ERROR) {

	        alert('검색 결과 중 오류가 발생했습니다.');
	        return;

	    }
	}
	
    
}

// 검색 결과 목록과 마커를 표출하는 함수입니다
function displayPlaces(places) {

    var placeListEl = document.getElementById('placesList'), 
    menuEl = document.getElementById('trip-search-result'),
    fragment = document.createDocumentFragment(), 
    bounds = new kakao.maps.LatLngBounds();
    
    // 검색 결과 목록에 추가된 항목들을 제거합니다
    removeAllChildNods(placeListEl);

    // 지도에 표시되고 있는 마커를 제거합니다
    removeMarker();
    
    for ( var i=0; i<places.length; i++ ) {

        // 마커를 생성하고 지도에 표시합니다
        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
            marker = addMarker(placePosition, i), 
            itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
        // LatLngBounds 객체에 좌표를 추가합니다
        bounds.extend(placePosition);
        
        fragment.appendChild(itemEl);
    }

    // 검색결과 항목들을 검색결과 목록 Element에 추가합니다
    placeListEl.appendChild(fragment);
    menuEl.scrollTop = 0;

    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
    map.setBounds(bounds);
}

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {

    var el = document.createElement('li'),
    itemStr = '<div><span class="markerbg marker_' + (index+1) + '"></span>' +
                '<div class="info">' +
                '   <span class="sl-name">' + places.place_name + '</span>';

    if (places.road_address_name) {
        itemStr += '    <span>' + places.road_address_name + '</span>' +
                    '   <span class="jibun gray">' +  places.address_name  + '</span>';
    } else {
        itemStr += '    <span>' +  places.address_name  + '</span>'; 
    }
                 
      itemStr += '  <span class="tel">' + places.phone  + '</span>' +
                '</div></div><button class="sl-searched-add" onclick="insertToTimeline(this)">+</button>';  

    el.innerHTML = itemStr;
    el.className = 'item trip-searchedItem trip-loc-card align-items-center';

    return el;
}

// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, idx, title) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
        imgOptions =  {
            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
            marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage 
        });

    marker.setMap(map); // 지도 위에 마커를 표출합니다
    markers.push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
    for ( var i = 0; i < markers.length; i++ ) {
        markers[i].setMap(null);
    }   
    markers = [];
}

// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(pagination) {
    var paginationEl = document.getElementById('pagination'),
        fragment = document.createDocumentFragment(),
        i; 

    // 기존에 추가된 페이지번호를 삭제합니다
    while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild (paginationEl.lastChild);
    }

    for (i=1; i<=pagination.last; i++) {
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = i;

        if (i===pagination.current) {
            el.className = 'on';
        } else {
            el.onclick = (function(i) {
                return function() {
                    pagination.gotoPage(i);
                }
            })(i);
        }

        fragment.appendChild(el);
    }
    paginationEl.appendChild(fragment);
}

 // 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) { 
    while (el.hasChildNodes()) {
        el.removeChild (el.lastChild);
    }
}