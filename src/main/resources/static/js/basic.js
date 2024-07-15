const host = 'http://' + window.location.host;

$(document).ready(function () {
    const auth = getToken();

    if (auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', auth);
        });
    } else {
        window.location.href = host + '/members/login-page';
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
                alert("토큰 에러 발생!!");
                window.location.href = '/members/login-page';
                return;
            }
            $('#username').text(username);
        })
        .fail(function (jqXHR, textStatus) {
            logout();
        });
})

function logout() {
    // 토큰 삭제
    Cookies.remove('Authorization', {path: '/'});
    alert("로그아웃 하셨습니다.");
    window.location.href = host + '/members/login-page';
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