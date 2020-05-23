function handleSession(sessionResult) {
    // <a class="nav-link navItem" href="login.html">Hello<br>Login?</br></a>
    // <a class="nav-link navItem" href="login.html">Hello, Bao<br>Logout?</br></a>
    var userName = sessionResult['user_name'];
    if(userName != null) {
        var userSessionHTML = '<a class="nav-link navItem" href="login.html">Hello, ' + userName + '<br>Logout?</br></a>';
        $('#userSession').html(userSessionHTML);
    } else {
        var userSessionHTML = '<a class="nav-link navItem" href="login.html">Hello<br>Login?</br></a>\n';
        $('#userSession').html(userSessionHTML);
    }
}

function handleSearch(searchFormSubmitEvent) {
    searchFormSubmitEvent.preventDefault();
    var searchForm = $("#navBarSearch").serialize();
    //var browsingHTML = 'browsing.html?' + 'search=' + $('input[name="search"]').val() + '&sortBy=title&order=asc&numResults=10&offset=0';
    //window.location.replace(browsingHTML);
}

$.ajax({
    method: 'GET',
    url: 'api/session',
    dataType: 'json',
    success: handleSession,
});


$('#navBarSearch').submit(handleSearch);