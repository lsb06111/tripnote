<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/WEB-INF/views/jspf/head.jspf" %> 
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="edu.example.tripnote.domain.board.BoardDetailDTO" %>

<style>
/* ... 기존 CSS 그대로 사용 ... */
</style>

</head>
<body class="portfolio-details-page">
<%@ include file="/WEB-INF/views/jspf/header.jspf" %> 

<main class="main">
<div class="page-title light-background">
  <div class="container">
    <h1>${board.title != null ? board.title : ''}</h1>
  </div>
</div>

<section id="portfolio-details" class="portfolio-details section" style="--subtle-border: none; padding-bottom:0px;">
<div class="container">
    <div class="project-hero" style="margin-bottom: 0;">
        <div class="project-meta-grid" style="padding-top:0">
            <div class="meta-column">
              <div class="meta-label">작성자</div>
              <div class="meta-value">${board.nickname}</div>
            </div>
            <div class="meta-column">
              <div class="meta-label">여행 지역</div>
              <div class="meta-value">${board.areaName}</div>
            </div>
            <div class="meta-column">
              <div class="meta-label">여행일정</div>
              <div class="meta-value">${board.startDate} ~ ${board.endDate}</div>
            </div>
            <div class="meta-column">
              <div class="meta-label">작성일</div>
              <div class="meta-value">${board.createdAt}</div>
            </div>
            <div class="meta-column">
              <div class="meta-value">
                <input type="checkbox" class="btn-check" id="btn1" autocomplete="off" />
                <label class="custom-toggle" for="btn1">팔로우 신청</label>
              </div>
            </div>
        </div>
    </div>

<section id="tabs" class="tabs section mt-5">
<div class="container">
    <div class="tabs-wrapper">
        <div class="tabs-header">
            <ul class="nav nav-tabs">
            <%
                List<List<BoardDetailDTO>> contents = (List<List<BoardDetailDTO>>) request.getAttribute("contents");
                int totalTripSize = (contents != null) ? contents.size() : 0;

                for(int i=0; i<totalTripSize; i++){ 
            %>
                <li class="nav-item">
                    <a class="nav-link <%= i==0? "active show" : "" %>" data-bs-toggle="tab" data-bs-target="#tabs-tab-<%= (i+1) %>">
                      <div class="tab-content-preview">
                        <span class="tab-number">0<%= (i+1) %></span>
                        <div class="tab-text">
                          <h6 style="margin-bottom:0"><%= (i+1) + "일차" %></h6>
                        </div>
                      </div>
                    </a>
                </li>
            <% } %>
            </ul>
        </div>

        <div class="tab-content">
        <%
            int totalReviewNumber = 3;
            List<Integer> locCntList = new ArrayList<>();
            List<List<String>> images = new ArrayList<>();
            HashMap<String, String> tagContents = new HashMap<>();
            tagContents.put("leaf-fill","멋있어요,예뻐요,다양하게 볼게 많아요");
            tagContents.put("house-door","청결해요,방음이 잘 돼요,가격이 저럼해요");
            tagContents.put("fork-knife","식사가 맛있어요,가격이 저렴해요,매장이 청결해요");
            String[] nodeIcons = {"house-door", "leaf-fill", "fork-knife", "leaf-fill", "leaf-fill"};

            // 날짜별 방문 관광지 수와 이미지 저장
            for(int ii=0; ii < totalTripSize; ii++){
                List<BoardDetailDTO> temp = contents.get(ii);
                int prevOrder = -1;
                int locCnt = 0;
                for(BoardDetailDTO dto : temp){
                    int currentOrder = dto.getTourOrder();
                    if(prevOrder != currentOrder){
                        List<String> locImgs = new ArrayList<>();
                        locImgs.add(dto.getImgSrc());
                        images.add(locImgs);
                        locCnt++;
                    } else {
                        if(!images.isEmpty()){
                            images.get(images.size()-1).add(dto.getImgSrc());
                        }
                    }
                    prevOrder = currentOrder;
                }
                locCntList.add(locCnt);
            }

            for(int ii=0; ii < totalTripSize; ii++){
                List<BoardDetailDTO> boardDetailsPerDay = contents.get(ii);
        %>
        <div class="tab-pane fade <%= ii==0 ? "active show" : "" %>" id="tabs-tab-<%= (ii+1) %>">
            <!-- routeBar 및 spotPanes 반복 생략 가능, 기존 코드 그대로 유지 -->
        </div>
        <% } %>
        </div>
    </div>
</div>
</section>

<script>
const totalTripSize = <%= totalTripSize %>;
const totalReviewNumber = <%= totalReviewNumber %>;
let sliderImageIndices = Array.from({ length: totalTripSize }, () => new Array(5).fill(0));

function sliderNextImg(tIndex, nIndex){
    if(++sliderImageIndices[tIndex][nIndex] === totalReviewNumber)
        sliderImageIndices[tIndex][nIndex] = 0;
    setReviewContent(tIndex, nIndex)
}
function sliderPrevImg(tIndex, nIndex){
    if(--sliderImageIndices[tIndex][nIndex] === -1)
        sliderImageIndices[tIndex][nIndex] = totalReviewNumber-1;
    setReviewContent(tIndex, nIndex)
}
function setReviewContent(tIndex, nIndex){
    const reviewDoms = document.querySelectorAll('#tabs-tab-' + (tIndex+1) + ' .review-contents-filter.review-content-'+nIndex);
    const reviewI = sliderImageIndices[tIndex][nIndex];
    reviewDoms.forEach((el, i) => el.style.display = (i === reviewI ? 'block' : 'none'));
}

document.addEventListener('DOMContentLoaded', function() {
    const heartIcon = document.getElementById("heartIcon");
    if(heartIcon){
        heartIcon.addEventListener("click", () => {
            heartIcon.classList.toggle("bi-heart");
            heartIcon.classList.toggle("bi-heart-fill");
        });
    }

    const replyContainer = document.getElementById('replyContainer');
    const loadMoreBtn = document.getElementById('loadMoreRepliesBtn');
    if(replyContainer) replyContainer.classList.add('collapsed');

    if(loadMoreBtn){
        loadMoreBtn.addEventListener('click', () => {
            replyContainer.classList.toggle('collapsed');
            loadMoreBtn.textContent = replyContainer.classList.contains('collapsed') ? '댓글 더보기' : '댓글 접기';
        });
    }
});

function showReReplyDiv(replyIndex){
    const el = document.getElementById('re_reply-'+replyIndex);
    if(el) el.style.display='block';
}
function disableReReplyDiv(replyIndex){
    const el = document.getElementById('re_reply-'+replyIndex);
    if(el) el.style.display='none';
}
</script>

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %> 
</body>
</html>
