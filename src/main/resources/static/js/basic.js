const host = 'http://' + window.location.host;
let targetId;
let folderTargetId;

$(document).ready(function () {
    const auth = getToken();

    if (auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', auth);
        });
    } else {
        window.location.href = host + '/api/members/login-page';
        return;
    }

    $.ajax({
        type: 'GET',
        url: `/api/members`,
        contentType: 'application/json',
    })
        .done(function (res, status, xhr) {
            const username = res.data.email;
            const isAdmin = !!res.admin;

            if (!username) {
                alert("여기");
                window.location.href = '/api/members/login-page';
                return;
            }

            $('#username').text(username);
            showBoard();
            /*
            if (isAdmin) {
                $('#admin').text(true);
                showProduct();
            } else {
                showProduct();
            }
            */

        })
        .fail(function (jqXHR, textStatus) {
            logout();
        });

})

function showBoard() {

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/boards?pageNum=1&size=5",
        data: {},
        beforeSend: function () {
            $('#board-container').html('보드 불러오는 중...');
            $('#spinner').show();
        },
        success: function (response) {
            /* Pagination 구현 */
            let totalPage = response["data"]["totalPage"];
            if(totalPage===0)
                totalPage=1;
            let currentPage = response["data"]["currentPage"];

            window.pagObj = $('#pagination').twbsPagination({
                totalPages: [[totalPage]], // 전체 페이지
                startPage: parseInt(currentPage), // 시작(현재) 페이지
                visiblePages: 10, // 최대로 보여줄 페이지
                prev: "‹", // Previous Button Label
                next: "›", // Next Button Label
                first: '«', // First Button Label
                last: '»', // Last Button Label
                onPageClick: function (event, page) { // Page Click event
                    getPaginationItem(page);
                    console.info("current page : " + page);
                }
            }).on('page', function (event, page) {
                //alert("tt");
            });
        },
        error(error) {
            alert("error!!");
            $('#board-container').empty();
            $('#board-container').append(
                `<div>
                        <span style="color:red;">보드를 불러오는 과정에서 오류가 발생했습니다.</span>
                </div>`);
        },
    }).done(function (fragment) {
        //alert("test");
        //$('#fragment').replaceWith(fragment);
    });

}

function getPaginationItem(page) {
    $('#board-container').empty();
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/boards?pageNum="+page+"&size=5",
        data: {},
        beforeSend: function () {
            // $('#board-container').html('보드 불러오는 중...');
        },
        success: function (response) {
            $('#spinner').hide();

            let board_list = response["data"]["boardResponseList"];
            // let mise_list = response["RealtimeCityAir"]["row"];
            for (let i = 0; i < board_list.length; i++) {
                let board = board_list[i];

                let tempHtml = addBoardItem(board);
                $('#board-container').append(tempHtml);
            }
            if (board_list.length === 0) {
                $('#board-container').append(
                    `<div>
                        <span style="color:red;">보유한 보드가 없습니다.</span>
                    </div>`);
            }
        },
        error(error) {
            alert("page error!!");
            $('#board-container').append(
                `<div>
                        <span style="color:red;">보드를 불러오는 과정에서 오류가 발생했습니다.</span>
                </div>`);
        },
    }).done(function (fragment) {
        //alert("test");
        //$('#fragment').replaceWith(fragment);
    });
}

const boardNameMap = new Map();
const boardContentMap = new Map();

function addBoardItem(board) {
    let board_id = board["boardId"];
    let board_name = board["boardName"];
    let board_content = board["content"];
    boardNameMap.set(board_id, board_name);
    boardContentMap.set(board_id, board_content);
    //console.log(board_id, board_name,board_content);
    return `<div class="row mt-1 mb-1">
                <div class="col-10 mt-2 mb-2">
                    <a href="api/boards/${board_id}" class="list-group-item list-group-item-action">
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <div class="ms-2 me-auto">
                                <div class="fw-bold">${board_name}</div>
                                ${board_content}
                            </div>
                            <span class="badge bg-primary rounded-pill">${board_id}</span>
                        </li>
                    </a>
                </div>
                <div class="col-2 row align-self-center">
                    <div class="mb-1">
                        <button type="button" class="col btn btn-outline-primary" onclick="clickUpdateBoard(${board_id})">수정</button>
                    </div>
                    <div class="mt-1">
                        <button type="button" class="col btn btn-outline-danger" onclick="deleteBoard(${board_id})">삭제</button>
                    </div>
                </div>
                
            </div>
    `;
}

function addBoardModal() {
    $('#modalLabel').text('보드 추가하기');
    $('#boardName').val('Board Name');
    $('#content').val('테스트');

    $('#addBtn').show();
    $('#updateBtn').hide();
}

function addBoard() {
    let boardName = $('#boardName').val();
    let content = $('#content').val();

    $.ajax({
        type: "POST",
        url: `/api/boards`,
        contentType: "application/json",
        data: JSON.stringify({boardName: boardName, content: content}),
    })
        .done(function (res, status, xhr) {
            alert("보드 생성 성공!!");
            document.location.reload();
        })
        .fail(function (jqXHR, textStatus) {
            let response = JSON.parse(jqXHR.responseText);
            alert("보드 생성 실패!! : " + response.message);
        });
}

function deleteBoard(boardId) {
    if(!confirm(boardNameMap.get(boardId) + " 보드를 삭제 하시겠습니까?")){
        alert("취소 되었습니다.");
    }else{
        $.ajax({
            type: "DELETE",
            url: `/api/boards/`+boardId,
            contentType: "application/json",
        })
            .done(function (res, status, xhr) {
                alert("보드 삭제 성공!!");
                document.location.reload();
            })
            .fail(function (jqXHR, textStatus) {
                let response = JSON.parse(jqXHR.responseText);
                alert("보드 삭제 실패!! : " + response.message);
            });
    }
}

let updateBoardId=0;
function clickUpdateBoard(boardId) {
    updateBoardId=boardId;

    $('#addBtn').hide();
    $('#updateBtn').show();

    $('#modalLabel').text('보드 수정하기');
    $('#boardName').val(boardNameMap.get(boardId));
    $('#content').val(boardContentMap.get(boardId));

    var myModal = new bootstrap.Modal(document.getElementById('addBoardModal'));
    myModal.show();
}

function updateBoard() {
    let updateBoardName = $('#boardName').val();
    let updateContent = $('#content').val();

    $.ajax({
        type: "PATCH",
        url: `/api/boards/`+updateBoardId,
        contentType: "application/json",
        data: JSON.stringify({boardName: updateBoardName, content: updateContent}),
    })
        .done(function (res, status, xhr) {
            alert("보드 수정 완료!!");
            document.location.reload();
        })
        .fail(function (jqXHR, textStatus) {
            let response = JSON.parse(jqXHR.responseText);
            alert("보드 수정 실패!! : " + response.message);
        });
}


function logout() {
    // 토큰 삭제
    Cookies.remove('Authorization', {path: '/'});
    alert("로그아웃 하셨습니다.");
    window.location.href = host + '/api/members/login-page';
}

function getToken() {
    let auth = Cookies.get('Authorization');

    if(auth === undefined) {
        return '';
    }

    // kakao 로그인 사용한 경우 Bearer 추가
    if(auth.indexOf('Bearer') === -1 && auth !== ''){
        auth = 'Bearer ' + auth;
    }

    return auth;
}