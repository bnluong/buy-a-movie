import * as utils from './utilities.js';

function handleAddToCart(movieID, movieTitle, moviePrice, movieQuantity) {
    $.ajax({
        type: "POST",
        url: "api/cart/add",
        data: 'id=' + movieID + '&title=' + movieTitle + '&price=' + moviePrice + '&quantity=' + movieQuantity,
        success: function(responseData) {

        }
    });
}

function getUser() {
    $.ajax({
        type: 'GET',
        url: 'api/session',
        success: function(responseData) {
            if(responseData['user_name'] == null)
                window.location.replace("login.html");
        }
    });
}

const addToCart = utils.getParameterByName('addToCart');

if(addToCart == 'true') {
    var movieID = utils.getParameterByName('movieID');
    var movieTitle = utils.getParameterByName('movieTitle');
    var moviePrice = utils.getParameterByName('moviePrice');
    var movieQuantity = utils.getParameterByName('movieQuantity');
    handleAddToCart(movieID, movieTitle, moviePrice, movieQuantity);
}