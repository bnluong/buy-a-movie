function handleSession(sessionResult) {
    // <a class="nav-link navItem" href="login.html">Hello<br>Login?</br></a>
    // <a class="nav-link navItem" href="login.html">Hello, Bao<br>Logout?</br></a>
    var userName = sessionResult['user_name'];
    if(userName != null) {
        var userSessionHTML = '<a class="nav-link navItem" href="#">Hello, ' + userName + '<br>Logout?</br></a>';
        $('#userSession').html(userSessionHTML);
        
        $("#userSession").on("click", function(event) {
        	event.preventDefault();
        	
        	$.ajax({
        	    method: 'POST',
        	    url: 'api/logout',
        	}).done(function() {
        		alert('Logging out');
        		window.location.replace("index.html");        	
        	});
        });
    } else {
        var userSessionHTML = '<a class="nav-link navItem" href="login.html">Hello<br>Login?</br></a>\n';
        $('#userSession').html(userSessionHTML);
    }
}

function handleNavBarSearch(searchFormSubmitEvent) {
    searchFormSubmitEvent.preventDefault();
    
    var searchForm = $("#navBarSearch").serialize();
    var browsingHTML = 'browsing.html?' + searchForm + '&sortBy=title&order=asc&numResults=10&offset=0';
    window.location.replace(browsingHTML);
}

$.ajax({
    method: 'GET',
    url: 'api/session',
    dataType: 'json',
    success: handleSession,
});


$('#navBarSearch').submit(handleNavBarSearch);