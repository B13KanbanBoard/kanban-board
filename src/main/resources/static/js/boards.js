let boardId;
$(document).ready(function () {
    // 숨겨진 요소에서 boardId 값을 가져오기
    boardId = $('#boardId').val();
    //alert("현재 접속한 보드 ID : " + boardId);
    //console.log(boardId);
    showCategory(boardId);
})


function showCategory(boardId) {

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/boards/"+ boardId +"/categories",
        data: {},
        beforeSend: function () {
            $('#category-container').html('카테고리 불러오는 중...');
            $('#spinner').show();
        },
        success: function (response) {
            $('#category-container').empty();
            $('#spinner').hide();

            let category_list = response["data"];
            for (let i = 0; i < category_list.length; i++) {
                let category = category_list[i];

                let tempHtml = addCategoryItem(category);
                $('#category-container').append(tempHtml);
                showCard(categoryIdList[i]);
            }
            if (category_list.length === 0) {
                $('#category-container').append(
                    `<div>
                        <span style="color:red;">보유한 카테고리가 없습니다.</span>
                    </div>`);
            }
        },
        error(error) {
            alert("error!!");
            $('#category-container').empty();
            $('#category-container').append(
                `<div>
                        <span style="color:red;">카테고리를 불러오는 과정에서 오류가 발생했습니다.</span>
                </div>`);
        },
    }).done(function (fragment) {
    });
}

let categoryIdList =[];
const categoryNameMap = new Map();
const categoryOrderMap = new Map();

function addCategoryItem(category) {
    let category_id = category["id"];
    let category_name = category["name"];
    let category_content = category["orderNumber"];
    categoryIdList.push(category_id);
    categoryNameMap.set(category_id, category_name);
    categoryOrderMap.set(category_id, category_content);
    //console.log(category_id, category_name,category_content);
    return `<div class="col">
                <div class="card h-100">
                    <h3 class = "card-title">${category_name}</h3>
                    <div class = "card-body" id="category-container-${category_id}">
<!--                        <h5 class="card-title">category title 1</h5>-->
                        <button class="open">card title 1</button>
                        <button class="open">card title 2</button>
                        <button class="open">card title 3</button>
                    </div>
                </div>
            </div>
    `;
}

function showCard(categoryId) {
    let container = '#category-container-'+categoryId;

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/categories/"+categoryId+"/cards",
        data: {},
        beforeSend: function () {
            console.log('#category-container-${categoryId}');
            $(container).html('카드 불러오는 중...');
        },
        success: function (response) {
            $(container).empty();

            let card_list = response["data"];
            for (let i = 0; i < card_list.length; i++) {
                let card = card_list[i];

                let tempHtml = addCardItem(card);
                $(container).append(tempHtml);
            }
            if (card_list.length === 0) {
                $(container).append(
                    `<div>
                        <span style="color:red;">보유한 카드가 없습니다.</span>
                    </div>`);
            }
            cardCreateEnd();

            let cardAddBtnHtml = `
                <div class="d-flex justify-content-center align-items-center my-4">
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addCardModal"
                            data-bs-whatever="@mdo" onClick="addCardModal(${categoryId})">카드 추가
                    </button>
                </div>`;
            $(container).append(cardAddBtnHtml);
        },
        error(error) {
            alert("error!!");
            $(container).empty();
            $(container).append(
                `<div>
                        <span style="color:red;">카테고리를 불러오는 과정에서 오류가 발생했습니다.</span>
                </div>`);
        },
    }).done(function (fragment) {
    });
}

const cardTitleMap = new Map();
const cardAssigneeMap = new Map();
const cardDescriptionMap = new Map();
const cardStartDateMap = new Map();
const cardEndDateMap = new Map();
const cardOrderNumberMap = new Map();

function addCardItem(card) {
    let card_id = card["cardId"];
    let card_title = card["title"];
    cardTitleMap.set(card_id, card["title"]);
    cardAssigneeMap.set(card_id, card["assignee"]);
    cardDescriptionMap.set(card_id, card["description"]);
    cardStartDateMap.set(card_id, card["startDate"]);
    cardEndDateMap.set(card_id, card["endDate"]);
    cardOrderNumberMap.set(card_id, card["orderNumber"]);

    return `<button class="open" id="card-${card_id}">${card_title}</button>
    `;
}

// 카드 생성 완료 후 이벤트 추가
function cardCreateEnd() {
    const openButtons = document.querySelectorAll('.open');
    const closeButton = document.querySelector('.close');

    const modal = document.querySelector('#modal'); // 모달 요소
    const modalCardTitle = document.querySelector('#modal-card-title');
    const modalCardAssignee = document.querySelector('#modal-card-assignee');
    const modalCardStartDate = document.querySelector('#modal-card-startDate');
    const modalCardEndDate = document.querySelector('#modal-card-endDate');
    const modalCardDescription = document.querySelector('#modal-card-description');

    openButtons.forEach(openButton => {
        openButton.addEventListener('click', () => {
            const cardId = openButton.id.split('-')[1]; // card-1 -> 1
            modalCardTitle.value = cardTitleMap.get(parseInt(cardId));
            modalCardAssignee.value = cardAssigneeMap.get(parseInt(cardId));
            modalCardStartDate.value = cardStartDateMap.get(parseInt(cardId));
            modalCardEndDate.value = cardEndDateMap.get(parseInt(cardId));
            modalCardDescription.value = cardDescriptionMap.get(parseInt(cardId));

            modal.style.display = 'flex';
            openButton.style.backgroundColor = '#bde6ff';
        })
    })

    closeButton.addEventListener('click', () => {
        modal.style.display = 'none';
        openButtons.forEach(openButton => {
            openButton.style.backgroundColor = 'transparent';
        })
    })
}

let lastSelectCategoryId;
function addCardModal(categoryId) {
    //$('#modalLabel').text('보드 추가하기');
    $('#modalCardName').val('과제 제출하기');
    $('#modalCardAssignee').val('강준모');
    $('#modalCardDescription').val('24.07.16일 KanbanBoard');

    lastSelectCategoryId=categoryId;
}

function addCard() {
    let title = $('#modalCardName').val();
    let assignee = $('#modalCardAssignee').val();
    let description = $('#modalCardDescription').val();

    $.ajax({
        type: "POST",
        url: `/api/categories/`+lastSelectCategoryId+`/cards`,
        contentType: "application/json",
        data: JSON.stringify({title: title, assignee: assignee, description: description}),
    })
        .done(function (res, status, xhr) {
            alert("카드 생성 성공!!");
            document.location.reload();
        })
        .fail(function (jqXHR, textStatus) {
            let response = JSON.parse(jqXHR.responseText);
            alert("카드 생성 실패!! : " + response.message);
        });
}

function addCategoryModal() {
    //$('#modalLabel').text('보드 추가하기');
    $('#modalCategoryName').val('상태 이름 (시작 전, 완료)');
}

function addCategory() {
    let name = $('#modalCategoryName').val();

    $.ajax({
        type: "POST",
        url: `/api/boards/`+boardId+`/categories`,
        contentType: "application/json",
        data: JSON.stringify({name: name}),
    })
        .done(function (res, status, xhr) {
            alert("카테고리 생성 성공!!");
            document.location.reload();
        })
        .fail(function (jqXHR, textStatus) {
            let response = JSON.parse(jqXHR.responseText);
            alert("카테고리 생성 실패!! : " + response.message);
        });
}