var container = document.getElementById('rcmd-map'); //지도를 담을 영역의 DOM 레퍼런스
var options = { //지도를 생성할 때 필요한 기본 옵션
	center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
	level: 3 //지도의 레벨(확대, 축소 정도)
};
var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

// 주소-좌표 변환 객체를 생성합니다
var geocoder = new kakao.maps.services.Geocoder();
var markers = [];
var previousMarker;
function drawMarkerWithXY(mapx, mapy){
	
	if(previousMarker){
		previousMarker.setMap(null);
	}
	
	var coords = new kakao.maps.LatLng(mapy, mapx);
    
    // 2. 결과 좌표로 지도에 마커를 표시합니다
    var marker = new kakao.maps.Marker({
        map: map,
        position: coords
    });

    // 3. 생성된 마커를 마커 배열에 추가합니다
    previousMarker = marker;
    //markers.push(marker);

    // 4. 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
    map.setCenter(coords);
}
var transportSelected = '';
var fixMarkers = [];

function setTransport(transportType){
	transportSelected = transportType;
	if(transportType != 'car')
		drawSimplePolyline('walk');
	else
		drawRoute(transportSelected);
}

function drawFixMarker(mapx, mapy) {
	var coords = new kakao.maps.LatLng(mapy, mapx);

	//1. 빨간색 마커 이미지 생성
	var imageSrc = '/tripnote/resources/assets/img/trip/fixMarker.png', // 마커 이미지 url
		imageSize = new kakao.maps.Size(42, 45), // 마커 이미지의 크기
		imageOption = { offset: new kakao.maps.Point(21, 42) }; // 마커의 좌표와 일치시킬 이미지 안에서의 좌표

	var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

	// 2. 결과 좌표로 지도에 마커를 표시합니다 (수정된 부분)
	var fixMarker = new kakao.maps.Marker({
		map: map,
		position: coords,
		image: markerImage //  2. 생성한 마커 이미지를 설정합니다.
	});

	fixMarkers.push(fixMarker);

	map.setCenter(coords);
	if(transportSelected == 'car')
		drawRoute(transportSelected);
	else if(transportSelected != '')
		drawSimplePolyline('walk');
			
}
var polyline;
function drawRoute(transportType) {
	// 출발지, 도착지, 경유지 설정
	if (fixMarkers.length < 2) {
		return;
	}

	const origin = fixMarkers[0];
	const destination = fixMarkers[fixMarkers.length - 1];
	document.querySelectorAll('.route-time').forEach(function(element) {
	    element.remove();
	});
	let waypoints = [];
	if (fixMarkers.length > 2) {
		// 중간 마커들을 경유지로 추가
		for (let i = 1; i < fixMarkers.length - 1; i++) {
			waypoints.push(fixMarkers[i]);
		}
	}
	switch (transportType) {
		case 'car':
			callDirectionsAPI(origin, destination, waypoints);
			break;
		case 'walk':
			callDirectionsAPI(origin, destination, waypoints);
			break;
		case 'transit':
			callDirectionsAPI(origin, destination, waypoints);
			break;
		default:
			console.error("지원하지 않는 transportType 입니다:", transportType);
	}
}


////
const colorPalette = [
	"#FF0000", "#FF7F00", "#FFFF00", "#00FF00", "#00FFFF",
	"#0000FF", "#8B00FF", "#FF00FF", "#FF1493", "#4B0082", "#000000"
];

// 2. 생성된 폴리라인들을 관리할 배열입니다.
var routePolylines = [];
var routeTimeOverlays = [];

