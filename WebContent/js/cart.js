import * as utils from './utilities.js';

function displayCart() {
    $.ajax({
        type: "POST",
        url: "api/cart/retrieve",
        data: 'user=' + user,
        success: function(responseData) {
            var subTotalFloat = 0.0;
            var totalItemsInt = 0;

            for(let i = 0; i < responseData.length; i++) {
                let cartID = 'cart' + (i + 1);
                let movieID = responseData[i]['movie_id'];
                let movieTitle = responseData[i]['movie_title'] + ' (' + responseData[i]['movie_year'] + ')';
                let movieQuantity = responseData[i]['movie_quantity'];
                let moviePrice = responseData[i]['movie_price'];
                let moviePoster = responseData[i]['movie_poster'];

                subTotalFloat += Number(moviePrice) * Number(movieQuantity);
                totalItemsInt += Number(movieQuantity);
                
                $('#cartContent').append(
                    $('<div>', { class: 'form-row', id: cartID }).append(
                        $('<div>', { class: 'col-sm-3 col-5' }).append(
                            $('<img>', { src: moviePoster, alt: responseData[i]['movie_title'] + ' Poster' })
                        ),
                        $('<div>', { class: 'col-6 mx-auto' }).append(
                            $('<div>', { class: 'row mx-auto' }).append(
                                $('<p>', { class: 'h5' }).append(
                                    $('<a>', { href: 'movie.html?id=' + movieID }).text(movieTitle)
                                )
                            ),
                            $('<div>', { class: 'form-group my-3' }).append(
                                $('<div>', { class: 'form-group' }).append(
                                    $('<label>', { class: 'small' }).text('Quantity'),
                                    $('<input>', { class: 'form-control', type: 'number', name: 'quantity' })
                                ),
                                $('<button>', { class: 'btn btn-dark btn-sm mr-1 cartItemUpdate', type: 'submit' }).text('Update'),
                                $('<button>', { class: 'btn btn-dark btn-sm mr-1 cartItemRemove', type: 'submit' }).text('Remove')
                            )
                        ),
                        $('<div>', { class: 'col-1 unitPrice' }).append(
                            $('<p>', { class: 'h6', name: 'unitPrice' }).text('$' + moviePrice)
                        )
                    ),
                    $('<hr>', { class: 'my-3' })
                );
                
                $('#' + cartID + ' input').val(Number(movieQuantity));
            }

            let subTotalStr = 'Subtotal ' + '(' + totalItemsInt + ' items): ' + '$' + subTotalFloat;
            $('#cartSubTotal').append(
                $('<p>', { class: 'h6' }).text(subTotalStr)
            )
        }
    });
}

function getCurrentUser() {
    $.ajax({
        type: 'GET',
        async: false,
        url: 'api/session',
        success: function(responseData) {
            if(responseData['user_name'] == null) {
            	alert('Please login before accessing to cart.');
                window.location.replace("login.html");
            }
            else
                user = responseData['user_email'];
        }
    });
}

function handleAddToCartResult(response) {
    if(response['status'] == 'success') {
        alert(response['message'])
        displayCart();
    } else {
        alert(response['message'])
        window.location = document.referrer;
    }
}

// function handleUpate() {
//     var data = $('#item1 input').serialize();
//     console.log( $('#item1 input').serialize());
//     console.log( $('#item1 .unitPrice').val());
// }

// function displayCart() {
//     var item1 = $('#item1 #item1UpdateBtn');
//     $( item1 ).click(handleUpate);

//     // $(item1).submit(handleUpate)
// }

const addToCart = utils.getParameterByName('addToCart');
var user = null;

getCurrentUser();

if(addToCart == 'true') {
    let movieID = utils.getParameterByName('movieID');
    let movieTitle = utils.getParameterByName('movieTitle');
    let moviePrice = utils.getParameterByName('moviePrice');
    let movieQuantity = utils.getParameterByName('movieQuantity');

    $.ajax({
        type: 'POST',
        url: 'api/cart/add',
        data: 'id=' + movieID + '&title=' + movieTitle + '&price=' + moviePrice + '&quantity=' + movieQuantity + '&user=' + user,
        success: handleAddToCartResult
    });
} else {
    displayCart();
}