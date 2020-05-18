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
    if(genreParam != 'None') {
        $('#browsingTitle').text('Buy-a-Movie Browsing by Genre: ' + genreParam);
        $('#browsingHeading').text('Browsing by Genre: ' + genreParam);
    }
    if(titleParam != 'None') {
        $('#browsingTitle').text('Buy-a-Movie Browsing by Title: ' + titleParam);
        $('#browsingHeading').text('Browsing by Title: ' + titleParam);
    }

    if(sortParam == 'rating') {
        if(orderParam == 'asc') {
            // <a href="#" class="badge badge-primary" id="sortRating">Rating <i class="fa fa-arrow-up" aria-hidden="true"></i></a>
            $('#sortRating').replaceWith('<a href="#" class="badge badge-primary" id="sortRating">Rating <i class="fa fa-arrow-up" aria-hidden="true"></i></a>')
        } else {
            // <a href="#" class="badge badge-primary" id="sortRating">Rating <i class="fa fa-arrow-down" aria-hidden="true"></i></a>
            $('#sortRating').replaceWith('<a href="#" class="badge badge-primary" id="sortRating">Rating <i class="fa fa-arrow-down" aria-hidden="true"></i></a>')
        }
    } else if(sortParam == 'alpha') {
        if(orderParam == 'asc') {
            $('#sortAZ').replaceWith('<a href="#" class="badge badge-primary" id="sortRating">A-Z <i class="fa fa-arrow-up" aria-hidden="true"></i></a>')
        } else {
            $('#sortAZ').replaceWith('<a href="#" class="badge badge-primary" id="sortRating">A-Z <i class="fa fa-arrow-down" aria-hidden="true"></i></a>')
        }
    } else {
        if(orderParam == 'asc') {
            $('#sortYear').replaceWith('<a href="#" class="badge badge-primary" id="sortAZ">Year <i class="fa fa-arrow-up" aria-hidden="true"></i></a>')
        } else {
            $('#sortYear').replaceWith('<a href="#" class="badge badge-primary" id="sortAZ">Year <i class="fa fa-arrow-down" aria-hidden="true"></i></a>')
        }
    }

    switch(numResultsParam) {
        // <a href="#" class="badge badge-primary" id="display10">10</i></a>
        case('10'):
            $('#display10').replaceWith('<a href="#" class="badge badge-primary" id="display10">10</i></a>')
            break;
        case('20'):
            $('#display10').replaceWith('<a href="#" class="badge badge-primary" id="display20">20</i></a>')
            break;
        case('30'):
            $('#display10').replaceWith('<a href="#" class="badge badge-primary" id="display30">30</i></a>')
            break;
        case('40'):
            $('#display10').replaceWith('<a href="#" class="badge badge-primary" id="display40">40</i></a>')
            break;
        case('50'):
            $('#display10').replaceWith('<a href="#" class="badge badge-primary" id="display50">50</i></a>')
            break;
        default:
            break;
    }

    var currentRange = Number(resultData.length-1) + Number(offsetParam);
    var pageRange = offsetParam + ' - ' + currentRange;
    var currentPage = 'Displaying ' + pageRange + ' of ' + resultData[resultData.length-1] + ' titles';
    $('.controlCurrentPage').text(currentPage);

    if(Number(offsetParam) == 0) {
    	$('ul.pagination li.previous').addClass('disabled');
    } else {
	    var previousOffset = Number(offsetParam) - Number(numResultsParam);
	    var previousPage = 'browsing.html?' + 'title=' + titleParam +
		    '&genre=' + genreParam + '&sortBy=' + sortParam +
		    '&order=' + orderParam + '&numResults=' + numResultsParam +
		    '&offset=' + previousOffset;
	    $('#paginationPrevious').attr('href', previousPage);
    }
    
    if(resultData.length < numResultsParam) {
    	$('ul.pagination li.next').addClass('disabled');
    } else {
	    var nextOffset = Number(offsetParam) + Number(numResultsParam);
	    var nextPage = 'browsing.html?' + 'title=' + titleParam +
		    '&genre=' + genreParam + '&sortBy=' + sortParam +
		    '&order=' + orderParam + '&numResults=' + numResultsParam +
		    '&offset=' + nextOffset;
	    $('#paginationNext').attr('href', nextPage);
    }
    
    var paginationTableHTML = $('#paginationTable').html('');

    for(let i = 0; i < resultData.length-1; i++) {
        let movieID = resultData[i]['movie_id'];
        let movieTitle = resultData[i]['movie_title'];
        let movieYear = resultData[i]['movie_year'];
        let movieDirector = resultData[i]['movie_director'];
        let movieRating = resultData[i]['movie_rating'];
        let movieGenres = parseGenres(resultData[i]['movie_genres']);
        let movieStars = parseStars(resultData[i]['movie_stars']);

        let moviePoster = resultData[i]['movie_poster'];
        if(moviePoster == '') {
            moviePoster = 'img/empty-poster.png';
        }

        let moviePrice = '$69.69';

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
                $('<a>', { href: 'movie-info.html?id=' + movieID }).text(movieTitle + ' ' + '(' + movieYear + ')')
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
                $('<a>', { href: '#', role: 'button' }).append('<i class="fa fa-cart-plus fa-3x " aria-hidden="true" style="color: grey;"></i>')
            )
        ).appendTo(trHTML);

        paginationTableHTML.append(trHTML);
    }
}

function parseGenres(movieGenres) {
    var genresHTML = '';
    for(let i = 0; i < movieGenres.length; i++) {
        let genreName = movieGenres[i]['genre_name'];
        genresHTML += '<a href="browsing.html?' + 'title=None' + '&genre=' + genreName + '&sortBy=rating&order=desc&numResults=10&offset=0' + '">' + genreName + '</a>'
        if(i != movieGenres.length - 1)
            genresHTML += ', ';
    }
    return genresHTML;
}

function parseStars(movieStars) {
    var starsHTML = '';
    for(let i = 0; i < movieStars.length; i++) {
        let starID = movieStars[i]['star_id'];
        let starName = movieStars[i]['star_name'];
        starsHTML += '<a href="star-info.html?' + 'id=' + starID + '">' + starName + '</a>'
        if(i != movieStars.length - 1)
            starsHTML += ', ';
    }
    return starsHTML;
}

const titleParam = utils.getParameterByName('title');
const genreParam = utils.getParameterByName('genre');
const sortParam = utils.getParameterByName('sortBy');
const orderParam = utils.getParameterByName('order');
const numResultsParam = utils.getParameterByName('numResults');
const offsetParam = utils.getParameterByName('offset');

$.ajax({
    method: 'GET',
    url: 'api/browsing?' + 'title=' + titleParam + '&genre=' + genreParam + '&sortBy=' + sortParam + '&order=' + orderParam + '&numResults=' + numResultsParam + '&offset=' + offsetParam,
    dataType: 'json',
    success: handleBrowsingResult,
});