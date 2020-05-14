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
    var paginationTableHTML = $('#paginationTable').html('');

    for(let i = 0; i < resultData.length; i++) {
        let movieID = resultData[i]['movie_id'];
        let movieTitle = resultData[i]['movie_title'];
        let movieYear = resultData[i]['movie_year'];
        let movieDirector = resultData[i]['movie_director'];
        let movieRating = resultData[i]['movie_rating'];
        let movieGenres = parseGenres(resultData[i]['movie_genres']);
        let movieStars = parseStars(resultData[i]['movie_stars']);

        let moviePrice = '$69.69';

        let trHTML = document.createElement('tr');
        
        $('<td>', {
            scope: 'row'
        }).appendTo(trHTML);

        $('<td>').append(
            $('<img />', {
                src: 'img/empty-poster.png',
                alt: 'Empty Poster',
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
                movieRating
            ),
            $('<p>').append(
                $('<strong>').text('Directed by: '),
                movieDirector
            ),
            $('<p>').append(
                $('<strong>').text('Stars: '),
                movieStars
            ),
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
    let genresHTML = '';
    for(let i = 0; i < movieGenres.length; i++) {
        let genreName = movieGenres[i];
        genresHTML += '<a href="browsing.html?' + 'genre=' + genreName + '&' + 'sortBy=rating&order=desc' + '&' + 'numResults=10' + '">' + genreName + '</a>'
        if(i != movieGenres.length - 1)
            genresHTML += ', ';
    }
    return genresHTML;
}

function parseStars(movieStars) {
    let starsHTML = '';
    for(let i = 0; i < movieStars.length; i++) {
        let starID = movieStars[i]['star_id'];
        let starName = movieStars[i]['star_name'];
        starsHTML += '<a href="star-info.html?' + 'id=' + starID + '">' + starName + '</a>'
        if(i != movieStars.length - 1)
            starsHTML += ', ';
    }
    return starsHTML;
}

$.ajax({
    method: 'GET',
    url: 'api/browsing',
    dataType: 'json',
    success: handleBrowsingResult,
});