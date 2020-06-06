import * as utils from './utilities.js';

// HTML Reference (#paginationTable)
/* 
    <tr>
    <td scope="row"></td>
    <td><img src="#" alt="" style="max-width: 200px;"></td>
    <td>
        <p class="h5"><a href="">Games of Thrones (2011)</a></p>
        <p class="small"> <a href="">Action</a>, <a href="">Adventure</a>, <a href="">Drama</a></p>
        <p class><i class="fa fa-star" aria-hidden="true"></i> 9.3</p>
        <p><strong>Directed by: </strong>David Benioff, D.B. Weiss</p>
        <p><strong>Stars: </strong><a href="">Emilia Clarke</a>, <a href="">Peter Dinklage</a>, <a href="">Kit Harington</a></p>
    </td>
    <td>
        <p class="h5">$15.99</p>
        <p><a href="#" role="button"><i class="fa fa-cart-plus fa-3x " aria-hidden="true" style="color: grey;"></i></a></p>
    </td>
    </tr> 
*/

function handleBrowsingResult(resultData) {
    if(topRated != 'null' && topRated != null) {
        $('#browsingTitle').text('Buy-a-Movie Search Top Rated Movies');
        $('#browsingHeading').text('Top Rated Movies');
    }
    if(searchParam != 'null' && searchParam != null) {
        $('#browsingTitle').text('Buy-a-Movie Search result for: ' + searchParam);
        $('#browsingHeading').text('Search result for: ' + searchParam);
    }
    if(genreParam != 'null' && genreParam != null) {
        $('#browsingTitle').text('Buy-a-Movie Browsing by Genre: ' + genreParam);
        $('#browsingHeading').text('Browsing by Genre: ' + genreParam);
    }
    if(titleParam != 'null' && titleParam != null) {
        $('#browsingTitle').text('Buy-a-Movie Browsing by Title: ' + titleParam);
        $('#browsingHeading').text('Browsing by Title: ' + titleParam);
    }

    if(resultData[resultData.length-1] == '0') {
        $('#paginationTable').html('<p>Nothing found...</p>');
        return false;
    }

    if(sortParam == 'rating') {
        if(orderParam == 'asc') {
            // <a href="#" class="badge badge-primary" id="sortRating">Rating <i class="fa fa-arrow-up" aria-hidden="true"></i></a>
            let sortOrder = 'desc';
            let sortRatingURL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
                '&sortBy=' + sortParam + '&order=' + sortOrder +
                '&numResults=' + numResultsParam + '&offset=' + '0';
            $('#sortRating').replaceWith('<a href="' + sortRatingURL + '" class="badge badge-primary" id="sortRating">Rating <i class="fa fa-arrow-up" aria-hidden="true"></i></a>')
        } else {
            // <a href="#" class="badge badge-primary" id="sortRating">Rating <i class="fa fa-arrow-down" aria-hidden="true"></i></a>
            let sortOrder = 'asc';
            let sortRatingURL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
                '&sortBy=' + sortParam + '&order=' + sortOrder +
                '&numResults=' + numResultsParam + '&offset=' + '0';
            $('#sortRating').replaceWith('<a href="' + sortRatingURL + '" class="badge badge-primary" id="sortRating">Rating <i class="fa fa-arrow-down" aria-hidden="true"></i></a>')
        }

        let sortTitleURL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
            '&sortBy=' + 'title' + '&order=' + 'asc' +
            '&numResults=' + numResultsParam + '&offset=' + '0';
        $('#sortAZ').attr('href', sortTitleURL);
        let sortYearURL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
            '&sortBy=' + 'year' + '&order=' + 'desc' +
            '&numResults=' + numResultsParam + '&offset=' + '0';
        $('#sortYear').attr('href', sortYearURL);
    } else if(sortParam == 'title') {
        if(orderParam == 'asc') {
            let sortTitleURL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
                '&sortBy=' + sortParam + '&order=' + 'desc' +
                '&numResults=' + numResultsParam + '&offset=' + '0';
            $('#sortAZ').replaceWith('<a href="' + sortTitleURL + '" class="badge badge-primary" id="sortRating">A-Z <i class="fa fa-arrow-up" aria-hidden="true"></i></a>')
        } else {
            let sortTitleURL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
                '&sortBy=' + sortParam + '&order=' + 'asc' +
                '&numResults=' + numResultsParam + '&offset=' + '0';
            $('#sortAZ').replaceWith('<a href="' + sortTitleURL + '" class="badge badge-primary" id="sortRating">A-Z <i class="fa fa-arrow-down" aria-hidden="true"></i></a>')
        }

        let sortRatingURL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
            '&sortBy=' + 'rating' + '&order=' + 'desc' +
            '&numResults=' + numResultsParam + '&offset=' + '0';
        $('#sortRating').attr('href', sortRatingURL);
        let sortYearURL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
            '&sortBy=' + 'year' + '&order=' + 'desc' +
            '&numResults=' + numResultsParam + '&offset=' + '0';
        $('#sortYear').attr('href', sortYearURL);
    } else if(sortParam == 'year') {
        if(orderParam == 'asc') {
            let sortYearURL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
                '&sortBy=' + sortParam + '&order=' + 'desc' +
                '&numResults=' + numResultsParam + '&offset=' + '0';
            $('#sortYear').replaceWith('<a href="' + sortYearURL + '" class="badge badge-primary" id="sortAZ">Year <i class="fa fa-arrow-up" aria-hidden="true"></i></a>')
        } else {
            let sortYearURL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
                '&sortBy=' + sortParam + '&order=' + 'asc' +
                '&numResults=' + numResultsParam + '&offset=' + '0';
            $('#sortYear').replaceWith('<a href="' + sortYearURL + '" class="badge badge-primary" id="sortAZ">Year <i class="fa fa-arrow-down" aria-hidden="true"></i></a>')
        }

        let sortRatingURL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
            '&sortBy=' + 'rating' + '&order=' + 'desc' +
            '&numResults=' + numResultsParam + '&offset=' + '0';
        $('#sortRating').attr('href', sortRatingURL);
        let sortTitleURL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
            '&sortBy=' + 'title' + '&order=' + 'asc' +
            '&numResults=' + numResultsParam + '&offset=' + '0';
        $('#sortAZ').attr('href', sortTitleURL);
    }

    switch(numResultsParam) {
        // <a href="#" class="badge badge-primary" id="display10">10</i></a>
        case('10'):
            $('#display10').replaceWith('<a href="#" class="badge badge-primary" id="display10">10</i></a>')
            break;
        case('20'):
            $('#display20').replaceWith('<a href="#" class="badge badge-primary" id="display20">20</i></a>')
            break;
        case('30'):
            $('#display30').replaceWith('<a href="#" class="badge badge-primary" id="display30">30</i></a>')
            break;
        case('40'):
            $('#display40').replaceWith('<a href="#" class="badge badge-primary" id="display40">40</i></a>')
            break;
        case('50'):
            $('#display50').replaceWith('<a href="#" class="badge badge-primary" id="display50">50</i></a>')
            break;
        default:
            break;
    }

    $('#display10').click(function(event) {
        if(numResultsParam == '10') {
            event.preventDefault();
        } else {
            let display10URL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
                '&sortBy=' + sortParam + '&order=' + orderParam +
                '&numResults=' + '10' + '&offset=' + '0';
            window.location.href = display10URL;
            return false;
        }

    });
    $('#display20').click(function(event) {
        if(numResultsParam == '20') {
            event.preventDefault();
        } else {
            let display20URL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
                '&sortBy=' + sortParam + '&order=' + orderParam +
                '&numResults=' + '20' + '&offset=' + '0';
            window.location.href = display20URL;
            return false;
        }

    });
    $('#display30').click(function(event) {
        if(numResultsParam == '30') {
            event.preventDefault();
        } else {
            let display30URL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
                '&sortBy=' + sortParam + '&order=' + orderParam +
                '&numResults=' + '30' + '&offset=' + '0';
            window.location.href = display30URL;
            return false;
        }

    });
    $('#display40').click(function(event) {
        if(numResultsParam == '40') {
            event.preventDefault();
        } else {
            let display40URL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
                '&sortBy=' + sortParam + '&order=' + orderParam +
                '&numResults=' + '40' + '&offset=' + '0';
            window.location.href = display40URL;
            return false;
        }
    });
    $('#display50').click(function(event) {
        if(numResultsParam == '50') {
            event.preventDefault();
        } else {
            let display50URL = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam + '&genre=' + genreParam +
                '&sortBy=' + sortParam + '&order=' + orderParam +
                '&numResults=' + '50' + '&offset=' + '0';
            window.location.href = display50URL;
            return false;
        }
    });

    var currentRange = Number(resultData.length - 1) + Number(offsetParam);
    var pageRange = offsetParam + ' - ' + currentRange;
    var currentPage = 'Displaying ' + pageRange + ' of ' + resultData[resultData.length - 1] + ' titles';
    $('.controlCurrentPage').text(currentPage);

    if(Number(offsetParam) == 0) {
        $('ul.pagination li.previous').addClass('disabled');
    } else {
        var previousOffset = Number(offsetParam) - Number(numResultsParam);
        var previousPage = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam +
            '&genre=' + genreParam + '&sortBy=' + sortParam +
            '&order=' + orderParam + '&numResults=' + numResultsParam +
            '&offset=' + previousOffset;
        $('.paginationPrevious').attr('href', previousPage);
    }

    if(currentRange >= resultData[resultData.length-1]) {
        $('ul.pagination li.next').addClass('disabled');
    } else {
        var nextOffset = Number(offsetParam) + Number(numResultsParam);
        var nextPage = 'browsing.html?' + 'topRated=' + topRated + '&search=' + searchParam + '&title=' + titleParam +
            '&genre=' + genreParam + '&sortBy=' + sortParam +
            '&order=' + orderParam + '&numResults=' + numResultsParam +
            '&offset=' + nextOffset;
        $('.paginationNext').attr('href', nextPage);
    }

    var paginationTableHTML = $('#paginationTable').html('');

    for(let i = 0; i < resultData.length - 1; i++) {
        let movieID = resultData[i]['movie_id'];
        let movieTitle = resultData[i]['movie_title'];
        let movieYear = resultData[i]['movie_year'];
        let movieDirector = resultData[i]['movie_director'];
        let movieRating = resultData[i]['movie_rating'];
        let movieGenres = utils.parseGenresAsHTML(resultData[i]['movie_genres']);
        let movieStars = utils.parseStarsAsHTML(resultData[i]['movie_stars']);

        let moviePoster = resultData[i]['movie_poster'];
        if(moviePoster == '') {
            moviePoster = 'img/empty-poster.png';
        }

        let moviePrice = '15.99';

        let trHTML = document.createElement('tr');

        $('<td>', {
            scope: 'row'
        }).appendTo(trHTML);

        $('<td>').append(
            $('<img />', {
                src: moviePoster,
                alt: movieTitle + ' Poster',
                style: 'max-width: 200px;'
            })
        ).appendTo(trHTML);

        $('<td>').append(
            $('<p>', { class: 'h5' }).append(
                $('<a>', { href: 'movie.html?id=' + movieID }).text(movieTitle + ' ' + '(' + movieYear + ')')
            ),
            $('<p>', { class: 'small' }).append(movieGenres),
            $('<p>').append(
                $('<i>', { class: 'fa fa-star', 'aria-hidden': 'true' }),
                ' ',
                movieRating
            ),
            $('<p>').append(
                $('<strong>').text('Directed by: '),
                movieDirector
            ),
            $('<p>').append(
                $('<strong>').text('Stars: '),
                movieStars
            )
        ).appendTo(trHTML);

        $('<td>').append(
            $('<p>', { class: 'h5' }).text(moviePrice),
            $('<p>').append(
                $('<a>', { 
                    href: 'cart.html?addToCart=true' + '&movieID=' + movieID + '&movieTitle=' + movieTitle + '&moviePrice=' + moviePrice + '&movieQuantity=1',
                    role: 'button' 
                }).append('<i class="fa fa-cart-plus fa-3x " aria-hidden="true" style="color: grey;"></i>')
            )
        ).appendTo(trHTML);

        paginationTableHTML.append(trHTML);
    }
}

const topRated = utils.getParameterByName('topRated');
const searchParam = utils.getParameterByName('search');
const titleParam = utils.getParameterByName('title');
const genreParam = utils.getParameterByName('genre');
const sortParam = utils.getParameterByName('sortBy');
const orderParam = utils.getParameterByName('order');
const numResultsParam = utils.getParameterByName('numResults');
const offsetParam = utils.getParameterByName('offset');

$.ajax({
    method: 'GET',
    url: 'api/browsing?' + 'topRated=' + topRated +'&search=' + searchParam + '&title=' + titleParam +
        '&genre=' + genreParam + '&sortBy=' + sortParam +
        '&order=' + orderParam + '&numResults=' + numResultsParam +
        '&offset=' + offsetParam,
    dataType: 'json',
    success: handleBrowsingResult,
});