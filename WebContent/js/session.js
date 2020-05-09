function handleSession(sessionResult) {
    // <a class="nav-link navItem" href="login.html">Hello<br>Login?</br></a>
	// <a class="nav-link navItem" href="login.html">Hello, Bao<br>Logout?</br></a>
	var userName = sessionResult['user_name'];
	if (userName != null) {
		var userSessionHTML = '<a class="nav-link navItem" href="login.html">Hello, ' + userName + '<br>Logout?</br></a>';
		$('#userSession').html(userSessionHTML);
	} else {
        var userSessionHTML = '<a class="nav-link navItem" href="login.html">Hello<br>Login?</br></a>\n';
        $('#userSession').html(userSessionHTML);
    }
}

$.ajax({
	method: 'GET',
	url: 'api/session',
	dataType: 'json',
	success: handleSession,
});