function getCurrentUser() {
    $.ajax({
        type: 'GET',
        async: false,
        url: 'api/session',
        success: function(responseData) {
            if(responseData['user_name'] == null) {
                alert('Please login before accessing to cart');
                window.location.replace("login.html");
            } else
                user = responseData['user_email'];
        }
    });
}

function displayCart() {
    $.ajax({
        type: "POST",
        url: "api/cart/retrieve",
        data: 'user=' + user,
        success: function(responseData) {
            var subTotalFloat = 0.0;
            var totalItemsInt = 0;

            for(let i = 0; i < responseData.length; i++) {
                let cartID = responseData[i]['cart_id'];
                let movieID = responseData[i]['movie_id'];
                let movieTitle = responseData[i]['movie_title'] + ' (' + responseData[i]['movie_year'] + ')';
                let movieQuantity = responseData[i]['movie_quantity'];
                let moviePrice = responseData[i]['movie_price'];

                let moviePoster = responseData[i]['movie_poster'];
                if(moviePoster == '') {
                    moviePoster = 'img/empty-poster.png';
                }

                subTotalFloat += Number(moviePrice) * Number(movieQuantity);
                totalItemsInt += Number(movieQuantity);

                $('#cartContent').append(
                    $('<div>', { class: 'form-row', id: cartID }).append(
                        $('<div>', { class: 'col-sm-2 col-3' }).append(
                            $('<img>', { src: moviePoster, alt: responseData[i]['movie_title'] + ' Poster' })
                        ),
                        $('<div>', { class: 'col-6 mx-auto' }).append(
                            $('<div>', { class: 'row mx-auto' }).append(
                                $('<p>', { class: 'h5' }).append(
                                    $('<a>', { href: 'movie.html?id=' + movieID }).text(movieTitle)
                                )
                            ),
                            $('<p>', { class: 'h6', name: 'unitPrice' }).text('$' + moviePrice),
                            $('<div>', { class: 'form-group my-3' }).append(
                                $('<div>', { class: 'form-group' }).append(
                                    $('<label>', { class: 'small' }).text('Quantity'),
                                    $('<input>', { class: 'form-control cartItemQuantity', type: 'number', name: 'quantity', disabled: ''}).val(movieQuantity)
                                )
                            ),
                            $('<p>', {class: 'small'}).text('Free Shipping')
                        )
                    ),
                    $('<hr>', { class: 'my-3' })
                );
            }

            let subTotalStr = 'Subtotal ' + '(' + totalItemsInt + ' items): ' + '$' + subTotalFloat;
        }
    });
}

function handleCheckout(event) {
    event.preventDefault();
    if($("#shippingForm").valid()) {
    	var address = $('#shippingForm').serializeArray();
    	console.log(address)
    }
//    if(!$("#shippingForm").valid() || !$("#creditCardForm").valid()) {
//    	return false;
//    } else {
//    	alert("Thank you for your purchase. Your order has been submitted.")
//    }
//    console.log($('#shippingForm').serialize());
//    console.log($('#creditCardForm').serialize());
}

$("#shippingForm").validate({
	focusInvalid: true,
    rules: {
    	firstName: {
    		required: true
    	},
    	lastName: {
    		required: true
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
    },
    messages: {
    	address: 'Address is too short.'
    }
});

$("#creditCardForm").validate({
	focusInvalid: true,
    rules: {
    	cardNumber: {
    		required: true,
    		digits: true,
    		maxlength: 19
    	},
    	expDate: {
    		required: true,
    		ccExpDate: true
    	},
    	securityCode: {
    		required: true,
    		minlength: 3,
    		maxlength: 3
    	},
    },
});

$.validator.addMethod("ccExpDate", function( value ) {
	var date = value.split('/');
	if(value.length != 5 || date[0].length != 2 || date[1].length != 2)
		return false;
	else
		return true;
}, "Invalid expiration date." );

var user = null;
getCurrentUser();
displayCart();

$('#comfirmCheckoutBtn').on('click', handleCheckout);
