import * as utils from './utilities.js';

function handleMovieResult(resultData) {
    var movieTitle = resultData['movie_title'];
    var movieYear = resultData['movie_year'];

    var moviePoster = resultData['movie_poster'];
    if(moviePoster == '')
        moviePoster = 'img/empty-poster.png';

    var movieRuntime = resultData['movie_runtime'];
    var movieGenres = utils.parseGenresAsHTML(resultData['movie_genres']);
    
    var movieRating = resultData['movie_rating'];
    if(movieRating == null)
    	movieRating = 'Not yet rated';
    
    var movieOverview = resultData['movie_overview'];
    var movieDirector = resultData['movie_director'];
    var movieStars = utils.parseStarsAsHTML(resultData['movie_stars']);
    var moviePrice = resultData['movie_price'];
    
    $('#movieTitle').text(movieTitle + ' (' + movieYear + ')');
    $('#moviePoster').append(
        $('<img>', {
            src: moviePoster,
            alt: movieTitle + ' Poster',
        })
    );
    $('#movieTitleHeading').append(
        $('<p>', {
            class: 'h4',
        }).text(movieTitle + ' (' + movieYear + ')')
    );
    $('#movieSubText').append(
        $('<span>', {
            class: 'small'
        }).text(movieRuntime),
        $('<span>').text(' | '),
        $('<span>', {
            class: 'small'
        }).append(movieGenres)
    );
    $('#movieRating').append(
        $('<p>').append(
            $('<i>', { class: 'fa fa-star', 'aria-hidden': 'true' }),
            ' ',
            movieRating
        )
    );
    $('#movieOverview').append(
        $('<p>').text(movieOverview)
    );
    $('#movieDirector').append(
        $('<p>').append(
            $('<strong>').text('Directed by: '),
            movieDirector
        )
    );
    $('#movieStars').append(
        $('<p>').append(
            $('<strong>').text('Stars: '),
            movieStars
        )
    );
    $('#moviePrice').append(
        $('<p>').append(
            $('<lead>').text('Price: $'),
            moviePrice
        ),
        $('<p>').append(
            $('<a>', {
                href: 'cart.html',
                role: 'butotn'
            }).append('<i class="fa fa-cart-plus fa-3x " aria-hidden="true" style="color: grey;"></i>')
        )
    )
}

const movieID = utils.getParameterByName('id');

$.ajax({
    method: 'GET',
    url: 'api/movie?id=' + movieID,
    dataType: 'json',
    success: handleMovieResult,
});