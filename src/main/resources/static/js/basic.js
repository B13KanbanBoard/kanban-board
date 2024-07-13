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
            if (isAdmin) {
                $('#admin').text(true);
                showProduct();
            } else {
                showProduct();
            }

            // 로그인한 유저의 폴더
            /*$.ajax({
                type: 'GET',
                url: `/api/user-folder`,
                error(error) {
                    logout();
                }
            }).done(function (fragment) {
                $('#fragment').replaceWith(fragment);
            });
            */
        })
        .fail(function (jqXHR, textStatus) {
            logout();
        });


    $('#see-area').show();
    $('#search-area').hide();
})

function addHTML(itemDto) {
    /**
     * class="search-itemDto" 인 녀석에서
     * image, title, lprice, addProduct 활용하기
     * 참고) onclick='addProduct(${JSON.stringify(itemDto)})'
     */
    return `<div class="search-itemDto">
        <div class="search-itemDto-left">
            <img src="${itemDto.image}" alt="">
        </div>
        <div class="search-itemDto-center">
            <div>${itemDto.title}</div>
            <div class="price">
                ${numberWithCommas(itemDto.lprice)}
                <span class="unit">원</span>
            </div>
        </div>
        <div class="search-itemDto-right">
            <img src="../images/icon-save.png" alt="" onclick='addProduct(${JSON.stringify(itemDto)})'>
        </div>
    </div>`
}


function logout() {
    // 토큰 삭제
    Cookies.remove('Authorization', {path: '/'});
    alert("토큰 삭제");
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