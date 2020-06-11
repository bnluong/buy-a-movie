function handleRegister(event) {
    event.preventDefault();
    if($(registerForm).valid()) {
        $.ajax({
            method: 'POST',
            url: 'api/register',
            data: $(registerForm).serialize(),
            dataType: 'json',
            success: function(response) {
                if(response['status'] == 'success') {
                    alert(response['message']);
                    window.location = 'index.html'
                } else {
                    alert(response['message']);
                    window.location = 'register.html'
                }
            }
        });
    }
}

$("#registerForm").validate({
    focusInvalid: true,
    rules: {
        firstName: {
            required: true
        },
        lastName: {
            required: true
        },
        email: {
            required: true,
            email: true
        },
        password: {
            required: true,
        },
        address: {
            required: true,
            minlength: 3
        },
        city: {
            required: true
        },
        state: {
            required: true
        },
        zipcode: {
            required: true,
            zipcodeUS: true
        },
        phoneNumber: {
            required: true,
            phoneUS: true
        },
        cardNumber: {
            required: true,
            digits: true,
            maxlength: 19
        },
        expDate: {
            required: true,
            date: true
        },
        securityCode: {
            required: true,
            minlength: 3,
            maxlength: 3
        },
    },
    messages: {
        address: 'Address is too short.'
    },
});

// Bind submit action of the form to a handler function
var registerForm = $('#registerForm');
registerForm.submit(handleRegister);