function callDirectionsAPI(origin, destination, waypoints) {
	const REST_API_KEY = '40030219edd182e4ad2c7cf1dd6edb07'; // 발급받은 REST API 키를 여기에 입력하세요.

	let url = 'https://apis-navi.kakaomobility.com/v1/directions';
	let params = new URLSearchParams({
		origin: `${origin.getPosition().getLng()},${origin.getPosition().getLat()}`,
		destination: `${destination.getPosition().getLng()},${destination.getPosition().getLat()}`
	});

	if (waypoints.length > 0) {
		let waypointsStr = waypoints.map(wp => `${wp.getPosition().getLng()},${wp.getPosition().getLat()}`).join('|');
		params.append('waypoints', waypointsStr);
	}
	
	const finalUrl = `${url}?${params.toString()}`;

	$.ajax({
		type: "GET",
		url: finalUrl,
		headers: {
			"Authorization": `KakaoAK ${REST_API_KEY}`
		},
		success: function (data) {
			// ✅ 3. 이전에 그려진 폴리라인들이 있다면 모두 제거합니다.
			routePolylines.forEach(line => line.setMap(null));
			routePolylines = [];
			
			document.querySelectorAll('.route-time').forEach(function(element) {
			    element.remove();
			});

			const sections = data.routes[0].sections;

			// ✅ 4. API 응답에 포함된 각 section에 대해 별도의 폴리라인을 생성합니다.
			sections.forEach((section, index) => {
				const linePath = [];
				section.roads.forEach(road => {
					for (let i = 0; i < road.vertexes.length; i += 2) {
						linePath.push(new kakao.maps.LatLng(road.vertexes[i+1], road.vertexes[i]));
					}
				});

				// ✅ 5. 각 section에 대한 폴리라인을 생성하고 색상을 지정합니다.
				let polyline = new kakao.maps.Polyline({
					path: linePath,
					strokeWeight: 7,
					// colorPalette 배열에서 순서대로 색상을 가져옵니다.
					strokeColor: colorPalette[index % colorPalette.length],
					strokeOpacity: 0.8,
					strokeStyle: 'solid',
					endArrow: true
				});

				polyline.setMap(map);          // 지도에 폴리라인을 표시합니다.
				routePolylines.push(polyline); // 관리 배열에 추가합니다.
				
				//////////////
				const duration = section.duration;

				// 2. 경로의 중간 지점 좌표 찾기
				const midPointIndex = Math.floor(linePath.length / 2);
				const midPoint = linePath[midPointIndex];

				// 3. 커스텀 오버레이에 표시할 내용 생성
				let content = `<div class="route-time" style="color:${colorPalette[index % colorPalette.length]}">${formatDuration(duration)}</div>`;
				
				// 4. 커스텀 오버레이 생성 및 지도에 표시
				let timeOverlay = new kakao.maps.CustomOverlay({
					content: content,
					position: midPoint,
					yAnchor: 1
				});
				
				timeOverlay.setMap(map);
				routeTimeOverlays.push(timeOverlay);
			});
		},
		error: function (request, status, error) {
			console.error("길찾기 API 오류", request, status, error);
			alert("길찾기 서비스에 오류가 발생했습니다.");
		}
	});
}

function formatDuration(seconds) {
    if (seconds < 60) {
        return `${seconds}초`;
    }
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    
    if (minutes < 60) {
        if (remainingSeconds === 0) {
            return `약 ${minutes}분`;
        }
        return `${minutes}분 ${remainingSeconds}초`;
    }
    
    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;
    
    return `${hours}시간 ${remainingMinutes}분`;
}

function drawSimplePolyline(type) {
	let linePath = fixMarkers.map(marker => marker.getPosition());

	let styleOptions = {};
	if (type === 'walk') {
		// 도보: 파란색 점선
		styleOptions = {
			strokeColor: '#0000FF',
			strokeStyle: 'dash'
		};
	} else { // transit
		// 대중교통: 녹색 실선
		styleOptions = {
			strokeColor: '#28A745',
			strokeStyle: 'solid'
		};
	}

	routePolylines.forEach(line => line.setMap(null));
	routePolylines = [];
	document.querySelectorAll('.route-time').forEach(function(element) {
	    element.remove();
	});
	
	let polyline = new kakao.maps.Polyline({
		path: linePath,
		strokeWeight: 6,
		strokeOpacity: 0.8,
		...styleOptions // ES6 spread syntax로 스타일 옵션 적용
	});

	polyline.setMap(map);
	routePolylines.push(polyline); // 관리 배열에 추가
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
//장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
	
	if (status === kakao.maps.services.Status.OK) {
        displayPlaces(data);
        displayPagination(pagination);
    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        alert('검색 결과가 존재하지 않습니다.');
        return;
    } else if (status === kakao.maps.services.Status.ERROR) {
        alert('검색 결과 중 오류가 발생했습니다.');
        return;
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