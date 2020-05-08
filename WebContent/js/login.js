function handleLogin(loginFormSubmitEvent) {
    loginFormSubmitEvent.preventDefault();

    $.ajax({
		method: 'POST',
		url: 'api/login',
		data: $(loginForm).serialize(),
		dataType: 'json',
		success: handleLoginResult,
    });
}

function handleLoginResult(loginResult) {
    if(loginResult['status'] == 'success') {
        window.location.replace("index.html");
    } else {
        let alertMsg = loginResult['message'];
        let alertHTML = '<div class="alert alert-danger alert-dismissible fade show" id="loginAlert" role="alert">';
        alertHTML += '<button type="button" class="close" data-dismiss="alert" aria-label="Close">\n';
        alertHTML += '<span aria-hidden="true">&times;</span>\n';
        alertHTML += '<span class="sr-only">Close</span>\n';
        alertHTML += '</button>\n';
        alertHTML += '<i class="fa fa-info-circle small" aria-hidden="true"></i>\n';
        alertHTML += '<label class="small" for="">'+ alertMsg + '</label>\n';
        alertHTML += '\<div>';
        $('#loginAlert').html(alertHTML);
        return false;
    }
}

// Bind submit action of the form to a handler function
var loginForm = $('#loginForm');
loginForm.submit(handleLogin);